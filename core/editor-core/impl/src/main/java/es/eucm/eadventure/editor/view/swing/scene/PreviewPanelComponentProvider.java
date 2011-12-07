package es.eucm.eadventure.editor.view.swing.scene;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.Scrollable;

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
import es.eucm.eadventure.gui.EAdScrollPane;
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
		JPanel panel = null;
		do {
			panel = gui.getPanel();
			Thread.yield();
		} while (panel == null);
		EAdScrollPane pane = new EAdScrollPane(panel, EAdScrollPane.VERTICAL_SCROLLBAR_ALWAYS, EAdScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		//pane.setMinimumSize(new Dimension(200, 150));
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(pane, BorderLayout.CENTER);
		
		JButton zoom = new JButton("zoom");
		mainPanel.add(zoom, BorderLayout.NORTH);
		zoom.addActionListener(new ZoomAction(panel, + 1));
		
		JButton zoom2 = new JButton("zoom");
		mainPanel.add(zoom2, BorderLayout.SOUTH);
		zoom2.addActionListener(new ZoomAction(panel, - 1));

		return mainPanel;
	}

	private static class ZoomAction implements ActionListener {
		
		private JPanel panel;
		
		private int sign;

		public ZoomAction(JPanel panel2, int sign) {
			panel = panel2;
			this.sign = sign;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			double width = panel.getPreferredSize().getWidth();
			double height = panel.getPreferredSize().getHeight();
			height = height / width * (width + sign * 100);
			width = width + sign * 100;
			panel.setPreferredSize(new Dimension((int) width, (int) height));
		}

	}


	
	
}
