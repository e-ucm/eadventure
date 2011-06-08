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

package es.eucm.eadventure.editor.control;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import es.eucm.eadventure.editor.control.Command;
import es.eucm.eadventure.editor.control.CommandManager;
import es.eucm.eadventure.editor.control.impl.CommandManagerImpl;
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
	
	/**
	 * Guice injector
	 */
	private Injector injector;
	
	/**
	 * The CommandManager
	 */
	private CommandManager commandManager;
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	public void setUp() {
		injector = Guice.createInjector(new TestModule());
		commandManager = injector.getInstance(CommandManager.class);

		MockitoAnnotations.initMocks(this);
		when(mockCommand.canUndo()).thenReturn(Boolean.TRUE);
		when(mockCommand.performCommand()).thenReturn(Boolean.TRUE);
		when(mockCommand.undoCommand()).thenReturn(Boolean.TRUE);
		when(mockCommand.canRedo()).thenReturn(Boolean.TRUE);
		
		when(cantUndoCommand.canUndo()).thenReturn(Boolean.FALSE);
		when(cantUndoCommand.performCommand()).thenReturn(Boolean.TRUE);
		when(cantUndoCommand.undoCommand()).thenReturn(Boolean.TRUE);
		when(cantUndoCommand.canRedo()).thenReturn(Boolean.TRUE);
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

		verify( mockCommand, times(1)).performCommand();
		verify( mockCommand, times(1)).undoCommand();
	}
	
	/**
	 * Perform an command that can't be undone, then check
	 * if there are changes in the acitonManager.
	 */
	@Test
	public void testPerformAndUndoFailCommand() {
		assertEquals(false, commandManager.isChanged());
		
		commandManager.performCommand(cantUndoCommand);
		assertEquals(true, commandManager.isChanged());

		commandManager.undoCommand();
		assertEquals(true, commandManager.isChanged());
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
		
		verify( mockCommand, times(2)).performCommand();
		verify( mockCommand, times(2)).undoCommand();

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
		
		verify( mockCommand, times(1)).performCommand();
		verify( mockCommand, times(1)).undoCommand();
		verify( mockCommand, times(1)).redoCommand();
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
		
		verify( mockCommand, times(4)).performCommand();
		verify( mockCommand, times(4)).undoCommand();
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

		verify( mockCommand, times(4)).performCommand();
		verify( mockCommand, times(4)).undoCommand();
	}

	@Test
	public void testCancelMultipleCommandsInStackFail() {
		assertEquals(false, commandManager.isChanged());

		commandManager.addStack();
		
		commandManager.performCommand(mockCommand);
		commandManager.performCommand(cantUndoCommand);
		
		assertEquals(false, commandManager.canUndo());
		
		commandManager.performCommand(mockCommand);
		commandManager.performCommand(mockCommand);

		commandManager.removeCommandStacks(true);
		
		assertEquals(true, commandManager.isChanged());
	}

}
