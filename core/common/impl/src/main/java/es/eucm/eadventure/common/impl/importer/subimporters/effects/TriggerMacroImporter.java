package es.eucm.eadventure.common.impl.importer.subimporters.effects;

import com.google.inject.Inject;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.MacroReferenceEffect;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.EAdMacro;
import es.eucm.eadventure.common.model.effects.impl.EAdTriggerMacro;
import es.eucm.eadventure.common.model.elements.EAdCondition;

public class TriggerMacroImporter extends EffectImporter<MacroReferenceEffect, EAdEffect>{
	
	private EAdElementFactory factory;
	
	@Inject
	public TriggerMacroImporter(
			EAdElementImporter<Conditions, EAdCondition> conditionImporter,
			EAdElementFactory factory) {
		super(conditionImporter);
		this.factory = factory;
	}

	@Override
	public EAdEffect init(MacroReferenceEffect oldObject) {
		return new EAdTriggerMacro("triggerMacro" + oldObject.getTargetId());
	}

	@Override
	public EAdEffect convert(MacroReferenceEffect oldObject, Object object) {
		EAdTriggerMacro effect = (EAdTriggerMacro) object;
		
		importConditions(oldObject, effect);
		effect.setMacro((EAdMacro) factory.getElementById(oldObject.getTargetId()));
		
		return effect;
	}

}
