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

package es.eucm.ead.editor.control.commands;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.inject.Guice;
import com.google.inject.Injector;

import es.eucm.ead.editor.EditorGuiceModule;
import es.eucm.ead.editor.control.Controller;
import es.eucm.ead.editor.model.EditorModel;
import es.eucm.ead.editor.model.nodes.DependencyNode;
import es.eucm.ead.editor.model.nodes.EngineNode;
import es.eucm.ead.engine.desktop.platform.GdxDesktopModule;
import ead.importer.BaseImporterModule;
import es.eucm.ead.tools.java.JavaToolsModule;
import es.eucm.ead.tools.reflection.ReflectionClassLoader;

/**
 * Class for testing the right functionality of ChangeValueActions that modify the game model.
 */
public class TestChangeValueCommand extends TestCase {

	/**
	 * Actions for testing the changes of String values.
	 */
	private ChangeFieldCommand<String> actionS, mockActionS;
	/**
	 * Actions for testing the changes of Integer values.
	 */
	private ChangeFieldCommand<Integer> actionI, actionIb, mockActionI;
	/**
	 * Actions for testing the changes of Boolean values.
	 */
	private ChangeFieldCommand<Boolean> actionB, mockActionB;
	/**
	 * Actions for testing the changes of String values.
	 */
	private ChangeFieldCommand<Object> actionO, actionOb, mockActionO;
	/**
	 * Custom object for testing the changes made by the actions with.
	 */
	private CommandTestObjects actionObj;
	/**
	 * Simple object for testing the actions that modify members of the Object class.
	 */
	private Object obj;
	/**
	 * Custom mock object for testing the interaction between methods in the actions.
	 */
	@Mock
	private CommandTestObjects mockActionObject;

	@Mock
	private Controller controller;

	private EditorModel editorModel;

	/**
	 * Must be called after mocks initialized
	 */
	public void prepareControllerAndModel() {
		Injector injector = Guice.createInjector(new BaseImporterModule(),
				new GdxDesktopModule(), new EditorGuiceModule(),
				new JavaToolsModule());

		// init reflection
		ReflectionClassLoader.init(injector
				.getInstance(ReflectionClassLoader.class));

		editorModel = injector.getInstance(EditorModel.class);

		when(controller.getModel()).thenReturn(editorModel);
	}

	/**
	 * Method that initiates both the mock objects and the regular objects of the class, works similar to a constructor.
	 */
	@Before
	@Override
	public void setUp() {

		MockitoAnnotations.initMocks(this);
		DependencyNode node1 = new EngineNode<String>(1, "test1");

		actionObj = new CommandTestObjects("oh", 1, false, new Object());
		obj = new Object();
		Object obj2 = new Object();

		actionS = new ChangeFieldCommand<String>("test", actionObj, "s", node1);

		actionI = new ChangeFieldCommand<Integer>(100, actionObj, "i", node1);
		actionIb = new ChangeFieldCommand<Integer>(200, actionObj, "i", node1);

		actionB = new ChangeFieldCommand<Boolean>(true, actionObj, "b", node1);

		actionO = new ChangeFieldCommand<Object>(obj, actionObj, "o", node1);
		actionOb = new ChangeFieldCommand<Object>(obj2, actionObj, "o", node1);

		mockActionS = new ChangeFieldCommand<String>("test", mockActionObject,
				"s", node1);
		mockActionI = new ChangeFieldCommand<Integer>(100, mockActionObject,
				"i", node1);
		mockActionO = new ChangeFieldCommand<Object>(obj, mockActionObject,
				"o", node1);
		mockActionB = new ChangeFieldCommand<Boolean>(true, mockActionObject,
				"b", node1);

		prepareControllerAndModel();
	}

	/**
	 * Method for testing the right functionality of the action's method for performing actions.
	 */
	@Test
	public void testPerformAction() {

		assertEquals(false, (actionObj.getS()).equals("test"));
		assertEquals(false, (actionObj.getI()).equals(100));
		assertEquals(false, (actionObj.getB()).equals(true));
		assertEquals(false, (actionObj.getO()).equals(obj));

		actionS.performCommand(editorModel);
		actionI.performCommand(editorModel);
		actionB.performCommand(editorModel);
		actionO.performCommand(editorModel);

		assertEquals(true, (actionObj.getS()).equals("test"));
		assertEquals(true, (actionObj.getI()).equals(100));
		assertEquals(true, (actionObj.getB()).equals(true));
		assertEquals(true, (actionObj.getO()).equals(obj));

		mockActionS.performCommand(editorModel);
		mockActionI.performCommand(editorModel);
		mockActionB.performCommand(editorModel);
		mockActionO.performCommand(editorModel);
		// FIXME
		//		verify(mockActionObject, times(1)).getS();
		//		verify(mockActionObject, times(1)).setS("test");
		//		verify(mockActionObject, times(1)).getI();
		//		verify(mockActionObject, times(1)).setI(100);
		//		verify(mockActionObject, times(1)).getB();
		//		verify(mockActionObject, times(1)).setB(true);
		//		verify(mockActionObject, times(1)).getO();
		//		verify(mockActionObject, times(1)).setO(obj);

	}

