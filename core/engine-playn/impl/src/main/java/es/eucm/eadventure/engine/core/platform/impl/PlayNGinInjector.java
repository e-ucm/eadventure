package es.eucm.eadventure.engine.core.platform.impl;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

import es.eucm.eadventure.engine.core.Game;
import es.eucm.eadventure.engine.core.gameobjects.ActorGO;
import es.eucm.eadventure.engine.core.gameobjects.ActorReferenceGO;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.gameobjects.TimerGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.SceneGOImpl;
import es.eucm.eadventure.engine.core.gameobjects.impl.transitions.SimpleTransitionGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.*;
import es.eucm.eadventure.engine.core.gameobjects.impl.events.*;
import es.eucm.eadventure.engine.core.gameobjects.impl.inventory.BasicInventoryGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.sceneelements.*;
import es.eucm.eadventure.engine.core.impl.modules.BasicGameModule;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformLauncher;
import es.eucm.eadventure.engine.core.platform.assets.impl.PlayNEngineImage;
import es.eucm.eadventure.engine.core.platform.impl.extra.PlayNAssetHandlerModule;
import es.eucm.eadventure.engine.core.platform.impl.extra.PlayNAssetRendererModule;
import es.eucm.eadventure.engine.core.platform.impl.extra.PlayNModule;

@GinModules({PlayNAssetHandlerModule.class, PlayNAssetRendererModule.class, PlayNModule.class, BasicGameModule.class})
public interface PlayNGinInjector extends Ginjector {
	
	public PlatformLauncher getPlatformLauncher();

	public Game getGame();
	
	public SimpleTransitionGO getSimpleTransitionGO();
	
	public ActiveElementEffectGO getActiveElementEffectGO();
	public ActorActionsEffectGO getActorActionEffectGO();
	public CancelEffectGO getCancelEffectGO();
	public ChangeAppearanceGO getChangeAppearanceGO();
	public ChangeSceneGO getChangeSceneGO();
	public ChangeFieldGO getChangeFieldGO();
	public ComplexBlockingEffectGO getComplexBlockingEffectGO();
	public HighlightEffectGO getHighlightEffectGO();
	public MakeActiveElementEffectGO getMakeActiveElementEffectGO();
	public ModifyActorStateGO getModifyActorStateGO();
	public MoveActiveElementGO getMoveActiveElementGO();
	public MoveSceneElementGO getMoveSceneElementGO();
	public PlaySoundEffectGO getPlaySoundEffectGO();
	public QuitGameEffectGO getQuitGameEffectGO();
	public RandomEffectGO getRandomEffectGO();
	public ShowSceneElementGO getShowSceneElementGO();
	public SpeakEffectGO getSpeakEffectGO();
	public TriggerMacroEffectGO getTriggerMacroEffectGO();
	public VarInterpolationGO getVarInterpolationGO();
	public WaitEffectGO getWaitEffectGO();
	
	public ConditionEventGO getConditionEventGO();
	public SceneElementEventGO getSceneElementEventGO();
	public SceneElementTimedEventGO getSceneElementTimedEventGO();
	public SystemEventGO getSystemEventGO();
	public TimerEventGO getTimerEventGO();
	
	public ActorGO getActorGO();
	public ActorReferenceGO getActorReferenceGO();
	public BasicSceneElementGO getBasicSceneElementGO();
	public ComplexSceneElementGO getComplexSceneElementGO();
	
	public SceneGOImpl getSceneGO();
	public TimerGO getTimerGO();

	public GUI getGUI();

	public PlayNEngineImage getPlayNEngineImage();

	public AssetHandler getAssetHandler();

	public BasicInventoryGO getBasicInventoryGO();
	
}