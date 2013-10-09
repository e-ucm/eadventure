/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.eucm.ead.editor.view.generic;

/**
 * A constraint for options. Only options that satisfy all constraints will have
 * their values written to the underlying model.  
 * Multiple constraints may be specified for a single field. 
 * 
 * @author mfreire
 */
public interface Constraint {
	/**
	 * tooltip to indicate what is considered valid, and/or why something is invalid.
	 * @return optional string to indicate what is considered valid, or why
	 * the current values are considered invalid.
	 */
	public String getTooltip();

	/**
	 * true if currently valid
	 * @return 
	 */
	public boolean isValid();

	/**
	 * called when control validity changes. Allows invalid controls to 
	 * affect other controls, bypassing normal change-event flow
	 * (since there is no event handling involved in non-model changes).
	 */
	public void validityChanged();
}
