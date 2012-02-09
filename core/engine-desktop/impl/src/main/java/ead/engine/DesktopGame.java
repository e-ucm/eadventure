package ead.engine;

import java.util.Map;

import com.google.inject.Guice;
import com.google.inject.Injector;

import ead.common.model.elements.EAdAdventureModel;
import ead.common.model.elements.EAdAdventureModelImpl;
import ead.common.model.elements.EAdChapterImpl;
import ead.common.model.elements.scene.EAdScene;
import ead.common.params.EAdURI;
import ead.common.params.EAdURIImpl;
import ead.common.params.text.EAdString;
import ead.common.resources.StringHandler;
import ead.elementfactories.EAdElementsFactory;
import ead.engine.core.game.Game;
import ead.engine.core.modules.BasicGameModule;
import ead.engine.core.platform.EngineConfiguration;
import ead.engine.core.platform.PlatformLauncher;
import ead.engine.core.platform.extra.DesktopAssetHandlerModule;
import ead.engine.core.platform.extra.DesktopModule;

public class DesktopGame {

	private Injector injector;

	private String file;

	public DesktopGame(EAdScene scene) {
		EAdAdventureModel model = new EAdAdventureModelImpl();
		EAdChapterImpl chapter = new EAdChapterImpl();
		chapter.setId("chapter1");
		chapter.getScenes().add(scene);
		chapter.setInitialScene(scene);
		model.getChapters().add(chapter);
		init(model, EAdElementsFactory.getInstance().getStringFactory().getStrings());
	}

	public DesktopGame(EAdAdventureModel adventureModel, String file, Map<EAdString, String> strings) {
		init(adventureModel, strings);
		this.file = file;
	}

	public void init(EAdAdventureModel model, Map<EAdString, String> strings) {
		injector = Guice.createInjector(new DesktopAssetHandlerModule(),
				new DesktopModule(), new BasicGameModule());

		Game game = injector.getInstance(Game.class);
		game.setGame(model, model.getChapters().get(0));

		StringHandler stringHandler = injector.getInstance(StringHandler.class);
		stringHandler.addStrings(strings);
	}

	public void launch() {
		final PlatformLauncher launcher = injector
				.getInstance(PlatformLauncher.class);
		final EAdURI uri = ( file == null ) ? null : new EAdURIImpl(file);

		EngineConfiguration conf = injector
				.getInstance(EngineConfiguration.class);
		conf.setSize(800, 600);

		new Thread() {
			public void run() {
				launcher.launch(uri);
			}
		}.start();
	}

}
