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

package es.eucm.ead.engine.utils;

import com.google.inject.Inject;
import es.eucm.ead.tools.reflection.ReflectionProvider;
import es.eucm.ead.model.assets.AssetDescriptor;
import es.eucm.ead.model.elements.EAdBehavior;
import es.eucm.ead.model.elements.EAdEffect;
import es.eucm.ead.model.elements.EAdElement;
import es.eucm.ead.model.elements.EAdEvent;
import es.eucm.ead.model.elements.effects.*;
import es.eucm.ead.model.elements.effects.text.QuestionEf;
import es.eucm.ead.model.elements.effects.variables.ChangeFieldEf;
import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.elements.extra.EAdMap;
import es.eucm.ead.model.elements.operations.EAdField;
import es.eucm.ead.model.elements.scenes.EAdScene;
import es.eucm.ead.model.elements.scenes.EAdSceneElement;
import es.eucm.ead.model.elements.scenes.EAdSceneElementDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.Map.Entry;

public class BasicSceneGraph implements SceneGraph {

	private Map<EAdScene, List<EAdScene>> graph;
	private static final Logger logger = LoggerFactory.getLogger("SceneGraph");

	/**
	 * This map stores fields of change scene effects. Whenever a change field
	 * effect changes any of this fields, it'll mean that the owner scene is
	 * connected to the scene defined in the change field effect
	 */
	private Map<EAdScene, List<EAdField<EAdScene>>> changeSceneFields;

	/**
	 * Contains a list with all the scenes that must be connected to all the
	 * scenes connected to them (when changeScene has null as next scene )
	 */
	private List<EAdScene> connectedToPrevious;

	/**
	 * Contains a list with all the effects visited
	 */
	private List<EAdEffect> effectsVisited;

	/**
	 * Effects can be shared in different places. To avoid loops, effects only
	 * must be checked once per scene. This map contains information to assure
	 * that
	 */
	private Map<EAdScene, List<EAdEffect>> effectsVisitedByScene;

	/**
	 * Map storing "all" assets for every scene
	 */
	private Map<EAdScene, List<AssetDescriptor>> sceneAssets;

	private List<EAdScene> newScenes;

	private ReflectionProvider reflectionProvider;

	@Inject
	public BasicSceneGraph(ReflectionProvider reflectionProvider) {
		graph = new HashMap<EAdScene, List<EAdScene>>();
		changeSceneFields = new HashMap<EAdScene, List<EAdField<EAdScene>>>();
		newScenes = new ArrayList<EAdScene>();
		connectedToPrevious = new ArrayList<EAdScene>();
		effectsVisited = new ArrayList<EAdEffect>();
		effectsVisitedByScene = new HashMap<EAdScene, List<EAdEffect>>();
		this.sceneAssets = new HashMap<EAdScene, List<AssetDescriptor>>();
		this.reflectionProvider = reflectionProvider;
	}

	public Set<EAdScene> getScenes() {
		return graph.keySet();
	}

	public Map<EAdScene, List<EAdScene>> getGraph() {
		return graph;
	}

	public List<EAdEffect> getEffectsVisited() {
		return effectsVisited;
	}

	public void generateGraph(EAdScene initialScene) {
		graph.clear();
		newScenes.clear();
		changeSceneFields.clear();
		effectsVisited.clear();
		sceneAssets.clear();
		newScenes.add(initialScene);
		while (newScenes.size() > 0) {
			EAdScene nextScene = newScenes.remove(0);
			graph.put(nextScene, new ArrayList<EAdScene>());
			changeSceneFields.put(nextScene,
					new ArrayList<EAdField<EAdScene>>());
			effectsVisitedByScene.put(nextScene, new ArrayList<EAdEffect>());
			sceneAssets.put(nextScene, new ArrayList<AssetDescriptor>());
			lookForConnections(nextScene);
		}

		// Connect to previous
		for (Entry<EAdScene, List<EAdScene>> entry : graph.entrySet()) {
			for (EAdScene s : entry.getValue()) {
				if (connectedToPrevious.contains(s)) {
					this.addConnection(s, entry.getKey());
				}
			}
		}
	}

	private void lookForConnections(EAdScene nextScene) {
		lookForConnections(nextScene, nextScene.getBackground());
		for (EAdSceneElement sceneElement : nextScene.getSceneElements()) {
			lookForConnections(nextScene, sceneElement);
		}
	}

	public Map<EAdScene, List<AssetDescriptor>> getSceneAssets() {
		return sceneAssets;
	}

	@SuppressWarnings("unchecked")
	private void lookForConnections(EAdScene currentScene,
			EAdSceneElement element) {
		// Behavior
		lookForConnections(currentScene, element.getBehavior());
		lookForConnections(currentScene, element.getDefinition().getBehavior());

		for (EAdMap<String, AssetDescriptor> map : element.getDefinition()
				.getResources().values()) {
			addAssets(currentScene, map);
		}

		// Events
		lookForConnectionsEvents(currentScene, element.getEvents());
		lookForConnectionsEvents(currentScene, element.getDefinition()
				.getEvents());

		// Actions
		EAdList<EAdSceneElementDef> list = (EAdList<EAdSceneElementDef>) element
				.getDefinition().getVars().get(ActorActionsEf.VAR_ACTIONS);
		lookForConnectionsActions(currentScene, list);
	}

