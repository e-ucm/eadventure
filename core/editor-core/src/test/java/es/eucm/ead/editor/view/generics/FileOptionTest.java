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

import java.io.File;

import es.eucm.ead.editor.control.Command;
import es.eucm.ead.editor.control.commands.ChangeFieldCommand;
import es.eucm.ead.editor.control.commands.ChangeFileCommand;
import es.eucm.ead.editor.control.commands.FileCache;
import es.eucm.ead.editor.model.ModelEvent;
import es.eucm.ead.editor.model.nodes.DependencyNode;
import es.eucm.ead.editor.model.nodes.EngineNode;
import es.eucm.ead.editor.view.generic.FileNameOption;
import es.eucm.ead.editor.view.generic.FileOption;
import es.eucm.ead.editor.view.generic.OptionPanel;
import es.eucm.ead.editor.view.generic.PanelImpl;
import es.eucm.ead.editor.util.Log4jConfig;

public class FileOptionTest extends AbstractOptionTest {

	private File target;

	public FileOptionTest() {
		model = new ExampleClass();
		init();

		final FileCache fc = new FileCache(new File("/tmp/cache"));
		DependencyNode node1 = new EngineNode<String>(1, "test1");

		FileOption fo = new FileOption("dest1", "tipsy tool 1", "Choose...",
				model, "dest", fc, node1) {
			@Override
			public Command createUpdateCommand() {
				return new ChangeFileCommand(getControlValue(), accessor, fc,
						changed) {
					@Override
					protected ModelEvent setValue(File value) {
						ModelEvent me = super.setValue(value);
						writeFile(target);
						return me;
					}
				};
			}
		};

		FileNameOption fno = new FileNameOption("name1", "toolTip1", model,
				"name", false, node1) {
			@Override
			public File resolveFile(String value) {
				return ExampleClass.resolveFile(value);
			}

			@Override
			public Command createUpdateCommand() {
				return new ChangeFieldCommand<String>(getControlValue(),
						accessor, changed) {
					@Override
					protected ModelEvent setValue(String value) {
						File src = resolveFile(accessor.read());
						ModelEvent me = super.setValue(value);
						target = resolveFile(value);
						if (!src.renameTo(target)) {
							System.err.println("---- Could not rename " + src
									+ " to " + target);
						}
						return me;
					}
				};
			}
		};
		target = fno.resolveFile(((ExampleClass) model).getName());

		OptionPanel p1 = new PanelImpl("Test",
				OptionPanel.LayoutPolicy.VerticalBlocks, 4);
		p1.add(fno).add(fo);

		controller.getModel().addModelListener(p1);
		childPanel.add(p1.getComponent(commandManager));
	}

	public static class ExampleClass {

		public String name = "aFile";
		public File dest = new File("/tmp/aFlea");

		public static File resolveFile(String name) {
			return new File("/tmp", name);
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public File getDest() {
			return dest;
		}

		public void setDest(File dest) {
			this.dest = dest;
		}

		@Override
		public String toString() {
			return "name: " + name + "\n" + "dest: " + dest;
		}
	}

	public static void main(String[] args) {
		Log4jConfig.configForConsole(Log4jConfig.Slf4jLevel.Debug,
				new Object[] {});
		AbstractOptionTest aot = new FileOptionTest();
		aot.setVisible(true);
	}
}
