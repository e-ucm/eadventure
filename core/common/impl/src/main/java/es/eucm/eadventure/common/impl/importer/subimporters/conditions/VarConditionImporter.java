package es.eucm.eadventure.common.impl.importer.subimporters.conditions;

import com.google.inject.Inject;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.conditions.Condition;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.model.conditions.impl.VarCondition;
import es.eucm.eadventure.common.model.conditions.impl.VarCondition.Operator;
import es.eucm.eadventure.common.model.conditions.impl.VarValCondition;
import es.eucm.eadventure.common.model.variables.impl.vars.IntegerVar;

public class VarConditionImporter implements EAdElementImporter<es.eucm.eadventure.common.data.chapter.conditions.VarCondition, VarCondition>{

	private EAdElementFactory factory;

	@Inject
	public VarConditionImporter(EAdElementFactory factory) {
		this.factory = factory;
	}
	
	@Override
	public VarCondition init(
			es.eucm.eadventure.common.data.chapter.conditions.VarCondition oldObject) {
		Operator op = getOperator( oldObject.getState() );
		IntegerVar var = (IntegerVar) factory.getVarByOldId(oldObject.getId(), Condition.VAR_CONDITION);
		
		VarValCondition condition = new VarValCondition(var, oldObject.getValue(), op );
		return condition;
	}
	

	@Override
	public VarCondition convert(
			es.eucm.eadventure.common.data.chapter.conditions.VarCondition oldObject, Object object) {
		return (VarValCondition) object;
	}

	private VarValCondition.Operator getOperator( int op ){
		switch( op ){
		case es.eucm.eadventure.common.data.chapter.conditions.VarCondition.VAR_EQUALS:
			return Operator.EQUAL;
		case es.eucm.eadventure.common.data.chapter.conditions.VarCondition.VAR_GREATER_EQUALS_THAN:
			return Operator.GREATER_EQUAL;
		case es.eucm.eadventure.common.data.chapter.conditions.VarCondition.VAR_GREATER_THAN:
			return Operator.GREATER;
		case es.eucm.eadventure.common.data.chapter.conditions.VarCondition.VAR_LESS_EQUALS_THAN:
			return Operator.LESS_EQUAL;
		case es.eucm.eadventure.common.data.chapter.conditions.VarCondition.VAR_LESS_THAN:
			return Operator.LESS;
		}
		return Operator.EQUAL;
	}

}
