package es.eucm.eadventure.engine.core.platform.impl;

import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.gameobjects.SceneGO;
import es.eucm.eadventure.engine.core.gameobjects.TimerGO;
import es.eucm.eadventure.engine.core.gameobjects.effect.physics.PhApplyForceGO;
import es.eucm.eadventure.engine.core.gameobjects.effect.physics.PhysicsEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.SceneGOImpl;
import es.eucm.eadventure.engine.core.gameobjects.impl.VideoSceneGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ActiveElementEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ActorActionsEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.CancelEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ChangeFieldGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ChangeSceneGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ComplexBlockingEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.HighlightEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.InterpolationGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ModifyActorStateGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.MoveActiveElementGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.MoveSceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.PlaySoundEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.QuitGameEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.RandomEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ShowSceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.SpeakEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.TriggerMacroEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.WaitEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.events.ConditionEventGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.events.SceneElementEventGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.events.SceneElementTimedEventGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.events.SystemEventGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.events.TimerEventGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.sceneelements.BasicSceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.sceneelements.ComplexSceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.transitions.SimpleTransitionGO;
import es.eucm.eadventure.engine.core.platform.EAdInjector;

public class PlayNInjector implements EAdInjector {

	private PlayNGinInjector ginjector;

	public PlayNInjector(PlayNGinInjector ginjector) {
		this.ginjector = ginjector;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getInstance(Class<T> clazz) {
		GameObject<?> go = null;
		if (clazz == SimpleTransitionGO.class)
			go = ginjector.getSimpleTransitionGO();
		else if (clazz == ActiveElementEffectGO.class)
			go = ginjector.getActiveElementEffectGO();
		else if (clazz == ActorActionsEffectGO.class)
			go = ginjector.getActorActionEffectGO();
		else if (clazz == CancelEffectGO.class)
			go = ginjector.getCancelEffectGO();
		else if (clazz == ChangeSceneGO.class)
			go = ginjector.getChangeSceneGO();
		else if (clazz == ChangeFieldGO.class)
			go = ginjector.getChangeFieldGO();
		else if (clazz == ComplexBlockingEffectGO.class)
			go = ginjector.getComplexBlockingEffectGO();
		else if (clazz == HighlightEffectGO.class)
			go = ginjector.getHighlightEffectGO();
		else if (clazz == ModifyActorStateGO.class)
			go = ginjector.getModifyActorStateGO();
		else if (clazz == MoveActiveElementGO.class)
			go = ginjector.getMoveActiveElementGO();
		else if (clazz == MoveSceneElementGO.class)
			go = ginjector.getMoveSceneElementGO();
		else if (clazz == PlaySoundEffectGO.class)
			go = ginjector.getPlaySoundEffectGO();
		else if (clazz == QuitGameEffectGO.class)
			go = ginjector.getQuitGameEffectGO();
		else if (clazz == RandomEffectGO.class)
			go = ginjector.getRandomEffectGO();
		else if (clazz == ShowSceneElementGO.class)
			go = ginjector.getShowSceneElementGO();
		else if (clazz == SpeakEffectGO.class)
			go = ginjector.getSpeakEffectGO();
		else if (clazz == TriggerMacroEffectGO.class)
			go = ginjector.getTriggerMacroEffectGO();
		else if (clazz == InterpolationGO.class)
			go = ginjector.getVarInterpolationGO();
		else if (clazz == WaitEffectGO.class)
			go = ginjector.getWaitEffectGO();
		else if (clazz == PhysicsEffectGO.class)
			go = ginjector.getPhysicsEffectGO();
		else if (clazz == PhApplyForceGO.class)
			go = ginjector.getPhApplyForceGO();
		else if (clazz == ConditionEventGO.class)
			go = ginjector.getConditionEventGO();
		else if (clazz == SceneElementEventGO.class)
			go = ginjector.getSceneElementEventGO();
		else if (clazz == SceneElementTimedEventGO.class)
			go = ginjector.getSceneElementTimedEventGO();
		else if (clazz == SystemEventGO.class)
			go = ginjector.getSystemEventGO();
		else if (clazz == TimerEventGO.class)
			go = ginjector.getTimerEventGO();
		else if (clazz == BasicSceneElementGO.class)
			go = ginjector.getBasicSceneElementGO();
		else if (clazz == ComplexSceneElementGO.class)
			go = ginjector.getComplexSceneElementGO();
		else if (clazz == SceneGO.class || clazz == SceneGOImpl.class)
			go = ginjector.getSceneGO();
		else if (clazz == TimerGO.class)
			go = ginjector.getTimerGO();
		else if (clazz == VideoSceneGO.class)
			go = ginjector.getVideoSceneGO();
		return (T) go;
	}

}
