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

package es.eucm.eadventure.editor.impl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.elements.extra.EAdList;
import es.eucm.eadventure.common.model.elements.extra.EAdListImpl;
import es.eucm.eadventure.editor.control.commands.impl.AddElementCommand;
import es.eucm.eadventure.editor.control.commands.impl.MoveElementCommand;
import es.eucm.eadventure.editor.control.commands.impl.RemoveElementCommand;

/**
 * Class for testing the right functionality of generic Commands to modify the lists of EAdElement instances in the game model.
 */
public class ElementCommandsTest extends TestCase {
	
	/**
	 * The mock object list in which the elements will be modified
	 */
	@Mock
	private EAdList<EAdElement> mockList;
	/**
	 * The mock object element to be modified
	 */
	@Mock
	private EAdElement mockElement;

	/**
	 * The list in which the elements will be modified
	 */
	private EAdList<EAdElement> elemList;
	/**
	 * The parent element to create EAdElement instances
	 */
	@Mock
	private EAdElement pelement;
	/**
	 * The element that will be modified
	 */
	@Mock
	private EAdElement element;
	/**
	 * The Command to add elements to a list
	 */
	private AddElementCommand<EAdElement> addComm, mockAdd;
	/**
	 * The Command to remove elements from a list
	 */
	private RemoveElementCommand<EAdElement> removeComm, mockRemove;
	/**
	 * The Command to move elements in a list
	 */
	private MoveElementCommand<EAdElement> moveComm, mockMove;
		
	/**
	 * Method that initiates both the mock objects and the regular objects of the class, works similar to a constructor.   
	 */
    @Before
    public void setUp(){
 
    	MockitoAnnotations.initMocks(this);
    	
    	elemList = new EAdListImpl<EAdElement>(EAdElement.class);
    	elemList.add(pelement);
    	
    	addComm = new AddElementCommand<EAdElement>(elemList, element);
    	removeComm = new RemoveElementCommand<EAdElement>(elemList, element);
    	moveComm = new MoveElementCommand<EAdElement>(elemList, element, 0);    	
    	
    	mockAdd = new AddElementCommand<EAdElement>(mockList, mockElement);
    	mockRemove = new RemoveElementCommand<EAdElement>(mockList, mockElement);
    	mockMove = new MoveElementCommand<EAdElement>(mockList, mockElement, 0);
    	
    }
    
    /**
	 * Testing the correct functionality of the method for performing commands.  
	 */
    @Test
	public void testPerformCommands() {
		
    	assertEquals(0, elemList.indexOf(pelement));
    	assertEquals(false, elemList.contains(element));
		addComm.performCommand();					
		assertEquals(true, elemList.contains(element));
		assertEquals(1, elemList.indexOf(element));
		
		removeComm.performCommand();
		assertEquals(false, elemList.contains(element));			
		addComm.performCommand();					
		assertEquals(true, elemList.contains(element));
		assertEquals(1, elemList.indexOf(element));
		
		moveComm.performCommand();
		assertEquals(0, elemList.indexOf(element));
		assertEquals(1, elemList.indexOf(pelement));
		
		mockAdd.performCommand();
		verify(mockList,times(1)).add(mockElement);
     
    } 
    
    /**
	 * Testing the correct functionality of the method for undoing commands.  
	 */
    @Test
	public void testUndoCommands() {
		
		addComm.performCommand();					
		assertEquals(true, elemList.contains(element));
		addComm.undoCommand();
		assertEquals(false, elemList.contains(element));
		
		addComm.performCommand();					
		assertEquals(true, elemList.contains(element));
		removeComm.performCommand();
		assertEquals(false, elemList.contains(element));			
		removeComm.undoCommand();				
		assertEquals(true, elemList.contains(element));
		
		moveComm.performCommand();
		assertEquals(0, elemList.indexOf(element));
		assertEquals(1, elemList.indexOf(pelement));
		moveComm.undoCommand();
		assertEquals(1, elemList.indexOf(element));
		assertEquals(0, elemList.indexOf(pelement));
		
		
		mockAdd.performCommand();
		verify(mockList,times(1)).add(mockElement);
		mockAdd.undoCommand();
		verify(mockList,times(1)).remove(mockElement);
		
		mockAdd.performCommand();
		mockRemove.performCommand();
		verify(mockList,times(1)).remove(mockElement);
		mockRemove.undoCommand();
		verify(mockList,times(1)).add(mockElement, mockRemove.getIndex());
		
		mockMove.performCommand();
		mockMove.undoCommand();
		verify(mockList,times(2)).remove(mockElement);
		verify(mockList,times(2)).add(mockElement, 0);
     
    } 

}