	/**
	 * Method for testing the right functionality of the action's method for redoing actions.
	 */
	@Test
	public void testRedoAction() {

		assertEquals(false, (actionObj.getS()).equals("test"));
		assertEquals(false, (actionObj.getI()).equals(100));
		assertEquals(false, (actionObj.getB()).equals(true));
		assertEquals(false, (actionObj.getO()).equals(obj));

		actionS.redoCommand(editorModel);
		actionI.redoCommand(editorModel);
		actionB.redoCommand(editorModel);
		actionO.redoCommand(editorModel);

		assertEquals(true, (actionObj.getS()).equals("test"));
		assertEquals(true, (actionObj.getI()).equals(100));
		assertEquals(true, (actionObj.getB()).equals(true));
		assertEquals(true, (actionObj.getO()).equals(obj));

		mockActionS.redoCommand(editorModel);
		mockActionI.redoCommand(editorModel);
		mockActionB.redoCommand(editorModel);
		mockActionO.redoCommand(editorModel);

		verify(mockActionObject, times(1)).setS("test");
		verify(mockActionObject, times(1)).setI(100);
		verify(mockActionObject, times(1)).setB(true);
		verify(mockActionObject, times(1)).setO(obj);

	}

	/**
	 * Method for testing the right functionality of the action's method for undoing actions.
	 */
	@Test
	public void testUndoAction() {

		assertEquals(false, (actionObj.getS()).equals("test"));
		assertEquals(false, (actionObj.getI()).equals(100));
		assertEquals(false, (actionObj.getB()).equals(true));
		assertEquals(false, (actionObj.getO()).equals(obj));

		actionS.performCommand(editorModel);
		actionI.performCommand(editorModel);
		actionB.performCommand(editorModel);
		actionO.performCommand(editorModel);

		assertEquals(true, (actionObj.getS()).equals("test"));
		assertEquals(true, (actionObj.getI()).equals(100));
		assertEquals(true, (actionObj.getB()).equals(true));
		assertEquals(true, (actionObj.getO()).equals(obj));

		actionS.undoCommand(editorModel);
		actionI.undoCommand(editorModel);
		actionB.undoCommand(editorModel);
		actionO.undoCommand(editorModel);

		assertEquals(false, (actionObj.getS()).equals("test"));
		assertEquals(false, (actionObj.getI()).equals(100));
		assertEquals(false, (actionObj.getB()).equals(true));
		assertEquals(false, (actionObj.getO()).equals(obj));

		mockActionS.undoCommand(editorModel);
		mockActionI.undoCommand(editorModel);
		mockActionB.undoCommand(editorModel);
		mockActionO.undoCommand(editorModel);

		verify(mockActionObject, times(1)).setS(mockActionS.getOldValue());
		verify(mockActionObject, times(1)).setI(mockActionI.getOldValue());
		verify(mockActionObject, times(1)).setB(mockActionB.getOldValue());
		verify(mockActionObject, times(1)).setO(mockActionO.getOldValue());

	}

	/**
	 * Method for testing the right functionality of the action's method for combining actions.
	 */
	@Test
	public void testCombineAction() {

		assertEquals(false, (actionObj.getS()).equals("test"));
		assertEquals(false, (actionObj.getI()).equals(100));
		assertEquals(false, (actionObj.getB()).equals(true));
		assertEquals(false, (actionObj.getO()).equals(obj));

		actionS.performCommand(editorModel);
		actionI.performCommand(editorModel);
		actionB.performCommand(editorModel);
		actionO.performCommand(editorModel);

		assertEquals(true, (actionObj.getS()).equals("test"));
		assertEquals(true, (actionObj.getI()).equals(100));
		assertEquals(true, (actionObj.getB()).equals(true));
		assertEquals(true, (actionObj.getO()).equals(obj));

		assertEquals(false, actionS.combine(actionI));
		assertEquals(false, actionB.combine(actionO));

		assertEquals(true, actionI.combine(actionIb));
		assertEquals(true, actionO.combine(actionOb));
		assertEquals(actionIb.getNewValue(), actionI.getNewValue());
		assertEquals(actionOb.getNewValue(), actionO.getNewValue());
	}
}
