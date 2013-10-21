package es.eucm.ead.techdemo.desktop;

import com.badlogic.gdx.utils.Array;
import es.eucm.ead.engine.desktop.DesktopGame;
import es.eucm.ead.engine.tracking.GameTracker;
import es.eucm.ead.engine.tracking.gleaner.GleanerGameTracker;
import es.eucm.ead.model.elements.AdventureGame;
import es.eucm.ead.model.elements.Chapter;
import es.eucm.ead.model.elements.scenes.Scene;
import es.eucm.ead.techdemo.elementfactories.scenes.scenes.InitScene;
import es.eucm.ead.tools.java.JavaTextFileWriter;
import es.eucm.ead.tools.java.reflection.JavaReflectionProvider;
import es.eucm.ead.writer.AdventureWriter;
import es.eucm.gleaner.tracker.JerseyTracker;
import es.eucm.gleaner.tracker.Tracker;

public class TechDemoMain {

	public static void main(String[] args) {
		Array a;
		DesktopGame g = new DesktopGame();
		g.setDebug(true);
		g.setBind(GameTracker.class, GleanerGameTracker.class);
		g.setBind(Tracker.class, JerseyTracker.class);
		g.setPath("src/main/resources");
		InitScene scene = new InitScene();
		Chapter chapter = new Chapter(scene);
		for (Scene s : scene.getScenes()) {
			chapter.addScene(s);
		}
		AdventureGame model = new AdventureGame();
		model.getChapters().add(chapter);
		AdventureWriter writer = new AdventureWriter(
				new JavaReflectionProvider());
		writer.write(model, "src/main/resources/", new JavaTextFileWriter());
		g.start();
	}

}
