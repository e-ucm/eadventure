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
import ead.editor.view.generic.TextOption;
import ead.editor.view.generic.FieldDescriptorImpl;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.WindowConstants;

import ead.editor.control.CommandManager;
import javax.swing.JFrame;
import javax.swing.JPanel;

import ead.editor.control.CommandManagerImpl;
import ead.editor.control.change.ChangeListener;
import ead.utils.Log4jConfig;

public class TextComponentProviderTest extends JFrame {

	private static final long serialVersionUID = 1L;

	private CommandManager commandManager;
	private JButton undo, redo;

	public static void main(String[] args) {
		Log4jConfig.configForConsole(Log4jConfig.Slf4jLevel.Debug,
				new Object[] {});

		(new TextComponentProviderTest()).test();
	}

	public void init() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setSize(400, 130);
		setLocationRelativeTo(null);

		commandManager = new CommandManagerImpl();
		commandManager.addChangeListener(new ChangeListener() {
			@Override
			public void processChange(Object event) {
				undo.setEnabled(commandManager.canUndo());
				redo.setEnabled(commandManager.canRedo());
			}
		});

		undo = new JButton(new AbstractAction("undo") {
			@Override
			public void actionPerformed(ActionEvent ae) {
				if (commandManager.canUndo()) {
					commandManager.undoCommand();
				}
			}
		});
		redo = new JButton(new AbstractAction("redo") {
			@Override
			public void actionPerformed(ActionEvent ae) {
				if (commandManager.canRedo()) {
					commandManager.redoCommand();
				}
			}
		});
		undo.setEnabled(false);
		redo.setEnabled(false);

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(undo);
		buttonPanel.add(redo);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	public static class ExampleClass {
		public String name = "initialName";

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	public void test() {
		init();

		JPanel jp = new JPanel();
		jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
		Object model = new ExampleClass();
		TextOption option1 = new TextOption("name1", "toolTip1",
				new FieldDescriptorImpl<String>(model, "name"));
		TextOption option2 = new TextOption("name2", "toolTip2",
				new FieldDescriptorImpl<String>(model, "name"));
		TextOption option3 = new TextOption("name2", "toolTip2",
				new FieldDescriptorImpl<String>(model, "name"));
		jp.add(option1.getComponent(commandManager), BorderLayout.NORTH);
		jp.add(option2.getComponent(commandManager), BorderLayout.NORTH);
		jp.add(option3.getComponent(commandManager), BorderLayout.NORTH);
		add(jp, BorderLayout.CENTER);

		setVisible(true);
	}
}
