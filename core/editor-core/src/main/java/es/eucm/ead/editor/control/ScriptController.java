/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.eucm.ead.editor.control;

import es.eucm.ead.editor.view.components.OutputLogPanel;

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
	 * @param source optional source of code
	 * @return result of executing script
	 */
	public Object eval(String script, OutputLogPanel out, String source);
}
