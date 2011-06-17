package es.eucm.eadventure.common.impl.importer.subimporters.conditions;

import com.google.inject.Inject;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.conditions.Condition;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.conditions.GlobalStateCondition;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.model.conditions.impl.ANDCondition;
import es.eucm.eadventure.common.model.conditions.impl.FlagCondition;
import es.eucm.eadventure.common.model.conditions.impl.ORCondition;
import es.eucm.eadventure.common.model.conditions.impl.VarCondition;
import es.eucm.eadventure.common.model.elements.EAdCondition;

public class ConditionsImporter implements EAdElementImporter<Conditions, EAdCondition> {

	private EAdElementImporter<es.eucm.eadventure.common.data.chapter.conditions.FlagCondition, FlagCondition> flagConditionImporter;
	private EAdElementImporter<es.eucm.eadventure.common.data.chapter.conditions.VarCondition, VarCondition> varConditionImporter;
	private EAdElementFactory factory;

	@Inject
	public ConditionsImporter(
			EAdElementImporter<es.eucm.eadventure.common.data.chapter.conditions.FlagCondition, FlagCondition> flagConditionImporter,
			EAdElementImporter<es.eucm.eadventure.common.data.chapter.conditions.VarCondition, VarCondition> varConditionImporter,
			EAdElementFactory factory) {
		this.factory = factory;
		this.flagConditionImporter = flagConditionImporter;
		this.varConditionImporter = varConditionImporter;
	}

	@Override
	public EAdCondition init(Conditions oldObject) {
		return new ANDCondition();
	}
	
	@Override
	public EAdCondition convert(Conditions oldObject, Object object) {
		ANDCondition newCondition = (ANDCondition) object;;
		for (Condition c : oldObject.getSimpleConditions()) {
			EAdCondition newC = getSimpleCondition(c);
			if (newC != null) {
				newCondition.addCondition(newC);
			}

		}

		for (int i = 0; i < oldObject.getEitherConditionsBlockCount(); i++) {
			ORCondition orCondition = new ORCondition();
			Conditions conditions = oldObject.getEitherBlock(i);
			for (Condition c : conditions.getSimpleConditions()) {
				EAdCondition cond = this.getSimpleCondition(c);
				if (cond != null) {
					orCondition.addCondition(cond);
				}
			}
			if (orCondition.getConds().size() > 0) {
				newCondition.addCondition(orCondition);
			}
		}

		return newCondition;
	}

	private EAdCondition getSimpleCondition(Condition c) {
		if (c.getType() == Condition.FLAG_CONDITION) {
			EAdCondition condition = flagConditionImporter.init((es.eucm.eadventure.common.data.chapter.conditions.FlagCondition) c);
			return flagConditionImporter.convert((es.eucm.eadventure.common.data.chapter.conditions.FlagCondition) c, condition);
		} else if (c.getType() == Condition.VAR_CONDITION) {
			EAdCondition condition = varConditionImporter
			.init((es.eucm.eadventure.common.data.chapter.conditions.VarCondition) c);
			return varConditionImporter
					.convert((es.eucm.eadventure.common.data.chapter.conditions.VarCondition) c, condition);
		} else if (c.getType() == Condition.GLOBAL_STATE_CONDITION) {
			return factory.getGlobalStateCondition(((GlobalStateCondition) c).getId());
		}
		return null;
	}

}
