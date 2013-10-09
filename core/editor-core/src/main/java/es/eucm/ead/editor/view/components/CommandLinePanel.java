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
						textArea.getActionMap().get(defaultEnterAction).actionPerformed(e);
					}
				}
			});
		textArea.getActionMap().put(substitutePrefix + defaultUpAction, 
			new AbstractAction(substitutePrefix + defaultUpAction) {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (interactiveModeCallback != null && 
							((interactiveDir == 1 && interactivePast.size()>1) ||
							 (interactiveDir != 1 && interactivePast.size()>0)) && 
							textArea.getCaretLineNumber() == 0) {
						if (interactiveDir == 1) {
							interactiveFuture.push(interactivePast.pop());
						} 
						String cmd = interactivePast.pop();
						interactiveFuture.push(cmd);
						textArea.setText(cmd);
						textArea.setCaretPosition(textArea.getText().length());
						interactiveDir = -1;
					} else {
						textArea.getActionMap().get(defaultUpAction).actionPerformed(e);
					}
				}
			});
		textArea.getActionMap().put(substitutePrefix + defaultDownAction, 
			new AbstractAction(substitutePrefix + defaultDownAction) {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (interactiveModeCallback != null && 
							((interactiveDir == -1 && interactiveFuture.size()>1) ||
							 (interactiveDir != -1 && interactiveFuture.size()>0)) && 
							 (textArea.getCaretLineNumber()+1 == textArea.getLineCount())) {
						if (interactiveDir == -1) {
							interactivePast.push(interactiveFuture.pop());
						} 
						String cmd = interactiveFuture.pop();
						interactivePast.push(cmd);
						textArea.setText(cmd);
						textArea.setCaretPosition(textArea.getText().length());
						interactiveDir = 1;
					} else {
						textArea.getActionMap().get(defaultDownAction).actionPerformed(e);
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
		while ( ! interactiveFuture.isEmpty()) {
			interactivePast.push(interactiveFuture.pop());
		}				
		interactivePast.push(text);
		interactiveDir = 0;		
	}
}
