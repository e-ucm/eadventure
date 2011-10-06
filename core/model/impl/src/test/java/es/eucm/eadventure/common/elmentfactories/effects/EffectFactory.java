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

package es.eucm.eadventure.common.elmentfactories.effects;

import es.eucm.eadventure.common.elmentfactories.EAdElementsFactory;
import es.eucm.eadventure.common.elmentfactories.StringFactory.StringType;
import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.effects.impl.EAdChangeAppearance;
import es.eucm.eadventure.common.model.effects.impl.EAdPlaySoundEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdVarInterpolationEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdVarInterpolationEffect.InterpolationType;
import es.eucm.eadventure.common.model.effects.impl.EAdVarInterpolationEffect.LoopType;
import es.eucm.eadventure.common.model.effects.impl.sceneelements.EAdMakeActiveElementEffect;
import es.eucm.eadventure.common.model.effects.impl.text.EAdShowQuestion;
import es.eucm.eadventure.common.model.effects.impl.text.EAdSpeakEffect;
import es.eucm.eadventure.common.model.effects.impl.timedevents.EAdShowSceneElement;
import es.eucm.eadventure.common.model.effects.impl.timedevents.EAdShowSceneElement.ShowTextAnimation;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeFieldValueEffect;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.EAdOperation;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.params.EAdString;
import es.eucm.eadventure.common.resources.EAdBundleId;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Caption;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.CaptionImpl;
import es.eucm.eadventure.common.resources.assets.multimedia.impl.SoundImpl;

public class EffectFactory {

	private static int ID_GENERATOR = 0;

	public EAdChangeAppearance getChangeAppearance(EAdElement element,
			EAdBundleId bundle) {
		EAdChangeAppearance effect = new EAdChangeAppearance("changeAppearance"
				+ ID_GENERATOR++, element, bundle);
		return effect;
	}

	public EAdVarInterpolationEffect getInterpolationEffect(EAdField<?> var,
			float startValue, float endValue, int time, LoopType loop,
			InterpolationType interpolationType) {
		EAdVarInterpolationEffect interpolation = new EAdVarInterpolationEffect(
				"interpolationEffect" + ID_GENERATOR++, var, startValue,
				endValue, time, loop, interpolationType);
		return interpolation;
	}

	public EAdShowSceneElement getShowText(String text, int x, int y,
			ShowTextAnimation animation, int maximumHeight) {
		EAdShowSceneElement effect = new EAdShowSceneElement();
		CaptionImpl c = EAdElementsFactory.getInstance().getCaptionFactory()
				.createCaption(text);
		c.setMaximumHeight(maximumHeight);
		effect.setCaption(c, x, y, animation);
		return effect;
	}

	public EAdShowSceneElement getShowText(String text, int x, int y,
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
	public EAdShowQuestion getShowQuestion(String question, int nAnswers) {
		EAdShowQuestion effect = new EAdShowQuestion();
		EAdElementsFactory.getInstance().getStringFactory().setString(effect.getQuestion(), question);
		
		for (int i = 0; i < nAnswers; i++) {
			int ordinal = i % StringType.values().length;
			EAdString answerString = EAdElementsFactory.getInstance()
					.getStringFactory().getString(StringType.values()[ordinal]);
			effect.addAnswer(answerString, new EAdSpeakEffect("tal"));
		}
		effect.setUpNewInstance();
		return effect;

	}

	public EAdShowSceneElement getShowText(String text, int x, int y) {
		return this.getShowText(text, x, y, ShowTextAnimation.NONE);
	}

	public EAdChangeFieldValueEffect getChangeVarValueEffect(EAdField<?> var,
			EAdOperation operation) {
		EAdChangeFieldValueEffect effect = new EAdChangeFieldValueEffect(
				"changeVarValue" + ID_GENERATOR++, var, operation);
		return effect;

	}

	public EAdSpeakEffect getSpeakEffect(String text,
			EAdSceneElement sceneElement) {
		EAdSpeakEffect effect = new EAdSpeakEffect("speakEffect"
				+ ID_GENERATOR++);
		EAdElementsFactory.getInstance().getStringFactory().setString(effect.getString(), text);

		effect.setPosition(new EAdFieldImpl<Integer>(sceneElement,
				EAdBasicSceneElement.VAR_X), new EAdFieldImpl<Integer>(
				sceneElement, EAdBasicSceneElement.VAR_Y),
				new EAdFieldImpl<Float>(sceneElement,
						EAdBasicSceneElement.VAR_DISP_X),
				new EAdFieldImpl<Float>(sceneElement,
						EAdBasicSceneElement.VAR_DISP_Y));
		effect.setStateVar(new EAdFieldImpl<String>(sceneElement,
				EAdBasicSceneElement.VAR_STATE));

		return effect;
	}

	public EAdMakeActiveElementEffect getMakeActiveElement(
			EAdSceneElement element) {
		EAdMakeActiveElementEffect effect = new EAdMakeActiveElementEffect(
				"makeActive");
		effect.setSceneElement(element);
		return effect;
	}

	public EAdPlaySoundEffect getPlaySound(String string) {
		SoundImpl sound = new SoundImpl(string);
		return new EAdPlaySoundEffect("playSound" + ID_GENERATOR++, sound);
	}

}
