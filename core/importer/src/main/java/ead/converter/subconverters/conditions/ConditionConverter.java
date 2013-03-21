package ead.converter.subconverters.conditions;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.conditions.ANDCond;
import ead.common.model.elements.conditions.EmptyCond;
import ead.common.model.elements.conditions.ORCond;
import ead.common.model.elements.conditions.OperationCond;
import ead.common.model.elements.operations.EAdField;
import ead.converter.ModelQuerier;
import es.eucm.eadventure.common.data.chapter.conditions.Condition;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.conditions.FlagCondition;
import es.eucm.eadventure.common.data.chapter.conditions.VarCondition;

@Singleton
public class ConditionConverter {

	private FlagConditionConverter flagConditionConverter;
	private VarConditionConverter varConditionConverter;
	private ModelQuerier modelQuerier;

	/**
	 * A list with the fields contained by the last condition converted
	 */
	private List<EAdField<?>> fieldsInLastCond;

	@Inject
	public ConditionConverter(FlagConditionConverter flagConditionConverter,
			VarConditionConverter varConditionConverter,
			ModelQuerier modelQuerier) {
		this.flagConditionConverter = flagConditionConverter;
		this.varConditionConverter = varConditionConverter;
		this.modelQuerier = modelQuerier;
		this.fieldsInLastCond = new ArrayList<EAdField<?>>();
		modelQuerier.setConditionConverter(this);
	}

	public EAdCondition convert(Conditions oldObject) {
		fieldsInLastCond.clear();
		ANDCond newCondition = new ANDCond();

		if (oldObject.getSimpleConditions().size() == 0
				&& oldObject.getEitherConditionsBlockCount() == 0) {
			return EmptyCond.TRUE;
		}

		for (Condition c : oldObject.getSimpleConditions()) {
			EAdCondition newC = getSimpleCondition(c);
			if (newC != null) {
				newCondition.addCondition(newC);
			}

		}

		for (int i = 0; i < oldObject.getEitherConditionsBlockCount(); i++) {
			ORCond orCondition = new ORCond();
			Conditions conditions = oldObject.getEitherBlock(i);
			for (Condition c : conditions.getSimpleConditions()) {
				EAdCondition cond = this.getSimpleCondition(c);
				if (cond != null) {
					orCondition.addCondition(cond);
				}
			}
			if (orCondition.getConditions().size() > 0) {
				newCondition.addCondition(orCondition);
			}
		}

		return newCondition;
	}

	private EAdCondition getSimpleCondition(Condition c) {
		if (c.getType() == Condition.FLAG_CONDITION) {
			OperationCond cond = flagConditionConverter
					.convert((FlagCondition) c);
			fieldsInLastCond.add((EAdField<?>) cond.getOp1());
			return cond;
		} else if (c.getType() == Condition.VAR_CONDITION) {
			OperationCond cond = varConditionConverter
					.convert((VarCondition) c);
			fieldsInLastCond.add((EAdField<?>) cond.getOp1());
			return cond;
		} else if (c.getType() == Condition.GLOBAL_STATE_CONDITION) {
			return modelQuerier.getGlobalState(c.getId());
		}
		return null;
	}

	public List<EAdField<?>> getFieldsLastCondition() {
		return fieldsInLastCond;
	}

}
