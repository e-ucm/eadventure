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

import java.awt.Component;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import es.eucm.eadventure.common.model.conditions.impl.VarCondition.Operator;
import es.eucm.eadventure.common.model.conditions.impl.VarVarCondition;
import es.eucm.eadventure.editor.impl.Messages;

/**
 * The renderer class for the VarVarCondition type
 * @author Roberto Tornero
 * 
 */
public class VarVarConditionRenderer extends ConditionRenderer{


	private static final long serialVersionUID = 6092041036265020569L;

	/**
	 * Constructor for the renderer of the VarVarCondition type
	 */	
	public VarVarConditionRenderer(ConditionsController controller, VarVarCondition vcondition, boolean not){

		super(controller, vcondition, not);

		try {
			iconLabel.setIcon(new ImageIcon(ImageIO.read(getResourceAsStream("@drawable/conditions/var16.png"))));

		} catch (IOException e) {
			logger.error(Messages.input_output_exception);
		}		

		Component subjectRender = getSubjectRender();
		if (subjectRender != null)
			renderComponents.add(subjectRender);
		Component verbRender = getVerbRender();
		if (verbRender != null)
			renderComponents.add(verbRender);
		Component directComplementRender = getDirectComplementRender();
		if (directComplementRender != null)
			renderComponents.add(directComplementRender);

		addComponents();

		revalidate();
	}

	/**
	 * Private method for adding components to the renderer's panel
	 */
	private void addComponents() {

		// Add items
		//add(iconLabel);
		for(Component component : renderComponents)
			add(component);
		add(buttonsPanel);		
	}

	/**
	 * Private method that returns the name of one of the condition variables as a label component 
	 */
	private Component getSubjectRender() {

		return new JLabel(((VarVarCondition)condition).getVar1().getName());
	}

	/**
	 * Private method that returns the value of operators as a label component 
	 */
	private Component getVerbRender() {

		if( ((VarVarCondition)condition).getOperator().equals(Operator.GREATER) )
			return new JLabel(">");

		else if( ((VarVarCondition)condition).getOperator().equals(Operator.GREATER_EQUAL) )
			return new JLabel(">=");

		else if( ((VarVarCondition)condition).getOperator().equals(Operator.EQUAL) ) 
			return new JLabel("=");

		else if( ((VarVarCondition)condition).getOperator().equals(Operator.LESS_EQUAL) ) 
			return new JLabel("<=");

		else if( ((VarVarCondition)condition).getOperator().equals(Operator.LESS) ) 
			return new JLabel("<");

		else return new JLabel("=/=");

	}

	/**
	 * Private method that returns the name of one of the condition variables as a label component 
	 */
	private Component getDirectComplementRender() {

		return new JLabel(((VarVarCondition)condition).getVar2().getName());
	}

}
