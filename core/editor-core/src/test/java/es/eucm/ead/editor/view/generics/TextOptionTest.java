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

import es.eucm.ead.editor.model.nodes.DependencyNode;
import es.eucm.ead.editor.model.nodes.EngineNode;
import es.eucm.ead.editor.view.generic.TextOption;
import es.eucm.ead.editor.view.generic.OptionPanel;
import es.eucm.ead.editor.view.generic.PanelImpl;
import es.eucm.ead.editor.util.Log4jConfig;
import es.eucm.ead.editor.view.generic.AbstractConstraint;
import es.eucm.ead.editor.view.generic.accessors.IntrospectingAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextOptionTest extends AbstractOptionTest {

	static private Logger logger = LoggerFactory
			.getLogger(TextOptionTest.class);

	TextOption ox, oy, oz;

	public TextOptionTest() {
		model = new ExampleClass();

		init();

		DependencyNode node1 = new EngineNode<String>(1, "test1");
		DependencyNode node2 = new EngineNode<String>(2, "test2");
		DependencyNode node3 = new EngineNode<String>(3, "test3");

		OptionPanel p1 = new PanelImpl("Test",
				OptionPanel.LayoutPolicy.VerticalBlocks, 4);
		p1.add(new TextOption("name1", "toolTip1", model, "name",
				TextOption.ExpectedLength.SHORT, node1));
		p1.add(new TextOption("name2", "toolTip2", model, "name",
				TextOption.ExpectedLength.SHORT, node1));
		p1.add(new TextOption("name3", "toolTip3", model, "name",
				TextOption.ExpectedLength.SHORT, node1));
		p1.add(new TextOption("desc1", "toolTipDesc1", model, "description",
				TextOption.ExpectedLength.LONG, node1));
		p1.add(new TextOption("desc2", "toolTipDesc2", model, "description",
				TextOption.ExpectedLength.LONG, node1));

		ox = new TextOption("X", "toolTipX", new IntrospectingAccessor<String>(
				model, "x"), TextOption.ExpectedLength.SHORT, node2, node3);
		oy = new TextOption("Y1", "toolTipY", new IntrospectingAccessor<String>(
				model, "y"), TextOption.ExpectedLength.SHORT, node2, node3);
		oz = new TextOption("Y2", "toolTipY", new IntrospectingAccessor<String>(
				model, "y"), TextOption.ExpectedLength.SHORT, node2, node3);
		MagicConstraint mc = new MagicConstraint();
		mc.install();
		
		p1.add(ox);
		p1.add(oy);
		p1.add(oz);

		controller.getModel().addModelListener(p1);
		childPanel.add(p1.getComponent(commandManager));
	}

	private class MagicConstraint extends AbstractConstraint {
		public MagicConstraint() {
			super("algo magico", ox, oy, oz);
		}
		public boolean isValid() {
			logger.debug("Have '" + ox.getControlValue() 
					+ "' + '" + oy.getControlValue() + "'");
			
			return //oz.getControlValue().equals(oy.getControlValue()) && 
					(ox.getControlValue() + oy.getControlValue()).equals("abracadabra");
		}
	}

	public static class ExampleClass {

		public String name = "initial name";
		public String description = "initial description";
		public String x = "abra";
		public String y = "cadabra";

		public String getX() {
			return x;
		}

		public void setX(String x) {
			this.x = x;
		}

		public String getY() {
			return y;
		}

		public void setY(String y) {
			this.y = y;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		@Override
		public String toString() {
			return "name: " + name + "\n" + "description: " + description;
		}
	}

	public static void main(String[] args) {
		Log4jConfig.configForConsole(Log4jConfig.Slf4jLevel.Debug,
				new Object[] {});
		AbstractOptionTest aot = new TextOptionTest();
		aot.setVisible(true);
	}
}
