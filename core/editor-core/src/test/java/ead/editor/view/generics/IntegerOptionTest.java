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

package ead.editor.view.generics;

import javax.swing.SpinnerNumberModel;

import ead.editor.model.nodes.DependencyNode;
import ead.editor.model.nodes.EngineNode;
import ead.editor.view.generic.IntegerOption;
import ead.editor.view.generic.OptionPanel;
import ead.editor.view.generic.PanelImpl;
import es.eucm.ead.tools.java.utils.Log4jConfig;

public class IntegerOptionTest extends AbstractOptionTest {

	public IntegerOptionTest() {
		model = new ExampleClass();
		init();

		DependencyNode node1 = new EngineNode<String>(1, "test1");
		DependencyNode node2 = new EngineNode<String>(2, "test2");

		OptionPanel p1 = new PanelImpl("Test",
				OptionPanel.LayoutPolicy.VerticalBlocks, 4);
		p1.add(new IntegerOption("name1", "toolTip1", model, "a", node1,
				new SpinnerNumberModel(0, 0, 10, 1)));
		p1.add(new IntegerOption("name2", "toolTip2", model, "b", node1,
				new SpinnerNumberModel(0, 0, 10, 1)));
		p1.add(new IntegerOption("name3", "toolTip3", model, "c", node1,
				new SpinnerNumberModel(0, 0, 10, 1)));
		OptionPanel p2 = new PanelImpl("Test",
				OptionPanel.LayoutPolicy.VerticalBlocks, 4);
		p2.add(new IntegerOption("name1", "toolTip1", model, "a", node1,
				new SpinnerNumberModel(0, 0, 10, 1)));
		p2.add(new IntegerOption("name2", "toolTip2", model, "b", node1,
				new SpinnerNumberModel(0, 0, 10, 1)));
		p2.add(new IntegerOption("name3", "toolTip3", model, "c", node1,
				new SpinnerNumberModel(0, 0, 10, 1)));
		OptionPanel p3 = new PanelImpl("Test0",
				OptionPanel.LayoutPolicy.VerticalEquallySpaced, 4);
		p3.add(p1);
		p3.add(p2);
		controller.getModel().addModelListener(p3);
		childPanel.add(p3.getComponent(commandManager));
	}

	public static class ExampleClass {

		public int a, b, c;

		public int getA() {
			return a;
		}

		public void setA(int a) {
			this.a = a;
		}

		public int getB() {
			return b;
		}

		public void setB(int b) {
			this.b = b;
		}

		public int getC() {
			return c;
		}

		public void setC(int c) {
			this.c = c;
		}

		@Override
		public String toString() {
			return "a: " + a + "; " + "b: " + b + "; " + "c: " + c;
		}
	}

	public static void main(String[] args) {
		Log4jConfig.configForConsole(Log4jConfig.Slf4jLevel.Debug,
				new Object[] {});
		AbstractOptionTest aot = new IntegerOptionTest();
		aot.setVisible(true);
	}
}
