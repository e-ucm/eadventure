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

import es.eucm.eadventure.common.model.EAdList;
import es.eucm.eadventure.common.model.conditions.impl.ANDCondition;
import es.eucm.eadventure.common.model.conditions.impl.FlagCondition;
import es.eucm.eadventure.common.model.conditions.impl.ListedCondition;
import es.eucm.eadventure.common.model.conditions.impl.ListedCondition.Operator;
import es.eucm.eadventure.common.model.conditions.impl.NOTCondition;
import es.eucm.eadventure.common.model.conditions.impl.ORCondition;
import es.eucm.eadventure.common.model.conditions.impl.VarValCondition;
import es.eucm.eadventure.common.model.conditions.impl.VarVarCondition;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.editor.control.Command;
import es.eucm.eadventure.editor.control.commands.impl.AddElementCommand;
import es.eucm.eadventure.editor.control.commands.impl.DuplicateElementCommand;
import es.eucm.eadventure.editor.control.commands.impl.RemoveElementCommand;
import es.eucm.eadventure.editor.impl.VarFlagSummary;

/**
 * Class for the controller of the conditions. It is in charge of adding new conditions,
 * deleting existing ones, editing their properties, etc.
 * @author Roberto Tornero
 *
 */
public class ConditionsController {

	/**
	 * The add, delete or duplicate command to be performed 
	 */
	private Command command;
	/**
	 * The current list of conditions with its operator
	 */
	private ListedCondition listCondition;
	/**
	 * The panel with the conditions to be changed by the controller
	 */
	private ConditionsPanel conditionsPanel;
	/**
	 * The summary with all the vars and flags 
	 */
	private VarFlagSummary vfsummary;

	/**
	 * Constructor for the controller of the conditions
	 * @param condPanel
	 * @param summary
	 */
	public ConditionsController(ConditionsPanel condPanel, VarFlagSummary summary){

		listCondition = null;
		conditionsPanel = condPanel;
		vfsummary = summary;		
	}

	/**
	 * A method for adding new conditions, using the Command interface
	 * @param listCondition
	 * @param condition
	 */
	public void addCondition(ListedCondition listCondition, EAdCondition condition) {		

		command = new AddElementCommand<EAdCondition>(listCondition.getConds(), condition);				
		command.performCommand();
	}

	/**
	 * A method for getting the conditions from the dialog and adding them
	 */
	public void addConditionFrom(){		

		ConditionsDialog dialog = new ConditionsDialog(conditionsPanel, vfsummary);
		dialog.setVisible(true);

		if (dialog.isOkPressed()){			

			EAdCondition condition = dialog.getConditionFrom(vfsummary);

			if (listCondition == null)
				listCondition = new ANDCondition(condition);			
			else addCondition(listCondition, condition);		

			addConditionRefs(condition); 		
			conditionsPanel.getRenderer().addComponents(this, listCondition);			
		}
	}

	/**
	 * A method for deleting conditions, using the Command interface
	 * @param listCondition
	 * @param condition
	 */
	public void deleteCondition(ListedCondition listCondition, EAdCondition condition){

		command = new RemoveElementCommand<EAdCondition>(listCondition.getConds(), condition);				
		command.performCommand();
	}

	/**
	 * A method for deleting the condition from the list
	 * @param cond
	 */
	public void deleteConditionFrom(EAdCondition cond) {

		deleteCondition(listCondition, cond);		
		deleteConditionRefs(cond);		
		conditionsPanel.getRenderer().addComponents(this, listCondition);		
	}

	/**
	 * A method for duplicating condition, using the Command interface
	 * @param listCondition
	 * @param condition
	 */
	public void duplicateCondition(ListedCondition listCondition, EAdCondition condition){

		command = new DuplicateElementCommand<EAdCondition>(listCondition.getConds(), condition);				
		command.performCommand();
	}

	/**
	 * A method for duplicating conditions of the list
	 * @param cond
	 */
	public void duplicateConditionFrom(EAdCondition cond) {

		duplicateCondition(listCondition, cond);
		addConditionRefs(cond);
		conditionsPanel.getRenderer().addComponents(this, listCondition);		
	}

