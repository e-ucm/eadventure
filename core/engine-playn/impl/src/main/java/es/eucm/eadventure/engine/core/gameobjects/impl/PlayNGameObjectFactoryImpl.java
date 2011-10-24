package es.eucm.eadventure.engine.core.gameobjects.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.gwtent.reflection.client.ClassHelper;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.MapProvider;
import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.effects.impl.physics.EAdPhysicsEffect;
import es.eucm.eadventure.common.model.effects.impl.physics.PhApplyImpluse;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.gameobjects.SceneGO;
import es.eucm.eadventure.engine.core.gameobjects.TimerGO;
import es.eucm.eadventure.engine.core.gameobjects.effect.physics.PhApplyForceGO;
import es.eucm.eadventure.engine.core.gameobjects.effect.physics.PhysicsEffectGO;
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
import es.eucm.eadventure.engine.core.platform.impl.PlayNGinInjector;

@Singleton
public class PlayNGameObjectFactoryImpl extends GameObjectFactoryImpl {

	private static final Logger logger = Logger.getLogger("PlayNGameObjectFactoryImpl");
	
	private PlayNGinInjector ginjector;
	
	@Inject
	public PlayNGameObjectFactoryImpl(
			MapProvider<Class<? extends EAdElement>,
			Class<? extends GameObject<?>>> map,
			PlayNGinInjector ginjector) {
		super(map);
		this.ginjector = ginjector;
		// this is horrible
		// Effects
		classMap.put(EAdPhysicsEffect.class, PhysicsEffectGO.class);
		classMap.put(PhApplyImpluse.class, PhApplyForceGO.class);
	}

	@Override
	public GameObject<?> getInstance(Class<? extends GameObject<?>> clazz) {
		if (clazz == SimpleTransitionGO.class)
			return ginjector.getSimpleTransitionGO();
		
		if (clazz == ActiveElementEffectGO.class)
			return ginjector.getActiveElementEffectGO();
		if (clazz == ActorActionsEffectGO.class)
			return ginjector.getActorActionEffectGO();
		if (clazz == CancelEffectGO.class)
			return ginjector.getCancelEffectGO();
		if (clazz == ChangeSceneGO.class)
			return ginjector.getChangeSceneGO();
		if (clazz == ChangeFieldGO.class)
			return ginjector.getChangeFieldGO();
		if (clazz == ComplexBlockingEffectGO.class)
			return ginjector.getComplexBlockingEffectGO();
		if (clazz == HighlightEffectGO.class)
			return ginjector.getHighlightEffectGO();
		if (clazz == ModifyActorStateGO.class)
			return ginjector.getModifyActorStateGO();
		if (clazz == MoveActiveElementGO.class)
			return ginjector.getMoveActiveElementGO();
		if (clazz == MoveSceneElementGO.class)
			return ginjector.getMoveSceneElementGO();
		if (clazz == PlaySoundEffectGO.class)
			return ginjector.getPlaySoundEffectGO();
		if (clazz == QuitGameEffectGO.class)
			return ginjector.getQuitGameEffectGO();
		if (clazz == RandomEffectGO.class)
			return ginjector.getRandomEffectGO();
		if (clazz == ShowSceneElementGO.class)
			return ginjector.getShowSceneElementGO();
		if (clazz == SpeakEffectGO.class)
			return ginjector.getSpeakEffectGO();
		if (clazz == TriggerMacroEffectGO.class)
			return ginjector.getTriggerMacroEffectGO();
		if (clazz == InterpolationGO.class)
			return ginjector.getVarInterpolationGO();
		if (clazz == WaitEffectGO.class)
			return ginjector.getWaitEffectGO();
		if (clazz == PhysicsEffectGO.class)
			return ginjector.getPhysicsEffectGO();
		if (clazz == PhApplyForceGO.class)
			return ginjector.getPhApplyForceGO();
		
		if (clazz == ConditionEventGO.class)
			return ginjector.getConditionEventGO();
		if (clazz == SceneElementEventGO.class)
			return ginjector.getSceneElementEventGO();
		if (clazz == SceneElementTimedEventGO.class)
			return ginjector.getSceneElementTimedEventGO();
		if (clazz == SystemEventGO.class)
			return ginjector.getSystemEventGO();
		if (clazz == TimerEventGO.class)
			return ginjector.getTimerEventGO();	

		if (clazz == BasicSceneElementGO.class)
			return ginjector.getBasicSceneElementGO();
		if (clazz == ComplexSceneElementGO.class)
			return ginjector.getComplexSceneElementGO();
		
		if (clazz == SceneGO.class || clazz == SceneGOImpl.class)
			return ginjector.getSceneGO();
		if (clazz == TimerGO.class)
			return ginjector.getTimerGO();
		return null;
	}

	@Override
	public <T extends EAdElement> Class<?> getRuntimeClass(T element) {
		Class<?> clazz = element.getClass();
		
		Element annotation = null;
		while (annotation == null && clazz != null ) {
			annotation = ClassHelper.AsClass(clazz).getAnnotation(Element.class);
			clazz = clazz.getSuperclass();
		}
		
		if ( annotation == null ){
			logger.log(Level.SEVERE, "No element annotation for class " + element.getClass());
			return null;
		}
		return annotation.runtime();
		
	}

}
