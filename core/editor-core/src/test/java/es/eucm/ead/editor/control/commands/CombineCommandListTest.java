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

import static org.mockito.Mockito.*;

import es.eucm.ead.editor.control.commands.CompositeCommand;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import es.eucm.ead.editor.control.Command;
import es.eucm.ead.editor.model.DefaultModelEvent;
import es.eucm.ead.editor.model.EditorModel;
import es.eucm.ead.editor.model.ModelEvent;
import es.eucm.ead.editor.util.Log4jConfig;
import junit.framework.TestCase;

public class CombineCommandListTest extends TestCase {

	/**
	 * The mock commands to be added to the list
	 */
	@Mock
	private Command mockCommand1, mockCommand2, mockCommand3;

	private ModelEvent modelEvent1, modelEvent2, modelEvent3;

	@Mock
	private EditorModel editorModel;

	/**
	 * The command to combine the actions of a list of commands
	 */
	private CompositeCommand comm;

	/**
	 * Method that initiates both the mock objects and the regular objects of
	 * the class, works similar to a constructor.
	 */
	@Before
	@Override
	public void setUp() {
		Log4jConfig.configForConsole(Log4jConfig.Slf4jLevel.Debug,
				new Object[] {});

		MockitoAnnotations.initMocks(this);
		comm = new CompositeCommand(mockCommand1, mockCommand2, mockCommand3);
		modelEvent1 = new DefaultModelEvent("test1", "test1", null, null);
		modelEvent2 = new DefaultModelEvent("test2", "test2", null, null);
		modelEvent3 = new DefaultModelEvent("test3", "test3", null, null);
	}

	/**
	 * Testing the correct functionality of the method for performing commands.
	 */
	@Test
	public void testPerformCommands() {

		comm.performCommand(editorModel);

		verify(mockCommand1).performCommand(editorModel); // returns null; aborts rest
		verify(mockCommand2, never()).performCommand(editorModel);
		verify(mockCommand3, never()).performCommand(editorModel);

		when(mockCommand1.performCommand(editorModel)).thenReturn(modelEvent1);
		when(mockCommand2.performCommand(editorModel)).thenReturn(modelEvent2);
		when(mockCommand3.performCommand(editorModel)).thenReturn(modelEvent3);

		comm.performCommand(editorModel);

		verify(mockCommand1, times(2)).performCommand(editorModel);
		verify(mockCommand2).performCommand(editorModel);
		verify(mockCommand3).performCommand(editorModel);

	}

	/**
	 * Testing the correct functionality of the method for undoing commands.
	 */
	@Test
	public void testUndoCommands() {

		comm.undoCommand(editorModel);

		verify(mockCommand3).undoCommand(editorModel); // returns null; aborts rest
		verify(mockCommand2, never()).undoCommand(editorModel);
		verify(mockCommand1, never()).undoCommand(editorModel);

		when(mockCommand3.undoCommand(editorModel)).thenReturn(modelEvent3);
		when(mockCommand2.undoCommand(editorModel)).thenReturn(modelEvent2);
		when(mockCommand1.undoCommand(editorModel)).thenReturn(modelEvent1);

		comm.undoCommand(editorModel);

		verify(mockCommand3, times(2)).undoCommand(editorModel);
		verify(mockCommand2).undoCommand(editorModel);
		verify(mockCommand1).undoCommand(editorModel);
	}

	/**
	 * Testing the correct functionality of the method for redoing commands.
	 */
	@Test
	public void testRedoCommands() {

		comm.redoCommand(editorModel);

		verify(mockCommand1).redoCommand(editorModel); // returns null; aborts rest
		verify(mockCommand2, never()).redoCommand(editorModel);
		verify(mockCommand3, never()).redoCommand(editorModel);

		when(mockCommand1.redoCommand(editorModel)).thenReturn(modelEvent1);
		when(mockCommand2.redoCommand(editorModel)).thenReturn(modelEvent2);
		when(mockCommand3.redoCommand(editorModel)).thenReturn(modelEvent3);

		comm.redoCommand(editorModel);

		verify(mockCommand1, times(2)).redoCommand(editorModel);
		verify(mockCommand2).redoCommand(editorModel);
		verify(mockCommand3).redoCommand(editorModel);
	}

}
