package es.eucm.eadventure.common.impl.importer.subimporters.effects.variables;

import com.google.inject.Inject;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.conditions.Condition;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.DecrementVarEffect;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.impl.importer.subimporters.effects.EffectImporter;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeVarValueEffect;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.variables.EAdVar;
import es.eucm.eadventure.common.model.variables.impl.operations.LiteralExpressionOperation;

public class DecrementVarImporter extends
		EffectImporter<DecrementVarEffect, EAdChangeVarValueEffect> {
	private EAdElementFactory factory;

	private static int ID_GENERATOR = 0;

	@Inject
	public DecrementVarImporter(
			EAdElementImporter<Conditions, EAdCondition> conditionImporter,
			EAdElementFactory factory) {
		super(conditionImporter);
		this.factory = factory;
	}

	@Override
	public EAdChangeVarValueEffect init(DecrementVarEffect oldObject) {
		EAdVar<?> var = factory.getVarByOldId(oldObject.getTargetId(),
				Condition.VAR_CONDITION);

		LiteralExpressionOperation op = new LiteralExpressionOperation(
				"literalExpression", "[0] - "
						+ oldObject.getDecrement(), var);
		EAdChangeVarValueEffect effect = new EAdChangeVarValueEffect(
				"changeVarValueFromIncrement" + ID_GENERATOR++, var, op);
		super.importConditions(oldObject, effect);
		return effect;
	}
	
	@Override
	public EAdChangeVarValueEffect convert(DecrementVarEffect oldObject, Object object) {
	return (EAdChangeVarValueEffect) object;
	}
}
