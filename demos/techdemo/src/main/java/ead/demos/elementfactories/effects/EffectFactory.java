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

package ead.demos.elementfactories.effects;

import ead.common.model.assets.drawable.basics.Caption;
import ead.common.model.assets.drawable.basics.EAdCaption;
import ead.common.model.assets.multimedia.Sound;
import ead.common.model.elements.EAdElement;
import ead.common.model.elements.effects.InterpolationEf;
import ead.common.model.elements.effects.PlaySoundEf;
import ead.common.model.elements.effects.enums.InterpolationLoopType;
import ead.common.model.elements.effects.enums.InterpolationType;
import ead.common.model.elements.effects.enums.ShowTextAnimation;
import ead.common.model.elements.effects.text.ShowQuestionEf;
import ead.common.model.elements.effects.text.SpeakEf;
import ead.common.model.elements.effects.timedevents.ShowSceneElementEf;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.operations.EAdField;
import ead.common.model.elements.operations.EAdOperation;
import ead.common.model.elements.predef.effects.ChangeAppearanceEf;
import ead.common.model.elements.predef.effects.MakeActiveElementEf;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.params.text.EAdString;
import ead.demos.elementfactories.EAdElementsFactory;
import ead.demos.elementfactories.StringFactory.StringType;

public class EffectFactory {

	public ChangeAppearanceEf getChangeAppearance(EAdElement element,
			String bundle) {
		ChangeAppearanceEf effect = new ChangeAppearanceEf(element, bundle);
		return effect;
	}

	public InterpolationEf getInterpolationEffect(EAdField<?> var,
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
	public ShowQuestionEf getShowQuestion(String question, int nAnswers) {
		ShowQuestionEf effect = new ShowQuestionEf();
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

	public ChangeFieldEf getChangeVarValueEffect(EAdField<?> var,
			EAdOperation operation) {
		ChangeFieldEf effect = new ChangeFieldEf(var, operation);
		return effect;

	}

	public MakeActiveElementEf getMakeActiveElement(EAdSceneElement element) {
		MakeActiveElementEf effect = new MakeActiveElementEf(element);
		return effect;
	}

	public PlaySoundEf getPlaySound(String string) {
		Sound sound = new Sound(string);
		PlaySoundEf effect = new PlaySoundEf(sound);
		return effect;
	}

}
