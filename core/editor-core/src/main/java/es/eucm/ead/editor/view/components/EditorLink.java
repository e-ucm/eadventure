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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.eucm.ead.editor.view.components;

import es.eucm.ead.editor.control.Controller;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.JButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A special button that looks like a "label with a link", including
 * and optional icon. Intended to represent an editor node in the views.
 *
 * @author mfreire
 */
public class EditorLink extends JButton implements ActionListener {

	private static final Logger logger = LoggerFactory.getLogger("EditorLink");

	private Controller controller;
	private String editorId;

	public EditorLink(String text, String editorId, Icon icon,
			Controller controller) {
		super(text);
		this.controller = controller;
		this.editorId = editorId;

		if (icon != null) {
			setIcon(icon);
		}
		setForeground(Color.blue);
		setBorderPainted(false);
		setMargin(new Insets(0, 0, 0, 0));
		setContentAreaFilled(false);
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		addActionListener(this);
		setToolTipText(text);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		controller.getViewController().addView("", editorId, true);
	}
}