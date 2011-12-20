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

package es.eucm.eadventure.engine.core.platform.test.launcher;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileFilter;

import com.google.inject.Guice;
import com.google.inject.Injector;

import es.eucm.eadventure.common.elementfactories.EAdElementsFactory;
import es.eucm.eadventure.common.elementfactories.demos.SceneDemo;
import es.eucm.eadventure.common.elementfactories.demos.TechDemoAdventure;
import es.eucm.eadventure.common.impl.importer.EAdventure1XImporter;
import es.eucm.eadventure.common.impl.importer.ImporterConfigurationModule;
import es.eucm.eadventure.common.impl.reader.EAdAdventureDOMModelReader;
import es.eucm.eadventure.common.impl.writer.EAdAdventureModelWriter;
import es.eucm.eadventure.common.model.elements.EAdAdventureModel;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.impl.EAdAdventureModelImpl;
import es.eucm.eadventure.common.model.impl.EAdChapterImpl;
import es.eucm.eadventure.common.params.EAdString;
import es.eucm.eadventure.common.params.EAdURIImpl;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.Game;
import es.eucm.eadventure.engine.core.debuggers.impl.EAdMainDebugger;
import es.eucm.eadventure.engine.core.debuggers.impl.FieldsDebugger;
import es.eucm.eadventure.engine.core.debuggers.impl.TrajectoryDebugger;
import es.eucm.eadventure.engine.core.impl.modules.BasicGameModule;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.PlatformLauncher;
import es.eucm.eadventure.engine.core.platform.impl.DesktopAssetHandler;
import es.eucm.eadventure.engine.core.platform.impl.DesktopPlatformLauncher;
import es.eucm.eadventure.engine.core.platform.impl.extra.DesktopAssetHandlerModule;
import es.eucm.eadventure.engine.core.platform.impl.extra.DesktopModule;
import es.eucm.eadventure.engine.core.test.launcher.BaseTestLauncher;

/**
 * Base class for desktop's launchers
 * 
 */
public class DesktopDemos extends BaseTestLauncher {

	protected static JComboBox comboBox;

	protected static JList list;

	private static final Dimension DIMENSIONS[] = new Dimension[] {
			new Dimension(800, 600), new Dimension(1200, 900),
			new Dimension(400, 300) };

	protected static JCheckBox checkBox;

	protected static JCheckBox trajectoryDebugger;

	protected static JCheckBox fieldsDebugger;

	public DesktopDemos(Injector injector, EAdAdventureModel model,
			Map<EAdString, String> strings) {
		super(injector, model, strings);
		System.setProperty("com.apple.mrj.application.apple.menu.about.name",
				"eAdventure");
	}

	public static void main(String args[]) {
		JFrame frame = new SceneDemosFrame();
		frame.setVisible(true);
	}

	public static class ClassListCellRenderer extends JLabel implements
			ListCellRenderer {
		private static final long serialVersionUID = 1L;

		public ClassListCellRenderer() {
			this.setOpaque(true);
		}

		@Override
		public Component getListCellRendererComponent(JList arg0, Object value,
				int arg2, boolean isSelected, boolean arg4) {
			SceneDemo scene = (SceneDemo) value;
			setText(scene.getDemoName() + " - " + scene.getSceneDescription());
			if (isSelected) {
				this.setBackground(Color.BLACK);
				this.setForeground(Color.WHITE);
			} else {
				this.setBackground(Color.WHITE);
				this.setForeground(Color.BLACK);
			}
			return this;
		}

	}

	public static class DimensionsCellRenderer extends JLabel implements
			ListCellRenderer {

		private static final long serialVersionUID = 1L;

		public DimensionsCellRenderer() {
			setOpaque(true);
		}

		@Override
		public Component getListCellRendererComponent(JList arg0, Object value,
				int arg2, boolean isSelected, boolean arg4) {
			Dimension d = (Dimension) value;
			setText((int) d.getWidth() + "x" + (int) d.getHeight());
			return this;
		}

	}

	public static class SceneDemosFrame extends JFrame {

		private static final long serialVersionUID = 3665422751105063444L;

