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

import es.eucm.ead.engine.assets.drawables.*;
import es.eucm.ead.engine.assets.drawables.shapes.GdxBezierShape;
import es.eucm.ead.engine.assets.drawables.shapes.GdxCircleShape;
import es.eucm.ead.engine.assets.drawables.shapes.GdxRectangleShape;
import es.eucm.ead.engine.assets.fonts.RuntimeFont;
import es.eucm.ead.engine.assets.multimedia.RuntimeSound;
import es.eucm.ead.engine.gameobjects.debuggers.FieldsDebuggerGO;
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
import es.eucm.ead.legacyplugins.engine.BubbleNameGO;
import es.eucm.ead.tools.gwt.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InjectorHelper implements Injector {

	static private Logger logger = LoggerFactory
			.getLogger(InjectorHelper.class);

	private GwtGinInjector ginjector;

	public InjectorHelper(GwtGinInjector injector) {
		this.ginjector = injector;
	}

	public void setInjector(GwtGinInjector ginjector) {
		this.ginjector = ginjector;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getInstance(Class<T> clazz) {
		Object o = null;
		// Game objects
		// Effects
		if (clazz == ModifyHudGO.class)
			o = ginjector.getModifyHudGO();
		else if (clazz == ModifyInventoryGO.class)
			o = ginjector.getModifyInventoryGO();
		else if (clazz == AddActorReferenceGO.class)
			o = ginjector.getAddActorReferenceGO();
		else if (clazz == PhysicsEffectGO.class)
			o = ginjector.getPhysicsEffectGO();
		else if (clazz == LoadGameGO.class)
			o = ginjector.getLoadGameGO();
		else if (clazz == HighlightSceneElementGO.class)
			o = ginjector.getHighlightSceneElementGO();
		else if (clazz == ShowSceneElementGO.class)
			o = ginjector.getShowSceneElementGO();
		else if (clazz == DragGO.class)
			o = ginjector.getDragGO();
		else if (clazz == ToggleSoundGO.class)
			o = ginjector.getToggleSoundGO();
		else if (clazz == TogglePauseGO.class)
			o = ginjector.getTogglePauseGO();
		else if (clazz == RandomGO.class)
			o = ginjector.getRandomGO();
		else if (clazz == RemoveGO.class)
			o = ginjector.getRemoveGO();
		else if (clazz == QuitGameGO.class)
			o = ginjector.getQuitGameGO();
		else if (clazz == ApplyForceGO.class)
			o = ginjector.getApplyForceGO();
		else if (clazz == MoveSceneElementGO.class)
			o = ginjector.getMoveSceneElementGO();
		else if (clazz == InterpolationGO.class)
			o = ginjector.getInterpolationGO();
		else if (clazz == WaitGO.class)
			o = ginjector.getWaitGO();
		else if (clazz == EmptyEffectGO.class)
			o = ginjector.getEmptyEffectGO();
		else if (clazz == AddChildGO.class)
			o = ginjector.getAddChildGO();
		else if (clazz == ActorActionsGO.class)
			o = ginjector.getActorActionsGO();
		else if (clazz == TriggerMacroGO.class)
			o = ginjector.getTriggerMacroGO();
		else if (clazz == WaitUntilGO.class)
			o = ginjector.getWaitUntilGO();
		else if (clazz == SpeakGO.class)
			o = ginjector.getSpeakGO();
		else if (clazz == ChangeFieldGO.class)
			o = ginjector.getChangeFieldGO();
		else if (clazz == PlaySoundGO.class)
			o = ginjector.getPlaySoundGO();
		else if (clazz == ChangeSceneGO.class)
			o = ginjector.getChangeSceneGO();
		else if (clazz == QuestionGO.class)
			o = ginjector.getQuestionGO();
		else if (clazz == ChangeColorGO.class)
			o = ginjector.getChangeColorGO();
		// SceneElements
		else if (clazz == TimedEvGO.class)
			o = ginjector.getTimedEvGO();
		else if (clazz == WatchFieldEvGO.class)
			o = ginjector.getWatchFieldEvGO();
		else if (clazz == ConditionEvGO.class)
			o = ginjector.getConditionEvGO();
		else if (clazz == SceneElementEvGO.class)
			o = ginjector.getSceneElementEvGO();
		// Events
		else if (clazz == VideoSceneGO.class)
			o = ginjector.getVideoSceneGO();
		else if (clazz == EmptyTransitionGO.class)
			o = ginjector.getEmptyTransitionGO();
		else if (clazz == ProfilerDebuggerGO.class)
			o = ginjector.getProfilerDebuggerGO();
		else if (clazz == MouseHudGO.class)
			o = ginjector.getMouseHudGO();
		else if (clazz == DisplaceTransitionGO.class)
			o = ginjector.getDisplaceTransitionGO();
		else if (clazz == SceneElementGO.class)
			o = ginjector.getSceneElementGO();
		else if (clazz == SceneGO.class)
			o = ginjector.getSceneGO();
		else if (clazz == MaskTransitionGO.class)
			o = ginjector.getMaskTransitionGO();
		else if (clazz == GroupElementGO.class)
			o = ginjector.getGroupElementGO();
		else if (clazz == SceneGO.class)
			o = ginjector.getSceneGO();
		else if (clazz == TextAreaGO.class)
			o = ginjector.getTextAreaGO();
		else if (clazz == GhostElementGO.class)
			o = ginjector.getGhostElementGO();
		else if (clazz == SceneGO.class)
			o = ginjector.getSceneGO();
		else if (clazz == FieldsDebuggerGO.class)
			o = ginjector.getFieldsDebuggerGO();
		else if (clazz == FadeInTransitionGO.class)
			o = ginjector.getFadeInTransitionGO();
		else if (clazz == TrajectoryDebuggerGO.class)
			o = ginjector.getTrajectoryDebuggerGO();
		else if (clazz == GhostDebuggerGO.class)
			o = ginjector.getGhostDebuggerGO();
		else if (clazz == ScaleTransitionGO.class)
			o = ginjector.getScaleTransitionGO();
		// Trajectories
		else if (clazz == NodeTrajectoryGO.class)
			o = ginjector.getNodeTrajectoryGO();
		else if (clazz == SimpleTrajectoryGO.class)
			o = ginjector.getSimpleTrajectoryGO();
		else if (clazz == PolygonTrajectoryGO.class)
			o = ginjector.getPolygonTrajectoryGO();
		// Assets
		else if (clazz == GdxRectangleShape.class)
			o = ginjector.getGdxRectangleShape();
		else if (clazz == RuntimeCaption.class)
			o = ginjector.getRuntimeCaption();
		else if (clazz == RuntimeFont.class)
			o = ginjector.getRuntimeFont();
		else if (clazz == RuntimeComposedDrawable.class)
			o = ginjector.getRuntimeComposedDrawable();
		else if (clazz == RuntimeFramesAnimation.class)
			o = ginjector.getRuntimeFramesAnimation();
		else if (clazz == RuntimeStateDrawable.class)
			o = ginjector.getRuntimeStateDrawable();
		else if (clazz == GdxCircleShape.class)
			o = ginjector.getGdxCircleShape();
		else if (clazz == RuntimeStateDrawable.class)
			o = ginjector.getRuntimeStateDrawable();
		else if (clazz == RuntimeNinePatchImage.class)
			o = ginjector.getRuntimeNinePatchImage();
		else if (clazz == RuntimeSound.class)
			o = ginjector.getRuntimeSound();
		else if (clazz == GdxBezierShape.class)
			o = ginjector.getGdxBezierShape();
		else if (clazz == RuntimeFilteredDrawable.class)
			o = ginjector.getRuntimeFilteredDrawable();
		else if (clazz == RuntimeImage.class)
			o = ginjector.getRuntimeImage();
		else if (clazz == RuntimeFilteredDrawable.class)
			o = ginjector.getRuntimeFilteredDrawable();
		else if (clazz == RuntimeFont.class)
			o = ginjector.getRuntimeFont();
		// Plugins FIXME
		else if (clazz == BubbleNameGO.class)
			o = ginjector.getBubbleNameGO();
		if (o == null) {
			logger
					.warn("Instance for class {} not defined in ginjector",
							clazz);
		}
		return (T) o;
	}
}
