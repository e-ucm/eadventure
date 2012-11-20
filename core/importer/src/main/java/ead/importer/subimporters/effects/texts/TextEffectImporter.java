/**
 * eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the
 *    <e-UCM> research group.
 *
 *    Copyright 2005-2010 <e-UCM> research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    <e-UCM> is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure, version 2.0
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */

package ead.importer.subimporters.effects.texts;

import java.util.ArrayList;
import java.util.List;

import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.conditions.OperationCond;
import ead.common.model.elements.conditions.enums.Comparator;
import ead.common.model.elements.effects.PlaySoundEf;
import ead.common.model.elements.effects.text.SpeakEf;
import ead.common.model.elements.variables.EAdField;
import ead.common.model.elements.variables.EAdOperation;
import ead.common.model.elements.variables.operations.BooleanOp;
import ead.common.params.fills.ColorFill;
import ead.common.params.fills.Paint;
import ead.common.resources.assets.drawable.basics.EAdCaption;
import ead.common.resources.assets.drawable.basics.shapes.extra.BalloonType;
import ead.common.resources.assets.multimedia.Sound;
import ead.common.resources.assets.text.BasicFont;
import ead.common.resources.assets.text.EAdFont;
import ead.importer.EAdElementImporter;
import ead.importer.annotation.ImportAnnotator;
import ead.importer.interfaces.EAdElementFactory;
import ead.importer.interfaces.ResourceImporter;
import ead.importer.subimporters.effects.EffectImporter;
import ead.tools.StringHandler;
import es.eucm.eadventure.common.data.chapter.conditions.Condition;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.AbstractEffect;
import es.eucm.eadventure.common.data.chapter.elements.NPC;

