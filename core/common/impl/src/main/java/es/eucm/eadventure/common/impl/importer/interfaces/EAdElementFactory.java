package es.eucm.eadventure.common.impl.importer.interfaces;

import es.eucm.eadventure.common.data.adventure.AdventureData;
import es.eucm.eadventure.common.data.chapter.Chapter;
import es.eucm.eadventure.common.model.EAdChapter;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.elements.EAdActor;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.variables.EAdVar;

public interface EAdElementFactory {

	/**
	 * Returns the actor with the given id from the current chapter
	 * 
	 * @param id
	 *            the id
	 * @return the actor with the given id from the current chapter
	 */
	EAdActor getActorByOldId(String id);

	/**
	 * Returns the currently chapter being imported
	 * 
	 * @return currently chapter being imported
	 */
	EAdChapter getCurrentChapterModel();

	/**
	 * Returns the old model from the chapter being imported
	 * 
	 * @return the old model from the chapter being imported
	 */
	Chapter getCurrentOldChapterModel();

	/**
	 * Sets the current chapter model imported
	 * 
	 * @param chapter
	 *            the new model for the chapter
	 * @param oldChapter
	 *            the old data from the chapter
	 */
	void setCurrentChapterModel(EAdChapter chapter, Chapter oldChapter);

	/**
	 * Returns the scene associated with the id in the old model
	 * 
	 * @param id
	 *            id in the old model for the scene
	 * @return the scene
	 */
	EAdScene getSceneByOldId(String id);

	/**
	 * Returns whether the game imported is first person
	 * 
	 * @return whether the game imported is first person
	 */
	boolean isFirstPerson();

	/**
	 * Sets the old data model
	 * 
	 * @param data
	 *            the old data model
	 */
	public void setOldDataModel(AdventureData data);

	/**
	 * Returns the variable's reference linked to the given id
	 * 
	 * @param id
	 *            variables's id
	 * @param varibles
	 *            type. Can be
	 *            {@link es.eucm.eadventure.common.data.chapter.conditions.Condition#FLAG_CONDITION}
	 *            or
	 *            {@link es.eucm.eadventure.common.data.chapter.conditions.Condition#VAR_CONDITION}
	 * @return the variable's reference
	 */
	public EAdVar<?> getVarByOldId(String id, int type);

	/**
	 * Returns the conditions associated with the global state represented by
	 * the given id
	 * 
	 * @param globalStateId
	 *            the global state's id
	 * @return the conditions representing the global state
	 */
	public EAdCondition getGlobalStateCondition(String globalStateId);

	/**
	 * Returns the effect associated to the conversation with the given id
	 * 
	 * @param conversationId
	 *            conversation id
	 * @return
	 */
	public EAdEffect getEffectForConversation(String conversationId);

}
