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

import ead.engine.core.assets.drawables.RuntimeCaption;
import ead.engine.core.assets.drawables.RuntimeComposedDrawable;
import ead.engine.core.assets.drawables.RuntimeFilteredDrawable;
import ead.engine.core.assets.drawables.RuntimeFramesAnimation;
import ead.engine.core.assets.drawables.RuntimeImage;
import ead.engine.core.assets.drawables.RuntimeStateDrawable;
import ead.engine.core.assets.drawables.shapes.GdxBezierShape;
import ead.engine.core.assets.drawables.shapes.GdxCircleShape;
import ead.engine.core.assets.drawables.shapes.GdxRectangleShape;
import ead.engine.core.assets.fonts.RuntimeFont;
import ead.engine.core.assets.multimedia.RuntimeSound;
import ead.engine.core.game.interfaces.Game;
import ead.engine.core.gameobjects.debuggers.FieldsDebuggerGO;
import ead.engine.core.gameobjects.debuggers.GhostDebuggerGO;
import ead.engine.core.gameobjects.debuggers.TrajectoryDebuggerGO;
import ead.engine.core.gameobjects.effects.ActorActionsGO;
import ead.engine.core.gameobjects.effects.AddActorReferenceGO;
import ead.engine.core.gameobjects.effects.AddChildGO;
import ead.engine.core.gameobjects.effects.ChangeFieldGO;
import ead.engine.core.gameobjects.effects.ChangeSceneGO;
import ead.engine.core.gameobjects.effects.HighlightSceneElementGO;
import ead.engine.core.gameobjects.effects.InterpolationGO;
import ead.engine.core.gameobjects.effects.ModifyHudGO;
import ead.engine.core.gameobjects.effects.ModifyInventoryGO;
import ead.engine.core.gameobjects.effects.MoveSceneElementGO;
import ead.engine.core.gameobjects.effects.PlaySoundGO;
import ead.engine.core.gameobjects.effects.QuitGameGO;
import ead.engine.core.gameobjects.effects.RandomGO;
import ead.engine.core.gameobjects.effects.RemoveGO;
import ead.engine.core.gameobjects.effects.ShowQuestionGO;
import ead.engine.core.gameobjects.effects.ShowSceneElementGO;
import ead.engine.core.gameobjects.effects.SpeakGO;
import ead.engine.core.gameobjects.effects.TriggerMacroGO;
import ead.engine.core.gameobjects.effects.WaitGO;
import ead.engine.core.gameobjects.events.ConditionEvGO;
import ead.engine.core.gameobjects.events.SceneElementEvGO;
import ead.engine.core.gameobjects.events.SystemEvGO;
import ead.engine.core.gameobjects.events.TimedEvGO;
import ead.engine.core.gameobjects.sceneelements.GhostElementGO;
import ead.engine.core.gameobjects.sceneelements.GroupElementGO;
import ead.engine.core.gameobjects.sceneelements.SceneElementGO;
import ead.engine.core.gameobjects.sceneelements.SceneGO;
import ead.engine.core.gameobjects.sceneelements.VideoSceneGO;
import ead.engine.core.gameobjects.sceneelements.transitions.DisplaceTransitionGO;
import ead.engine.core.gameobjects.sceneelements.transitions.EmptyTransitionGO;
import ead.engine.core.gameobjects.sceneelements.transitions.FadeInTransitionGO;
import ead.engine.core.gameobjects.trajectories.dijkstra.NodeTrajectoryGO;
import ead.engine.core.gameobjects.trajectories.polygon.PolygonTrajectoryGO;
import ead.engine.core.gameobjects.trajectories.simple.SimpleTrajectoryGO;
import ead.engine.core.gameobjects.widgets.TextAreaGO;
import ead.engine.core.gdx.html.platform.module.GwtModule;
import ead.tools.GenericInjector;
import ead.tools.gwt.GWTToolsModule;

@GinModules( { GwtModule.class, GWTToolsModule.class })
public interface GwtGinInjector extends Ginjector {

	// Platform
	ApplicationListener getEngine();

	GenericInjector getGenericInjector();

	Game getGame();

	// Assets

	// ----- Game objects ---- //

	ActorActionsGO getActorActionsGO();

	ShowSceneElementGO getShowSceneElementGO();

	TriggerMacroGO getTriggerMacroGO();

	ShowQuestionGO getShowQuestionGO();

	WaitGO getWaitGO();

	RemoveGO getRemoveGO();

	QuitGameGO getQuitGameGO();

	SpeakGO getSpeakGO();

	InterpolationGO getInterpolationGO();

	HighlightSceneElementGO getHighlightSceneElementGO();

	AddActorReferenceGO getAddActorReferenceGO();

	ChangeSceneGO getChangeSceneGO();

	PlaySoundGO getPlaySoundGO();

	RandomGO getRandomGO();

	ModifyInventoryGO getModifyInventoryGO();

	ModifyHudGO getModifyHudGO();

	MoveSceneElementGO getMoveSceneElementGO();

	AddChildGO getAddChildGO();

	ChangeFieldGO getChangeFieldGO();

	SceneElementEvGO getSceneElementEvGO();

	SystemEvGO getSystemEvGO();

	ConditionEvGO getConditionEvGO();

	TimedEvGO getTimedEvGO();

	GhostDebuggerGO getGhostDebuggerGO();

	SceneElementGO getSceneElementGO();

	EmptyTransitionGO getEmptyTransitionGO();

	TrajectoryDebuggerGO getTrajectoryDebuggerGO();

	DisplaceTransitionGO getDisplaceTransitionGO();

	FieldsDebuggerGO getFieldsDebuggerGO();

	GroupElementGO getGroupElementGO();

	GhostElementGO getGhostElementGO();

	FadeInTransitionGO getFadeInTransitionGO();

	TextAreaGO getTextAreaGO();

	SceneGO getSceneGO();

	VideoSceneGO getVideoSceneGO();

	PolygonTrajectoryGO getPolygonTrajectoryGO();

	SimpleTrajectoryGO getSimpleTrajectoryGO();

	NodeTrajectoryGO getNodeTrajectoryGO();

	// Game object
	RuntimeImage getRuntimeImage();

	GdxBezierShape getGdxBezierShape();

	GdxRectangleShape getGdxRectangleShape();

	GdxCircleShape getGdxCircleShape();

	RuntimeCaption getRuntimeCaption();

	RuntimeSound getRuntimeSound();

	RuntimeComposedDrawable getRuntimeComposedDrawable();

	RuntimeFilteredDrawable getRuntimeFilteredDrawable();

	RuntimeFramesAnimation getRuntimeFramesAnimation();

	RuntimeStateDrawable getRuntimeStateDrawable();

	RuntimeFont getGdxFont();

}
