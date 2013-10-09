/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.eucm.ead.editor.view.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A panel where source-code can be displayed and edited.
 * 
 * @author mfreire
 */
public class SyntaxPanel extends JPanel {

	static private Logger logger = LoggerFactory
			.getLogger(SyntaxPanel.class);

	protected RSyntaxTextArea textArea;
	protected RTextScrollPane scrollPane;

	private ComponentAdapter previousAdapter;
	private JComponent previousReference;
	
	public SyntaxPanel(String syntaxStyle) {
		textArea = new RSyntaxTextArea();
		textArea.setSyntaxEditingStyle(syntaxStyle);
		textArea.setCodeFoldingEnabled(true);
		textArea.setAntiAliasingEnabled(true);
		scrollPane = new RTextScrollPane(textArea);
		scrollPane.setFoldIndicatorEnabled(true);

		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
	}
	
	public String getText() {
		return textArea.getText();
	}

	public void setText(String text) {
		textArea.setText(text);
	}
	

	public final void setReferenceComponent(final JComponent c, final int mx) {
		if (c != previousReference) {
			if (previousReference != null) {
				previousReference.removeComponentListener(previousAdapter);
			}
			previousReference = c;
			ComponentAdapter nextAdapter = new ComponentAdapter() {
				private boolean isUpdating = false;
				private Dimension oldSize;
				@Override
				public void componentResized(ComponentEvent e) {
					if (oldSize != null && oldSize.equals(c.getSize())) {
						return; 
					} else {
						oldSize.setSize(c.getSize());
					}
					
					if (isVisible() && !isUpdating) {
						isUpdating = true;
						FontMetrics fm = textArea
								.getFontMetricsForTokenType(TokenTypes.WHITESPACE);
						int w = c.getWidth() - mx;
						int h = c.getHeight();
						int fw = fm.getMaxAdvance();
						int cols = w / fw;
						logger.debug(
							"resized {} to {}x{}, @ {}x?, yielding {} cols",
							hashCode(), "" + w, "" + h, "" + fw, ""	+ cols);
						textArea.setColumns(cols);
					} else {
						logger.debug("avoided resizing {}", hashCode());
					}
					isUpdating = false;
				}

			};
			c.addComponentListener(nextAdapter);
			previousAdapter = nextAdapter;
		}
	}	
}
