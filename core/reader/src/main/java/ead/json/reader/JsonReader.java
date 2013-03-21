package ead.json.reader;

import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ead.common.model.elements.scenes.EAdScene;
import ead.json.reader.model.JsonAsset;
import ead.json.reader.model.JsonEvent;
import ead.json.reader.model.JsonScene;
import ead.reader.model.ObjectsFactory;
import ead.tools.reflection.ReflectionProvider;

public class JsonReader {

	private Gson gson;

	private ObjectsFactory objectsFactory;

	private AssetsReader assetsReader;

	private SceneReader sceneReader;

	private EventReader eventReader;

	public JsonReader(ReflectionProvider reflectionProvider) {
		gson = new Gson();
		objectsFactory = new ObjectsFactory(reflectionProvider, null);
		assetsReader = new AssetsReader(objectsFactory);
		sceneReader = new SceneReader(objectsFactory);
		eventReader = new EventReader(objectsFactory);

	}

	public void addAssets(String file) {
		FileReader reader = null;
		try {
			reader = new FileReader(file);
			Collection<JsonAsset> assets = gson.fromJson(reader,
					new TypeToken<Collection<JsonAsset>>() {
					}.getType());
			assetsReader.parseAssets(assets);
		} catch (Exception e) {
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {

				}
			}
		}
	}

	public EAdScene getScene(String file) {
		FileReader reader = null;
		try {
			reader = new FileReader(file);
			JsonScene scene = gson.fromJson(reader, JsonScene.class);
			return sceneReader.parseScene(scene);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {

				}
			}
		}
		return null;
	}

	public void addEvents(String file) {
		FileReader reader = null;
		try {
			reader = new FileReader(file);
			Collection<JsonEvent> events = gson.fromJson(reader,
					new TypeToken<Collection<JsonEvent>>() {
					}.getType());
			eventReader.addEvents(events);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {

				}
			}
		}
	}

}
