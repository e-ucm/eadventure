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

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.eucm.eadventure.common.model.conditions.impl.FlagCondition;
import es.eucm.eadventure.common.model.conditions.impl.FlagCondition.Value;
import es.eucm.eadventure.common.model.conditions.impl.VarCondition.Operator;
import es.eucm.eadventure.common.model.conditions.impl.VarValCondition;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.variables.impl.vars.BooleanVar;
import es.eucm.eadventure.common.model.variables.impl.vars.NumberVar;
import es.eucm.eadventure.editor.impl.Messages;
import es.eucm.eadventure.editor.impl.VarFlagSummary;
import es.eucm.eadventure.gui.eadcanvaspanel.EAdCanvasPanel;
import es.eucm.eadventure.gui.eadcanvaspanel.scrollcontainers.EAdAutosizeScrollCanvasPanel;
import es.eucm.eadventure.utils.i18n.CommonMessages;

/**
 * The dialog for editing the properties of existing or new conditions
 * @author Roberto Tornero
 *
 */
public class ConditionsDialog extends JDialog implements GenericConditionsDialog{


	private static final long serialVersionUID = 4039077734486721887L;

	protected static final Logger logger = LoggerFactory.getLogger("ConditionsDialog");

	/**
	 * The state of the dialog, if editing variables, flags or global states 
	 */
	private enum State {VAR, FLAG, GLOBAL}
	/**
	 * The button for creating conditions based on variables
	 */
	private JButton varCondition;
	/**
	 * The button for creating conditions based on flags
	 */
	private JButton flagCondition;
	/**
	 * The button for creating conditions based on global states
	 */
	private JButton globalCondition;
	/**
	 * The button to confirm the creation process
	 */
	private JButton okButton;
	/**
	 * The button to cancel the creation process
	 */
	private JButton cancelButton;
	/**
	 * The panel for displaying the var, flag or global conditions buttons
	 */
	private EAdCanvasPanel topPanel;
	/**
	 * The panel for displaying the fields of the condition
	 */
	private EAdCanvasPanel bottomPanel;
	/**
	 * The panel for displaying the OK and Cancel buttons
	 */
	private EAdCanvasPanel buttonPanel;
	/**
	 * The container for the topPanel
	 */
	private EAdAutosizeScrollCanvasPanel topScrollPanel;
	/**
	 * The container for the bottomPanel
	 */
	private EAdAutosizeScrollCanvasPanel bottomScrollPanel;
	/**
	 * The container for the buttonPanel
	 */
	private EAdAutosizeScrollCanvasPanel buttonScrollPanel;
	/**
	 * The box for selecting the variables
	 */
	private JComboBox varCombo;
	/**
	 * The box for selecting the operators of the condition
	 */
	private JComboBox operatorCombo;
	/**
	 * The box for selecting the flags
	 */
	private JComboBox flagCombo;
	/**
	 * The box for selecting the global states
	 */
	private JComboBox globalCombo;
	/**
	 * The box for selecting the flag state, between ACTIVE and INACTIVE
	 */
	private JComboBox stateCombo;
	/**
	 * The spinner for the comparison value
	 */
	private JSpinner valSpinner;
	/**
	 * The state of the dialog
	 */
	private State conditionState;
	/**
	 * A boolean that indicates if the Ok button is or is not pressed
	 */
	private boolean okPressed;

	/**
	 * The constructor of the conditions dialog. A dialog owner is required in order to be a modal dialog
	 * @param owner
	 * @param summary
	 */
	public ConditionsDialog(Dialog owner, VarFlagSummary summary) {

		super(owner, true);

		setSize(new Dimension(500,300));		

		topScrollPanel = new EAdAutosizeScrollCanvasPanel();
		topScrollPanel.setBorder(BorderFactory.createTitledBorder(ConditionMessages.select_condition));
		this.add(topScrollPanel, BorderLayout.NORTH);
		topScrollPanel.setBounds(0,0,500,100);

		bottomScrollPanel = new EAdAutosizeScrollCanvasPanel();
		this.add(bottomScrollPanel, BorderLayout.CENTER);
		bottomScrollPanel.setBounds(0,100,500,100);
		bottomPanel = bottomScrollPanel.getCanvas();

		buttonScrollPanel = new EAdAutosizeScrollCanvasPanel();
		this.add(buttonScrollPanel, BorderLayout.SOUTH);
		bottomScrollPanel.setBounds(0,200,500,100);

		setSummary(summary);
		globalCombo = new JComboBox(new String[]{"Global1", "Global2"});

		operatorCombo = new JComboBox(new String[]{">", ">=", "=","<", "<=", "=/="});
		stateCombo = new JComboBox(new String[]{ConditionMessages.active, ConditionMessages.inactive});
		valSpinner = new JSpinner(new SpinnerNumberModel(1, -100, 100, 1));      

		createTopPanel();
		createVarConditionSubpanel();
		createButtonsPanel();

		okPressed = false;
	}

