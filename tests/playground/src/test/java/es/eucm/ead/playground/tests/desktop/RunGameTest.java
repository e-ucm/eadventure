package es.eucm.ead.playground.tests.desktop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import es.eucm.ead.engine.EAdEngine;
import es.eucm.ead.engine.debugger.CommandInterpreter;
import es.eucm.ead.engine.desktop.DesktopGame;
import es.eucm.ead.model.assets.drawable.basics.shapes.RectangleShape;
import es.eucm.ead.model.elements.AdventureGame;
import es.eucm.ead.model.elements.Chapter;
import es.eucm.ead.model.elements.scenes.Scene;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.params.fills.ColorFill;
import es.eucm.ead.tools.java.JavaTextFileWriter;
import es.eucm.ead.tools.java.reflection.JavaReflectionProvider;
import es.eucm.ead.writer.AdventureWriter;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class RunGameTest {

	private Error error;

	@Test
	public void testGame() {
		final DesktopGame g = new DesktopGame();
		g.setDebug(true);
		g.setPath("src/main/resources/testproject/");
		Scene scene = new Scene();
		scene.setId("Initial");
		scene.setBackground(new SceneElement(new RectangleShape(800, 600,
				ColorFill.RED)));
		Chapter chapter = new Chapter(scene);
		chapter.addScene(scene);
		AdventureGame model = new AdventureGame();
		model.getChapters().add(chapter);
		AdventureWriter writer = new AdventureWriter(
				new JavaReflectionProvider());
		writer.write(model, "src/main/resources/testproject",
				new JavaTextFileWriter());
		g.initInjector();
		EAdEngine e = g.getInjector().getInstance(EAdEngine.class);
		Tester tester = new Tester(g.getInjector().getInstance(
				CommandInterpreter.class));
		tester.addCommandTest(new CommandTest(0, "scene", "Initial"));
		e.addApplicationListener(tester);
		g.start();
		synchronized (this) {
			try {
				this.wait();
			} catch (Throwable throwable) {
				throwable.printStackTrace();
			}
		}
		if (error != null) {
			throw error;
		}
	}

	public class Tester implements ApplicationListener {

		private CommandInterpreter interpreter;

		private Array<CommandTest> tests;

		private CommandTest currentTest;

		public Tester(CommandInterpreter interpreter) {
			this.interpreter = interpreter;
			tests = new Array<CommandTest>();
		}

		@Override
		public void create() {
		}

		@Override
		public void resize(int width, int height) {
		}

		@Override
		public void render() {
			try {
				if (currentTest == null) {
					if (tests.size > 0) {
						currentTest = tests.removeIndex(0);
					} else {
						synchronized (RunGameTest.this) {
							RunGameTest.this.notify();
						}
					}
				}

				if (currentTest != null) {
					currentTest.time -= Gdx.graphics.getDeltaTime() * 1000;
					if (currentTest.time <= 0) {
						assertEquals(
								interpreter.interpret(currentTest.command),
								currentTest.result);
						if (tests.size == 0) {
							synchronized (RunGameTest.this) {
								RunGameTest.this.notify();
							}
						}
					}
				}
			} catch (Error e) {
				error = e;
				synchronized (RunGameTest.this) {
					RunGameTest.this.notify();
				}
			}

		}

		@Override
		public void pause() {
		}

		@Override
		public void resume() {
		}

		@Override
		public void dispose() {
		}

		public void addCommandTest(CommandTest commandTest) {
			this.tests.add(commandTest);
		}
	}

	public static class CommandTest {
		public float time;
		public String command;
		public String result;

		public CommandTest(int time, String command, String result) {
			this.time = time;
			this.command = command;
			this.result = result;
		}
	}
}
