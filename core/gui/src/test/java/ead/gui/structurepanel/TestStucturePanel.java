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

package ead.gui.structurepanel;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import ead.gui.EAdHideingSplitPane;
import ead.gui.extra.EAdBorderLayout;
import ead.gui.structurepanel.StructureElement;
import ead.gui.structurepanel.StructureElementChangeListener;
import ead.gui.structurepanel.StructureElementProvider;
import ead.gui.structurepanel.StructurePanel;
import ead.gui.structurepanel.StructureSubElement;

public class TestStucturePanel {

	public static void main(String args[]) {
		JFrame f = new JFrame("Test");

		int selectElement = 0;
		int width = 200;
		
		StructurePanel structurePanel = new StructurePanel(width,selectElement);
		

		JPanel right = new JPanel();
		
		// agrego elementos de prueba
		StructureElement chapterElement = new StructureElement(new StructureElementProvider() {

			@Override
			public String getLabel() {
				return "Capitulo";
			}

			@Override
			public Icon getIcon() {
				return null;
			}

			@Override
			public boolean canHaveChildren() {
				return false;
			}

			@Override
			public int getChildCount() {
				return 0;
			}
			
		});
		//StructureElement scenesElement = new StructureElement("Escenas","src/test/resources/structurepanel/scenes.png",false);
		StructureElement cutScenesElement = new StructureElement(new StructureElementProvider() {

			@Override
			public String getLabel() {
				return "cutscene elements";
			}

			@Override
			public Icon getIcon() {
				return null;
			}

			@Override
			public boolean canHaveChildren() {
				return false;
			}

			@Override
			public int getChildCount() {
				return 0;
			}
			
		}

		);
		cutScenesElement.addChangeListener(new StructureElementChangeListener() {

			@Override
			public void selectionChanged() {
				System.out.println("escenas intermedias");
			}

			@Override
			public void selectedChildChange(StructureSubElement child) {
				System.out.println("Child change: " + child.getName());
			}

			@Override
			public void addChild() {
				System.out.println("Add child");
			}

			@Override
			public void removeChild(int indexOf) {
				System.out.println("Remove child " + indexOf);
			}

			@Override
			public void duplicateChild(int indexOf) {
				System.out.println("Duplicate child "+ indexOf);
			}
			
		});
				
		//structurePanel.addElement(scenesElement,0);
		structurePanel.addElement(cutScenesElement,1);
		
		structurePanel.addElement(chapterElement,0);
		
		
		structurePanel.createElements();
		

//		EAdSplitPane pane = new EAdSplitPane(EAdSplitPane.HORIZONTAL_SPLIT, structurePanel, right);
//		pane.setOneTouchExpandable(true);

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(structurePanel, BorderLayout.CENTER);

		EAdHideingSplitPane pane = new EAdHideingSplitPane(panel, right);


		f.add(pane, EAdBorderLayout.CENTER);
		f.setSize(new Dimension(800, 600));
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}

}
