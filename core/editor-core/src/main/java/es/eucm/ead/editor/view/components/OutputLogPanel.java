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
