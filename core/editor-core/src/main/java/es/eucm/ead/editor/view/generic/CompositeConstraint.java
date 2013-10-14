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
