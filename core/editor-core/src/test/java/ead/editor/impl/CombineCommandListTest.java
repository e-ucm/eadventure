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

package ead.editor.impl;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ead.editor.control.Command;
import ead.editor.control.change.ChangeEvent;
import ead.editor.control.commands.CombineCommandList;
import junit.framework.TestCase;

public class CombineCommandListTest extends TestCase {

	/**
	 * The mock commands to be added to the list
	 */
	@Mock
	private Command mockCommand1, mockCommand2, mockCommand3;

	@Mock
	private ChangeEvent changeEvent1, changeEvent2, changeEvent3;

	/**
	 * The command to combine the actions of a list of commands
	 */
	private CombineCommandList comm;

	/**
	 * Method that initiates both the mock objects and the regular objects of the class, works similar to a constructor.   
	 */
	@Before
	public void setUp() {

		MockitoAnnotations.initMocks(this);
		comm = new CombineCommandList(mockCommand1, mockCommand2, mockCommand3);

	}

	/**
	 * Testing the correct functionality of the method for performing commands.  
	 */
	@Test
	public void testPerformCommands() {

		comm.performCommand();

		verify(mockCommand1).performCommand();
		verify(mockCommand2, never()).performCommand();
		verify(mockCommand3, never()).performCommand();

		when(mockCommand1.performCommand()).thenReturn(changeEvent1);
		when(mockCommand2.performCommand()).thenReturn(changeEvent2);
		when(mockCommand3.performCommand()).thenReturn(changeEvent3);

		comm.performCommand();

		verify(mockCommand1, times(2)).performCommand();
		verify(mockCommand2).performCommand();
		verify(mockCommand3).performCommand();

	}

	/**
	 * Testing the correct functionality of the method for undoing commands.  
	 */
	@Test
	public void testUndoCommands() {

		comm.undoCommand();

		verify(mockCommand1).undoCommand();
		verify(mockCommand2, never()).undoCommand();
		verify(mockCommand3, never()).undoCommand();

		when(mockCommand1.undoCommand()).thenReturn(changeEvent1);
		when(mockCommand2.undoCommand()).thenReturn(changeEvent1);
		when(mockCommand3.undoCommand()).thenReturn(changeEvent1);

		comm.undoCommand();

		verify(mockCommand1, times(2)).undoCommand();
		verify(mockCommand2).undoCommand();
		verify(mockCommand3).undoCommand();
	}

	/**
	 * Testing the correct functionality of the method for redoing commands.  
	 */
	@Test
	public void testRedoCommands() {

		comm.redoCommand();

		verify(mockCommand1).redoCommand();
		verify(mockCommand2, never()).redoCommand();
		verify(mockCommand3, never()).redoCommand();

		when(mockCommand1.redoCommand()).thenReturn(changeEvent1);
		when(mockCommand2.redoCommand()).thenReturn(changeEvent1);
		when(mockCommand3.redoCommand()).thenReturn(changeEvent1);

		comm.redoCommand();

		verify(mockCommand1, times(2)).redoCommand();
		verify(mockCommand2).redoCommand();
		verify(mockCommand3).redoCommand();
	}

}
