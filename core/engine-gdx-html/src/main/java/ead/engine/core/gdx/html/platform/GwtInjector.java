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

package ead.engine.core.gdx.html.platform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import ead.common.model.elements.debuggers.FieldsDebugger;
import ead.common.model.elements.debuggers.GhostDebugger;
import ead.common.model.elements.debuggers.TrajectoryDebugger;
import ead.common.model.elements.effects.ActorActionsEf;
import ead.common.model.elements.effects.AddActorReferenceEf;
import ead.common.model.elements.effects.AddChildEf;
import ead.common.model.elements.effects.ChangeSceneEf;
import ead.common.model.elements.effects.InterpolationEf;
import ead.common.model.elements.effects.ModifyInventoryEf;
import ead.common.model.elements.effects.PlaySoundEf;
import ead.common.model.elements.effects.QuitGameEf;
import ead.common.model.elements.effects.RandomEf;
import ead.common.model.elements.effects.RemoveEf;
import ead.common.model.elements.effects.TriggerMacroEf;
import ead.common.model.elements.effects.hud.ModifyHUDEf;
import ead.common.model.elements.effects.sceneelements.MoveSceneElementEf;
import ead.common.model.elements.effects.text.ShowQuestionEf;
import ead.common.model.elements.effects.text.SpeakEf;
import ead.common.model.elements.effects.timedevents.HighlightSceneElementEf;
import ead.common.model.elements.effects.timedevents.ShowSceneElementEf;
import ead.common.model.elements.effects.timedevents.WaitEf;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.events.ConditionedEv;
import ead.common.model.elements.events.SceneElementEv;
import ead.common.model.elements.events.SystemEv;
import ead.common.model.elements.events.TimedEv;
import ead.common.model.elements.scenes.BasicScene;
import ead.common.model.elements.scenes.EAdScene;
import ead.common.model.elements.scenes.GhostElement;
import ead.common.model.elements.scenes.GroupElement;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.scenes.VideoScene;
import ead.common.model.elements.trajectories.NodeTrajectory;
import ead.common.model.elements.trajectories.PolygonTrajectory;
import ead.common.model.elements.trajectories.SimpleTrajectory;
import ead.common.model.elements.transitions.DisplaceTransition;
import ead.common.model.elements.transitions.EmptyTransition;
import ead.common.model.elements.transitions.FadeInTransition;
import ead.common.model.elements.widgets.TextArea;
import ead.engine.core.platform.LoadingScreen;
import ead.tools.GenericInjector;

public class GwtInjector implements GenericInjector {

	private static final Logger logger = LoggerFactory.getLogger("GwtInjector");

	private GwtGinInjector ginjector;

	@Inject
	public GwtInjector(GwtGinInjector injector) {
		this.ginjector = injector;
	}

	public void setInjector(GwtGinInjector ginjector) {
		this.ginjector = ginjector;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getInstance(Class<T> clazz) {
		Object o = null;
		if (clazz == HighlightSceneElementEf.class)
			o = ginjector.getHighlightSceneElementGO();
		else if (clazz == ActorActionsEf.class)
			o = ginjector.getActorActionsGO();
		else if (clazz == RemoveEf.class)
			o = ginjector.getRemoveGO();
		else if (clazz == ShowSceneElementEf.class)
			o = ginjector.getShowSceneElementGO();
		else if (clazz == AddActorReferenceEf.class)
			o = ginjector.getAddActorReferenceGO();
		else if (clazz == TriggerMacroEf.class)
			o = ginjector.getTriggerMacroGO();
		else if (clazz == ModifyHUDEf.class)
			o = ginjector.getModifyHudGO();
		else if (clazz == PlaySoundEf.class)
			o = ginjector.getPlaySoundGO();
		else if (clazz == ShowQuestionEf.class)
			o = ginjector.getShowQuestionGO();
		else if (clazz == InterpolationEf.class)
			o = ginjector.getInterpolationGO();
		else if (clazz == QuitGameEf.class)
			o = ginjector.getQuitGameGO();
		else if (clazz == ChangeSceneEf.class)
			o = ginjector.getChangeSceneGO();
		else if (clazz == AddChildEf.class)
			o = ginjector.getAddChildGO();
		else if (clazz == RandomEf.class)
			o = ginjector.getRandomGO();
		else if (clazz == WaitEf.class)
			o = ginjector.getWaitGO();
		else if (clazz == ModifyInventoryEf.class)
			o = ginjector.getModifyInventoryGO();
		else if (clazz == SpeakEf.class)
			o = ginjector.getSpeakGO();
		else if (clazz == MoveSceneElementEf.class)
			o = ginjector.getMoveSceneElementGO();
		else if (clazz == ChangeFieldEf.class)
			o = ginjector.getChangeFieldGO();
		else if (clazz == ConditionedEv.class)
			o = ginjector.getConditionEvGO();
		else if (clazz == SceneElementEv.class)
			o = ginjector.getSceneElementEvGO();
		else if (clazz == SystemEv.class)
			o = ginjector.getSystemEvGO();
		else if (clazz == TimedEv.class)
			o = ginjector.getTimedEvGO();
		else if (clazz == TextArea.class)
			o = ginjector.getTextAreaGO();
		else if (clazz == FieldsDebugger.class)
			o = ginjector.getFieldsDebuggerGO();
		else if (clazz == EAdScene.class)
			o = ginjector.getSceneGO();
		else if (clazz == BasicScene.class)
			o = ginjector.getSceneGO();
		else if (clazz == FadeInTransition.class)
			o = ginjector.getFadeInTransitionGO();
		else if (clazz == VideoScene.class)
			o = ginjector.getVideoSceneGO();
		else if (clazz == DisplaceTransition.class)
			o = ginjector.getDisplaceTransitionGO();
		else if (clazz == EmptyTransition.class)
			o = ginjector.getEmptyTransitionGO();
		else if (clazz == GhostDebugger.class)
			o = ginjector.getGhostDebuggerGO();
		else if (clazz == GhostElement.class)
			o = ginjector.getGhostElementGO();
		else if (clazz == GroupElement.class)
			o = ginjector.getGroupElementGO();
		else if (clazz == LoadingScreen.class)
			o = ginjector.getSceneGO();
		else if (clazz == SceneElement.class)
			o = ginjector.getSceneElementGOImpl();
		else if (clazz == TrajectoryDebugger.class)
			o = ginjector.getTrajectoryDebuggerGO();
		else if (clazz == PolygonTrajectory.class)
			o = ginjector.getPolygonTrajectoryGO();
		else if (clazz == SimpleTrajectory.class)
			o = ginjector.getSimpleTrajectoryGO();
		else if (clazz == NodeTrajectory.class)
			o = ginjector.getNodeTrajectoryGO();
		if (o == null) {
			logger
					.warn("Instance for class {} not defined in ginjector",
							clazz);
		}
		return (T) o;
	}
}
