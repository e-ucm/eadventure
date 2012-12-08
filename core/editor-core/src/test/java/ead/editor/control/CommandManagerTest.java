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

package ead.editor.control;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import ead.editor.model.EditorModel;
import ead.editor.model.EditorModel.ModelEvent;
import ead.editor.model.DefaultModelChange;

import junit.framework.TestCase;

/**
 * Tests the CommandManager
 */
public class CommandManagerTest extends TestCase {

	/**
	 * Guice model for the test
	 */
	private class TestModule extends AbstractModule {

		@Override
		protected void configure() {
			bind(CommandManager.class).to(CommandManagerImpl.class);
		}

	}

	/**
	 * Mock command that can be undone and redone successfully
	 */
	@Mock
	Command mockCommand;

	/**
	 * Mock command that can't be undone
	 */
	@Mock
	Command cantUndoCommand;

	@Mock
	EditorModel editorModel;

	@Mock
	Controller controller;

	/**
	 * Guice injector
	 */
	private Injector injector;

	/**
	 * The CommandManager
	 */
	private CommandManager commandManager;

	ModelEvent completelyUnrelatedEvent;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		injector = Guice.createInjector(new TestModule());
		commandManager = injector.getInstance(CommandManager.class);
		commandManager.setController(controller);
		when(controller.getModel()).thenReturn(editorModel);

		completelyUnrelatedEvent = new DefaultModelChange("test", "test", null,
				null);

		when(mockCommand.performCommand(editorModel)).thenReturn(
				completelyUnrelatedEvent);
		when(mockCommand.canUndo()).thenReturn(Boolean.TRUE);
		when(mockCommand.undoCommand(editorModel)).thenReturn(
				completelyUnrelatedEvent);
		when(mockCommand.canRedo()).thenReturn(Boolean.TRUE);
		when(mockCommand.redoCommand(editorModel)).thenReturn(
				completelyUnrelatedEvent);

		when(cantUndoCommand.canUndo()).thenReturn(Boolean.FALSE);
		when(cantUndoCommand.performCommand(editorModel)).thenReturn(null);
	}

	/**
	 * Perform an command and undo it successfully, then check
	 * if there are no changes in the commandManager.
	 */
	@Test
	public void testPerformAndUndoCommand() {
		assertEquals(false, commandManager.isChanged());

		commandManager.performCommand(mockCommand);
		assertEquals(true, commandManager.isChanged());

		commandManager.undoCommand();
		assertEquals(false, commandManager.isChanged());

		verify(mockCommand, times(1)).performCommand(editorModel);
		verify(mockCommand, times(1)).undoCommand(editorModel);
	}

	/**
	 * Perform an command that can't be undone, then check
	 * if there are changes in the actionManager.
	 */
	@Test
	public void testPerformAndUndoFailCommand() {
		assertEquals(false, commandManager.isChanged());

		commandManager.performCommand(cantUndoCommand);
		assertEquals(false, commandManager.isChanged());

		// actually, it will short-circuit and not even attempt to undo the command...
		commandManager.undoCommand();
		assertEquals(false, commandManager.isChanged());
		verify(cantUndoCommand, never()).undoCommand(editorModel);
	}

	/**
	 * Perform multiple command and undo them sequentially,
	 * checking that there are changes until there are no more
	 * commands to undo.
	 */
	@Test
	public void testPerformMultipleCommand() {
		assertEquals(false, commandManager.isChanged());

		commandManager.performCommand(mockCommand);
		commandManager.performCommand(mockCommand);

		assertEquals(true, commandManager.isChanged());

		commandManager.undoCommand();
		assertEquals(true, commandManager.isChanged());

		commandManager.undoCommand();
		assertEquals(false, commandManager.isChanged());

		verify(mockCommand, times(2)).performCommand(editorModel);
		verify(mockCommand, times(2)).undoCommand(editorModel);

	}

	@Test
	public void testPerformUndoRedoUndoCommand() {
		assertEquals(false, commandManager.isChanged());

		commandManager.performCommand(mockCommand);
		assertEquals(true, commandManager.isChanged());

		commandManager.undoCommand();
		assertEquals(false, commandManager.isChanged());

		commandManager.redoCommand();
		assertEquals(true, commandManager.isChanged());

		verify(mockCommand, times(1)).performCommand(editorModel);
		verify(mockCommand, times(1)).undoCommand(editorModel);
		verify(mockCommand, times(1)).redoCommand(editorModel);
	}

	@Test
	public void testPerformMultipleCommandsInStack() {
		assertEquals(false, commandManager.isChanged());

		commandManager.addStack();

		commandManager.performCommand(mockCommand);
		commandManager.performCommand(mockCommand);
		commandManager.performCommand(mockCommand);
		commandManager.performCommand(mockCommand);

		commandManager.removeCommandStacks(false);

		assertEquals(true, commandManager.isChanged());

		commandManager.undoCommand();
		assertEquals(false, commandManager.isChanged());

		verify(mockCommand, times(4)).performCommand(editorModel);
		verify(mockCommand, times(4)).undoCommand(editorModel);
	}

	@Test
	public void testCancelMultipleCommandsInStack() {
		assertEquals(false, commandManager.isChanged());

		commandManager.addStack();

		commandManager.performCommand(mockCommand);
		commandManager.performCommand(mockCommand);
		commandManager.performCommand(mockCommand);
		commandManager.performCommand(mockCommand);

		commandManager.removeCommandStacks(true);

		assertEquals(false, commandManager.isChanged());

		verify(mockCommand, times(4)).performCommand(editorModel);
		verify(mockCommand, times(4)).undoCommand(editorModel);
	}

	@Test
	public void testCancelMultipleCommandsInStackFail() {
		assertEquals(false, commandManager.isChanged());

		commandManager.addStack();

		commandManager.performCommand(mockCommand);
		commandManager.performCommand(cantUndoCommand);

		// performed a bad command - ignored & nothing changed (can still undo first)		
		assertEquals(true, commandManager.canUndo());

		commandManager.performCommand(mockCommand);
		commandManager.performCommand(mockCommand);

		assertEquals(true, commandManager.isChanged());

		commandManager.removeCommandStacks(true);

		// now, all 3 instances should have been undone; so we are back to the start

		assertEquals(false, commandManager.isChanged());
	}
}
