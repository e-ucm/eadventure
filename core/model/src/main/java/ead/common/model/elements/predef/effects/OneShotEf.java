package ead.common.model.elements.predef.effects;

import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.conditions.NOTCond;
import ead.common.model.elements.conditions.OperationCond;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.operations.BasicField;
import ead.common.model.elements.operations.BooleanOp;
import ead.common.model.params.variables.VarDef;

/**
 * Represents an effect that is launched only once
 *
 */
public class OneShotEf extends ChangeFieldEf {
	
	public static final VarDef<Boolean> LAUNCHED = new VarDef<Boolean>("OneShotEf.Launched", Boolean.class, false);
	
	/**
	 * 
	 * @param effect the effect to launch only once
	 */
	public OneShotEf(EAdEffect effect){
		// Sets true launched variable
		BasicField<Boolean> f = new BasicField<Boolean>(this, LAUNCHED);
		addField(f);
		setOperation(BooleanOp.TRUE_OP);
		// Sets as condition that launched variable is false
		setCondition(new NOTCond(new OperationCond(f)));
		
		// Adds as a next effect the one shot effect
		getNextEffects().add(effect);
	}

}
