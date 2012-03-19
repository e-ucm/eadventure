package ead.engine.core.debuggers;

import java.util.List;

public interface DebuggerHandler extends Debugger {

	/**
	 * Init the debugger handler with all the classes for the used debugger
	 * 
	 * @param classes
	 *            debuggers classes
	 */
	public void init(List<Class<? extends Debugger>> debuggers);

}
