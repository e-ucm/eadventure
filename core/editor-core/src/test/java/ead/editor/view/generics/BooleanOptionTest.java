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

import java.awt.BorderLayout;

import ead.editor.view.generic.BooleanOption;
import ead.editor.view.generic.FieldDescriptorImpl;
import ead.editor.view.generic.Panel;
import ead.editor.view.generic.PanelImpl;
import ead.utils.Log4jConfig;

public class BooleanOptionTest extends AbstractOptionTest {

	public BooleanOptionTest() {
		model = new ExampleClass();
		init();

		Panel p1 = new PanelImpl("Test", Panel.LayoutPolicy.VerticalBlocks, 4)
				.addElement(
						new BooleanOption("name1", "toolTip1",
								new FieldDescriptorImpl<Boolean>(model, "a")))
				.addElement(
						new BooleanOption("name2", "toolTip2",
								new FieldDescriptorImpl<Boolean>(model, "b")))
				.addElement(
						new BooleanOption("name3", "toolTip3",
								new FieldDescriptorImpl<Boolean>(model, "c")));
		Panel p2 = new PanelImpl("Test", Panel.LayoutPolicy.Flow, 4)
				.addElement(
						new BooleanOption("name1", "toolTip1",
								new FieldDescriptorImpl<Boolean>(model, "a")))
				.addElement(
						new BooleanOption("name2", "toolTip2",
								new FieldDescriptorImpl<Boolean>(model, "b")))
				.addElement(
						new BooleanOption("name3", "toolTip3",
								new FieldDescriptorImpl<Boolean>(model, "c")));
		Panel p3 = new PanelImpl("Test0",
				Panel.LayoutPolicy.VerticalEquallySpaced, 4);
		p3.addElement(p1);
		p3.addElement(p2);
		add(p3.getComponent(commandManager), BorderLayout.CENTER);
	}

	public static class ExampleClass {
		public boolean a, b, c;

		public boolean isA() {
			return a;
		}

		public void setA(boolean a) {
			this.a = a;
		}

		public boolean isB() {
			return b;
		}

		public void setB(boolean b) {
			this.b = b;
		}

		public boolean isC() {
			return c;
		}

		public void setC(boolean c) {
			this.c = c;
		}

		@Override
		public String toString() {
			return "a: " + a + "\n" + "b: " + b + "\n" + "c: " + c;
		}
	}

	public static void main(String[] args) {
		Log4jConfig.configForConsole(Log4jConfig.Slf4jLevel.Debug,
				new Object[] {});
		new BooleanOptionTest().setVisible(true);
	}
}
