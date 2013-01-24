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

package ead.editor.view.panel;

import static org.mockito.Mockito.when;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.inject.Guice;
import com.google.inject.Injector;

import ead.editor.EditorGuiceModule;
import ead.editor.control.CommandManager;
import ead.editor.control.CommandManagerImpl;
import ead.editor.control.Controller;
import ead.editor.control.change.ChangeListener;
import ead.editor.model.EditorModel;
import ead.editor.model.nodes.DependencyNode;
import ead.engine.core.gdx.desktop.platform.GdxDesktopModule;
import ead.importer.BaseImporterModule;
import ead.tools.java.JavaToolsModule;
import ead.tools.reflection.ReflectionClassLoader;

public abstract class AbstractPanelTester extends JFrame {

	protected CommandManager commandManager;
	protected JButton undo, redo, dump;

	protected AbstractElementPanel one;
	protected AbstractElementPanel two;

	private JSplitPane splitPane;

	@Mock
	protected Controller controller;

	abstract AbstractElementPanel<? extends DependencyNode> createPanel();

	abstract DependencyNode getTarget();

	public void prepareControllerAndModel() {
		Injector injector = Guice.createInjector(new BaseImporterModule(),
				new GdxDesktopModule(), new EditorGuiceModule(),
				new JavaToolsModule());

		// init reflection
		ReflectionClassLoader.init(injector
				.getInstance(ReflectionClassLoader.class));

		EditorModel editorModel = injector.getInstance(EditorModel.class);
		MockitoAnnotations.initMocks(this);
		when(controller.getModel()).thenReturn(editorModel);
	}

	public void createFrame() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		add(splitPane, BorderLayout.CENTER);
		setLocationRelativeTo(null);
	}

	public void init() {

		createFrame();
		prepareControllerAndModel();
		commandManager = new CommandManagerImpl();
		commandManager.setController(controller);
		when(controller.getCommandManager()).thenReturn(commandManager);

		commandManager.addChangeListener(new ChangeListener<String>() {
			@Override
			public void processChange(String event) {
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

		one = createPanel();
		one.setController(controller);
		one.setTarget(getTarget());
		splitPane.setLeftComponent(one);

		two = createPanel();
		two.setController(controller);
		two.setTarget(getTarget());
		splitPane.setRightComponent(two);

		setSize(1000, 500);
		splitPane.setDividerLocation(500);
	}
}
