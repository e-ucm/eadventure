package ead.guitools.enginegui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import ead.common.model.elements.BasicAdventureModel;
import ead.common.model.elements.BasicChapter;
import ead.common.model.elements.EAdAdventureModel;
import ead.common.model.elements.guievents.MouseGEv;
import ead.common.model.elements.scenes.BasicScene;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.params.fills.ColorFill;
import ead.common.params.text.EAdString;
import ead.common.resources.assets.drawable.basics.shapes.RectangleShape;
import ead.common.util.EAdPosition.Corner;
import ead.engine.core.debuggers.DebuggerHandler;
import ead.engine.core.debuggers.GhostElementDebugger;
import ead.engine.core.debuggers.TrajectoryDebugger;
import ead.engine.core.gameobjects.factories.EffectGOFactory;
import ead.engine.core.gdx.desktop.DesktopGame;
import ead.guitools.enginegui.effects.loadgame.LoadGameEffect;
import ead.guitools.enginegui.effects.loadgame.LoadGameGO;

public class EngineGUI {

	private static Properties properties;

	public static void main(String args[]) {
		try {

			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {

		} catch (InstantiationException e) {

		} catch (IllegalAccessException e) {

		} catch (UnsupportedLookAndFeelException e) {

		}

		loadProperties();

		DesktopGame engine = new DesktopGame();
		addPlugins(engine);
		boolean trajectories = Boolean.parseBoolean(getProperty("dtraj",
				"false"));
		DebuggerHandler debuggerHandler = engine
				.getInstance(DebuggerHandler.class);
		if (trajectories) {
			debuggerHandler.add(TrajectoryDebugger.class);
		}
		
		debuggerHandler.add(GhostElementDebugger.class);

		SceneElement element = new SceneElement(new RectangleShape(100, 100,
				ColorFill.BLACK));
		BasicScene scene = new BasicScene(new RectangleShape(800, 600,
				ColorFill.WHITE));
		element.setPosition(Corner.CENTER, 400, 300);
		scene.add(element);
		element.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, new LoadGameEffect());
		BasicAdventureModel adventure = new BasicAdventureModel();
		adventure.getChapters().add(new BasicChapter(scene));
		engine.load((EAdAdventureModel) adventure,
				new HashMap<EAdString, String>(), new HashMap<String, String>());
	}

	private static void addPlugins(DesktopGame engine) {
		EffectGOFactory effectFactory = engine
				.getInstance(EffectGOFactory.class);
		effectFactory.put(LoadGameEffect.class, LoadGameGO.class);
//		effectFactory.put(UseTracesEffect.class, UseTracesEffectGO.class);
	}

	public static String getProperty(String key, String defaultValue) {
		if (properties == null) {
			loadProperties();
		}
		return properties.getProperty(key, defaultValue);
	}

	public static void setProperty(String key, String value) {
		if (properties == null) {
			loadProperties();
		}
		properties.setProperty(key, value);
		saveProperties();
	}

	private static void loadProperties() {
		properties = new Properties();
		File file = new File("enginegui.properties");
		FileInputStream is = null;
		if (file.exists()) {
			try {
				is = new FileInputStream(file);
				properties.load(is);
			} catch (FileNotFoundException e) {

			} catch (IOException e) {

			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
					}
				}
			}
		}
	}

	private static void saveProperties() {
		File file = new File("enginegui.properties");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {

			}
		}
		FileOutputStream os = null;
		try {
			os = new FileOutputStream(file);
			properties.store(os, "");

		} catch (Exception e) {

		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {

				}
			}
		}

	}

}
