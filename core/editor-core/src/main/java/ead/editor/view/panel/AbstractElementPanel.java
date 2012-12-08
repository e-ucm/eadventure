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

package ead.editor.view.panel;

import ead.editor.control.Controller;
import ead.editor.model.EditorModel;
import ead.editor.model.EditorModel.ModelEvent;
import ead.editor.model.nodes.DependencyNode;
import ead.editor.view.dock.ElementPanel;
import ead.editor.view.generic.OptionPanel;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * An elementPanel that can display anything.
 *
 * @author mfreire
 */
public abstract class AbstractElementPanel<T extends DependencyNode> extends
		JPanel implements ElementPanel<T>, EditorModel.ModelListener {

	protected T target;
	protected Controller controller;
	protected ArrayList<OptionPanel> panels = new ArrayList<OptionPanel>();

	@Override
	public void setTarget(T target) {
		if (this.target == null) {
			this.target = target;
			rebuild();
		} else if (this.target.getId() != target.getId()) {
			cleanup();
			this.target = target;
			rebuild();
			controller.getModel().addModelListener(this);
		}
	}

	@Override
	public void cleanup() {
		controller.getModel().removeModelListener(this);
	}

	@Override
	public void setController(Controller controller) {
		this.controller = controller;
	}

	@Override
	public T getTarget() {
		return target;
	}

	@Override
	public void modelChanged(ModelEvent event) {
		if (requiresRebuild(event)) {
			rebuild();
		} else if (requiresRefresh(event)) {
			for (OptionPanel p : panels) {
				p.modelChanged(event);
			}
		} else if (requiresResurrection(event)) {
			removeAll();
			setLayout(new BorderLayout());
			add(new JLabel("Sorry, this view has been erased externally"),
					BorderLayout.CENTER);
			cleanup();
		}
	}

	/**
	 * Checks if a change requires a rebuild of this view
	 * @param event describing the change
	 * @return true if rebuild required
	 */
	protected boolean requiresRebuild(ModelEvent event) {
		return Arrays.binarySearch(event.getChanged(), target) >= 0;
	}

	/**
	 * Checks if a change requires a refresh of this view
	 * @param event describing the change
	 * @return true if refresh of fields required
	 */
	protected boolean requiresRefresh(ModelEvent event) {
		return false;
	}

	/**
	 * Checks if a change completely invalidates this view
	 * @param event describing the change
	 * @return true if resurrection required
	 */
	protected boolean requiresResurrection(ModelEvent event) {
		return Arrays.binarySearch(event.getRemoved(), target) >= 0;
	}

	/**
	 * Rebuild contents after a change to the target
	 */
	protected abstract void rebuild();
}