/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.eucm.ead.editor.view.generic;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Sinmple constraint.
 * @author mfreire
 */
public abstract class AbstractConstraint implements Constraint {

	protected ArrayList<AbstractOption<?>> options = new ArrayList<AbstractOption<?>>();
	
	protected String tooltip;
	
	public AbstractConstraint(String tooltip, AbstractOption<?> ...options) {
		this.options.addAll(Arrays.asList(options));
		this.tooltip = tooltip;
	}
	
	public void install() {
		for (AbstractOption<?> o : options) {
			o.getConstraints().add(this);
		}		
	}
		
	@Override
	public String getTooltip() {
		return tooltip;
	}

	@Override
	public abstract boolean isValid();
	
	@Override
	public void validityChanged() {
		for (AbstractOption<?> o : options) {
			o.refreshValid();
		}
	}
}
