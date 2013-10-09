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

package es.eucm.ead.editor.view.toolbar;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import com.google.inject.Inject;
import es.eucm.ead.editor.R;

import es.eucm.ead.editor.control.Controller;
import es.eucm.ead.editor.control.change.ChangeListener;
import es.eucm.ead.editor.model.DefaultModelEvent;
import es.eucm.ead.editor.model.EditorModel;
import es.eucm.ead.editor.model.EditorModelImpl;
import es.eucm.ead.editor.model.nodes.DependencyNode;
import es.eucm.ead.editor.model.nodes.QueryNode;
import es.eucm.ead.editor.view.menu.AbstractEditorMenu;
import es.eucm.ead.editor.view.menu.Messages;
import es.eucm.ead.editor.util.SwingUtilities;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of the tool panel
 */
public class ToolPanel implements ChangeListener<String> {

	static private Logger logger = LoggerFactory.getLogger(ToolPanel.class);

	/**
	 * The pane where the tools are drawn
	 */
	private JPanel toolPanel;

	/**
	 * Search button
	 */
	private JButton searchButton;

	/**
	 * Search text field
	 */
	private JTextField searchField;

	/**
	 * Current controller
	 */
	private Controller controller;

	/**
	 *
	 * @param actionManager
	 * @param navigationController
	 */
	@Inject
	public ToolPanel(final Controller controller) {
		this.controller = controller;

		toolPanel = new JPanel(new FlowLayout());
		SwingUtilities.doInEDTNow(new Runnable() {
			@Override
			public void run() {
				addButton(controller.getAction(AbstractEditorMenu
						.removeMenuKeyIndicator(Messages.edit_menu_undo)));
				addButton(controller.getAction(AbstractEditorMenu
						.removeMenuKeyIndicator(Messages.edit_menu_redo)));
				addSeparator();
				addButton(controller.getAction(AbstractEditorMenu
						.removeMenuKeyIndicator(Messages.window_menu_prev)));
				addButton(controller.getAction(AbstractEditorMenu
						.removeMenuKeyIndicator(Messages.window_menu_next)));
				addSeparator();
				addSearchFieldAndButton();
			}
		});
	}

	public JPanel getPanel() {
		controller.getProjectController().addChangeListener(this);
		return toolPanel;
	}

	@Override
	public final void processChange(String s) {
		SwingUtilities.doInEDT(new Runnable() {
			@Override
			public void run() {
				searchField
						.setEnabled(controller.getModel().getEngineModel() != null);
				searchButton
						.setEnabled(controller.getModel().getEngineModel() != null);
			}
		});
	}

	/**
	 *
	 * @param a
	 */
	private void addButton(final Action a) {
		final JButton jb = new JButton((Icon) a.getValue(Action.SMALL_ICON));
		jb.setToolTipText((String) a.getValue(Action.NAME));
		a.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				jb.setEnabled(a.isEnabled());
			}
		});
		jb.addActionListener(a);
		jb.setEnabled(a.isEnabled());
		toolPanel.add(jb);
	}

	/**
	 * Add a separator in the tool panel
	 */
	private void addSeparator() {
		JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
		separator.setPreferredSize(new Dimension(2, 24));
		toolPanel.add(separator);
	}

	/**
	 * Add the search field and button
	 */
	private void addSearchFieldAndButton() {
		searchButton = new JButton(new ImageIcon(ClassLoader
				.getSystemClassLoader().getResource(
						R.Drawable.toolbar__search_png)));
		searchButton.setEnabled(false);
		searchField = new JTextField(20);
		searchField.setEnabled(false);
		searchField.setToolTipText(Messages.toolbar_search);
		toolPanel.add(searchField);
		toolPanel.add(searchButton);

		ActionListener queryListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				String query = searchField.getText();
				EditorModel m = controller.getModel();
				QueryNode qn = new QueryNode(m.generateId(null));
				qn.setModel((EditorModelImpl) m);
				qn.setQueryString(query);
				((EditorModelImpl) m).registerNode(qn, "AddQuery");
				logger.info("Added query node with ID {}", qn.getId());
				controller.getViewController().addView("query",
						"" + qn.getId(), false);
			}
		};

		searchField.addActionListener(queryListener);
		searchButton.addActionListener(queryListener);
	}
}