		public SceneDemosFrame() {
			super("Scenes demo");
			TechDemoAdventure model = new TechDemoAdventure();
			model.setInventory(EAdElementsFactory.getInstance()
					.getInventory());
			Object scenes[] = model.getScenes().toArray();

			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			JPanel container = new JPanel();

			JPanel p = new JPanel();
			container.setLayout(new BorderLayout());

			checkBox = new JCheckBox("Write and read from XML");
			p.add(checkBox);
			container.add(p, BorderLayout.NORTH);

			list = new JList(scenes);
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			list.setCellRenderer(new ClassListCellRenderer());
			list.setSelectedIndex(0);

			comboBox = new JComboBox(DIMENSIONS);
			comboBox.setRenderer(new DimensionsCellRenderer());
			comboBox.setSelectedIndex(0);

			trajectoryDebugger = new JCheckBox("Trajectory Debugger");
			trajectoryDebugger.setSelected(false);

			fieldsDebugger = new JCheckBox("Fields Debugger");
			fieldsDebugger.setSelected(false);

			p.add(comboBox);
			p.add(trajectoryDebugger);
			p.add(fieldsDebugger);

			final JButton openProject = new JButton("Open 2.0 project");
			openProject.addActionListener(new OpenNewProject());
			p.add(openProject);

			final JButton importProject = new JButton("Import old project");
			importProject.addActionListener(new ImportProject());
			p.add(importProject);

			container.add(list, BorderLayout.CENTER);

			JButton button = new JButton("Start");
			button.addActionListener(new StartDemo(model));

			container.add(button, BorderLayout.SOUTH);

			setContentPane(container);
			pack();

		}
	}

	private static class StartDemo implements ActionListener {

		private TechDemoAdventure model;

		public StartDemo(TechDemoAdventure model) {
			this.model = model;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			new Thread() {
				public void run() {
					Dimension d = (Dimension) comboBox.getSelectedItem();
					EAdScene scene = (EAdScene) list.getSelectedValue();
					model.setInitialScene(scene);
					model.setGameWidth(d.width);
					model.setGameHeight(d.height);

					if (trajectoryDebugger.isSelected()) {
						EAdMainDebugger.addDebugger(TrajectoryDebugger.class);
					}
					if (fieldsDebugger.isSelected()) {
						EAdMainDebugger.addDebugger(FieldsDebugger.class);
					}

					if (checkBox.isSelected()) {
						EAdAdventureModel model = new EAdAdventureModelImpl();
						EAdChapterImpl chapter = new EAdChapterImpl();
						chapter.setId("chapter1");
						chapter.getScenes().add(scene);
						chapter.setInitialScene(scene);
						model.getChapters().add(chapter);
						((EAdAdventureModelImpl) model).setGameWidth((int) d
								.getWidth());
						((EAdAdventureModelImpl) model).setGameHeight((int) d
								.getHeight());

						File f = new File("src/test/resources/sceneDemo.xml");
						// Write to XML
						try {
							FileOutputStream os = new FileOutputStream(f);
							new EAdAdventureModelWriter().write(model, os);
							os.close();
							FileInputStream is = new FileInputStream(f);
							model = new EAdAdventureDOMModelReader().read(is);
							is.close();

							Injector injector = createNewInjector();
							new DesktopDemos(injector, model,
									EAdElementsFactory.getInstance()
											.getStringFactory().getStrings())
									.start();

						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}

					} else {
						new DesktopDemos(createNewInjector(), model,
								EAdElementsFactory.getInstance()
										.getStringFactory().getStrings())
								.start();
					}
				}
			}.start();

		}
	}

