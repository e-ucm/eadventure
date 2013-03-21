package ead.converter.subconverters.conditions;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.elements.conditions.OperationCond;
import ead.common.model.elements.conditions.enums.Comparator;
import ead.common.model.elements.operations.EAdField;
import ead.converter.ModelQuerier;
import es.eucm.eadventure.common.data.chapter.conditions.VarCondition;

@Singleton
public class VarConditionConverter {

	private ModelQuerier modelQuerier;

	@Inject
	public VarConditionConverter(ModelQuerier modelQuerier) {
		this.modelQuerier = modelQuerier;
	}

	public OperationCond convert(VarCondition oldObject) {
		Comparator op = getOperator(oldObject.getState());
		EAdField<Integer> var = modelQuerier.getVariable(oldObject.getId());
		OperationCond condition = new OperationCond(var, oldObject.getValue(),
				op);
		return condition;
	}

	private Comparator getOperator(int op) {
		switch (op) {
		case VarCondition.VAR_EQUALS:
			return Comparator.EQUAL;
		case VarCondition.VAR_GREATER_EQUALS_THAN:
			return Comparator.GREATER_EQUAL;
		case VarCondition.VAR_GREATER_THAN:
			return Comparator.GREATER;
		case VarCondition.VAR_LESS_EQUALS_THAN:
			return Comparator.LESS_EQUAL;
		case VarCondition.VAR_LESS_THAN:
			return Comparator.LESS;
		}
		return Comparator.EQUAL;
	}

}
