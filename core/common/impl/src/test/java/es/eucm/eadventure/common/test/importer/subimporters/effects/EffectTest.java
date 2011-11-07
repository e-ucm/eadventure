package es.eucm.eadventure.common.test.importer.subimporters.effects;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.effects.Effect;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.test.importer.test.ImporterTestTemplate;

public abstract class EffectTest<OldEffectType extends Effect, NewEffectType extends EAdEffect>
		extends ImporterTestTemplate<OldEffectType, NewEffectType> {

	public EffectTest(
			Class<? extends EAdElementImporter<OldEffectType, NewEffectType>> importerClass) {
		super(importerClass);
	}

	@Override
	public boolean equals(OldEffectType oldObject, NewEffectType newObject) {
		return newObject.isBlocking() && newObject.isOpaque()
				&& newObject.isQueueable();
	}

}
