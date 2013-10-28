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

import es.eucm.ead.editor.model.nodes.DependencyNode;
import es.eucm.ead.editor.model.nodes.EngineNode;
import es.eucm.ead.editor.util.Log4jConfig;
import es.eucm.ead.editor.view.generic.OptionPanel;
import es.eucm.ead.editor.view.generic.PanelImpl;
import es.eucm.ead.editor.view.generic.TextOption;
import es.eucm.ead.editor.view.generic.table.ColumnSpec;
import es.eucm.ead.editor.view.generic.table.MapOption;
import es.eucm.ead.editor.view.generic.table.TextColumn;
import es.eucm.ead.model.elements.extra.EAdMap;

public class MapOptionTest extends AbstractOptionTest {

	public MapOptionTest() {
		model = new ExampleClass();
		init();

		final DependencyNode node1 = new EngineNode<String>(1, "test1");

		OptionPanel p1 = new PanelImpl("Test",
				OptionPanel.LayoutPolicy.VerticalBlocks, 4);
		p1.add(new TextOption("name1", "toolTip1", model, "name",
				TextOption.ExpectedLength.SHORT, node1));
		p1.add(new TextOption("name2", "toolTip2", model, "name",
				TextOption.ExpectedLength.SHORT, node1));
		p1.add(new MapOption("map1", "toolTip3", model, "map",
				StringPair.class, node1) {

			@Override
			public ColumnSpec<StringPair, String>[] getValueColumns() {
				return (ColumnSpec<StringPair, String>[]) new ColumnSpec[] {
						new TextColumn("A", "a", true, -1),
						new TextColumn("B", "b", true, -1) };
			}
		});
		p1.add(new MapOption("map2", "toolTip4", model, "map",
				StringPair.class, node1) {

			@Override
			public ColumnSpec<StringPair, String>[] getKeyColumns() {
				return (ColumnSpec<StringPair, String>[]) new ColumnSpec[] { new TextColumn<StringPair, String>(
						"Key", null, true, true, 50) };
			}

			@Override
			public ColumnSpec<StringPair, String>[] getValueColumns() {
				return (ColumnSpec<StringPair, String>[]) new ColumnSpec[] {
						new TextColumn("A", "a", false, -1),
						new TextColumn("B", "b", false, -1) };
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

	public static class StringPair {

		String a, b;

		public StringPair() {
		}

		public StringPair(String a, String b) {
			this.a = a;
			this.b = b;
		}

		public String getA() {
			return a;
		}

		public void setA(String a) {
			this.a = a;
		}

		public String getB() {
			return b;
		}

		public void setB(String b) {
			this.b = b;
		}

		public String toString() {
			return "a: " + a + " b: " + b;
		}
	}

	public static class ExampleClass {

		public String name = "initial name";
		public EAdMap<StringPair> map = new EAdMap<StringPair>();

		public ExampleClass() {
			map.put("hola", new StringPair("buenos", "dias"));
			map.put("buenos", new StringPair("dias", "tengas"));
			map.put("días", new StringPair("tenga", "usted"));
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public EAdMap<StringPair> getMap() {
			return map;
		}

		public void EAdMap(EAdMap<StringPair> map) {
			this.map = map;
		}

		@Override
		public String toString() {
			return "name: " + name + "\n" + "map: " + map;
		}
	}

	public static void main(String[] args) {
		Log4jConfig.configForConsole(Log4jConfig.Slf4jLevel.Debug,
				new Object[] {});
		AbstractOptionTest aot = new MapOptionTest();
		aot.setVisible(true);
	}
}