	/**
	 * Method for setting a new list of conditions
	 * @param list
	 */
	public void setListedCondition(ListedCondition list) {

		listCondition = list;
		conditionsPanel.getRenderer().addComponents(this, listCondition);		
	}	

	/**
	 * Method for editing the values of the condition using the conditions dialog
	 * @param cond
	 */
	public void editCondition(EAdCondition cond){		

		int index = listCondition.getConds().indexOf(cond);
		EAdCondition oldCondition = listCondition.getConds().get(index);
		ConditionsDialog dialog = new ConditionsDialog(conditionsPanel, vfsummary);
		dialog.setConditionOn(oldCondition);
		dialog.setVisible(true);

		if (dialog.isOkPressed()){			

			EAdCondition newCondition = dialog.getConditionFrom(vfsummary);
			listCondition.getConds().remove(oldCondition);
			deleteConditionRefs(oldCondition);
			listCondition.getConds().add(newCondition, index);
			addConditionRefs(newCondition);
			conditionsPanel.getRenderer().addComponents(this, listCondition);			
		}
	}

	/**
	 * Return the summary of vars and flags
	 * @return
	 */
	public VarFlagSummary getSummary(){
		return vfsummary;
	}

	/**
	 * Adds new references for a var o flag if used on a condition
	 * @param condition
	 */
	public void addConditionRefs(EAdCondition condition){

		if (condition instanceof VarValCondition)
			vfsummary.addVarReference(((VarValCondition)condition).getVar());
		else if (condition instanceof VarVarCondition){
			vfsummary.addVarReference(((VarVarCondition)condition).getVar1());
			vfsummary.addVarReference(((VarVarCondition)condition).getVar2());
		}
		else vfsummary.addFlagReference(((FlagCondition)condition).getFlag());

	}

	/**
	 * Deletes existing references for a var or flag if the are no longer used by a condition
	 * @param condition
	 */
	public void deleteConditionRefs(EAdCondition condition){

		if (condition instanceof VarValCondition)
			vfsummary.deleteVarReference(((VarValCondition)condition).getVar());
		else if (condition instanceof VarVarCondition){
			vfsummary.deleteVarReference(((VarVarCondition)condition).getVar1());
			vfsummary.deleteVarReference(((VarVarCondition)condition).getVar2());
		}
		else vfsummary.deleteFlagReference(((FlagCondition)condition).getFlag());

	}

	/**
	 * Method for updating the value of the operator in the list of conditions
	 * @param operator
	 */
	public void updateOperator(Operator operator) {

		if (!listCondition.getOperator().equals(operator)){

			EAdList<EAdCondition> oldList = (EAdList<EAdCondition>) listCondition.getConds();

			if (operator.equals(Operator.OR))	
				listCondition = new ORCondition(oldList.get(0));			

			else if (operator.equals(Operator.AND))				
				listCondition = new ANDCondition(oldList.get(0));			

			for(int i = 1; i < oldList.size(); i++){
				addCondition(listCondition, oldList.get(i));
			}

			conditionsPanel.getRenderer().addComponents(this, listCondition);
		}
	}

	/**
	 * Method for creating and deleting NOTCondition from a simple condition
	 * @param condition
	 * @param not
	 * @return
	 */
	public EAdCondition setNotCondition(EAdCondition condition, boolean not) {

		int index;
		index = listCondition.getConds().indexOf(condition);

		EAdCondition oldCondition = listCondition.getConds().get(index);
		EAdCondition newCondition = null;
		listCondition.getConds().remove(oldCondition);		

		if (oldCondition instanceof NOTCondition)
			newCondition = ((NOTCondition) oldCondition).getCondition();
		else newCondition = new NOTCondition(oldCondition);				

		listCondition.getConds().add(newCondition, index);
		conditionsPanel.getRenderer().addComponents(this, listCondition);	

		return newCondition;		
	}


}
