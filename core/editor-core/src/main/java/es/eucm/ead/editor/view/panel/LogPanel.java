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

package es.eucm.ead.editor.view.panel;

import es.eucm.ead.editor.R;
import es.eucm.ead.editor.model.nodes.CharacterNode;
import es.eucm.ead.editor.util.Log4jConfig;
import es.eucm.ead.editor.util.Log4jConfig.LogListener;
import es.eucm.ead.editor.view.components.CommandLinePanel;
import es.eucm.ead.editor.view.components.OutputLogPanel;
import es.eucm.ead.editor.view.menu.AbstractEditorMenu.AbstractEditorAction;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.script.ScriptContext;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An elementPanel that can display anything, in a non-editable fashion.
 *
 * @author mfreire
 */
public class LogPanel extends AbstractElementPanel<CharacterNode> implements
		LogListener {

	static private Logger logger = LoggerFactory.getLogger(LogPanel.class);

	private OutputLogPanel log;
	private CommandLinePanel clp;
	private JPanel executePanel;
	private final JButton helpButton;
	private final JButton executeButton;
	private Action executeAction;
	private Action helpAction;
	private JSplitPane verticalSplit;
	private JCheckBox expandedCommandToggle;
	
	public OutputLogPanel getLog() {
		return log;
	}
	
	public LogPanel() {
		log = new OutputLogPanel();
		log.setText(Log4jConfig.getBuffer());
		log.setReferenceComponent(this, 30);

		clp = new CommandLinePanel();
		clp.setMinimumSize(new Dimension(100, 15));

		executeAction = new AbstractEditorAction("_runCmd", KeyEvent.VK_R,
				KeyEvent.CTRL_DOWN_MASK, R.Drawable.assets__runcmd_png) {
			@Override
			public void actionPerformed(ActionEvent e) {
				clp.pushToHistory();
				ScriptContext sc = controller.getScriptController().getContext();
				sc.getBindings(ScriptContext.ENGINE_SCOPE).put("panel", LogPanel.this);
				controller.getScriptController().eval(clp.getText(), log, sc, "executeAction");
			}
		};
		helpAction = new AbstractEditorAction("_helpCmd", KeyEvent.VK_H,
				KeyEvent.CTRL_DOWN_MASK, R.Drawable.assets__helpcmd_png) {
			@Override
			public void actionPerformed(ActionEvent e) {
				ScriptContext sc = controller.getScriptController().getContext();
				sc.getBindings(ScriptContext.ENGINE_SCOPE).put("cmd.panel", LogPanel.this);
				controller.getScriptController().eval("help", log, sc, "helpRequested");
			}
		};
		
		executeButton = new JButton(executeAction);
		executeButton.setText("");
		helpButton = new JButton(helpAction);
		helpButton.setText("");
		expandedCommandToggle = new JCheckBox("multiline");
		expandedCommandToggle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (expandedCommandToggle.isSelected()) {
					setNonInteractive();
				} else {
					setInteractive();
				}
			}
		});
	}
	
	private void setInteractive() {		
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1f, 1f,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						4, 4, 4, 4), 0, 0);
		executePanel.removeAll();
		executePanel.add(clp, gbc);
		gbc.gridx++;
		gbc.weightx = 0;
		gbc.weighty = 0;
		executePanel.add(executeButton, gbc);
		gbc.gridx++;
		executePanel.add(helpButton, gbc);
		gbc.gridx++;
		executePanel.add(expandedCommandToggle, gbc);
		executePanel.revalidate();

		verticalSplit.setResizeWeight(1.0);
		verticalSplit.setDividerLocation(getHeight() - 48);
		clp.setReferenceComponent(this, 280);
		clp.setInteractive(new Runnable() {
			@Override
			public void run() {
				ScriptContext sc = controller.getScriptController().getContext();
				sc.getBindings(ScriptContext.ENGINE_SCOPE).put("panel", LogPanel.this);
				controller.getScriptController().eval(clp.getText(), log, sc, "keyboardEnter");
			}
		});		
	}
	
	private void setNonInteractive() {		
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 3, 1f, 1f,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(
						4, 4, 4, 4), 0, 0);
		executePanel.removeAll();
		executePanel.add(clp, gbc);
		gbc.gridheight = 1;
		gbc.gridx++;
		gbc.weightx = 0;
		gbc.weighty = 0;
		executePanel.add(executeButton, gbc);
		gbc.gridy++;
		executePanel.add(helpButton, gbc);
		gbc.gridy++;
		gbc.weighty = 1;
		executePanel.add(expandedCommandToggle, gbc);
		executePanel.revalidate();

		verticalSplit.setResizeWeight(.5);
		verticalSplit.setDividerLocation(.5);	
		clp.setReferenceComponent(this, 80);
		clp.setInteractive(null);
	}
	
	@Override
	protected void rebuild() {
		removeAll();
		setLayout(new BorderLayout());

		executePanel = new JPanel(new GridBagLayout());
		
		verticalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		verticalSplit.setTopComponent(log);
		verticalSplit.setOneTouchExpandable(true);
		verticalSplit.setBottomComponent(executePanel);

		add(verticalSplit, BorderLayout.CENTER);

		Log4jConfig.subscribe(this);
		revalidate();
		setInteractive();
	}

	@Override
	public void logChanged(String change) {
		log.append(change);
	}
}