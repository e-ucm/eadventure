/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.eucm.ead.editor.control;

import es.eucm.ead.editor.view.components.OutputLogPanel;
import javax.script.ScriptContext;

/**
 * Controls script retrieval, launching and execution. Scripts are also used
 * as an extension mechanism for the editor.
 * 
 * @author mfreire
 */
public interface ScriptController {

	/**
	 * Resets engine state, and reloads available scripts.
	 */
	public void refreshScripts();

	/**
	 * Runs a script.
	 * @param script to execute
	 * @param out optional output (null = ignore output)
	 * @param context to execute code in
	 * @param source optional source of code
	 * @return result of executing script
	 */
	public Object eval(String script, OutputLogPanel out, ScriptContext context, String source);
	

	/**
	 * Set the actual super-controller.
	 * @param controller the main controller, providing access to model, views,
	 * and more
	 */
	void setController(Controller controller);	
	
	/**
	 * Requests the script execution context
	 * @return the private scope
	 */
	ScriptContext getContext();
}
