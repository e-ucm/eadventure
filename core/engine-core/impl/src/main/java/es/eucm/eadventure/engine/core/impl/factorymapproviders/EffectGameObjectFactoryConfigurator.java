package es.eucm.eadventure.engine.core.impl.factorymapproviders;

import java.util.Map;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.effects.impl.EAdActorActionsEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdCancelEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdChangeAppearance;
import es.eucm.eadventure.common.model.effects.impl.EAdChangeScene;
import es.eucm.eadventure.common.model.effects.impl.EAdComplexBlockingEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdModifyActorState;
import es.eucm.eadventure.common.model.effects.impl.EAdMoveSceneElement;
import es.eucm.eadventure.common.model.effects.impl.EAdQuitGame;
import es.eucm.eadventure.common.model.effects.impl.EAdRandomEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdTriggerMacro;
import es.eucm.eadventure.common.model.effects.impl.EAdVarInterpolationEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdWaitEffect;
import es.eucm.eadventure.common.model.effects.impl.actorreference.EAdHighlightActorReference;
import es.eucm.eadventure.common.model.effects.impl.text.EAdShowText;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeVarValueEffect;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ActorActionsEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.CancelEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ChangeAppearanceGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ChangeScreenGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ChangeVariableGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ComplexBlockingEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.HighlightEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ModifyActorStateGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.MoveSceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.QuitGameEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.RandomEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ShowTextEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.TriggerMacroEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.VarInterpolationGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.WaitEffectGO;

public class EffectGameObjectFactoryConfigurator {
	
	public void configure(
			Map<Class<? extends EAdElement>, Class<? extends GameObject<?>>> factoryMap) {
		factoryMap.put(EAdShowText.class, ShowTextEffectGO.class);
		factoryMap.put(EAdComplexBlockingEffect.class,
				ComplexBlockingEffectGO.class);
		factoryMap.put(EAdChangeScene.class, ChangeScreenGO.class);
		factoryMap.put(EAdMoveSceneElement.class, MoveSceneElementGO.class);
		factoryMap.put(EAdHighlightActorReference.class,
				HighlightEffectGO.class);
		factoryMap.put(EAdWaitEffect.class, WaitEffectGO.class);
		factoryMap.put(EAdCancelEffect.class, CancelEffectGO.class);
		factoryMap.put(EAdChangeVarValueEffect.class, ChangeVariableGO.class);
		factoryMap.put(EAdActorActionsEffect.class, ActorActionsEffectGO.class);
		factoryMap.put(EAdQuitGame.class, QuitGameEffectGO.class);
		factoryMap.put(EAdChangeAppearance.class, ChangeAppearanceGO.class);
		factoryMap.put(EAdTriggerMacro.class, TriggerMacroEffectGO.class);
		factoryMap.put(EAdModifyActorState.class, ModifyActorStateGO.class);
		factoryMap.put(EAdRandomEffect.class, RandomEffectGO.class);
		factoryMap.put(EAdVarInterpolationEffect.class, VarInterpolationGO.class);
	}

}
