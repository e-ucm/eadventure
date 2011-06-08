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

package es.eucm.eadventure.editor.impl.conditionspanel;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import es.eucm.eadventure.common.model.conditions.impl.FlagCondition;
import es.eucm.eadventure.common.model.conditions.impl.NOTCondition;
import es.eucm.eadventure.common.model.conditions.impl.VarValCondition;
import es.eucm.eadventure.common.model.conditions.impl.VarVarCondition;
import es.eucm.eadventure.common.model.elements.EAdCondition;

/**
 * The renderer class for the NOTCondition type
 * @author Roberto Tornero
 * 
 */
public class NOTConditionRenderer extends JPanel{


	private static final long serialVersionUID = -2178033632997460363L;

	/**
	 * The NOTCondition attribute of the renderer
	 */
	private NOTCondition notCondition;

	/**
	 * Constructor for the NOTCondition renderer class. Depending on the value of the condition held by the NOTCondition
	 * object, it adds the condition's proper renderer.	 * 
	 * @param controller
	 * @param condition
	 * @param not
	 */
	public NOTConditionRenderer(ConditionsController controller, NOTCondition condition, boolean not){

		super();
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

		notCondition = condition;

		EAdCondition cond = condition.getCondition();

		if (cond instanceof FlagCondition)
			add(new FlagConditionRenderer(controller, (FlagCondition) cond, not));
		else if (cond instanceof VarVarCondition)
			add(new VarVarConditionRenderer(controller, (VarVarCondition) cond, not));
		else if (cond instanceof VarValCondition)
			add(new VarValConditionRenderer(controller, (VarValCondition) cond, not));

	}

	/**
	 * Getter for the NOTCondition attribute
	 * @return
	 */
	public NOTCondition getNotCondition(){
		return notCondition;
	}
}
