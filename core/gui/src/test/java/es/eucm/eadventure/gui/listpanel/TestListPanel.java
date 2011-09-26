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

package es.eucm.eadventure.gui.listpanel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

import es.eucm.eadventure.gui.EAdPanel;
import es.eucm.eadventure.gui.extra.EAdBorderLayout;
import es.eucm.eadventure.gui.listpanel.columntypes.ConditionsCellRendererEditor;
import es.eucm.eadventure.gui.listpanel.columntypes.ButtonCellRendererEditor;
import es.eucm.eadventure.gui.listpanel.columntypes.StringCellRendererEditor;

public class TestListPanel {

	public static void main(String args[]) {
		

		JFrame f = new JFrame("Test List Panel");

		// created a list elements
		ListPanelListener listPanelListener = new TestElementList();

		
		// -----------------LIST PANEL----------------------
		ListPanel listPanel = new ListPanel(listPanelListener);
		// ----------------EXAMPLE COLUMNS------------------
		// Create a column only with a name
		listPanel.addColumn(new ColumnDescriptor("Primera", "", null));
		// Create another column only with a string and the help (not
		// implemented), furthermore is editable.
		listPanel.addColumn(new ColumnDescriptor("Segunda", "prueba.html",
				new StringCellRendererEditor()));
		// Create a column of conditions
		listPanel.addColumn(new ColumnDescriptor("Tercera", "conditions.html",
				new ConditionsCellRendererEditor()));
		// Create a column of documentation
		listPanel.addColumn(new ColumnDescriptor("Cuarta", "documentation.html",
				new ButtonCellRendererEditor("button", new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						System.out.println("click");
					}
					
				})));

		//this initialize the panel, it's necessary
		listPanel.createElements();
		// ---------------END LIST PANEL-------------------

		// Created a JSplitPanel with the ListPanel and a EAdPanel
		JSplitPane tableWithSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				listPanel, new EAdPanel());
		tableWithSplit.setOneTouchExpandable(true);
		tableWithSplit.setDividerLocation(140);
		tableWithSplit.setContinuousLayout(true);
		tableWithSplit.setResizeWeight(0.5);
		tableWithSplit.setDividerSize(10);

		f.add(tableWithSplit, EAdBorderLayout.CENTER);
		f.setSize(new Dimension(800, 600));
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}

}
