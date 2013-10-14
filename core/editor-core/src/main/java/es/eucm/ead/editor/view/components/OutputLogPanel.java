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
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.eucm.ead.editor.view.components;

import java.awt.Rectangle;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An output log for Log4j messages or general output.
 * Uses JEdit-Syntax (MIT-Licensed) underneath
 * 
 * @author mfreire
 */
public class OutputLogPanel extends SyntaxPanel {

	private static Logger logger = LoggerFactory
			.getLogger(OutputLogPanel.class);

	public OutputLogPanel() {
		super(RSyntaxTextArea.SYNTAX_STYLE_NONE);
		textArea.setEditable(false);
		textArea.setFont(textArea.getFont().deriveFont(8.0f));
		textArea.setWrapStyleWord(false);
		textArea.setLineWrap(true);
		textArea.setAutoIndentEnabled(true);
		scrollPane.setFoldIndicatorEnabled(false);
		scrollPane.setLineNumbersEnabled(false);
	}

	public void append(String prefix, String text) {
		for (String s : text.split("\n")) {
			append(prefix + s + "\n");
		}
	}

	public void append(String text) {
		textArea.append(text);
		scrollPane.scrollRectToVisible(new Rectangle(0, textArea.getHeight()));
	}
}