	private static class OpenNewProject implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			new Thread() {
				public void run() {

					JFileChooser fileChooser = new JFileChooser();

					FileFilter filter = new FileFilter() {

						@Override
						public boolean accept(File f) {
							return f.isDirectory()
									|| f.getName().endsWith(".xml");
						}

						@Override
						public String getDescription() {
							return "eAdventure 2.0 projects";
						}

					};
					fileChooser.setFileFilter(filter);

					fileChooser.showOpenDialog(null);

					EAdAdventureModel model = null;

					FileInputStream is;
					try {
						if (trajectoryDebugger.isSelected()) {
							EAdMainDebugger
									.addDebugger(TrajectoryDebugger.class);
						}
						if (fieldsDebugger.isSelected()) {
							EAdMainDebugger.addDebugger(FieldsDebugger.class);
						}

						is = new FileInputStream(fileChooser.getSelectedFile());
						model = new EAdAdventureDOMModelReader().read(is);
						is.close();

						Dimension d = (Dimension) comboBox.getSelectedItem();

						((EAdAdventureModelImpl) model).setGameWidth((int) d
								.getWidth());
						((EAdAdventureModelImpl) model).setGameHeight((int) d
								.getHeight());

						Injector injector = createNewInjector();
						DesktopAssetHandler assetHandler = (DesktopAssetHandler) injector
								.getInstance(AssetHandler.class);
						assetHandler.setResourceLocation(fileChooser
								.getCurrentDirectory());

						new DesktopDemos(injector, model, EAdElementsFactory
								.getInstance().getStringFactory().getStrings())
								.start();

					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}.start();
		}
	}

	private static class ImportProject implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			new Thread() {
				public void run() {
					JFileChooser fileChooser = new JFileChooser();

					FileFilter filter = new FileFilter() {

						@Override
						public boolean accept(File f) {
							return f.isDirectory()
									|| f.getName().endsWith(".eap")
									|| f.getName().endsWith(".ead")
									|| f.getName().endsWith(".zip");
						}

						@Override
						public String getDescription() {
							return "eAdventure projects";
						}

					};
					fileChooser.setFileFilter(filter);

					if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

						File f = fileChooser.getSelectedFile();

						String folder = f.getParentFile().getAbsolutePath();

						String projectName = f.getName();

						Injector injector = Guice.createInjector(
								new ImporterConfigurationModule(folder + "/"
										+ projectName),
								new DesktopAssetHandlerModule(),
								new DesktopModule(), new BasicGameModule());
						EAdventure1XImporter importer = injector
								.getInstance(EAdventure1XImporter.class);

						projectName = projectName.substring(0,
								projectName.length() - 4);
						JDialog dialog = new JDialog();
						dialog.setTitle("eAdventure");
						dialog.setModal(false);
						dialog.setSize(50, 50);
						dialog.setResizable(false);
						dialog.setLocationRelativeTo(null);
						JLabel label = new JLabel();
						label.setText("Importing...");
						dialog.getContentPane().setLayout(new BorderLayout());
						dialog.getContentPane().add(label, BorderLayout.CENTER);
						dialog.setVisible(true);
						EAdAdventureModel model = importer.importGame(folder
								+ "/" + projectName + "Imported");
						dialog.setVisible(false);

						if (model != null) {

							Dimension d = (Dimension) comboBox
									.getSelectedItem();
							if (trajectoryDebugger.isSelected()) {
								EAdMainDebugger
										.addDebugger(TrajectoryDebugger.class);
							}
							if (fieldsDebugger.isSelected()) {
								EAdMainDebugger
										.addDebugger(FieldsDebugger.class);
							}

							((EAdAdventureModelImpl) model).setGameWidth(d.width);
							((EAdAdventureModelImpl) model).setGameHeight(d.height);

							injector.getInstance(StringHandler.class)
									.setString(new EAdString("Loading"),
											"loading");

							Game game = injector.getInstance(Game.class);
							game.setGame(model, model.getChapters().get(0));

							System.setProperty(
									"com.apple.mrj.application.apple.menu.about.name",
									"eAdventure");

							PlatformLauncher launcher = injector
									.getInstance(PlatformLauncher.class);

							// TODO extract file from args or use default?
							File file = new File(folder, projectName
									+ "Imported");
							// File file = new File("/ProyectoJuegoFINAL.ead");
							((DesktopPlatformLauncher) launcher)
									.launch(new EAdURIImpl(file.toString()));
						} else {
							JOptionPane
									.showMessageDialog(
											null,
											"Error importing the game. Check the console for more information.",
											"Import error",
											JOptionPane.ERROR_MESSAGE);
						}
					}
				}

			}.start();
		}
	}

	private static Injector createNewInjector() {
		Injector i = Guice.createInjector(new DesktopAssetHandlerModule(),
				new DesktopModule(), new BasicGameModule());
		return i;
	}

}
