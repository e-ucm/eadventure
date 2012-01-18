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

package ead.engine.core.platform;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

import ead.common.resources.StringHandler;
import ead.engine.core.game.Game;
import ead.engine.core.gameobjects.ComposedSceneGOImpl;
import ead.engine.core.gameobjects.SceneGOImpl;
import ead.engine.core.gameobjects.VideoSceneGO;
import ead.engine.core.gameobjects.effects.ActorActionsGO;
import ead.engine.core.gameobjects.effects.CancelEffectGO;
import ead.engine.core.gameobjects.effects.ChangeFieldGO;
import ead.engine.core.gameobjects.effects.ChangeSceneGO;
import ead.engine.core.gameobjects.effects.ComplexBlockingEffectGO;
import ead.engine.core.gameobjects.effects.HighlightSceneElementGO;
import ead.engine.core.gameobjects.effects.InterpolationGO;
import ead.engine.core.gameobjects.effects.ModifyInventoryGO;
import ead.engine.core.gameobjects.effects.MoveSceneElementGO;
import ead.engine.core.gameobjects.effects.PlaySoundGO;
import ead.engine.core.gameobjects.effects.QuitGameGO;
import ead.engine.core.gameobjects.effects.RandomEffectGO;
import ead.engine.core.gameobjects.effects.ShowSceneElementGO;
import ead.engine.core.gameobjects.effects.SpeakGO;
import ead.engine.core.gameobjects.effects.TriggerMacroGO;
import ead.engine.core.gameobjects.effects.WaitGO;
import ead.engine.core.gameobjects.effects.physics.PhApplyForceGO;
import ead.engine.core.gameobjects.effects.physics.PhysicsEffectGO;
import ead.engine.core.gameobjects.events.ConditionEvGO;
import ead.engine.core.gameobjects.events.SceneElementEvGO;
import ead.engine.core.gameobjects.events.SystemEvGO;
import ead.engine.core.gameobjects.events.TimedEvGO;
import ead.engine.core.gameobjects.sceneelements.BasicSceneElementGO;
import ead.engine.core.gameobjects.sceneelements.ComplexSceneElementGO;
import ead.engine.core.gameobjects.transitions.BasicTransitionGO;
import ead.engine.core.gameobjects.transitions.DisplaceTransitionGO;
import ead.engine.core.gameobjects.transitions.FadeInTransitionGO;
import ead.engine.core.input.InputHandler;
import ead.engine.core.inventory.InventoryHandler;
import ead.engine.core.modules.BasicGameModule;
import ead.engine.core.platform.assets.PlayNEngineImage;
import ead.engine.core.platform.extra.PlayNAssetHandlerModule;
import ead.engine.core.platform.extra.PlayNModule;

@GinModules({PlayNAssetHandlerModule.class, PlayNModule.class, BasicGameModule.class})
public interface PlayNGinInjector extends Ginjector {
	
	public PlatformLauncher getPlatformLauncher();

	public Game getGame();
	
	public BasicTransitionGO getSimpleTransitionGO();
	public DisplaceTransitionGO getDisplaceTransitionGO();
	public FadeInTransitionGO getFadeInTransitionGO();
	
	
	public ActorActionsGO getActorActionEffectGO();
	public CancelEffectGO getCancelEffectGO();
	public ChangeSceneGO getChangeSceneGO();
	public ChangeFieldGO getChangeFieldGO();
	public ComplexBlockingEffectGO getComplexBlockingEffectGO();
	public HighlightSceneElementGO getHighlightEffectGO();
	public ModifyInventoryGO getModifyActorStateGO();
	public MoveSceneElementGO getMoveSceneElementGO();
	public PlaySoundGO getPlaySoundEffectGO();
	public QuitGameGO getQuitGameEffectGO();
	public RandomEffectGO getRandomEffectGO();
	public ShowSceneElementGO getShowSceneElementGO();
	public SpeakGO getSpeakEffectGO();
	public TriggerMacroGO getTriggerMacroEffectGO();
	public InterpolationGO getVarInterpolationGO();
	public WaitGO getWaitEffectGO();
	
	public ConditionEvGO getConditionEventGO();
	public SceneElementEvGO getSceneElementEventGO();
	public TimedEvGO getSceneElementTimedEventGO();
	public SystemEvGO getSystemEventGO();
	
	public BasicSceneElementGO getBasicSceneElementGO();
	public ComplexSceneElementGO getComplexSceneElementGO();
	
	public SceneGOImpl getSceneGO();
	public VideoSceneGO getVideoSceneGO();

	public GUI getGUI();

	public PlayNEngineImage getPlayNEngineImage();

	public AssetHandler getAssetHandler();
	
	public StringHandler getStringHandler();

	public InputHandler getMouseState();

	public PhysicsEffectGO getPhysicsEffectGO();

	public PhApplyForceGO getPhApplyForceGO();

	public EngineConfiguration getPlatformConfiguration();
	
	public PlayNInjector getPlayNInjector();
	
	public InventoryHandler getInventoryHandler();

	public ComposedSceneGOImpl getComposedSceneGO();
	
}