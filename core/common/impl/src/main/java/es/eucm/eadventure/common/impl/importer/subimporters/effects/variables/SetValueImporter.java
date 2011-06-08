package es.eucm.eadventure.common.impl.importer.subimporters.effects.variables;

import com.google.inject.Inject;

import es.eucm.eadventure.common.Importer;
import es.eucm.eadventure.common.data.chapter.conditions.Condition;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.SetValueEffect;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.impl.importer.subimporters.effects.EffectImporter;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeVarValueEffect;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.variables.EAdVar;
import es.eucm.eadventure.common.model.variables.impl.operations.LiteralExpressionOperation;

public class SetValueImporter extends
		EffectImporter<SetValueEffect, EAdChangeVarValueEffect> {

	private EAdElementFactory factory;

	private static int ID_GENERATOR = 0;

	@Inject
	public SetValueImporter(
			Importer<Conditions, EAdCondition> conditionImporter,
			EAdElementFactory factory) {
		super(conditionImporter);
		this.factory = factory;
	}

	@Override
	public EAdChangeVarValueEffect convert(SetValueEffect oldObject) {
		EAdVar<?> var = factory.getVarByOldId(oldObject.getTargetId(),
				Condition.VAR_CONDITION);
		LiteralExpressionOperation op = new LiteralExpressionOperation(
				"literalExpression", oldObject.getValue() + "", var);
		EAdChangeVarValueEffect effect = new EAdChangeVarValueEffect(
				"changeVarValueFromSet" + ID_GENERATOR++, var, op);
		super.importConditions(oldObject, effect);
		return effect;
	}

}
