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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.google.inject.Guice;
import com.google.inject.Injector;

import es.eucm.eadventure.common.elmentfactories.EAdElementsFactory;
import es.eucm.eadventure.common.elmentfactories.scenedemos.SceneDemo;
import es.eucm.eadventure.common.elmentfactories.scenedemos.SceneDemos;
import es.eucm.eadventure.common.impl.reader.EAdAdventureModelReader;
import es.eucm.eadventure.common.impl.reader.subparsers.AdventureHandler;
import es.eucm.eadventure.common.impl.writer.EAdAdventureModelWriter;
import es.eucm.eadventure.common.model.elements.EAdAdventureModel;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.impl.EAdAdventureModelImpl;
import es.eucm.eadventure.common.model.impl.EAdChapterImpl;
import es.eucm.eadventure.common.params.EAdString;
import es.eucm.eadventure.engine.core.debuggers.impl.EAdMainDebugger;
import es.eucm.eadventure.engine.core.debuggers.impl.FieldsDebugger;
import es.eucm.eadventure.engine.core.debuggers.impl.TrajectoryDebugger;
import es.eucm.eadventure.engine.core.impl.modules.BasicGameModule;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;
import es.eucm.eadventure.engine.core.platform.impl.extra.DesktopAssetHandlerModule;
import es.eucm.eadventure.engine.core.platform.impl.extra.DesktopAssetRendererModule;
import es.eucm.eadventure.engine.core.platform.impl.extra.DesktopModule;
import es.eucm.eadventure.engine.core.test.launcher.BaseTestLauncher;
import es.eucm.eadventure.gui.EAdGUILookAndFeel;

/**
 * Base class for desktop's launchers
 * 
 */
public class DesktopDemos extends BaseTestLauncher {

	private static final Dimension DIMENSIONS[] = new Dimension[] {
			new Dimension(800, 600), new Dimension(400, 300) };

	public DesktopDemos(Injector injector, EAdScene scene) {
		super(injector, scene);
		System.setProperty("com.apple.mrj.application.apple.menu.about.name",
				"eAdventure");
	}

	public DesktopDemos(Injector injector, EAdAdventureModel model,
			Map<EAdString, String> strings) {
		super(injector, model, strings);
	}

	public static void main(String args[]) {
		try {
			UIManager.setLookAndFeel(EAdGUILookAndFeel.getInstance());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
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
			setText(scene.getDemoName() + " - " + scene.getDescription());
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
			Object scenes[] = SceneDemos.getInstance().getScenes().toArray();

			EAdMainDebugger.addDebugger(TrajectoryDebugger.class);
			EAdMainDebugger.addDebugger(FieldsDebugger.class);

			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			JPanel container = new JPanel();

			JPanel p = new JPanel();
			container.setLayout(new BorderLayout());

			final JCheckBox checkBox = new JCheckBox("Write and read from XML");
			p.add(checkBox);
			container.add(p, BorderLayout.NORTH);

			final JList list = new JList(scenes);
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			list.setCellRenderer(new ClassListCellRenderer());
			list.setSelectedIndex(0);

			final JComboBox comboBox = new JComboBox(DIMENSIONS);
			comboBox.setRenderer(new DimensionsCellRenderer());
			comboBox.setSelectedIndex(0);

			p.add(comboBox);

			container.add(list, BorderLayout.CENTER);

			JButton button = new JButton("Start");
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					new Thread() {
						public void run() {
							Dimension d = (Dimension) comboBox
									.getSelectedItem();
							Object o = list.getSelectedValue();
							if (checkBox.isSelected()) {
								EAdScene scene = (EAdScene) o;
								EAdAdventureModel model = new EAdAdventureModelImpl();
								EAdChapterImpl chapter = new EAdChapterImpl(
										"chapter1");
								chapter.getScenes().add(scene);
								chapter.setInitialScene(scene);
								model.getChapters().add(chapter);

								File f = new File(
										"src/test/resources/sceneDemo.xml");
								// Write to XML
								try {
									FileOutputStream os = new FileOutputStream(
											f);
									new EAdAdventureModelWriter().write(model,
											os);
									os.close();
									FileInputStream is = new FileInputStream(f);
									model = new EAdAdventureModelReader(
											new AdventureHandler()).read(is);
									is.close();

									new DesktopDemos(createNewInjector(
											(int) d.getWidth(),
											(int) d.getHeight()), model,
											EAdElementsFactory.getInstance()
													.getStringFactory()
													.getStrings()).start();

								} catch (FileNotFoundException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								}

							} else {
								new DesktopDemos(
										createNewInjector((int) d.getWidth(),
												(int) d.getHeight()),
										(EAdScene) o).start();
							}
						}
					}.start();

				}

			});

			container.add(button, BorderLayout.SOUTH);

			setContentPane(container);
			pack();

		}
	}

	private static Injector createNewInjector(int width, int height) {
		Injector i = Guice.createInjector(new DesktopAssetHandlerModule(),
				new DesktopAssetRendererModule(null), new DesktopModule(),
				new BasicGameModule());
		PlatformConfiguration conf = i.getInstance(PlatformConfiguration.class);
		conf.setWidth(width);
		conf.setHeight(height);
		return i;
	}

}
