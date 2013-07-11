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

package ead.importer.auxiliar;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import ead.common.model.assets.drawable.basics.EAdImage;
import ead.common.model.assets.drawable.basics.Image;
import ead.common.model.elements.EAdChapter;
import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.EAdElement;
import ead.common.model.elements.huds.MouseHud;
import ead.common.model.elements.operations.BasicField;
import ead.common.model.elements.operations.EAdField;
import ead.common.model.elements.scenes.EAdSceneElementDef;
import ead.common.model.params.variables.VarDef;
import ead.importer.EAdElementImporter;
import ead.importer.GenericImporter;
import ead.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.data.adventure.AdventureData;
import es.eucm.eadventure.common.data.adventure.DescriptorData;
import es.eucm.eadventure.common.data.chapter.Chapter;
import es.eucm.eadventure.common.data.chapter.conditions.Condition;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.conversation.Conversation;

@Singleton
public class EAdElementFactoryImpl implements EAdElementFactory {

	private Map<String, Map<String, EAdElement>> elements;
	private Map<String, Map<String, EAdField<?>>> chapterVars;
	private Map<String, Map<String, EAdCondition>> chapterGlobalStates;
	private List<EAdSceneElementDef> draggableActors;
	private Map<String, EAdImage> defaultCursors;

	private Map<String, Object> oldType;

	private EAdChapter currentChapter;

	private Chapter currentOldChapter;

	private AdventureData model;

	private EAdElementImporter<Conditions, EAdCondition> conditionImporter;

	private Injector injector;

	public static Map<Class<?>, Class<? extends GenericImporter<?, ?>>> importerMap = new LinkedHashMap<Class<?>, Class<? extends GenericImporter<?, ?>>>();

	@Inject
	public EAdElementFactoryImpl(
			EAdElementImporter<Conditions, EAdCondition> conditionImporter,
			EAdElementImporter<Conversation, EAdEffect> conversationImporter,
			Injector injector) {
		this.conditionImporter = conditionImporter;
		draggableActors = new ArrayList<EAdSceneElementDef>();
		elements = new LinkedHashMap<String, Map<String, EAdElement>>();
		chapterVars = new LinkedHashMap<String, Map<String, EAdField<?>>>();
		chapterGlobalStates = new LinkedHashMap<String, Map<String, EAdCondition>>();
		oldType = new LinkedHashMap<String, Object>();
		defaultCursors = new LinkedHashMap<String, EAdImage>();
		this.injector = injector;
	}

	@Override
	public void registerOldElement(String id, Object oldElement) {
		oldType.put(id, oldElement);
	}

	@Override
	public EAdElement getElementById(String id) {
		Map<String, EAdElement> chapterElements = getChapterElements();

		EAdElement element = chapterElements.get(id);
		if (element == null) {
			element = getElement(id, oldType.get(id));
		}
		return element;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S> EAdElement getElement(String id, S oldElement) {
		EAdElementImporter<S, EAdElement> importer = (EAdElementImporter<S, EAdElement>) findElementImporter(oldElement
				.getClass());
		EAdElement newElement = importer.init(oldElement);
		getChapterElements().put(id, newElement);
		newElement = importer.convert(oldElement, newElement);
		return newElement;
	}

	public Map<String, EAdElement> getChapterElements() {
		Map<String, EAdElement> chapterElements = elements.get(currentChapter
				.getId());

		if (chapterElements == null) {
			chapterElements = new LinkedHashMap<String, EAdElement>();
			elements.put(currentChapter.getId(), chapterElements);
		}

		return chapterElements;
	}

	@SuppressWarnings("unchecked")
	private <S, I extends EAdElement> EAdElementImporter<S, I> findElementImporter(
			Class<S> clazz) {
		return (EAdElementImporter<S, I>) findGenericImporter(clazz);
	}

	@SuppressWarnings("unchecked")
	private <S, I extends EAdElement> GenericImporter<S, I> findGenericImporter(
			Class<S> clazz) {
		Class<? extends GenericImporter<?, ?>> importerClass = importerMap
				.get(clazz);
		GenericImporter<?, ?> genericImporter = injector
				.getInstance(importerClass);
		return (GenericImporter<S, I>) genericImporter;
	}

	@Override
	public EAdChapter getCurrentChapterModel() {
		return currentChapter;
	}

	@Override
	public void setCurrentChapterModel(EAdChapter chapter, Chapter oldChapter) {
		this.currentChapter = chapter;
		this.currentOldChapter = oldChapter;
		draggableActors.clear();

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

	public EAdField<?> getVarByOldId(String id, int type) {
		Map<String, EAdField<?>> vars = chapterVars.get(currentChapter.getId());

		if (vars == null) {
			vars = new LinkedHashMap<String, EAdField<?>>();
			chapterVars.put(currentChapter.getId(), vars);
		}

		EAdField<?> var = vars.get(id);
		if (var == null) {

			if (type == Condition.FLAG_CONDITION) {
				VarDef<Boolean> varDef = new VarDef<Boolean>(id, Boolean.class,
						Boolean.FALSE);
				var = new BasicField<Boolean>(currentChapter,
						(VarDef<Boolean>) varDef);
				currentChapter.setVarInitialValue(varDef, varDef
						.getInitialValue());

			} else {
				VarDef<Integer> varDef = new VarDef<Integer>(id, Integer.class,
						0);
				var = new BasicField<Integer>(currentChapter,
						(VarDef<Integer>) varDef);
				currentChapter.setVarInitialValue(varDef, varDef
						.getInitialValue());
			}
			vars.put(id, var);
		}
		return var;
	}

	@Override
	public EAdCondition getGlobalStateCondition(String id) {
		Map<String, EAdCondition> globalStates = chapterGlobalStates
				.get(currentChapter.getId());

		if (globalStates == null) {
			globalStates = new LinkedHashMap<String, EAdCondition>();
			chapterGlobalStates.put(currentChapter.getId(), globalStates);
		}

		EAdCondition condition = globalStates.get(id);
		if (condition == null) {
			condition = conditionImporter.init(currentOldChapter
					.getGlobalState(id));
			condition = conditionImporter.convert(currentOldChapter
					.getGlobalState(id), condition);
			if (condition != null)
				globalStates.put(id, condition);
		}
		return condition;
	}

	@Override
	public Chapter getCurrentOldChapterModel() {
		return currentOldChapter;
	}

	@Override
	public AdventureData getOldDataModel() {
		return model;
	}

	@Override
	public void addDraggableActor(EAdSceneElementDef actor) {
		if (!draggableActors.contains(actor)) {
			draggableActors.add(actor);
		}

	}

	@Override
	public boolean isDraggableActor(EAdSceneElementDef actor) {
		return draggableActors.contains(actor);
	}

	@Override
	public void init() {
		this.elements.clear();
		this.chapterGlobalStates.clear();
		this.chapterVars.clear();
		this.currentChapter = null;
		this.currentOldChapter = null;
		this.defaultCursors.clear();
		this.draggableActors.clear();
		this.elements.clear();
		this.model = null;
		this.oldType.clear();
	}

	@Override
	public String createCursor(Image image) {
		//XXX Add image as bundle of mouse element
		return MouseHud.DEFAULT_CURSOR;
	}

	@Override
	public String getDefaultCursor(String cursor) {
		//XXX
		return MouseHud.DEFAULT_CURSOR;
	}

}
