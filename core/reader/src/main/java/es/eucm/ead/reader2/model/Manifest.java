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

package es.eucm.ead.reader2.model;

import es.eucm.ead.model.elements.BasicElement;
import es.eucm.ead.model.elements.EAdAdventureModel;
import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.elements.extra.EAdMap;
import es.eucm.ead.model.interfaces.Param;

public class Manifest extends BasicElement {

	/**
	 * List of chapter ids in the game
	 */
	@Param
	private EAdList<String> chapters;

	/**
	 * Initial scenes ids for each of the chapters
	 */
	@Param
	private EAdList<String> initialScenes;

	/**
	 * Map relating chapters ids wit their scenes
	 */
	@Param
	private EAdMap<String, EAdList<String>> chaptersScenes;

	/**
	 * Id of the initial chapter
	 */
	@Param
	private String initialChapter;

	/**
	 * Translation between abbreviations and classes
	 */
	@Param
	private EAdMap<String, String> classes;

	/**
	 * Translation between abbreviations and fields
	 */
	@Param
	private EAdMap<String, String> fields;

	/**
	 * Translation between abbreviations and params
	 */
	@Param
	private EAdMap<String, String> params;

	@Param
	private EAdAdventureModel model;

	@Param
	private EAdMap<String, EAdList<String>> sceneGraph;

	/**
	 * Creates an empty manifest. You need to call {@link Manifest#clear()} to init its fields
	 */
	public Manifest() {

	}

	/**
	 * Initializes or clears the manifest
	 */
	public void clear() {
		model = null;
		if (chapters == null) {
			chapters = new EAdList<String>();
		} else {
			chapters.clear();
		}
		initialChapter = null;
		if (initialScenes == null) {
			initialScenes = new EAdList<String>();
		} else {
			initialScenes.clear();
		}
		if (classes == null) {
			classes = new EAdMap<String, String>();
		} else {
			classes.clear();
		}
		if (fields == null) {
			fields = new EAdMap<String, String>();
		} else {
			fields.clear();
		}
		if (params == null) {
			params = new EAdMap<String, String>();
		} else {
			params.clear();
		}
		if (chaptersScenes == null) {
			chaptersScenes = new EAdMap<String, EAdList<String>>();
		} else {
			chaptersScenes.clear();
		}
	}

	public void addChapterId(String id) {
		chapters.add(id);
	}

	public void setInitialChapter(String initialChapter) {
		this.initialChapter = initialChapter;
	}

	public void setModel(EAdAdventureModel model) {
		this.model = model;
	}

	public EAdAdventureModel getModel() {
		return model;
	}

	public void addInitScene(String id) {
		this.initialScenes.add(id);
	}

	public String getInitialChapter() {
		return initialChapter;
	}

	public EAdList<String> getChapterIds() {
		return this.chapters;
	}

	public EAdList<String> getChapters() {
		return chapters;
	}

	public void setChapters(EAdList<String> chapters) {
		this.chapters = chapters;
	}

	public EAdList<String> getInitialScenes() {
		return initialScenes;
	}

	public void setInitialScenes(EAdList<String> initialScenes) {
		this.initialScenes = initialScenes;
	}

	public EAdMap<String, String> getClasses() {
		return classes;
	}

	public void setClasses(EAdMap<String, String> classes) {
		this.classes = classes;
	}

	public EAdMap<String, String> getFields() {
		return fields;
	}

	public void setFields(EAdMap<String, String> fields) {
		this.fields = fields;
	}

	public EAdMap<String, String> getParams() {
		return params;
	}

	public void setParams(EAdMap<String, String> params) {
		this.params = params;
	}

	public EAdMap<String, EAdList<String>> getSceneGraph() {
		return sceneGraph;
	}

	public void setSceneGraph(EAdMap<String, EAdList<String>> sceneGraph) {
		this.sceneGraph = sceneGraph;
	}

	public EAdMap<String, EAdList<String>> getChaptersScenes() {
		return chaptersScenes;
	}

	public void setChaptersScenes(EAdMap<String, EAdList<String>> chaptersScenes) {
		this.chaptersScenes = chaptersScenes;
	}

	public void addScene(String chapterId, String sceneId) {
		EAdList<String> scenes = chaptersScenes.get(chapterId);
		if (scenes == null) {
			scenes = new EAdList<String>();
			chaptersScenes.put(chapterId, scenes);
		}
		scenes.add(sceneId);
	}

}
