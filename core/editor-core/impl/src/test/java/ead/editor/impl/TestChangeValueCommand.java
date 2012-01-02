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

import junit.framework.TestCase;
import org.junit.*;
import org.mockito.*;
import static org.mockito.Mockito.*;

import ead.editor.control.commands.ChangeValueCommand;

/**
 * Class for testing the right functionality of ChangeValueActions that modify the game model.
 */
public class TestChangeValueCommand extends TestCase {	
	
	/**
	 * Actions for testing the changes of String values. 
	 */
    private ChangeValueCommand<String> actionS, mockActionS;
    /**
	 * Actions for testing the changes of Integer values. 
	 */
    private ChangeValueCommand<Integer> actionI, actionIb, mockActionI;
    /**
	 * Actions for testing the changes of Boolean values. 
	 */
    private ChangeValueCommand<Boolean> actionB, mockActionB;
    /**
	 * Actions for testing the changes of String values. 
	 */
    private ChangeValueCommand<Object> actionO, actionOb, mockActionO;
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
	
	/**
	 * Method that initiates both the mock objects and the regular objects of the class, works similar to a constructor.   
	 */
    @Before
    public void setUp(){
 
    	MockitoAnnotations.initMocks(this);
    	
    	actionObj = new CommandTestObjects("oh",1,false,new Object());
    	obj = new Object();
    	
    	actionS = new ChangeValueCommand<String>(actionObj, "test", "setS", "getS");
    	actionI = new ChangeValueCommand<Integer>(actionObj, 100, "setI", "getI");
    	actionIb = new ChangeValueCommand<Integer>(actionObj, 200, "setI", "getI");
    	actionB = new ChangeValueCommand<Boolean>(actionObj, true, "setB", "getB");
    	actionO = new ChangeValueCommand<Object>(actionObj, obj, "setO", "getO");
    	actionOb = new ChangeValueCommand<Object>(actionObj, new Object(), "setO", "getO");
    	
    	mockActionS = new ChangeValueCommand<String>(mockActionObject, "test", "setS", "getS");
    	mockActionI = new ChangeValueCommand<Integer>(mockActionObject, 100, "setI", "getI");
    	mockActionB = new ChangeValueCommand<Boolean>(mockActionObject, true, "setB", "getB");
    	mockActionO = new ChangeValueCommand<Object>(mockActionObject, obj, "setO", "getO");
    	
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
		
		actionS.performCommand();	
		actionI.performCommand();	
		actionB.performCommand();		
		actionO.performCommand();
		
		assertEquals(true, (actionObj.getS()).equals("test"));
		assertEquals(true, (actionObj.getI()).equals(100));
		assertEquals(true, (actionObj.getB()).equals(true));
		assertEquals(true, (actionObj.getO()).equals(obj));
		
		mockActionS.performCommand();	
		mockActionI.performCommand();	
		mockActionB.performCommand();		
		mockActionO.performCommand();
		
		verify(mockActionObject, times(1)).getS();
		verify(mockActionObject, times(1)).setS("test");
		verify(mockActionObject, times(1)).getI();
		verify(mockActionObject, times(1)).setI(100);
		verify(mockActionObject, times(1)).getB();
		verify(mockActionObject, times(1)).setB(true);
		verify(mockActionObject, times(1)).getO();
		verify(mockActionObject, times(1)).setO(obj);	
     
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
		
		actionS.redoCommand();	
		actionI.redoCommand();	
		actionB.redoCommand();		
		actionO.redoCommand();
		
		assertEquals(true, (actionObj.getS()).equals("test"));
		assertEquals(true, (actionObj.getI()).equals(100));
		assertEquals(true, (actionObj.getB()).equals(true));
		assertEquals(true, (actionObj.getO()).equals(obj));
		
		mockActionS.redoCommand();	
		mockActionI.redoCommand();	
		mockActionB.redoCommand();		
		mockActionO.redoCommand();		
		
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
		
		actionS.performCommand();	
		actionI.performCommand();	
		actionB.performCommand();		
		actionO.performCommand();
		
		assertEquals(true, (actionObj.getS()).equals("test"));
		assertEquals(true, (actionObj.getI()).equals(100));
		assertEquals(true, (actionObj.getB()).equals(true));
		assertEquals(true, (actionObj.getO()).equals(obj));
		
		actionS.undoCommand();	
		actionI.undoCommand();	
		actionB.undoCommand();		
		actionO.undoCommand();
		
		assertEquals(false, (actionObj.getS()).equals("test"));
		assertEquals(false, (actionObj.getI()).equals(100));
		assertEquals(false, (actionObj.getB()).equals(true));
		assertEquals(false, (actionObj.getO()).equals(obj));
		
		mockActionS.undoCommand();	
		mockActionI.undoCommand();	
		mockActionB.undoCommand();		
		mockActionO.undoCommand();		
		
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
		
		actionS.performCommand();	
		actionI.performCommand();	
		actionB.performCommand();		
		actionO.performCommand();
		
		assertEquals(true, (actionObj.getS()).equals("test"));
		assertEquals(true, (actionObj.getI()).equals(100));
		assertEquals(true, (actionObj.getB()).equals(true));
		assertEquals(true, (actionObj.getO()).equals(obj));
		
		actionS.combine(actionI);	
		actionB.combine(actionO);
		actionI.combine(actionIb);	
		actionO.combine(actionOb);
		
		assertEquals(true, (actionI.getNewValue()).equals(actionIb.getNewValue()));		
		assertEquals(true, (actionO.getNewValue()).equals(actionOb.getNewValue()));		    	
     
    } 



}
