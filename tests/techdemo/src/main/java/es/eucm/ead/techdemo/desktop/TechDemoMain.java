package es.eucm.ead.techdemo.desktop;

import es.eucm.ead.engine.desktop.DesktopGame;
import es.eucm.ead.engine.tracking.GameTracker;
import es.eucm.ead.engine.tracking.gleaner.GleanerGameTracker;
import es.eucm.ead.model.elements.BasicAdventureModel;
import es.eucm.ead.model.elements.BasicChapter;
import es.eucm.ead.model.elements.EAdAdventureModel;
import es.eucm.ead.model.elements.scenes.EAdScene;
import es.eucm.ead.techdemo.elementfactories.scenes.scenes.InitScene;
import es.eucm.ead.tools.java.JavaTextFileWriter;
import es.eucm.ead.tools.java.reflection.JavaReflectionProvider;
import es.eucm.ead.writer.AdventureWriter;
import es.eucm.gleaner.tracker.JerseyTracker;
import es.eucm.gleaner.tracker.Tracker;

public class TechDemoMain {

	public static void main(String[] args) {
		DesktopGame g = new DesktopGame();
		g.setDebug(true);
		g.setBind(GameTracker.class, GleanerGameTracker.class);
		g.setBind(Tracker.class, JerseyTracker.class);
		g.setPath("src/main/resources");
		InitScene scene = new InitScene();
		BasicChapter chapter = new BasicChapter(scene);
		for (EAdScene s : scene.getScenes()) {
			chapter.addScene(s);
		}
		EAdAdventureModel model = new BasicAdventureModel();
		model.getChapters().add(chapter);
		AdventureWriter writer = new AdventureWriter(
				new JavaReflectionProvider());
		writer.write(model, "src/main/resources/", new JavaTextFileWriter());
		g.start();
		g.load();
	}

}
