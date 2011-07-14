package es.eucm.eadventure.common.elmentfactories.effects;

import es.eucm.eadventure.common.elmentfactories.EAdElementsFactory;
import es.eucm.eadventure.common.elmentfactories.StringFactory.StringType;
import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.effects.impl.EAdChangeAppearance;
import es.eucm.eadventure.common.model.effects.impl.EAdVarInterpolationEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdVarInterpolationEffect.LoopType;
import es.eucm.eadventure.common.model.effects.impl.text.EAdShowQuestion;
import es.eucm.eadventure.common.model.effects.impl.text.EAdShowText;
import es.eucm.eadventure.common.model.effects.impl.text.EAdShowText.ShowTextAnimation;
import es.eucm.eadventure.common.model.effects.impl.text.extra.Answer;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeVarValueEffect;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.variables.EAdOperation;
import es.eucm.eadventure.common.model.variables.EAdVar;
import es.eucm.eadventure.common.resources.EAdBundleId;
import es.eucm.eadventure.common.resources.assets.drawable.Caption;
import es.eucm.eadventure.common.resources.assets.drawable.impl.CaptionImpl;

public class EffectFactory {

	private static int ID_GENERATOR = 0;

	public EAdChangeAppearance getChangeAppearance(EAdElement element,
			EAdBundleId bundle) {
		EAdChangeAppearance effect = new EAdChangeAppearance("changeAppearance"
				+ ID_GENERATOR++, element, bundle);
		return effect;
	}

	public EAdVarInterpolationEffect getInterpolationEffect(EAdVar<?> var,
			float startValue, float endValue, int time, LoopType loop) {
		EAdVarInterpolationEffect interpolation = new EAdVarInterpolationEffect(
				"interpolationEffect" + ID_GENERATOR++, var, startValue, endValue, time, loop);
		return interpolation;
	}

	public EAdShowText getShowText(String text, int x, int y,
			ShowTextAnimation animation, int maximumHeight) {
		EAdShowText effect = new EAdShowText();
		CaptionImpl c = EAdElementsFactory.getInstance().getCaptionFactory()
				.createCaption(text);
		c.setMaximumHeight(maximumHeight);
		effect.setCaption(c, x, y, animation);
		return effect;
	}
	
	public EAdShowText getShowText(String text, int x, int y,
			ShowTextAnimation animation){
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
		effect.setQuestion(EAdElementsFactory.getInstance()
				.getSceneElementFactory().createSceneElement(question, 10, 10));
		for (int i = 0; i < nAnswers; i++) {
			int ordinal = i % StringType.values().length;
			Answer a = new Answer("answer" + ID_GENERATOR++);
			String s = StringType.values()[ordinal].getString();
			a.getResources().addAsset(
					a.getInitialBundle(),
					EAdBasicSceneElement.appearance,
					EAdElementsFactory.getInstance().getCaptionFactory()
							.createCaption(s));
			effect.getAnswers().add(a);
		}
		effect.setUpNewInstance();
		return effect;

	}

	public EAdShowText getShowText(String text, int x, int y) {
		return this.getShowText(text, x, y, ShowTextAnimation.NONE);
	}

	public EAdChangeVarValueEffect getChangeVarValueEffect(EAdVar<?> var,
			EAdOperation operation) {
		EAdChangeVarValueEffect effect = new EAdChangeVarValueEffect( "changeVarValue" + ID_GENERATOR++, var, operation );
		return effect;
		
	}

}