	/**
	 * A method for creating the topPanel and its components
	 */
	private void createTopPanel() {

		topPanel = topScrollPanel.getCanvas();

		try {			
			varCondition = new JButton(new ImageIcon(ImageIO.read(getResourceAsStream("@drawable/conditions/vars.png"))));
			varCondition.setText(ConditionMessages.vars);
			varCondition.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					createVarConditionSubpanel();					
				}

			});

			flagCondition = new JButton(new ImageIcon(ImageIO.read(getResourceAsStream("@drawable/conditions/flags.png"))));
			flagCondition.setText(ConditionMessages.flags);
			flagCondition.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					createFlagConditionSubpanel();					
				}

			});

			globalCondition = new JButton(new ImageIcon(ImageIO.read(getResourceAsStream("@drawable/conditions/group-2.png"))));
			globalCondition.setText(ConditionMessages.global_state);
			globalCondition.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					createGlobalConditionSubpanel();					
				}

			});			

		} catch (IOException e) {
			logger.error(Messages.input_output_exception);
		}

		varCondition.setBounds(55,10,130,60);
		flagCondition.setBounds(185,10,130,60);
		globalCondition.setBounds(315,10,130,60);

		topPanel.add(varCondition);
		topPanel.add(flagCondition);
		topPanel.add(globalCondition);		

	}

	/**
	 * A method for creating the Ok and Cancel buttons panel
	 */
	private void createButtonsPanel() {

		buttonPanel = buttonScrollPanel.getCanvas();

		okButton = new JButton(CommonMessages.ok);
		cancelButton = new JButton(CommonMessages.cancel);

		okButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {

				okPressed = true;
				ConditionsDialog.this.setVisible(false);
			}

		});

		cancelButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {

				ConditionsDialog.this.setVisible(false);				
			}

		});

		okButton.setBounds(125,10,120,40);
		cancelButton.setBounds(250,10,120,40);

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);		

	}

	/**
	 * Creates the bottomPanel with the options of a condition based on a variable
	 */
	protected void createVarConditionSubpanel() {

		conditionState = State.VAR;

		bottomPanel.removeAll();
		bottomPanel.setBorder(BorderFactory.createTitledBorder(ConditionMessages.condition_based_variable));

		bottomPanel.add(varCombo);
		bottomPanel.add(operatorCombo);
		bottomPanel.add(valSpinner);

		varCombo.setBounds(50, 30, 100, 30);
		operatorCombo.setBounds(160, 30, 50, 30);
		valSpinner.setBounds(220, 30, 100, 30);

		bottomPanel.revalidate();

	}

	/**
	 * Creates the bottomPanel with the options of a condition based on a flag
	 */
	protected void createFlagConditionSubpanel() {

		conditionState = State.FLAG;

		bottomPanel.removeAll();
		bottomPanel.setBorder(BorderFactory.createTitledBorder(ConditionMessages.condition_based_flag));

		bottomPanel.add(flagCombo);
		bottomPanel.add(stateCombo);

		flagCombo.setBounds(50, 30, 100, 30);
		stateCombo.setBounds(220, 30, 100, 30);

		bottomPanel.revalidate();

	}

	/**
	 * Creates the bottomPanel with the options of a condition based on a global state
	 */
	protected void createGlobalConditionSubpanel() {

		conditionState = State.GLOBAL;

		bottomPanel.removeAll();
		bottomPanel.setBorder(BorderFactory.createTitledBorder(ConditionMessages.condition_based_global));

		bottomPanel.add(globalCombo);

		globalCombo.setBounds(160, 30, 100, 30);

		bottomPanel.revalidate();

	}

	/**
	 * Overrided method from the generic dialog interface, to set up the summary of vars and flags
	 */
	@Override
	public void setSummary(VarFlagSummary summary) {

		varCombo = new JComboBox(summary.getVarNamesArray());
		flagCombo = new JComboBox(summary.getFlags());

	}

	/**
	 * Overrided method from the generic dialog interface, to set up an existing condition on the dialog options
	 */
	@Override
	public void setConditionOn(EAdCondition oldCondition) {

		if (oldCondition instanceof VarValCondition){
			varCombo.setSelectedItem(((VarValCondition)oldCondition).getVar());
			operatorCombo.setSelectedItem(getOperatorAsString(((VarValCondition)oldCondition).getOperator()));
			valSpinner.setValue(((VarValCondition)oldCondition).getVal());
		}
		else if (oldCondition instanceof FlagCondition){
			createFlagConditionSubpanel();
			flagCombo.setSelectedItem(((FlagCondition)oldCondition).getFlag());
			stateCombo.setSelectedItem(getFlagValueAsString(((FlagCondition)oldCondition).getValue()));
		}
		else {}

	}

	/**
	 * Overrided method from the generic dialog interface, to create a condition with the selected options of the dialog
	 */
	@Override
	public EAdCondition getConditionFrom(VarFlagSummary summary) {

		EAdCondition condition = null;
		//Var condition
		if (conditionState.equals(State.VAR)){
			String varName = (String)varCombo.getSelectedItem();
			NumberVar<?> var = (NumberVar<?>) summary.getVarForName(varName);
			condition = new VarValCondition(var, (Integer)valSpinner.getValue(), getOperatorFromString((String)operatorCombo.getSelectedItem()));
			return condition;
		}
		//Flag condition
		else if (conditionState.equals(State.FLAG)){
			BooleanVar var = new BooleanVar((String)flagCombo.getSelectedItem());
			condition = new FlagCondition(var, getFlagValueFromString((String)stateCombo.getSelectedItem()));
			return condition;
		}
		//Global state condition
		else return null; 
	}

	/**
	 * Returns the String representation of the Operator op
	 * @param op
	 * @return
	 */
	private String getOperatorAsString(Operator op){

		if (op.equals(Operator.GREATER))
			return ">";
		else if (op.equals(Operator.GREATER_EQUAL)) 
			return ">=";
		else if (op.equals(Operator.EQUAL))
			return "=";
		else if (op.equals(Operator.LESS_EQUAL))
			return "<=";
		else if (op.equals(Operator.LESS))
			return "<";
		else return "=/=";

	}

	/**
	 * Returns the Operator from its String representation
	 * @param s
	 * @return
	 */
	private Operator getOperatorFromString(String s){

		if (s.equals(">"))
			return Operator.GREATER;
		else if (s.equals(">=")) 
			return Operator.GREATER_EQUAL;
		else if (s.equals("="))
			return Operator.EQUAL;
		else if (s.equals("<="))
			return Operator.LESS_EQUAL;
		else if (s.equals("<"))
			return Operator.LESS;
		else return Operator.DIFFERENT;

	}

	/**
	 * Returns the String representation of the flag value 
	 * @param val
	 * @return
	 */
	private String getFlagValueAsString(Value val){

		if (val.equals(Value.ACTIVE))
			return ConditionMessages.active;
		else return ConditionMessages.inactive;

	}

	/**
	 * Returns the falg value from its String representation
	 * @param s
	 * @return
	 */
	private Value getFlagValueFromString(String s){

		if (s.equals(ConditionMessages.active))
			return Value.ACTIVE;
		else return Value.INACTIVE;
	}

	/**
	 * Returns the value of the okPressed boolean
	 * @return
	 */
	public boolean isOkPressed(){
		return okPressed;
	}	

	/**
	 * +++Temporal method for loading resources, future AssetHandler instance expected+++  
	 */
	private InputStream getResourceAsStream(String path) {

		String location = path.replaceAll("@", "");
		return ClassLoader.getSystemResourceAsStream(location);

	}

}
