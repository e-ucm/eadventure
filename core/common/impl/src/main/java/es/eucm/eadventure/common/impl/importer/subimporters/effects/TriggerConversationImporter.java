package es.eucm.eadventure.common.impl.importer.subimporters.effects;

import com.google.inject.Inject;

import es.eucm.eadventure.common.Importer;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.TriggerConversationEffect;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.EAdMacro;
import es.eucm.eadventure.common.model.effects.impl.EAdMacroImpl;
import es.eucm.eadventure.common.model.effects.impl.EAdTriggerMacro;
import es.eucm.eadventure.common.model.elements.EAdCondition;

public class TriggerConversationImporter extends EffectImporter<TriggerConversationEffect, EAdEffect>{
	
	
	private EAdElementFactory factory;
	
	@Inject
	public TriggerConversationImporter(
			Importer<Conditions, EAdCondition> conditionImporter, EAdElementFactory factory) {
		super(conditionImporter);
		this.factory = factory;
	}

	@Override
	public EAdEffect convert(TriggerConversationEffect oldObject) {
		EAdMacro macro = new EAdMacroImpl("macroConversation");
		EAdTriggerMacro triggerMacro = new EAdTriggerMacro( macro );
		super.importConditions(oldObject, triggerMacro);
		EAdEffect effect = factory.getEffectForConversation(oldObject.getTargetId());
		if ( effect != null )
			macro.getEffects().add(effect);
		
		return triggerMacro;
	}

	@Override
	public boolean equals(TriggerConversationEffect oldObject,
			EAdEffect newObject) {
		// TODO Auto-generated method stub
		return false;
	}

}
