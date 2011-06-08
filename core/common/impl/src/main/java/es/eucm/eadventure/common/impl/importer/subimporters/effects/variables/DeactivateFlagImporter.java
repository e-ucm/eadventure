package es.eucm.eadventure.common.impl.importer.subimporters.effects.variables;

import com.google.inject.Inject;

import es.eucm.eadventure.common.Importer;
import es.eucm.eadventure.common.data.chapter.conditions.Condition;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.DeactivateEffect;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.impl.importer.subimporters.effects.EffectImporter;
import es.eucm.eadventure.common.model.conditions.impl.EmptyCondition;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeVarValueEffect;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.variables.impl.operations.BooleanOperation;
import es.eucm.eadventure.common.model.variables.impl.vars.BooleanVar;

public class DeactivateFlagImporter extends EffectImporter<DeactivateEffect, EAdChangeVarValueEffect>{
	
	private EAdElementFactory factory;
	
	private static int ID_GENERATOR = 0;

	@Inject
	public DeactivateFlagImporter(
			Importer<Conditions, EAdCondition> conditionImporter, EAdElementFactory factory) {
		super(conditionImporter);
		this.factory = factory;
	}

	@Override
	public EAdChangeVarValueEffect convert(DeactivateEffect oldObject) {
		BooleanVar var = (BooleanVar) factory.getVarByOldId(oldObject.getTargetId(), Condition.FLAG_CONDITION);
		BooleanOperation op = new BooleanOperation( "boolOperation" );
		op.setCondition(EmptyCondition.FALSE_EMPTY_CONDITION);
		
		EAdChangeVarValueEffect changeVar = new EAdChangeVarValueEffect( "changeVarValue" + ID_GENERATOR++, var, op );
		super.importConditions(oldObject, changeVar);
		
		return changeVar;
	}

}
