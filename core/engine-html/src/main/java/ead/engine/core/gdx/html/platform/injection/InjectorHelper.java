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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.engine.core.assets.drawables.RuntimeCaption;
import ead.engine.core.assets.drawables.RuntimeComposedDrawable;
import ead.engine.core.assets.drawables.RuntimeFilteredDrawable;
import ead.engine.core.assets.drawables.RuntimeFramesAnimation;
import ead.engine.core.assets.drawables.RuntimeImage;
import ead.engine.core.assets.drawables.RuntimeNinePatchImage;
import ead.engine.core.assets.drawables.RuntimeStateDrawable;
import ead.engine.core.assets.drawables.shapes.GdxBezierShape;
import ead.engine.core.assets.drawables.shapes.GdxCircleShape;
import ead.engine.core.assets.drawables.shapes.GdxRectangleShape;
import ead.engine.core.assets.fonts.RuntimeFont;
import ead.engine.core.assets.multimedia.RuntimeSound;
import ead.engine.core.gameobjects.debuggers.FieldsDebuggerGO;
import ead.engine.core.gameobjects.debuggers.GhostDebuggerGO;
import ead.engine.core.gameobjects.debuggers.ProfilerDebuggerGO;
import ead.engine.core.gameobjects.debuggers.TrajectoryDebuggerGO;
import ead.engine.core.gameobjects.effects.ActorActionsGO;
import ead.engine.core.gameobjects.effects.AddActorReferenceGO;
import ead.engine.core.gameobjects.effects.AddChildGO;
import ead.engine.core.gameobjects.effects.ApplyForceGO;
import ead.engine.core.gameobjects.effects.ChangeFieldGO;
import ead.engine.core.gameobjects.effects.ChangeSceneGO;
import ead.engine.core.gameobjects.effects.HighlightSceneElementGO;
import ead.engine.core.gameobjects.effects.InterpolationGO;
import ead.engine.core.gameobjects.effects.ModifyHudGO;
import ead.engine.core.gameobjects.effects.ModifyInventoryGO;
import ead.engine.core.gameobjects.effects.MoveSceneElementGO;
import ead.engine.core.gameobjects.effects.PhysicsEffectGO;
import ead.engine.core.gameobjects.effects.PlaySoundGO;
import ead.engine.core.gameobjects.effects.QuestionGO;
import ead.engine.core.gameobjects.effects.QuitGameGO;
import ead.engine.core.gameobjects.effects.RandomGO;
import ead.engine.core.gameobjects.effects.RemoveGO;
import ead.engine.core.gameobjects.effects.ShowSceneElementGO;
import ead.engine.core.gameobjects.effects.SpeakGO;
import ead.engine.core.gameobjects.effects.ToggleSoundGO;
import ead.engine.core.gameobjects.effects.TriggerMacroGO;
import ead.engine.core.gameobjects.effects.WaitGO;
import ead.engine.core.gameobjects.effects.WaitUntilGO;
import ead.engine.core.gameobjects.effects.sceneelement.ChangeColorGO;
import ead.engine.core.gameobjects.events.ConditionEvGO;
import ead.engine.core.gameobjects.events.SceneElementEvGO;
import ead.engine.core.gameobjects.events.TimedEvGO;
import ead.engine.core.gameobjects.events.WatchFieldEvGO;
import ead.engine.core.gameobjects.sceneelements.GhostElementGO;
import ead.engine.core.gameobjects.sceneelements.GroupElementGO;
import ead.engine.core.gameobjects.sceneelements.SceneElementGO;
import ead.engine.core.gameobjects.sceneelements.SceneGO;
import ead.engine.core.gameobjects.sceneelements.VideoSceneGO;
import ead.engine.core.gameobjects.sceneelements.huds.MouseHudGO;
import ead.engine.core.gameobjects.sceneelements.transitions.DisplaceTransitionGO;
import ead.engine.core.gameobjects.sceneelements.transitions.EmptyTransitionGO;
import ead.engine.core.gameobjects.sceneelements.transitions.FadeInTransitionGO;
import ead.engine.core.gameobjects.sceneelements.transitions.MaskTransitionGO;
import ead.engine.core.gameobjects.sceneelements.transitions.ScaleTransitionGO;
import ead.engine.core.gameobjects.trajectories.dijkstra.NodeTrajectoryGO;
import ead.engine.core.gameobjects.trajectories.polygon.PolygonTrajectoryGO;
import ead.engine.core.gameobjects.trajectories.simple.SimpleTrajectoryGO;
import ead.engine.core.gameobjects.widgets.TextAreaGO;
import ead.plugins.engine.bubbledescription.BubbleNameGO;
import ead.tools.gwt.Injector;

public class InjectorHelper implements Injector {

