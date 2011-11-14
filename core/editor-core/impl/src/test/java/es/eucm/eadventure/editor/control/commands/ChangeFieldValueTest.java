package es.eucm.eadventure.editor.control.commands;

import org.junit.Test;

import es.eucm.eadventure.editor.control.commands.impl.ChangeFieldValueCommand;
import es.eucm.eadventure.editor.view.generics.FieldDescriptor;
import es.eucm.eadventure.editor.view.generics.impl.FieldDescriptorImpl;

import junit.framework.TestCase;

public class ChangeFieldValueTest extends TestCase {

	FieldDescriptor<Boolean> fieldDescriptor;
	
	TestClass testElement;
	
	
	@Override
	public void setUp() {
		testElement = new TestClass();
		fieldDescriptor = new FieldDescriptorImpl<Boolean>(testElement, "value");
	}
	
	@Test
	public void testPerformAndUndoFailCommand() {
		assert(!testElement.getValue());
		ChangeFieldValueCommand<Boolean> command = new ChangeFieldValueCommand<Boolean>(Boolean.TRUE, fieldDescriptor);
		command.performCommand();
		assert(testElement.getValue());
		command.undoCommand();
		assert(!testElement.getValue());
	}
	
	public static class TestClass {
		
		private Boolean value;
		
		public void setValue(Boolean value) {
			this.value = value;
		}
		
		public Boolean getValue() {
			return value;
		}
	}
}
