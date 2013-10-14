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

import java.awt.BorderLayout;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.LinkedList;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A panel where source-code can be displayed and edited.
 * 
 * @author mfreire
 */
public class CommandLinePanel extends SyntaxPanel {

	static private Logger logger = LoggerFactory
			.getLogger(CommandLinePanel.class);

	protected ComponentAdapter previousAdapter;

	protected Runnable interactiveModeCallback = null;
	protected LinkedList<String> interactivePast = new LinkedList<String>();
	protected int interactiveDir = 0;
	protected LinkedList<String> interactiveFuture = new LinkedList<String>();
	protected final static String defaultEnterAction = "insert-break";
	protected final static String defaultUpAction = "caret-up";
	protected final static String defaultDownAction = "caret-down";
	protected final static String substitutePrefix = "interactive-";

	public CommandLinePanel() {
		super(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);

		textArea.getActionMap().put(substitutePrefix + defaultEnterAction,
				new AbstractAction(substitutePrefix + defaultEnterAction) {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (interactiveModeCallback != null) {
							pushToHistory();
							interactiveModeCallback.run();
							textArea.setText("");
						} else {
							textArea.getActionMap().get(defaultEnterAction)
									.actionPerformed(e);
						}
					}
				});
		textArea.getActionMap().put(substitutePrefix + defaultUpAction,
				new AbstractAction(substitutePrefix + defaultUpAction) {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (interactiveModeCallback != null
								&& ((interactiveDir == 1 && interactivePast
										.size() > 1) || (interactiveDir != 1 && interactivePast
										.size() > 0))
								&& textArea.getCaretLineNumber() == 0) {
							if (interactiveDir == 1) {
								interactiveFuture.push(interactivePast.pop());
							}
							String cmd = interactivePast.pop();
							interactiveFuture.push(cmd);
							textArea.setText(cmd);
							textArea.setCaretPosition(textArea.getText()
									.length());
							interactiveDir = -1;
						} else {
							textArea.getActionMap().get(defaultUpAction)
									.actionPerformed(e);
						}
					}
				});
		textArea.getActionMap().put(substitutePrefix + defaultDownAction,
				new AbstractAction(substitutePrefix + defaultDownAction) {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (interactiveModeCallback != null
								&& ((interactiveDir == -1 && interactiveFuture
										.size() > 1) || (interactiveDir != -1 && interactiveFuture
										.size() > 0))
								&& (textArea.getCaretLineNumber() + 1 == textArea
										.getLineCount())) {
							if (interactiveDir == -1) {
								interactivePast.push(interactiveFuture.pop());
							}
							String cmd = interactiveFuture.pop();
							interactivePast.push(cmd);
							textArea.setText(cmd);
							textArea.setCaretPosition(textArea.getText()
									.length());
							interactiveDir = 1;
						} else {
							textArea.getActionMap().get(defaultDownAction)
									.actionPerformed(e);
						}
					}
				});

		textArea.getInputMap().put(KeyStroke.getKeyStroke("ENTER"),
				substitutePrefix + defaultEnterAction);
		textArea.getInputMap().put(KeyStroke.getKeyStroke("UP"),
				substitutePrefix + defaultUpAction);
		textArea.getInputMap().put(KeyStroke.getKeyStroke("DOWN"),
				substitutePrefix + defaultDownAction);
	}

	public void setInteractive(Runnable callback) {
		interactiveModeCallback = callback;
		logger.info("Set interactive callback to {}", callback);
	}

	public void pushToHistory() {
		String text = textArea.getText();
		while (!interactiveFuture.isEmpty()) {
			interactivePast.push(interactiveFuture.pop());
		}
		interactivePast.push(text);
		interactiveDir = 0;
	}
}
