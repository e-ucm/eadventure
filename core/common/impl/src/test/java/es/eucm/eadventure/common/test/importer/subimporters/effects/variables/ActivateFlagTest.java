package es.eucm.eadventure.common.test.importer.subimporters.effects.variables;

import es.eucm.eadventure.common.data.chapter.effects.ActivateEffect;
import es.eucm.eadventure.common.impl.importer.subimporters.effects.variables.ActivateFlagImporter;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeFieldValueEffect;
import es.eucm.eadventure.common.model.variables.impl.operations.BooleanOperation;
import es.eucm.eadventure.common.test.importer.subimporters.effects.EffectTest;

public class ActivateFlagTest extends
		EffectTest<ActivateEffect, EAdChangeFieldValueEffect> {

	public ActivateFlagTest() {
		super(ActivateFlagImporter.class);
	}
	
	@Override
	public void addOldObjects() {
		addTestObject(new ActivateEffect("flag1"));
		addTestObject(new ActivateEffect("flag1"));
		addTestObject(new ActivateEffect("flag2"));
		addTestObject(new ActivateEffect("anotherFlag"));
		addTestObject(new ActivateEffect("ñºº"));
		
	}

	@Override
	public boolean equals(ActivateEffect oldObject,
			EAdChangeFieldValueEffect newObject) {
		boolean ok = super.equals(oldObject, newObject);
		ok = newObject.getOperation().equals(BooleanOperation.TRUE_OP) && ok;
		ok = newObject.getVars().size() == 0;		
		return ok;
	}

}
