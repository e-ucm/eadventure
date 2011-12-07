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

package es.eucm.eadventure.editor.view.swing.scene;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

import es.eucm.eadventure.editor.control.CommandManager;
import es.eucm.eadventure.editor.view.ComponentProvider;
import es.eucm.eadventure.editor.view.generics.impl.SceneInterfaceElement;
import es.eucm.eadventure.gui.eadcanvaspanel.EAdCanvasPanel;
import es.eucm.eadventure.gui.eadcanvaspanel.listeners.DragListener;
import es.eucm.eadventure.gui.eadcanvaspanel.listeners.ResizeListener;
import es.eucm.eadventure.gui.eadcanvaspanel.scrollcontainers.EAdFixScrollCanvasPanel;

public class SceneEditionComponentProvider implements ComponentProvider<SceneInterfaceElement, JComponent> {

	private CommandManager commandManager;
	
	public SceneEditionComponentProvider(CommandManager commandManager) {
		this.commandManager = commandManager;
	}

	@Override
	public JComponent getComponent(SceneInterfaceElement element) {
		EAdFixScrollCanvasPanel scroll = new EAdFixScrollCanvasPanel();

		EAdCanvasPanel dragPanel = scroll.getCanvas();

		DragListener listener = new ResizeListener(dragPanel);
		
		@SuppressWarnings("serial")
		JComponent j = new JComponent() {
			protected void paintComponent(Graphics g) {
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, getWidth(), getHeight());
				g.setColor(Color.BLACK);
				g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
			}

		};
		j.setBounds(0, 0, 50, 50);
		j.addKeyListener(listener);
		j.addMouseListener(listener);
		j.addMouseMotionListener(listener);
		dragPanel.add(j);

		return scroll;
	}

	
	
	
}
