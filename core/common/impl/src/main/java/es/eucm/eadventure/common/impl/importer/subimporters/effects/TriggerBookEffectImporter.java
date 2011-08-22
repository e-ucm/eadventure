package es.eucm.eadventure.common.impl.importer.subimporters.effects;

import com.google.inject.Inject;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.TriggerBookEffect;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.model.effects.impl.EAdChangeScene;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.EAdScene;

public class TriggerBookEffectImporter extends
		EffectImporter<TriggerBookEffect, EAdChangeScene> {

	private EAdElementFactory factory;

	@Inject
	public TriggerBookEffectImporter(
			EAdElementImporter<Conditions, EAdCondition> conditionImporter,
			EAdElementFactory factory) {
		super(conditionImporter);
		this.factory = factory;
	}

	@Override
	public EAdChangeScene init(TriggerBookEffect oldObject) {
		return new EAdChangeScene("triggerBook_" + oldObject.getTargetId());
	}

	@Override
	public EAdChangeScene convert(TriggerBookEffect oldObject, Object newElement) {
		EAdChangeScene effect = super.convert(oldObject, newElement);
		EAdScene scene = (EAdScene) factory.getElementById(oldObject
				.getTargetId());
		effect.setNextScene(scene);
		return effect;

	}
}
