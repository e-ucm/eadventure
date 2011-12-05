package es.eucm.eadventure.editor.view.swing.scene;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.google.inject.Guice;
import com.google.inject.Injector;

import es.eucm.eadventure.common.elementfactories.EAdElementsFactory;
import es.eucm.eadventure.common.elementfactories.scenedemos.EmptyScene;
import es.eucm.eadventure.common.elementfactories.scenedemos.InitScene;
import es.eucm.eadventure.common.model.elements.EAdAdventureModel;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.impl.EAdAdventureModelImpl;
import es.eucm.eadventure.common.model.impl.EAdChapterImpl;
import es.eucm.eadventure.editor.control.CommandManager;
import es.eucm.eadventure.editor.view.ComponentProvider;
import es.eucm.eadventure.editor.view.generics.impl.SceneInterfaceElement;
import es.eucm.eadventure.editor.view.generics.scene.PreviewPanel;
import es.eucm.eadventure.engine.core.Game;
import es.eucm.eadventure.engine.core.impl.modules.BasicGameModule;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;
import es.eucm.eadventure.engine.core.platform.PlatformLauncher;
import es.eucm.eadventure.engine.core.platform.impl.extra.DesktopAssetHandlerModule;
import es.eucm.eadventure.engine.core.platform.impl.extra.DesktopModule;
import es.eucm.eadventure.gui.eadcanvaspanel.EAdCanvasPanel;
import es.eucm.eadventure.gui.eadcanvaspanel.listeners.DragListener;
import es.eucm.eadventure.gui.eadcanvaspanel.listeners.ResizeListener;
import es.eucm.eadventure.gui.eadcanvaspanel.scrollcontainers.EAdFixScrollCanvasPanel;

public class PreviewPanelComponentProvider implements ComponentProvider<PreviewPanel, JComponent> {

	private PlatformLauncher launcher;

	private EAdAdventureModel model;

	private EAdChapterImpl c;

	private CommandManager commandManager;
	
	private Game game;
	
	private DesktopEditorGUI gui;
	
	public PreviewPanelComponentProvider(CommandManager commandManager) {
		this.commandManager = commandManager;
		
		Injector injector = Guice.createInjector(new DesktopAssetHandlerModule(),
				new DesktopEditorModule(), new BasicGameModule());
		PlatformConfiguration conf = injector.getInstance(PlatformConfiguration.class);
		conf.setWidth(800);
		conf.setHeight(600);

		launcher = injector.getInstance(PlatformLauncher.class);
		model = new EAdAdventureModelImpl();
		if (EAdElementsFactory.getInstance().getInventory() != null) {
			model.setInventory(EAdElementsFactory.getInstance().getInventory());
		}
		c = new EAdChapterImpl();
		model.getChapters().add(c);


		gui = (DesktopEditorGUI) injector.getInstance(GUI.class);
		
		EAdScene scene = new InitScene();
		
		c.getScenes().add(scene);
		c.setInitialScene(scene);

		game = injector.getInstance(Game.class);
		game.setGame(model, model.getChapters().get(0));

		new Thread(new Runnable() {

			@Override
			public void run() {
				launcher.launch(null);
			}
			
		}).start();
	}

	@Override
	public JComponent getComponent(PreviewPanel element) {
		JComponent panel = null;
		do {
			panel = gui.getPanel();
			Thread.yield();
		} while (panel == null);
		return panel;
	}

	
	
	
}
