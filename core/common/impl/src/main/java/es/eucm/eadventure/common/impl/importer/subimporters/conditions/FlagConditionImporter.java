package es.eucm.eadventure.common.impl.importer.subimporters.conditions;

import com.google.inject.Inject;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.conditions.Condition;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.model.conditions.impl.FlagCondition;
import es.eucm.eadventure.common.model.conditions.impl.FlagCondition.Value;
import es.eucm.eadventure.common.model.variables.impl.vars.BooleanVar;

public class FlagConditionImporter
		implements
		EAdElementImporter<es.eucm.eadventure.common.data.chapter.conditions.FlagCondition, FlagCondition> {

	private EAdElementFactory factory;

	@Inject
	public FlagConditionImporter(EAdElementFactory factory) {
		this.factory = factory;
	}

	public FlagCondition init(es.eucm.eadventure.common.data.chapter.conditions.FlagCondition oldObject) {
		BooleanVar var = (BooleanVar) factory.getVarByOldId(oldObject.getId(),
				Condition.FLAG_CONDITION);
		FlagCondition f = new FlagCondition(var);
		if (oldObject.isActiveState())
			f.setValue(Value.ACTIVE);
		else
			f.setValue(Value.INACTIVE);
		return f;
	}
	
	@Override
	public FlagCondition convert(
			es.eucm.eadventure.common.data.chapter.conditions.FlagCondition oldObject, Object object) {
		return (FlagCondition) object;
	}

}
