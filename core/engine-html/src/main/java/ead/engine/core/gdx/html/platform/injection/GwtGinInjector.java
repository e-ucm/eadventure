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

package ead.engine.core.gdx.html.platform.injection;

import com.badlogic.gdx.ApplicationListener;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import ead.engine.core.assets.drawables.*;
import ead.engine.core.assets.drawables.shapes.GdxBezierShape;
import ead.engine.core.assets.drawables.shapes.GdxCircleShape;
import ead.engine.core.assets.drawables.shapes.GdxRectangleShape;
import ead.engine.core.assets.fonts.RuntimeFont;
import ead.engine.core.assets.multimedia.RuntimeSound;
import ead.engine.core.game.interfaces.Game;
import ead.engine.core.gameobjects.debuggers.FieldsDebuggerGO;
import ead.engine.core.gameobjects.debuggers.GhostDebuggerGO;
import ead.engine.core.gameobjects.debuggers.ProfilerDebuggerGO;
import ead.engine.core.gameobjects.debuggers.TrajectoryDebuggerGO;
import ead.engine.core.gameobjects.effects.*;
import ead.engine.core.gameobjects.effects.sceneelement.ChangeColorGO;
import ead.engine.core.gameobjects.events.ConditionEvGO;
import ead.engine.core.gameobjects.events.SceneElementEvGO;
import ead.engine.core.gameobjects.events.TimedEvGO;
import ead.engine.core.gameobjects.events.WatchFieldEvGO;
import ead.engine.core.gameobjects.sceneelements.*;
import ead.engine.core.gameobjects.sceneelements.huds.MouseHudGO;
import ead.engine.core.gameobjects.sceneelements.transitions.*;
import ead.engine.core.gameobjects.trajectories.dijkstra.NodeTrajectoryGO;
import ead.engine.core.gameobjects.trajectories.polygon.PolygonTrajectoryGO;
import ead.engine.core.gameobjects.trajectories.simple.SimpleTrajectoryGO;
import ead.engine.core.gameobjects.widgets.TextAreaGO;
import ead.engine.core.gdx.html.platform.module.GwtModule;
import ead.plugins.engine.bubbledescription.BubbleNameGO;
import ead.tools.GenericInjector;
import ead.tools.gwt.GWTToolsModule;

@GinModules( { GwtModule.class, GWTToolsModule.class })
public interface GwtGinInjector extends Ginjector {

	// Platform
	ApplicationListener getEngine();

	GenericInjector getGenericInjector();

	Game getGame();

	// Effects
	ModifyHudGO getModifyHudGO();

	ModifyInventoryGO getModifyInventoryGO();

	AddActorReferenceGO getAddActorReferenceGO();

	PhysicsEffectGO getPhysicsEffectGO();

	LoadGameGO getLoadGameGO();

	HighlightSceneElementGO getHighlightSceneElementGO();

	ShowSceneElementGO getShowSceneElementGO();

	DragGO getDragGO();

	ToggleSoundGO getToggleSoundGO();

	TogglePauseGO getTogglePauseGO();

	RandomGO getRandomGO();

	RemoveGO getRemoveGO();

	QuitGameGO getQuitGameGO();

	ApplyForceGO getApplyForceGO();

	MoveSceneElementGO getMoveSceneElementGO();

	InterpolationGO getInterpolationGO();

	WaitGO getWaitGO();

	EmptyEffectGO getEmptyEffectGO();

	AddChildGO getAddChildGO();

	ActorActionsGO getActorActionsGO();

	TriggerMacroGO getTriggerMacroGO();

	WaitUntilGO getWaitUntilGO();

	SpeakGO getSpeakGO();

	ChangeFieldGO getChangeFieldGO();

	PlaySoundGO getPlaySoundGO();

	ChangeSceneGO getChangeSceneGO();

	QuestionGO getQuestionGO();

	ChangeColorGO getChangeColorGO();

	// Events
	TimedEvGO getTimedEvGO();

	WatchFieldEvGO getWatchFieldEvGO();

	ConditionEvGO getConditionEvGO();

	SceneElementEvGO getSceneElementEvGO();

	// SeneElements
	VideoSceneGO getVideoSceneGO();

	EmptyTransitionGO getEmptyTransitionGO();

	ProfilerDebuggerGO getProfilerDebuggerGO();

	MouseHudGO getMouseHudGO();

	DisplaceTransitionGO getDisplaceTransitionGO();

	SceneElementGO getSceneElementGO();

	SceneGO getSceneGO();

	MaskTransitionGO getMaskTransitionGO();

	GroupElementGO getGroupElementGO();

	TextAreaGO getTextAreaGO();

	GhostElementGO getGhostElementGO();

	FieldsDebuggerGO getFieldsDebuggerGO();

	FadeInTransitionGO getFadeInTransitionGO();

	TrajectoryDebuggerGO getTrajectoryDebuggerGO();

	GhostDebuggerGO getGhostDebuggerGO();

	ScaleTransitionGO getScaleTransitionGO();

	// Trajectories
	NodeTrajectoryGO getNodeTrajectoryGO();

	SimpleTrajectoryGO getSimpleTrajectoryGO();

	PolygonTrajectoryGO getPolygonTrajectoryGO();

	// Assets
	GdxRectangleShape getGdxRectangleShape();

	RuntimeCaption getRuntimeCaption();

	RuntimeFont getRuntimeFont();

	RuntimeComposedDrawable getRuntimeComposedDrawable();

	RuntimeFramesAnimation getRuntimeFramesAnimation();

	RuntimeStateDrawable getRuntimeStateDrawable();

	GdxCircleShape getGdxCircleShape();

	RuntimeNinePatchImage getRuntimeNinePatchImage();

	RuntimeSound getRuntimeSound();

	GdxBezierShape getGdxBezierShape();

	RuntimeFilteredDrawable getRuntimeFilteredDrawable();

	RuntimeImage getRuntimeImage();

	// Plugins
	// FIXME This CAN NOT be here
	BubbleNameGO getBubbleNameGO();

}