	private static final Logger logger = LoggerFactory.getLogger("GwtInjector");

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
		if (clazz == HighlightSceneElementGO.class)
			o = ginjector.getHighlightSceneElementGO();
		else if (clazz == SpeakGO.class)
			o = ginjector.getSpeakGO();
		else if (clazz == ModifyHudGO.class)
			o = ginjector.getModifyHudGO();
		else if (clazz == MoveSceneElementGO.class)
			o = ginjector.getMoveSceneElementGO();
		else if (clazz == ActorActionsGO.class)
			o = ginjector.getActorActionsGO();
		else if (clazz == ModifyInventoryGO.class)
			o = ginjector.getModifyInventoryGO();
		else if (clazz == AddChildGO.class)
			o = ginjector.getAddChildGO();
		else if (clazz == WaitUntilGO.class)
			o = ginjector.getWaitUntilGO();
		else if (clazz == ShowSceneElementGO.class)
			o = ginjector.getShowSceneElementGO();
		else if (clazz == ChangeSceneGO.class)
			o = ginjector.getChangeSceneGO();
		else if (clazz == QuestionGO.class)
			o = ginjector.getQuestionGO();
		else if (clazz == RandomGO.class)
			o = ginjector.getRandomGO();
		else if (clazz == PlaySoundGO.class)
			o = ginjector.getPlaySoundGO();
		else if (clazz == RemoveGO.class)
			o = ginjector.getRemoveGO();
		else if (clazz == ChangeFieldGO.class)
			o = ginjector.getChangeFieldGO();
		else if (clazz == AddActorReferenceGO.class)
			o = ginjector.getAddActorReferenceGO();
		else if (clazz == WaitGO.class)
			o = ginjector.getWaitGO();
		else if (clazz == PhysicsEffectGO.class)
			o = ginjector.getPhysicsEffectGO();
		else if (clazz == ChangeColorGO.class)
			o = ginjector.getChangeColorGO();
		else if (clazz == QuitGameGO.class)
			o = ginjector.getQuitGameGO();
		else if (clazz == InterpolationGO.class)
			o = ginjector.getInterpolationGO();
		else if (clazz == ApplyForceGO.class)
			o = ginjector.getApplyForceGO();
		else if (clazz == ToggleSoundGO.class)
			o = ginjector.getToggleSoundGO();
		else if (clazz == TriggerMacroGO.class)
			o = ginjector.getTriggerMacroGO();
		// SceneElements
		else if (clazz == TimedEvGO.class)
			o = ginjector.getTimedEvGO();
		else if (clazz == SceneElementEvGO.class)
			o = ginjector.getSceneElementEvGO();
		else if (clazz == WatchFieldEvGO.class)
			o = ginjector.getWatchFieldEvGO();
		else if (clazz == ConditionEvGO.class)
			o = ginjector.getConditionEvGO();
		// Events
		else if (clazz == TextAreaGO.class)
			o = ginjector.getTextAreaGO();
		else if (clazz == DisplaceTransitionGO.class)
			o = ginjector.getDisplaceTransitionGO();
		else if (clazz == VideoSceneGO.class)
			o = ginjector.getVideoSceneGO();
		else if (clazz == GroupElementGO.class)
			o = ginjector.getGroupElementGO();
		else if (clazz == SceneGO.class)
			o = ginjector.getSceneGO();
		else if (clazz == FadeInTransitionGO.class)
			o = ginjector.getFadeInTransitionGO();
		else if (clazz == FieldsDebuggerGO.class)
			o = ginjector.getFieldsDebuggerGO();
		else if (clazz == SceneGO.class)
			o = ginjector.getSceneGO();
		else if (clazz == EmptyTransitionGO.class)
			o = ginjector.getEmptyTransitionGO();
		else if (clazz == MaskTransitionGO.class)
			o = ginjector.getMaskTransitionGO();
		else if (clazz == GhostElementGO.class)
			o = ginjector.getGhostElementGO();
		else if (clazz == SceneGO.class)
			o = ginjector.getSceneGO();
		else if (clazz == ScaleTransitionGO.class)
			o = ginjector.getScaleTransitionGO();
		else if (clazz == SceneElementGO.class)
			o = ginjector.getSceneElementGO();
		else if (clazz == GhostDebuggerGO.class)
			o = ginjector.getGhostDebuggerGO();
		else if (clazz == TrajectoryDebuggerGO.class)
			o = ginjector.getTrajectoryDebuggerGO();
		else if (clazz == ProfilerDebuggerGO.class)
			o = ginjector.getProfilerDebuggerGO();
		else if (clazz == MouseHudGO.class)
			o = ginjector.getMouseHudGO();
		// Trajectories
		else if (clazz == NodeTrajectoryGO.class)
			o = ginjector.getNodeTrajectoryGO();
		else if (clazz == SimpleTrajectoryGO.class)
			o = ginjector.getSimpleTrajectoryGO();
		else if (clazz == PolygonTrajectoryGO.class)
			o = ginjector.getPolygonTrajectoryGO();
		// Assets
		else if (clazz == GdxCircleShape.class)
			o = ginjector.getGdxCircleShape();
		else if (clazz == RuntimeStateDrawable.class)
			o = ginjector.getRuntimeStateDrawable();
		else if (clazz == GdxRectangleShape.class)
			o = ginjector.getGdxRectangleShape();
		else if (clazz == RuntimeFramesAnimation.class)
			o = ginjector.getRuntimeFramesAnimation();
		else if (clazz == RuntimeStateDrawable.class)
			o = ginjector.getRuntimeStateDrawable();
		else if (clazz == RuntimeFont.class)
			o = ginjector.getRuntimeFont();
		else if (clazz == RuntimeFilteredDrawable.class)
			o = ginjector.getRuntimeFilteredDrawable();
		else if (clazz == GdxBezierShape.class)
			o = ginjector.getGdxBezierShape();
		else if (clazz == RuntimeFilteredDrawable.class)
			o = ginjector.getRuntimeFilteredDrawable();
		else if (clazz == RuntimeSound.class)
			o = ginjector.getRuntimeSound();
		else if (clazz == RuntimeNinePatchImage.class)
			o = ginjector.getRuntimeNinePatchImage();
		else if (clazz == RuntimeImage.class)
			o = ginjector.getRuntimeImage();
		else if (clazz == RuntimeFont.class)
			o = ginjector.getRuntimeFont();
		else if (clazz == RuntimeComposedDrawable.class)
			o = ginjector.getRuntimeComposedDrawable();
		else if (clazz == RuntimeCaption.class)
			o = ginjector.getRuntimeCaption();
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
