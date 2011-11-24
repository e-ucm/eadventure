package es.eucm.eadventure.common.impl.importer.subimporters.effects;

import com.google.inject.Inject;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.MovePlayerEffect;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdMoveActiveElement;
import es.eucm.eadventure.common.model.elements.EAdCondition;

public class MovePlayerEffectImporter extends EffectImporter<MovePlayerEffect, EAdMoveActiveElement>{

	@Inject
	public MovePlayerEffectImporter(
			EAdElementImporter<Conditions, EAdCondition> conditionImporter) {
		super(conditionImporter);
	}

	@Override
	public EAdMoveActiveElement init(MovePlayerEffect oldObject) {
		EAdMoveActiveElement effect =  new EAdMoveActiveElement();
		effect.setId("movePlayerEffect" + ID_GENERATOR++);
		return effect;
	}

	@Override
	public EAdMoveActiveElement convert(MovePlayerEffect oldObject,
			Object newElement) {
		EAdMoveActiveElement effect = super.convert(oldObject, newElement);
		effect.setTargetX(oldObject.getX());
		effect.setTargetY(oldObject.getY());
		return effect;
	}

}
