package es.eucm.eadventure.common.impl.importer.auxiliar;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.Importer;
import es.eucm.eadventure.common.data.adventure.AdventureData;
import es.eucm.eadventure.common.data.adventure.DescriptorData;
import es.eucm.eadventure.common.data.chapter.Chapter;
import es.eucm.eadventure.common.data.chapter.conditions.Condition;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.conversation.Conversation;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.model.EAdChapter;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.elements.EAdActor;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicActor;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneImpl;
import es.eucm.eadventure.common.model.variables.EAdVar;
import es.eucm.eadventure.common.model.variables.impl.vars.BooleanVar;
import es.eucm.eadventure.common.model.variables.impl.vars.IntegerVar;

@Singleton
public class EAdElementFactoryImpl implements EAdElementFactory {

	private Map<String, Map<String, EAdActor>> chapterActors;
	private Map<String, Map<String, EAdScene>> chapterScenes;
	private Map<String, Map<String, EAdVar<?>>> chapterVars;
	private Map<String, Map<String, EAdCondition>> chapterGlobalStates;
	private Map<String, Map<String, EAdEffect>> chapterConversations;

	private EAdChapter currentChapter;
	
	private Chapter currentOldChapter;

	private AdventureData model;

	private Importer<Conditions, EAdCondition> conditionImporter;
	
	private Importer<Conversation, EAdEffect> conversationImporter;

	@Inject
	public EAdElementFactoryImpl(
			Importer<Conditions, EAdCondition> conditionImporter,
			Importer<Conversation, EAdEffect> conversationImporter) {
		this.conditionImporter = conditionImporter;
		this.conversationImporter = conversationImporter;
		chapterActors = new HashMap<String, Map<String, EAdActor>>();
		chapterScenes = new HashMap<String, Map<String, EAdScene>>();
		chapterVars = new HashMap<String, Map<String, EAdVar<?>>>();
		chapterGlobalStates = new HashMap<String, Map<String, EAdCondition>>();
		chapterConversations= new HashMap<String, Map<String, EAdEffect>>();
	}

	@Override
	public EAdActor getActorByOldId(String id) {
		Map<String, EAdActor> actors = chapterActors
				.get(currentChapter.getId());

		if (actors == null) {
			actors = new HashMap<String, EAdActor>();
			chapterActors.put(currentChapter.getId(), actors);
		}

		EAdActor actor = actors.get(id);
		if (actor == null) {
			actor = new EAdBasicActor(currentChapter.getId() + "_" + id);
			actors.put(id, actor);
		}
		return actor;
	}

	@Override
	public EAdChapter getCurrentChapterModel() {
		return currentChapter;
	}

	@Override
	public void setCurrentChapterModel(EAdChapter chapter, Chapter oldChapter ) {
		this.currentChapter = chapter;
		this.currentOldChapter = oldChapter;

	}

	@Override
	public EAdScene getSceneByOldId(String id) {
		Map<String, EAdScene> scenes = chapterScenes
				.get(currentChapter.getId());

		if (scenes == null) {
			scenes = new HashMap<String, EAdScene>();
			chapterScenes.put(currentChapter.getId(), scenes);
		}

		EAdScene scene = scenes.get(id);
		if (scene == null) {
			scene = new EAdSceneImpl(currentChapter.getId() + "_" + id);
			scenes.put(id, scene);
		}
		return scene;
	}

	/**
	 * Sets the old data model
	 * 
	 * @param data
	 *            the old data model
	 */
	public void setOldDataModel(AdventureData data) {
		this.model = data;
	}

	@Override
	public boolean isFirstPerson() {
		return model.getPlayerMode() == DescriptorData.MODE_PLAYER_1STPERSON;
	}

	public EAdVar<?> getVarByOldId(String id, int type) {
		Map<String, EAdVar<?>> vars = chapterVars.get(currentChapter.getId());

		if (vars == null) {
			vars = new HashMap<String, EAdVar<?>>();
			chapterVars.put(currentChapter.getId(), vars);
		}

		EAdVar<?> var = vars.get(id);
		if (var == null) {
			if (type == Condition.FLAG_CONDITION)
				var = new BooleanVar(id);
			else
				var = new IntegerVar(id);
			vars.put(id, var);
		}
		return var;
	}

	@Override
	public EAdCondition getGlobalStateCondition(String id) {
		Map<String, EAdCondition> globalStates = chapterGlobalStates.get(currentChapter.getId());

		if (globalStates == null) {
			globalStates = new HashMap<String, EAdCondition>();
			chapterGlobalStates.put(currentChapter.getId(), globalStates);
		}

		EAdCondition condition = globalStates.get(id);
		if (condition == null) {
			condition = conditionImporter.convert(currentOldChapter.getGlobalState(id));
			if ( condition != null )
				globalStates.put(id, condition);
		}
		return condition;
	}

	@Override
	public Chapter getCurrentOldChapterModel() {
		return currentOldChapter;
	}

	@Override
	public EAdEffect getEffectForConversation(String conversationId) {
		Map<String, EAdEffect> conversations = chapterConversations.get(currentChapter.getId());

		if (conversations == null) {
			conversations = new HashMap<String, EAdEffect>();
			chapterConversations.put(currentChapter.getId(), conversations);
		}

		EAdEffect effect = conversations.get(conversationId);
		if (effect == null) {
			effect = conversationImporter.convert(currentOldChapter.getConversation(conversationId));
			if ( effect != null )
				conversations.put(conversationId, effect);
		}
		return effect;
	}

}
