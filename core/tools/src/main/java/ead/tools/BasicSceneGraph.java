package ead.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import ead.common.model.EAdElement;
import ead.common.model.elements.EAdAction;
import ead.common.model.elements.EAdBehavior;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.EAdEvent;
import ead.common.model.elements.effects.AddActorReferenceEf;
import ead.common.model.elements.effects.ChangeSceneEf;
import ead.common.model.elements.effects.EffectsMacro;
import ead.common.model.elements.effects.RandomEf;
import ead.common.model.elements.effects.TriggerMacroEf;
import ead.common.model.elements.effects.text.ShowQuestionEf;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.scene.EAdScene;
import ead.common.model.elements.scene.EAdSceneElement;
import ead.common.model.elements.variables.EAdField;
import ead.common.resources.EAdResources;
import ead.common.resources.assets.AssetDescriptor;
import ead.tools.reflection.ReflectionProvider;

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

	private void lookForConnections(EAdScene currentScene,
			EAdSceneElement element) {
		// Behavior
		lookForConnections(currentScene, element.getBehavior());
		lookForConnections(currentScene, element.getDefinition().getBehavior());
		
		addAssets( currentScene, element.getDefinition().getResources() );

		// Events
		lookForConnectionsEvents(currentScene, element.getEvents());
		lookForConnectionsEvents(currentScene, element.getDefinition()
				.getEvents());

		// Actions
		lookForConnectionsActions(currentScene, element.getDefinition()
				.getActions());
	}

	private void addAssets(EAdScene currentScene, EAdResources resources) {
		sceneAssets.get(currentScene).addAll(resources.getAllAssets());		
	}

	private void lookForConnectionsActions(EAdScene currentScene,
			EAdList<EAdAction> actions) {

		for (EAdAction a : actions) {
			for (EAdEffect e : a.getEffects()) {
				lookForConnections(currentScene, e);
			}
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
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
			EAdElement element = changeScene.getNextScene();
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
			for (EffectsMacro macro : triggerMacro.getMacros()) {
				for (EAdEffect e : macro.getEffects()) {
					lookForConnections(currentScene, e);
				}
			}
		} else if (effect instanceof RandomEf) {
			RandomEf randomEf = (RandomEf) effect;
			for (EAdEffect e : randomEf.getEffects().keySet()) {
				lookForConnections(currentScene, e);
			}
		} else if (effect instanceof ShowQuestionEf) {
			ShowQuestionEf showQuestion = (ShowQuestionEf) effect;
			for (EAdEffect e : showQuestion.getAnswers().values()) {
				lookForConnections(currentScene, e);
			}
		} else if (effect instanceof ChangeFieldEf) {
			checkChangeField((ChangeFieldEf) effect);
		} else if (effect instanceof AddActorReferenceEf) {
			AddActorReferenceEf addActor = (AddActorReferenceEf) effect;
			lookForConnections(currentScene, addActor.getInitialEffect());
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
			logger.info("{} is linked to all its previous scenes",
					currentScene.getId());
		}
	}

	private void addConnection(EAdScene currentScene, EAdScene scene) {
		List<EAdScene> scenes = graph.get(currentScene);
		if (!scenes.contains(scene)) {
			scenes.add(scene);
			logger.info("{} is linked to {}",
					new Object[] { currentScene.getId(), scene.getId() });
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
