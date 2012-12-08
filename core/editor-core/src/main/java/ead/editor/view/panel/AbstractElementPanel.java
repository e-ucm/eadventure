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
import javax.swing.JLabel;
import javax.swing.JPanel;

import ead.editor.model.ModelEventUtils;

/**
 * An elementPanel that can display anything. Intended as the common base
 * of all elementPanels. Panels should
 * <ul>
 * <li>register their OptionPanels in 'panels' during rebuild.
 * </ul>
 *
 * @author mfreire
 */
public abstract class AbstractElementPanel<T extends DependencyNode> extends
		JPanel implements ElementPanel<T>, EditorModel.ModelListener {

	protected T target;
	protected Controller controller;
	protected ArrayList<OptionPanel> panels = new ArrayList<OptionPanel>();

	/**
	 * Sets the target. Internally calls rebuild() (or cleanup & rebuild) 
	 * as necessary.
	 */
	@Override
	public void setTarget(T target) {
		if (this.target != null) {
			if (this.target.getId() == target.getId()) {
				// same target - nothing to do;
				return;
			} else {
				cleanup();
			}
		}
		this.target = target;
		rebuild();
		controller.getModel().addModelListener(this);
	}

	/**
	 * Frees any resources and listeners used by this panel. Subclasses
	 * that listen on anything but modelEvents should override this.
	 */
	@Override
	public void cleanup() {
		controller.getModel().removeModelListener(this);
	}

	/**
	 * Sets the controller. Guaranteed to be called before setTarget.
	 */
	@Override
	public void setController(Controller controller) {
		this.controller = controller;
	}

	@Override
	public T getTarget() {
		return target;
	}

	/**
	 * Reacts to a model change. If requiresRebuild(), then a full rebuild()
	 * is triggered. Otherwise, if requiresRefresh(), the event will be 
	 * re-dispatched to all options. Finally, if requiresResurrection() 
	 * returns true, then target has been erased from the model; it is up to
	 * us to restore it.
	 * @param event 
	 */
	@Override
	public void modelChanged(ModelEvent event) {
		if (requiresRebuild(event)) {
			cleanup();
			rebuild();
			controller.getModel().addModelListener(this);
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
	 * Checks if a change requires a full rebuild of this view. Called from
	 * modelChanged.
	 * @param event describing the change
	 * @return true if rebuild required
	 */
	protected boolean requiresRebuild(ModelEvent event) {
		return false;
	}

	/**
	 * Checks if a change requires a refresh of this view. Called from
	 * modelChanged.
	 * @param event describing the change
	 * @return true if refresh of fields required
	 */
	protected boolean requiresRefresh(ModelEvent event) {
		return ModelEventUtils.changes(event, target);
	}

	/**
	 * Checks if a change completely invalidates this view. Called from
	 * modelChanged.
	 * @param event describing the change
	 * @return true if resurrection required
	 */
	protected boolean requiresResurrection(ModelEvent event) {
		return ModelEventUtils.contains(event.getRemoved(), target);
	}

	/**
	 * Rebuild contents after a (deep) change to the target. Smaller changes
	 * should be managed automatically via ModelEvent dispatch from modelChanged().
	 */
	protected abstract void rebuild();
}