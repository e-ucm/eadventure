package es.eucm.eadventure.common.impl.importer.subimporters.effects;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.AbstractEffect;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.elements.EAdCondition;

public abstract class EffectImporter<OldEffect extends AbstractEffect, NewEffect extends EAdEffect> implements EAdElementImporter<OldEffect, NewEffect>{
	
	private EAdElementImporter<Conditions, EAdCondition> conditionImporter;
	
	public EffectImporter(EAdElementImporter<Conditions, EAdCondition> conditionImporter ){
		this.conditionImporter = conditionImporter;
	}
	
	protected void importConditions( OldEffect oldEffect, NewEffect newEffect ){
		EAdCondition condition = conditionImporter.init( oldEffect.getConditions() );
		condition = conditionImporter.convert( oldEffect.getConditions(), condition );
		if ( condition != null )
			newEffect.setCondition(condition);
	}

}
