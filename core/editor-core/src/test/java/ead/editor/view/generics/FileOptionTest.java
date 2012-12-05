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
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import ead.editor.control.change.ChangeEvent;
import ead.editor.control.change.ChangeListener;
import ead.editor.control.commands.ChangeFieldValueCommand;
import ead.editor.control.commands.ChangeFileValueCommand;
import ead.editor.control.commands.FileCache;
import ead.editor.view.generic.FieldDescriptorImpl;
import ead.editor.view.generic.FileNameOption;
import ead.editor.view.generic.FileOption;
import ead.editor.view.generic.Panel;
import ead.editor.view.generic.PanelImpl;
import ead.utils.FileUtils;
import ead.utils.Log4jConfig;

public class FileOptionTest extends AbstractOptionTest {

	public FileOptionTest() {
		model = new ExampleClass();
		init();

		FileCache fc = new FileCache(new File("/tmp/cache"));

		final FileOption fo = new FileOption("dest1", "tippsy tool 1",
				"Choose...", new FieldDescriptorImpl<File>(model, "dest"), fc);
		final FileNameOption fno = new FileNameOption("name1", "toolTip1",
				new FieldDescriptorImpl<String>(model, "name"), false) {
			@Override
			public File resolveFile(String value) {
				return ExampleClass.resolveFile(value);
			}
		};

		Panel p1 = new PanelImpl("Test", Panel.LayoutPolicy.VerticalBlocks, 4)
				.addElement(fno).addElement(fo);
		add(p1.getComponent(commandManager), BorderLayout.CENTER);

		commandManager.addChangeListener(new ChangeListener<ChangeEvent>() {
			@Override
			public void processChange(ChangeEvent event) {
				ExampleClass m = (ExampleClass) model;
				if (event.hasChanged(fo.getFieldDescriptor())) {
					// the source file has changed: overwrite target
					ChangeFileValueCommand c = (ChangeFileValueCommand) event;
					c.writeFile(ExampleClass.resolveFile(m.getName()));
				} else if (event.hasChanged(fno.getFieldDescriptor())) {
					// the target has changed - remove old, switch to new
					ChangeFieldValueCommand<String> c = (ChangeFieldValueCommand<String>) event;
					File prev = m.getName().equals(c.getNewValue()) ? ExampleClass
							.resolveFile(c.getOldValue())
							: ExampleClass.resolveFile(c.getNewValue());
					File f = ExampleClass.resolveFile(m.getName());
					prev.renameTo(f);
				}
			}
		});
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
		new FileOptionTest().setVisible(true);
	}
}
