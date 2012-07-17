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

package ead.editor.view.swing.scene;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.google.inject.Guice;
import com.google.inject.Injector;

import ead.common.model.elements.BasicAdventureModel;
import ead.common.model.elements.BasicChapter;
import ead.common.model.elements.EAdAdventureModel;
import ead.common.model.elements.effects.ChangeSceneEf;
import ead.common.model.elements.scene.EAdScene;
import ead.editor.control.CommandManager;
import ead.editor.view.ComponentProvider;
import ead.editor.view.generics.scene.PreviewPanel;
import ead.editor.view.generics.scene.impl.EditionScene;
import ead.elementfactories.EAdElementsFactory;
import ead.elementfactories.demos.scenes.EmptyScene;
import ead.engine.core.game.Game;
import ead.engine.core.game.GameState;
import ead.engine.core.platform.EngineConfiguration;
import ead.engine.core.platform.GUI;
import ead.engine.desktop.core.platform.module.DesktopAssetHandlerModule;
import ead.engine.java.core.platform.modules.JavaBasicGameModule;
import javax.swing.JScrollPane;

public class PreviewPanelComponentProvider implements ComponentProvider<PreviewPanel, JComponent> {

	private EAdAdventureModel model;

	private BasicChapter c;

	@SuppressWarnings("unused")
	private CommandManager commandManager;

	private Game game;

	private GameState gameState;

	private DesktopEditorGUI gui;

	private EngineConfiguration conf;

	public PreviewPanelComponentProvider(CommandManager commandManager) {
		this.commandManager = commandManager;
		Injector injector = Guice.createInjector(new DesktopAssetHandlerModule(),
				new DesktopEditorModule(), new JavaBasicGameModule());

		model = new BasicAdventureModel();
		if (EAdElementsFactory.getInstance().getInventory() != null) {
			model.setInventory(EAdElementsFactory.getInstance().getInventory());
		}
		c = new BasicChapter();
		model.getChapters().add(c);


		gui = (DesktopEditorGUI) injector.getInstance(GUI.class);
		conf = injector.getInstance(EngineConfiguration.class);

		EAdScene scene = new EmptyScene();

		c.getScenes().add(scene);
		c.setInitialScene(scene);

		game = injector.getInstance(Game.class);
		game.setGame(model, model.getChapters().get(0));
		gameState = injector.getInstance(GameState.class);

		new Thread(new Runnable() {

			@Override
			public void run() {
				//launcher.launch(null);
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

		ChangeSceneEf changeScene = new ChangeSceneEf();
		//TODO will need to clear scene stack...
		changeScene.setNextScene(new EditionScene(element.getScene()));
		gameState.addEffect(changeScene);

		JScrollPane pane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		//pane.setMinimumSize(new Dimension(200, 150));

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(pane, BorderLayout.CENTER);

		JPanel zoomPanel = new JPanel();
		zoomPanel.setLayout(new GridLayout(1,0));
		JButton zoom = new JButton("zoom +");
		zoomPanel.add(zoom);
		zoom.addActionListener(new ZoomAction(panel, + 1, pane, conf, game));

		JButton zoom2 = new JButton("zoom -");
		zoomPanel.add(zoom2);
		zoom2.addActionListener(new ZoomAction(panel, - 1, pane, conf, game));

		mainPanel.add(zoomPanel, BorderLayout.NORTH);
		return mainPanel;
	}

	private static class ZoomAction implements ActionListener {

		private JPanel panel;

		private int sign;

		private EngineConfiguration conf;

		private JScrollPane pane;

		private Game game;

		public ZoomAction(JPanel panel2, int sign, JScrollPane pane, EngineConfiguration conf, Game game ) {
			panel = panel2;
			this.pane = pane;
			this.sign = sign;
			this.conf = conf;
			this.game = game;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			double width = panel.getPreferredSize().getWidth();
			double height = panel.getPreferredSize().getHeight();
			height = height / width * (width + sign * 100);
			width = width + sign * 100;
			//TODO check if enough zoom
			if (width > 100 && width < 2000) {
				panel.setPreferredSize(new Dimension((int) width, (int) height));
				conf.setSize((int) width, (int) height);
				game.updateInitialTransformation();
				pane.getViewport().setView(panel);
			}
		}
	}
}
