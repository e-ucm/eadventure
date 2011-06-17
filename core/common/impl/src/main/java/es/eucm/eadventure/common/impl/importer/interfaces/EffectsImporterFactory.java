package es.eucm.eadventure.common.impl.importer.interfaces;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.effects.Effect;
import es.eucm.eadventure.common.interfaces.Factory;
import es.eucm.eadventure.common.model.effects.EAdEffect;

/**
 * Factory for effects importers
 * 
 */
public interface EffectsImporterFactory extends Factory<Class<? extends EAdElementImporter<?, ?>>>{
	
	EAdElementImporter<?, ?> getImporter( Class<?> clazz );
	
	EAdEffect getEffect( Effect effect );

}
