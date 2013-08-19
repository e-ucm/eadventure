package es.eucm.ead.reader2.model;

import es.eucm.ead.model.elements.EAdAdventureModel;
import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.elements.extra.EAdMap;
import es.eucm.ead.model.interfaces.Param;
import es.eucm.ead.model.interfaces.features.Identified;

public class Manifest implements Identified {

	private String id;

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

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
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

	public EAdList<String> getInitialScenesIds() {
		return this.initialScenes;
	}
}
