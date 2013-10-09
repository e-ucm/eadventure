/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.eucm.ead.editor.view.generic;

import java.util.ArrayList;

/**
 * A collection of constraints that acts as a single one (using AND). 
 * Empty by default. 
 * 
 * @author mfreire
 */
public class CompositeConstraint implements Constraint {
	private ArrayList<Constraint> all = new ArrayList<Constraint>();

	@Override
	public String getTooltip() {
		StringBuilder sb = new StringBuilder("<html>");
		for (Constraint c : all) {
			sb.append(c.getTooltip());
			sb.append("<br>\n");
		}
		int last = sb.lastIndexOf("<br>\n");
		if (last != -1) {
			sb.replace(last, sb.length(), "");
		}
		sb.append("</html>");
		return sb.toString();
	}

	public ArrayList<Constraint> getList() {
		return all;
	}

	@Override
	public boolean isValid() {
		for (Constraint c : all) {
			if (!c.isValid())
				return false;
		}
		return true;
	}

	@Override
	public void validityChanged() {
		for (Constraint c : all) {
			c.validityChanged();
		}
	}
}
