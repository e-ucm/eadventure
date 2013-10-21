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

package es.eucm.ead.engine.html.platform.injection;

import com.badlogic.gdx.ApplicationListener;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import es.eucm.ead.engine.assets.drawables.*;
import es.eucm.ead.engine.assets.drawables.shapes.GdxBezierShape;
import es.eucm.ead.engine.assets.drawables.shapes.GdxCircleShape;
import es.eucm.ead.engine.assets.drawables.shapes.GdxRectangleShape;
import es.eucm.ead.engine.assets.fonts.RuntimeFont;
import es.eucm.ead.engine.assets.multimedia.RuntimeMusic;
import es.eucm.ead.engine.assets.multimedia.RuntimeSound;
import es.eucm.ead.engine.game.Game;
import es.eucm.ead.engine.gameobjects.debuggers.GhostDebuggerGO;
import es.eucm.ead.engine.gameobjects.debuggers.ProfilerDebuggerGO;
import es.eucm.ead.engine.gameobjects.debuggers.TrajectoryDebuggerGO;
import es.eucm.ead.engine.gameobjects.effects.*;
import es.eucm.ead.engine.gameobjects.effects.sceneelement.ChangeColorGO;
import es.eucm.ead.engine.gameobjects.events.ConditionEvGO;
import es.eucm.ead.engine.gameobjects.events.SceneElementEvGO;
import es.eucm.ead.engine.gameobjects.events.TimedEvGO;
import es.eucm.ead.engine.gameobjects.events.WatchFieldEvGO;
import es.eucm.ead.engine.gameobjects.sceneelements.*;
import es.eucm.ead.engine.gameobjects.sceneelements.huds.MouseHudGO;
import es.eucm.ead.engine.gameobjects.sceneelements.transitions.*;
import es.eucm.ead.engine.gameobjects.trajectories.dijkstra.NodeTrajectoryGO;
import es.eucm.ead.engine.gameobjects.trajectories.polygon.PolygonTrajectoryGO;
import es.eucm.ead.engine.gameobjects.trajectories.simple.SimpleTrajectoryGO;
import es.eucm.ead.engine.gameobjects.widgets.TextAreaGO;
import es.eucm.ead.engine.html.platform.module.GwtModule;
import es.eucm.ead.legacyplugins.engine.BubbleNameGO;
import es.eucm.ead.legacyplugins.engine.events.TimerGO;
import es.eucm.ead.legacyplugins.engine.sceneelements.ClockDisplayGO;
import es.eucm.ead.legacyplugins.engine.sceneelements.DynamicSceneGO;
import es.eucm.ead.tools.GenericInjector;
import es.eucm.ead.tools.gwt.GWTToolsModule;

@GinModules( { GwtModule.class, GWTToolsModule.class })
public interface GwtGinInjector extends Ginjector {

	// Platform
	ApplicationListener getEngine();

	GenericInjector getGenericInjector();

	Game getGame();

	// Effects
	TogglePauseGO getTogglePauseGO();

	ShowSceneElementGO getShowSceneElementGO();

	LoadGameGO getLoadGameGO();

	RandomGO getRandomGO();

	ModifyHudGO getModifyHudGO();

	ChangeSceneGO getChangeSceneGO();

	RemoveGO getRemoveGO();

	SpeakGO getSpeakGO();

	ActorActionsGO getActorActionsGO();

	PhysicsEffectGO getPhysicsEffectGO();

	ChangeFieldGO getChangeFieldGO();

	ChangeChapterGO getChangeChapterGO();

	DragGO getDragGO();

	QuitGameGO getQuitGameGO();

	AddChildGO getAddChildGO();

	EmptyEffectGO getEmptyEffectGO();

	WaitUntilGO getWaitUntilGO();

	QuestionGO getQuestionGO();

	ToggleSoundGO getToggleSoundGO();

	PlayMusicGO getPlayMusicGO();

	HighlightSceneElementGO getHighlightSceneElementGO();

	ChangeColorGO getChangeColorGO();

	WaitGO getWaitGO();

	TriggerMacroGO getTriggerMacroGO();

	MoveSceneElementGO getMoveSceneElementGO();

	InterpolationGO getInterpolationGO();

	ModifyInventoryGO getModifyInventoryGO();

	AddActorReferenceGO getAddActorReferenceGO();

	PlaySoundGO getPlaySoundGO();

	ApplyForceGO getApplyForceGO();

	// Events
	WatchFieldEvGO getWatchFieldEvGO();

	SceneElementEvGO getSceneElementEvGO();

	TimedEvGO getTimedEvGO();

	ConditionEvGO getConditionEvGO();

	// SeneElements
	GhostDebuggerGO getGhostDebuggerGO();

	SceneElementGO getSceneElementGO();

	GroupElementGO getGroupElementGO();

	MaskTransitionGO getMaskTransitionGO();

	SceneGO getSceneGO();

	TrajectoryDebuggerGO getTrajectoryDebuggerGO();

	ScaleTransitionGO getScaleTransitionGO();

	VideoSceneGO getVideoSceneGO();

	DisplaceTransitionGO getDisplaceTransitionGO();

	ProfilerDebuggerGO getProfilerDebuggerGO();

	FadeInTransitionGO getFadeInTransitionGO();

	GhostElementGO getGhostElementGO();

	MouseHudGO getMouseHudGO();

	TextAreaGO getTextAreaGO();

	EmptyTransitionGO getEmptyTransitionGO();

	// Trajectories
	SimpleTrajectoryGO getSimpleTrajectoryGO();

	NodeTrajectoryGO getNodeTrajectoryGO();

	PolygonTrajectoryGO getPolygonTrajectoryGO();

	// Assets
	GdxCircleShape getGdxCircleShape();

	RuntimeCaption getRuntimeCaption();

	RuntimeImage getRuntimeImage();

	RuntimeFont getRuntimeFont();

	RuntimeStateDrawable getRuntimeStateDrawable();

	RuntimeFilteredDrawable getRuntimeFilteredDrawable();

	GdxBezierShape getGdxBezierShape();

	RuntimeNinePatchImage getRuntimeNinePatchImage();

	RuntimeMusic getRuntimeMusic();

	RuntimeSound getRuntimeSound();

	RuntimeComposedDrawable getRuntimeComposedDrawable();

	RuntimeFramesAnimation getRuntimeFramesAnimation();

	GdxRectangleShape getGdxRectangleShape();

	// Plugins
	// FIXME This CAN NOT be here
	BubbleNameGO getBubbleNameGO();

	DynamicSceneGO getDynamicSceneGO();

	TimerGO getTimerGO();

	ClockDisplayGO getClockDisplayGO();
}
