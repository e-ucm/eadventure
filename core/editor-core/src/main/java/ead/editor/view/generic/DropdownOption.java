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

package ead.editor.view.generic;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;

import ead.editor.control.Command;
import ead.editor.control.commands.ChangeFieldCommand;
import ead.editor.model.nodes.DependencyNode;

public class DropdownOption<T> extends DefaultAbstractOption<T> {

	protected JComboBox combo;
	protected ComboBoxModel model;
	protected Item<T>[] items;
	protected HashMap<T, Item<T>> itemLookup = new HashMap<T, Item<T>>();

	protected static class Item<T> {
		public final String name;
		public final String tooltip;
		public final T value;
		public final int index;

		public Item(String name, String tooltip, T value, int index) {
			this.name = name;
			this.tooltip = tooltip;
			this.value = value;
			this.index = index;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	@SuppressWarnings("unchecked")
	private void initializeItems(T[] values, String[] names, String[] tooltips) {
		items = (Item<T>[]) new Item[values.length];
		for (int i = 0; i < values.length; i++) {
			String n = (names != null) ? names[i] : values[i].toString();
			String t = (tooltips != null) ? tooltips[i] : n;
			items[i] = new Item<T>(n, t, values[i], i);
			itemLookup.put(values[i], items[i]);
		}
	}

	/**
	 * A number option for integers from min (included) to max (excluded)
	 * @param title
	 * @param toolTipText
	 * @param object
	 * @param fieldName
	 * @param node
	 * @param choices
	 */
	public DropdownOption(String title, String toolTipText, Object object,
			String fieldName, DependencyNode node, T[] choices) {
		super(title, toolTipText, object, fieldName, node);
		initializeItems(choices, null, null);
		this.model = new DefaultComboBoxModel(items);
	}

	/**
	 * A number option for integers from min (included) to max (excluded)
	 * @param title
	 * @param toolTipText
	 * @param object
	 * @param fieldName
	 * @param node
	 * @param choices
	 */
	public DropdownOption(String title, String toolTipText, Object object,
			String fieldName, DependencyNode node, T[] choices, String[] names) {
		super(title, toolTipText, object, fieldName, node);
		initializeItems(choices, names, null);
		this.model = new DefaultComboBoxModel(items);
	}

	@Override
	public T getControlValue() {
		if (combo.getSelectedItem() == null) {
			return null;
		}
		return ((Item<T>) combo.getSelectedItem()).value;
	}

	@Override
	public void setControlValue(T newValue) {
		model.setSelectedItem(itemLookup.get(newValue));
	}

	@Override
	public JComponent createControl() {
		combo = new JComboBox(model);
		model.setSelectedItem(readModelValue());
		combo.setToolTipText(getToolTipText());
		combo.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				T newValue = getControlValue();
				T oldValue = readModelValue();
				if (changeConsideredRelevant(oldValue, newValue)) {
					update();
				}
			}
		});
		return combo;
	}

	@Override
	protected Command createUpdateCommand() {
		return new ChangeFieldCommand<T>(getControlValue(),
				getFieldDescriptor(), changed);
	}
}
