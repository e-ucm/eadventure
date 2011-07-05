package es.eucm.eadventure.common.impl.importer.auxiliar;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.GenericImporter;
import es.eucm.eadventure.common.data.adventure.AdventureData;
import es.eucm.eadventure.common.data.adventure.DescriptorData;
import es.eucm.eadventure.common.data.chapter.Chapter;
import es.eucm.eadventure.common.data.chapter.conditions.Condition;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.conversation.Conversation;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.model.EAdChapter;
import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.variables.EAdVar;
import es.eucm.eadventure.common.model.variables.impl.vars.BooleanVar;
import es.eucm.eadventure.common.model.variables.impl.vars.IntegerVar;

@Singleton
public class EAdElementFactoryImpl implements EAdElementFactory {

	private Map<String, Map<String, EAdElement>> elements;
	private Map<String, Map<String, EAdVar<?>>> chapterVars;
	private Map<String, Map<String, EAdCondition>> chapterGlobalStates;
	
	
	private Map<String, Object> oldType;
	
	private EAdChapter currentChapter;
	
	private Chapter currentOldChapter;

	private AdventureData model;

	private EAdElementImporter<Conditions, EAdCondition> conditionImporter;
	
	private EAdElementImporter<Conversation, EAdEffect> conversationImporter;

	private Injector injector;
	
	public static Map<Class<?>, Class<? extends GenericImporter<?, ?>>> importerMap = new HashMap<Class<?>, Class<? extends GenericImporter<?, ?>>>();
	
	@Inject
	public EAdElementFactoryImpl(
			EAdElementImporter<Conditions, EAdCondition> conditionImporter,
			EAdElementImporter<Conversation, EAdEffect> conversationImporter,
			Injector injector) {
		this.conditionImporter = conditionImporter;
		this.conversationImporter = conversationImporter;
		elements = new HashMap<String, Map<String, EAdElement>>();
		chapterVars = new HashMap<String, Map<String, EAdVar<?>>>();
		chapterGlobalStates = new HashMap<String, Map<String, EAdCondition>>();
		oldType = new HashMap<String, Object>();
		this.injector = injector;
	}
	
	@Override
	public void registerOldElement(String id, Object oldElement) {
		oldType.put(id, oldElement);
	}

	@Override
	public EAdElement getElementById(String id) {
		Map<String, EAdElement> chapterElements = elements.get(currentChapter.getId());

		if (chapterElements == null) {
			chapterElements = new HashMap<String, EAdElement>();
			elements.put(currentChapter.getId(), chapterElements);
		}

		EAdElement element = chapterElements.get(id);
		if (element == null) {
			element = getElement(oldType.get(id).getClass(), id, chapterElements);
		}
		return element;
	}
	
	private <S> EAdElement getElement(Class<S> clazz, String id, Map<String, EAdElement> chapterElements) {
		Object element = oldType.get(id);
		EAdElementImporter<S, ? extends EAdElement> importer = findElementImporter(clazz);
		EAdElement newElement = importer.init((S) element);
		chapterElements.put(id, newElement);
		newElement = importer.convert((S) element, newElement);
		return newElement;
	}
	
	private <S, I extends EAdElement> EAdElementImporter<S, I> findElementImporter(Class<S> clazz) {
		return (EAdElementImporter<S, I>) findGenericImporter(clazz);
	}
	
	private <S, I> GenericImporter<S, I> findGenericImporter(Class<S> clazz) {
		Class<? extends GenericImporter<?, ?>> importerClass = importerMap.get(clazz);
		GenericImporter<?, ?> genericImporter = injector.getInstance(importerClass);
		return (GenericImporter<S, I>) genericImporter;
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
			condition = conditionImporter.init(currentOldChapter.getGlobalState(id));
			condition = conditionImporter.convert(currentOldChapter.getGlobalState(id), condition);
			if ( condition != null )
				globalStates.put(id, condition);
		}
		return condition;
	}

	@Override
	public Chapter getCurrentOldChapterModel() {
		return currentOldChapter;
	}

}
