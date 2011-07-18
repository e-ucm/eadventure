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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import com.google.inject.Guice;
import com.google.inject.Injector;

import es.eucm.eadventure.common.elmentfactories.scenedemos.SceneDemos;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.engine.core.impl.modules.BasicGameModule;
import es.eucm.eadventure.engine.core.platform.impl.extra.DesktopAssetHandlerModule;
import es.eucm.eadventure.engine.core.platform.impl.extra.DesktopAssetRendererModule;
import es.eucm.eadventure.engine.core.platform.impl.extra.DesktopModule;
import es.eucm.eadventure.engine.core.test.launcher.BaseTestLauncher;

/**
 * Base class for desktop's launchers
 * 
 */
public class DesktopDemos extends BaseTestLauncher {

	public DesktopDemos(Injector injector, Class<? extends EAdScene> scene) {
		super(injector, scene);
		System.setProperty("com.apple.mrj.application.apple.menu.about.name",
				"eAdventure");
	}

	public static void main(String args[]) {
		final Injector injector = Guice.createInjector(
				new DesktopAssetHandlerModule(),
				new DesktopAssetRendererModule(null), new DesktopModule(),
				new BasicGameModule());

		Object classes[] = SceneDemos.getInstance().getSceneDemos().toArray();

		final JFrame frame = new JFrame("Scenes demo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel container = new JPanel();

		container.setLayout(new BorderLayout());

		final JList list = new JList(classes);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setCellRenderer(new ClassListCellRenderer());
		list.setSelectedIndex(0);

		container.add(list, BorderLayout.CENTER);

		JButton button = new JButton("Start");
		button.addActionListener(new ActionListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.setVisible(false);
				new Thread() {
					public void run() {
						Object o = list.getSelectedValue();
						new DesktopDemos(injector,
								(Class<? extends EAdScene>) o).start();
					}
				}.start();

			}

		});

		container.add(button, BorderLayout.SOUTH);

		frame.setSize(400, 400);
		frame.setLocationRelativeTo(null);
		frame.setContentPane(container);
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
			Class<?> clazz = (Class<?>) value;
			this.setText(clazz.getSimpleName());
			if ( isSelected ){
				this.setBackground(Color.BLACK);
				this.setForeground(Color.WHITE);
			}
			else{
				this.setBackground(Color.WHITE);
				this.setForeground(Color.BLACK);
			}
			return this;
		}

	}

}