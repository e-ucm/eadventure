package ead.converter.subconverters.conditions;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.elements.conditions.OperationCond;
import ead.common.model.elements.operations.EAdField;
import ead.converter.ModelQuerier;
import es.eucm.eadventure.common.data.chapter.conditions.FlagCondition;

@Singleton
public class FlagConditionConverter {

	private ModelQuerier modelQuerier;

	@Inject
	public FlagConditionConverter(ModelQuerier modelQuerier) {
		this.modelQuerier = modelQuerier;
	}

	public OperationCond convert(FlagCondition oldObject) {
		EAdField<Boolean> var = modelQuerier.getFlag(oldObject.getId());
		OperationCond f = new OperationCond(var);
		if (oldObject.isActiveState())
			f.setOp2(OperationCond.TRUE);
		else
			f.setOp2(OperationCond.FALSE);
		return f;
	}

}
