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

package es.eucm.ead.techdemo.elementfactories.effects;

import es.eucm.ead.model.assets.drawable.basics.Caption;
import es.eucm.ead.model.assets.drawable.basics.EAdCaption;
import es.eucm.ead.model.assets.multimedia.Sound;
import es.eucm.ead.model.elements.BasicElement;
import es.eucm.ead.model.elements.effects.InterpolationEf;
import es.eucm.ead.model.elements.effects.PlaySoundEf;
import es.eucm.ead.model.elements.effects.enums.InterpolationLoopType;
import es.eucm.ead.model.elements.effects.enums.InterpolationType;
import es.eucm.ead.model.elements.effects.enums.ShowTextAnimation;
import es.eucm.ead.model.elements.effects.text.QuestionEf;
import es.eucm.ead.model.elements.effects.text.SpeakEf;
import es.eucm.ead.model.elements.effects.timedevents.ShowSceneElementEf;
import es.eucm.ead.model.elements.effects.variables.ChangeFieldEf;
import es.eucm.ead.model.elements.operations.ElementField;
import es.eucm.ead.model.elements.operations.Operation;
import es.eucm.ead.model.elements.predef.effects.ChangeAppearanceEf;
import es.eucm.ead.model.elements.predef.effects.MakeActiveElementEf;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.params.text.EAdString;
import es.eucm.ead.techdemo.elementfactories.EAdElementsFactory;
import es.eucm.ead.techdemo.elementfactories.StringFactory.StringType;

public class EffectFactory {

	public ChangeAppearanceEf getChangeAppearance(BasicElement element,
			String bundle) {
		ChangeAppearanceEf effect = new ChangeAppearanceEf(element, bundle);
		return effect;
	}

	public InterpolationEf getInterpolationEffect(ElementField var,
			float startValue, float endValue, int time,
			InterpolationLoopType loop, InterpolationType interpolationType) {
		InterpolationEf interpolation = new InterpolationEf(var, startValue,
				endValue, time, loop, interpolationType);
		return interpolation;
	}

	public ShowSceneElementEf getShowText(String text, int x, int y,
			ShowTextAnimation animation, int maximumHeight) {
		ShowSceneElementEf effect = new ShowSceneElementEf();
		Caption c = EAdElementsFactory.getInstance().getCaptionFactory()
				.createCaption(text);
		c.setPreferredHeight(maximumHeight);
		effect.setCaption(c, x, y, animation);
		return effect;
	}

	public ShowSceneElementEf getShowText(String text, int x, int y,
			ShowTextAnimation animation) {
		return this.getShowText(text, x, y, animation, EAdCaption.SCREEN_SIZE);
	}

	/**
	 * Returns an EAdShowQuestion with the given question and the number of
	 * answers
	 * 
	 * @param question
	 * @param nAnswers
	 * @return
	 */
	public QuestionEf getShowQuestion(String question, int nAnswers) {
		QuestionEf effect = new QuestionEf();
		EAdElementsFactory.getInstance().getStringFactory().setString(
				effect.getQuestion(), question);

		for (int i = 0; i < nAnswers; i++) {
			int ordinal = i % StringType.values().length;
			EAdString answerString = EAdElementsFactory.getInstance()
					.getStringFactory().getString(StringType.values()[ordinal]);
			effect.addAnswer(answerString, new SpeakEf(new EAdString(
					"showQuestion" + (int) (Math.random() * 1000000))));
		}
		return effect;

	}

	public ShowSceneElementEf getShowText(String text, int x, int y) {
		return this.getShowText(text, x, y, ShowTextAnimation.NONE);
	}

	public ChangeFieldEf getChangeVarValueEffect(ElementField var,
			Operation operation) {
		ChangeFieldEf effect = new ChangeFieldEf(var, operation);
		return effect;

	}

	public MakeActiveElementEf getMakeActiveElement(SceneElement element) {
		MakeActiveElementEf effect = new MakeActiveElementEf(element);
		return effect;
	}

	public PlaySoundEf getPlaySound(String string) {
		Sound sound = new Sound(string);
		PlaySoundEf effect = new PlaySoundEf(sound);
		return effect;
	}

}