public abstract class TextEffectImporter<T extends AbstractEffect> extends
		EffectImporter<T, SpeakEf> {

	public static final String WHISPER = "#:*";
	public static final String THOUGHT = "#O";
	public static final String YELL = "#!";

	protected static final EAdFont DEFAULT_FONT = new BasicFont(20.0f);

	protected static int ID_GENERATOR = 0;

	protected StringHandler stringHandler;

	protected EAdElementFactory factory;

	private ResourceImporter resourceImporter;

	public TextEffectImporter(StringHandler stringHandler,
			EAdElementImporter<Conditions, EAdCondition> conditionImporter,
			EAdElementFactory factory, ImportAnnotator annotator,
			ResourceImporter resourceImporter) {
		super(conditionImporter, annotator);
		this.stringHandler = stringHandler;
		this.factory = factory;
		this.resourceImporter = resourceImporter;
	}

	@Override
	public SpeakEf init(T oldObject) {
		return new SpeakEf();
	}

	public SpeakEf convert(T oldObject, Object object) {
		SpeakEf showText = super.convert(oldObject, object);
		showText.setBlocking(true);
		showText.setOpaque(true);
		showText.setFont(DEFAULT_FONT);
		return showText;
	}

	protected void addSound(String audioPath, SpeakEf effect) {
		if (audioPath != null && !audioPath.equals("")) {
			Sound s = (Sound) resourceImporter.getAssetDescritptor(audioPath,
					Sound.class);
			if (s != null) {
				PlaySoundEf playSound = new PlaySoundEf(s);
				effect.getPreviousEffects().add(playSound);
			}
		}
	}

	public static List<EAdOperation> getOperations(String text,
			EAdElementFactory factory) {
		int i = 0;
		ArrayList<EAdOperation> operations = new ArrayList<EAdOperation>();
		boolean finished = false;
		while (!finished && i < text.length()) {
			int beginIndex = text.indexOf('(', i);
			int endIndex = text.indexOf(')', i);
			int questionMark = text.indexOf('?', i);
			if (beginIndex != -1 && endIndex != -1 && endIndex > beginIndex
					&& questionMark > beginIndex) {
				EAdOperation op = createOperation(text.substring(
						beginIndex + 2, questionMark), factory);
				if (op != null) {
					operations.add(op);
				}
				i = endIndex + 1;
			} else {
				finished = true;
			}
		}
		return operations;

	}

	private static EAdOperation createOperation(String condition,
			EAdElementFactory factory) {
		Comparator comparator = Comparator.DIFFERENT;
		String[] comparison = new String[] { new String(condition) };
		if (condition.contains(">=")) {
			comparator = Comparator.GREATER_EQUAL;
			comparison = condition.split(">=");
		} else if (condition.contains(">")) {
			comparator = Comparator.GREATER;
			comparison = condition.split(">");
		} else if (condition.contains("<=")) {
			comparator = Comparator.LESS_EQUAL;
			comparison = condition.split("<=");
		} else if (condition.contains("<")) {
			comparator = Comparator.LESS;
			comparison = condition.split("<");
		} else if (condition.contains("==")) {
			comparator = Comparator.EQUAL;
			comparison = condition.split("==");
		} else {
			return factory.getVarByOldId(condition, Condition.FLAG_CONDITION);
		}

		if (comparison.length == 2) {
			EAdField<?> op1 = factory.getVarByOldId(comparison[0],
					Condition.VAR_CONDITION);
			Integer number = null;
			try {
				number = new Integer(comparison[1]);
			} catch (NumberFormatException e) {
				return null;
			}
			if (op1 != null && number != null)
				return new BooleanOp(new OperationCond(op1, number, comparator));
		}

		return null;
	}

	public static String translateLine(String text) {
		String finalText = text;
		int i = 0;
		int varNumber = 0;
		boolean finished = false;
		while (!finished && i < text.length()) {
			int beginIndex = text.indexOf('(', i);
			int endIndex = text.indexOf(')', i);
			int questionMark = text.indexOf('?', i);
			if (beginIndex != -1 && endIndex != -1 && endIndex > beginIndex
					&& questionMark > beginIndex) {
				String varName = text.substring(beginIndex + 2, questionMark);
				finalText = finalText.replace("#" + varName, "[" + varNumber
						+ "]");
				varNumber++;
				i = endIndex + 1;
			} else {
				finished = true;
			}
		}
		return finalText;
	}

	/**
	 * Sets the ballon type for the effect and deletes the balloon type tag form
	 * the line and returns it
	 * 
	 * @param effect
	 * @param line
	 * @return
	 */
	public static String setBallonType(SpeakEf effect, String line) {
		BalloonType type = BalloonType.ROUNDED_RECTANGLE;
		if (line.startsWith(WHISPER)) {
			// TODO Whisper balloon
			type = BalloonType.ROUNDED_RECTANGLE;
			line = line.substring(WHISPER.length());
		} else if (line.startsWith(THOUGHT)) {
			type = BalloonType.CLOUD;
			line = line.substring(THOUGHT.length());
		} else if (line.startsWith(YELL)) {
			type = BalloonType.ELECTRIC;
			line = line.substring(YELL.length());
		}

		effect.setBalloonType(type);
		return line;
	}

	public static void setSpeakEffect(SpeakEf effect, String originalLine,
			NPC npc, EAdElementFactory factory, StringHandler stringHandler) {
		if (originalLine != null) {
			EAdCaption caption = effect.getCaption();
			String line = setBallonType(effect, originalLine);
			translateText(stringHandler, caption, line, factory);

		}

		ColorFill center = new ColorFill("0x"
				+ npc.getTextFrontColor().substring(1) + "ff");
		ColorFill border = new ColorFill("0x"
				+ npc.getTextBorderColor().substring(1) + "ff");

		ColorFill bubbleCenter = new ColorFill("0x"
				+ npc.getBubbleBkgColor().substring(1) + "ff");
		ColorFill bubbleBorder = new ColorFill("0x"
				+ npc.getBubbleBorderColor().substring(1) + "ff");

		effect.setColor(new Paint(center, border), new Paint(bubbleCenter,
				bubbleBorder));
	}

	public static void translateText(StringHandler stringHandler,
			EAdCaption caption, String originalLine, EAdElementFactory factory) {

		for (EAdOperation op : TextEffectImporter.getOperations(originalLine,
				factory)) {
			caption.getFields().add(op);
		}
		String finalLine = TextEffectImporter.translateLine(originalLine);
		stringHandler.setString(caption.getText(), finalLine);
	}

}