	private void addAssets(EAdScene currentScene,
			EAdMap<String, AssetDescriptor> resources) {
		sceneAssets.get(currentScene).addAll(resources.values());
	}

	private void lookForConnectionsActions(EAdScene currentScene,
			EAdList<EAdSceneElementDef> actions) {

		if (actions != null)
			for (EAdSceneElementDef a : actions) {
				lookForConnections(currentScene, a.getBehavior());
			}
	}

	private void lookForConnections(EAdScene currentScene, EAdBehavior behavior) {
		for (EAdEffect e : behavior.getAllEffects()) {
			lookForConnections(currentScene, e);
		}
	}

	private void lookForConnectionsEvents(EAdScene currentScene,
			EAdList<EAdEvent> events) {
		for (EAdEvent e : events) {
			for (EAdEffect ef : e.getAllEffects()) {
				lookForConnections(currentScene, ef);
			}
		}
	}

	@SuppressWarnings( { "unchecked", "rawtypes" })
	private void lookForConnections(EAdScene currentScene, EAdEffect effect) {
		if (effect == null) {
			logger.warn(
					"A null effect in scene {}. You should check your model",
					currentScene.getId());
			return;
		}
		// Add to the list of effects visited
		if (!effectsVisited.contains(effect)) {
			effectsVisited.add(effect);
		}

		List<EAdEffect> sceneEffects = effectsVisitedByScene.get(currentScene);
		if (sceneEffects.contains(effect)) {
			return;
		}

		sceneEffects.add(effect);

		if (effect instanceof ChangeSceneEf) {
			ChangeSceneEf changeScene = (ChangeSceneEf) effect;
			EAdElement element = null;
			if (element == null) {
				addConnectToPrevious(currentScene);
			} else if (element instanceof EAdScene) {
				addConnection(currentScene, (EAdScene) element);
			} else if (element instanceof EAdField) {
				EAdField field = (EAdField) element;
				if (reflectionProvider.isAssignableFrom(EAdScene.class, field
						.getVarDef().getType())) {
					addConnectionField(currentScene, field);
				}
			}
		} else if (effect instanceof TriggerMacroEf) {
			TriggerMacroEf triggerMacro = (TriggerMacroEf) effect;
			for (EAdList<EAdEffect> macro : triggerMacro.getMacros()) {
				for (EAdEffect e : macro) {
					lookForConnections(currentScene, e);
				}
			}
		} else if (effect instanceof RandomEf) {
			RandomEf randomEf = (RandomEf) effect;
			for (EAdEffect e : randomEf.getEffects().keySet()) {
				lookForConnections(currentScene, e);
			}
		} else if (effect instanceof QuestionEf) {
			QuestionEf showQuestion = (QuestionEf) effect;
			for (List<EAdEffect> effects : showQuestion.getEffects()) {
				for (EAdEffect e : effects) {
					lookForConnections(currentScene, e);
				}
			}
		} else if (effect instanceof ChangeFieldEf) {
			checkChangeField((ChangeFieldEf) effect);
		} else if (effect instanceof AddActorReferenceEf) {
			AddActorReferenceEf addActor = (AddActorReferenceEf) effect;
			lookForConnections(currentScene, addActor.getEffect());
			lookForConnections(currentScene, addActor.getActor().getBehavior());
			lookForConnectionsEvents(currentScene, addActor.getActor()
					.getEvents());
		}

		for (EAdEffect nextEffect : effect.getNextEffects()) {
			lookForConnections(currentScene, nextEffect);
		}
	}

	private void checkChangeField(ChangeFieldEf effect) {
		// TODO
	}

	private void addConnectToPrevious(EAdScene currentScene) {
		if (!connectedToPrevious.contains(currentScene)) {
			connectedToPrevious.add(currentScene);
			logger.info("{} is linked to all its previous scenes", currentScene
					.getId());
		}
	}

	private void addConnection(EAdScene currentScene, EAdScene scene) {
		List<EAdScene> scenes = graph.get(currentScene);
		if (!scenes.contains(scene)) {
			scenes.add(scene);
			logger.info("{} is linked to {}", new Object[] {
					currentScene.getId(), scene.getId() });
			if (!graph.containsKey(scene)) {
				newScenes.add(scene);
			}
		}
	}

	private void addConnectionField(EAdScene currentScene,
			EAdField<EAdScene> field) {
		List<EAdField<EAdScene>> fields = changeSceneFields.get(currentScene);
		if (!fields.contains(field)) {
			fields.add(field);
		}
	}
}
