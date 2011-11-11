package es.eucm.eadventure.engine.core.platform.impl;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.Game;
import es.eucm.eadventure.engine.core.MouseState;
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
import es.eucm.eadventure.engine.core.impl.modules.BasicGameModule;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;
import es.eucm.eadventure.engine.core.platform.PlatformLauncher;
import es.eucm.eadventure.engine.core.platform.assets.impl.PlayNEngineImage;
import es.eucm.eadventure.engine.core.platform.impl.extra.PlayNAssetHandlerModule;
import es.eucm.eadventure.engine.core.platform.impl.extra.PlayNModule;

@GinModules({PlayNAssetHandlerModule.class, PlayNModule.class, BasicGameModule.class})
public interface PlayNGinInjector extends Ginjector {
	
	public PlatformLauncher getPlatformLauncher();

	public Game getGame();
	
	public SimpleTransitionGO getSimpleTransitionGO();
	
	public ActiveElementEffectGO getActiveElementEffectGO();
	public ActorActionsEffectGO getActorActionEffectGO();
	public CancelEffectGO getCancelEffectGO();
	public ChangeSceneGO getChangeSceneGO();
	public ChangeFieldGO getChangeFieldGO();
	public ComplexBlockingEffectGO getComplexBlockingEffectGO();
	public HighlightEffectGO getHighlightEffectGO();
	public ModifyActorStateGO getModifyActorStateGO();
	public MoveActiveElementGO getMoveActiveElementGO();
	public MoveSceneElementGO getMoveSceneElementGO();
	public PlaySoundEffectGO getPlaySoundEffectGO();
	public QuitGameEffectGO getQuitGameEffectGO();
	public RandomEffectGO getRandomEffectGO();
	public ShowSceneElementGO getShowSceneElementGO();
	public SpeakEffectGO getSpeakEffectGO();
	public TriggerMacroEffectGO getTriggerMacroEffectGO();
	public InterpolationGO getVarInterpolationGO();
	public WaitEffectGO getWaitEffectGO();
	
	public ConditionEventGO getConditionEventGO();
	public SceneElementEventGO getSceneElementEventGO();
	public SceneElementTimedEventGO getSceneElementTimedEventGO();
	public SystemEventGO getSystemEventGO();
	public TimerEventGO getTimerEventGO();
	
	public BasicSceneElementGO getBasicSceneElementGO();
	public ComplexSceneElementGO getComplexSceneElementGO();
	
	public SceneGOImpl getSceneGO();
	public VideoSceneGO getVideoSceneGO();

	public GUI getGUI();

	public PlayNEngineImage getPlayNEngineImage();

	public AssetHandler getAssetHandler();
	
	public StringHandler getStringHandler();

	public MouseState getMouseState();

	public PhysicsEffectGO getPhysicsEffectGO();

	public PhApplyForceGO getPhApplyForceGO();

	public PlatformConfiguration getPlatformConfiguration();
	
	public PlayNInjector getPlayNInjector();
	
}