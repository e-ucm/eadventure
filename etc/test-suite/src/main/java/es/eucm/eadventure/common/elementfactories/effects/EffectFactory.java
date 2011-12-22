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

package es.eucm.eadventure.common.elementfactories.effects;

import es.eucm.eadventure.common.elementfactories.EAdElementsFactory;
import es.eucm.eadventure.common.elementfactories.StringFactory.StringType;
import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.elements.effects.InterpolationEf;
import es.eucm.eadventure.common.model.elements.effects.PlaySoundEf;
import es.eucm.eadventure.common.model.elements.effects.enums.InterpolationLoopType;
import es.eucm.eadventure.common.model.elements.effects.enums.InterpolationType;
import es.eucm.eadventure.common.model.elements.effects.enums.ShowTextAnimation;
import es.eucm.eadventure.common.model.elements.effects.text.ShowQuestionEf;
import es.eucm.eadventure.common.model.elements.effects.text.SpeakEf;
import es.eucm.eadventure.common.model.elements.effects.timedevents.ShowSceneElementEf;
import es.eucm.eadventure.common.model.elements.effects.variables.ChangeFieldEf;
import es.eucm.eadventure.common.model.elements.scene.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.variables.EAdField;
import es.eucm.eadventure.common.model.elements.variables.EAdOperation;
import es.eucm.eadventure.common.model.predef.effects.ChangeAppearanceEf;
import es.eucm.eadventure.common.model.predef.effects.MakeActiveElementEf;
import es.eucm.eadventure.common.params.text.EAdString;
import es.eucm.eadventure.common.resources.EAdBundleId;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Caption;
import es.eucm.eadventure.common.resources.assets.drawable.basics.CaptionImpl;
import es.eucm.eadventure.common.resources.assets.multimedia.SoundImpl;

public class EffectFactory {

	private static int ID_GENERATOR = 0;

	public ChangeAppearanceEf getChangeAppearance(EAdElement element,
			EAdBundleId bundle) {
		ChangeAppearanceEf effect = new ChangeAppearanceEf( element, bundle);
		effect.setId("changeAppearance"
				+ ID_GENERATOR++);
		return effect;
	}

	public InterpolationEf getInterpolationEffect(EAdField<?> var,
			float startValue, float endValue, int time, InterpolationLoopType loop,
			InterpolationType interpolationType) {
		InterpolationEf interpolation = new InterpolationEf(var, startValue,
				endValue, time, loop, interpolationType);
		return interpolation;
	}

	public ShowSceneElementEf getShowText(String text, int x, int y,
			ShowTextAnimation animation, int maximumHeight) {
		ShowSceneElementEf effect = new ShowSceneElementEf();
		CaptionImpl c = EAdElementsFactory.getInstance().getCaptionFactory()
				.createCaption(text);
		c.setPreferredHeight(maximumHeight);
		effect.setCaption(c, x, y, animation);
		return effect;
	}

	public ShowSceneElementEf getShowText(String text, int x, int y,
			ShowTextAnimation animation) {
		return this.getShowText(text, x, y, animation, Caption.SCREEN_SIZE);
	}

	/**
	 * Returns an EAdShowQuestion with the given question and the number of
	 * answers
	 * 
	 * @param question
	 * @param nAnswers
	 * @return
	 */
	public ShowQuestionEf getShowQuestion(String question, int nAnswers) {
		ShowQuestionEf effect = new ShowQuestionEf();
		EAdElementsFactory.getInstance().getStringFactory().setString(effect.getQuestion(), question);
		
		for (int i = 0; i < nAnswers; i++) {
			int ordinal = i % StringType.values().length;
			EAdString answerString = EAdElementsFactory.getInstance()
					.getStringFactory().getString(StringType.values()[ordinal]);
			effect.addAnswer(answerString, new SpeakEf());
		}
		effect.setUpNewInstance();
		return effect;

	}

	public ShowSceneElementEf getShowText(String text, int x, int y) {
		return this.getShowText(text, x, y, ShowTextAnimation.NONE);
	}

	public ChangeFieldEf getChangeVarValueEffect(EAdField<?> var,
			EAdOperation operation) {
		ChangeFieldEf effect = new ChangeFieldEf( var, operation);
		effect.setId("changeVarValue" + ID_GENERATOR++);
		return effect;

	}

	public MakeActiveElementEf getMakeActiveElement(
			EAdSceneElement element) {
		MakeActiveElementEf effect = new MakeActiveElementEf(element);
		return effect;
	}

	public PlaySoundEf getPlaySound(String string) {
		SoundImpl sound = new SoundImpl(string);
		PlaySoundEf effect = new PlaySoundEf( sound);
		effect.setId("playSound" + ID_GENERATOR++);
		return effect;
	}

}
