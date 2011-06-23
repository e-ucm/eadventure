package es.eucm.eadventure.common.elmentfactories.effects;

import es.eucm.eadventure.common.elmentfactories.EAdElementsFactory;
import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.effects.impl.EAdChangeAppearance;
import es.eucm.eadventure.common.model.effects.impl.EAdVarInterpolationEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdVarInterpolationEffect.LoopType;
import es.eucm.eadventure.common.model.effects.impl.text.EAdShowText;
import es.eucm.eadventure.common.model.effects.impl.text.EAdShowText.ShowTextAnimation;
import es.eucm.eadventure.common.model.variables.EAdVar;
import es.eucm.eadventure.common.resources.EAdBundleId;

public class EffectFactory {
	
	private static int ID_GENERATOR = 0;
	
	public EAdChangeAppearance getChangeAppearance( EAdElement element, EAdBundleId bundle ){
		EAdChangeAppearance effect = new EAdChangeAppearance( "changeAppearance" + ID_GENERATOR++, element, bundle );
		return effect;
	}
	
	public EAdVarInterpolationEffect getInterpolationEffect( EAdVar<?> var, float startValue, float endValue, int time, LoopType loop){
		EAdVarInterpolationEffect interpolation = new EAdVarInterpolationEffect("interpolationEffect" + ID_GENERATOR++ );
		interpolation.setInterpolation(var, startValue, endValue, time, loop);
		return interpolation;
	}
	
	public EAdShowText getShowText( String text, int x, int y, ShowTextAnimation animation ){
		EAdShowText effect = new EAdShowText( );
		effect.setCaption(EAdElementsFactory.getInstance().getCaptionFactory().createCaption(text), x, y, animation );
		return effect;
	}
	
	public EAdShowText getShowText( String text, int x, int y ){
		return this.getShowText(text, x, y, ShowTextAnimation.NONE);
	}

}
