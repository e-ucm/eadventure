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

package es.eucm.ead.editor.view.generics;

import java.awt.BorderLayout;

import javax.swing.JComponent;

import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.editor.model.nodes.DependencyNode;
import es.eucm.ead.editor.model.nodes.EngineNode;
import es.eucm.ead.editor.view.generic.table.ListOption;
import es.eucm.ead.editor.view.generic.OptionPanel;
import es.eucm.ead.editor.view.generic.PanelImpl;
import es.eucm.ead.editor.view.generic.TextOption;
import es.eucm.ead.editor.util.Log4jConfig;

public class ListOptionTest extends AbstractOptionTest {

	public ListOptionTest() {
		model = new ExampleClass();
		init();

		DependencyNode node1 = new EngineNode<String>(1, "test1");

		OptionPanel p1 = new PanelImpl("Test",
				OptionPanel.LayoutPolicy.VerticalBlocks, 4);
		p1.add(new TextOption("name1", "toolTip1", model, "name",
				TextOption.ExpectedLength.SHORT, node1));
		p1.add(new TextOption("name2", "toolTip2", model, "name",
				TextOption.ExpectedLength.SHORT, node1));
		p1.add(new ListOption<String>("list1", "toolTip3", model, "list",
				String.class, node1));
		p1.add(new ListOption<String>("list2", "toolTip4", model, "list",
				String.class, node1) {
			int cosa = 1;

			@Override
			public String chooseElementToAdd() {
				return "Elemento " + (++cosa);
			}
			
			

		});

		controller.getModel().addModelListener(p1);
		JComponent internal = p1.getComponent(commandManager);
		System.err.println("Internal size demanded: "
				+ internal.getPreferredSize());
		childPanel.add(internal, BorderLayout.CENTER);
		System.err.println("Total size demanded: "
				+ childPanel.getPreferredSize());
	}

	public static class ExampleClass {

		public String name = "initial name";
		public EAdList<String> list = new EAdList<String>();

		public ExampleClass() {
			list.add("hola");
			list.add("buenos");
			list.add("días");
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public EAdList<String> getList() {
			return list;
		}

		public void setList(EAdList<String> list) {
			this.list = list;
		}

		@Override
		public String toString() {
			return "name: " + name + "\n" + "list: " + list;
		}
	}

	public static void main(String[] args) {
		Log4jConfig.configForConsole(Log4jConfig.Slf4jLevel.Debug,
				new Object[] {});
		AbstractOptionTest aot = new ListOptionTest();
		aot.setVisible(true);
	}
}
