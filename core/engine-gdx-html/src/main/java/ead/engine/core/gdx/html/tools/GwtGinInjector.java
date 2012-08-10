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

package ead.engine.core.gdx.html.tools;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

import ead.engine.core.game.Game;
import ead.engine.core.gameobjects.ComposedSceneGOImpl;
import ead.engine.core.gameobjects.SceneGOImpl;
import ead.engine.core.gameobjects.VideoSceneGO;
import ead.engine.core.gameobjects.effects.ActorActionsGO;
import ead.engine.core.gameobjects.effects.AddActorReferenceGO;
import ead.engine.core.gameobjects.effects.CancelEffectGO;
import ead.engine.core.gameobjects.effects.ChangeFieldGO;
import ead.engine.core.gameobjects.effects.ChangeSceneGO;
import ead.engine.core.gameobjects.effects.ComplexBlockingEffectGO;
import ead.engine.core.gameobjects.effects.HighlightSceneElementGO;
import ead.engine.core.gameobjects.effects.InterpolationGO;
import ead.engine.core.gameobjects.effects.ModifyHudGO;
import ead.engine.core.gameobjects.effects.ModifyInventoryGO;
import ead.engine.core.gameobjects.effects.MoveSceneElementGO;
import ead.engine.core.gameobjects.effects.PlaySoundGO;
import ead.engine.core.gameobjects.effects.QuitGameGO;
import ead.engine.core.gameobjects.effects.RandomGO;
import ead.engine.core.gameobjects.effects.ShowSceneElementGO;
import ead.engine.core.gameobjects.effects.SpeakGO;
import ead.engine.core.gameobjects.effects.TriggerMacroGO;
import ead.engine.core.gameobjects.effects.WaitGO;
import ead.engine.core.gameobjects.events.ConditionEvGO;
import ead.engine.core.gameobjects.events.SceneElementEvGO;
import ead.engine.core.gameobjects.events.SystemEvGO;
import ead.engine.core.gameobjects.events.TimedEvGO;
import ead.engine.core.gameobjects.sceneelements.BasicSceneElementGO;
import ead.engine.core.gameobjects.sceneelements.ComplexSceneElementGO;
import ead.engine.core.gameobjects.transitions.BasicTransitionGO;
import ead.engine.core.gameobjects.transitions.DisplaceTransitionGO;
import ead.engine.core.gameobjects.transitions.FadeInTransitionGO;
import ead.engine.core.gdx.gameobjects.GdxApplyForceGO;
import ead.engine.core.gdx.gameobjects.GdxPhysicsEffectGO;
import ead.engine.core.gdx.platform.GdxCanvas;
import ead.engine.core.input.InputHandler;
import ead.engine.core.platform.EngineConfiguration;
import ead.engine.core.platform.FontHandler;
import ead.tools.GenericInjector;
import ead.tools.SceneGraph;
import ead.tools.StringHandler;
import ead.tools.gwt.GWTToolsModule;

@GinModules({ GwtModule.class, GWTToolsModule.class })
public interface GwtGinInjector extends Ginjector {

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

	public RandomGO getRandomEffectGO();

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

	public StringHandler getStringHandler();

	public InputHandler getMouseState();

	public GdxPhysicsEffectGO getPhysicsEffectGO();

	public GdxApplyForceGO getPhApplyForceGO();

	public ComposedSceneGOImpl getComposedSceneGO();

	public ModifyHudGO getModifyHUGGO();

	public GenericInjector getPlayNInjector();

	public EngineConfiguration getEngineConfiguration();

	public GdxCanvas getCanvas();

	public Game getGame();

	public InputHandler getInputHandler();

	public FontHandler getFontHandler();
	
	public SceneGraph getSceneGraph();

	public AddActorReferenceGO getAddActorReferenceGO();

}