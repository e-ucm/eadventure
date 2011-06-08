package es.eucm.eadventure.common.impl.importer.subimporters.effects;

import es.eucm.eadventure.common.Importer;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.AbstractEffect;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.elements.EAdCondition;

public abstract class EffectImporter<OldEffect extends AbstractEffect, NewEffect extends EAdEffect> implements Importer<OldEffect, NewEffect>{
	
	private Importer<Conditions, EAdCondition> conditionImporter;
	
	public EffectImporter(Importer<Conditions, EAdCondition> conditionImporter ){
		this.conditionImporter = conditionImporter;
	}

	@Override
	public boolean equals(OldEffect oldObject, NewEffect newObject) {
		// TODO Auto-generated method stub
		return false;
	}
	
	protected void importConditions( OldEffect oldEffect, NewEffect newEffect ){
		EAdCondition condition = conditionImporter.convert( oldEffect.getConditions() );
		if ( condition != null )
			newEffect.setCondition(condition);
	}

}
