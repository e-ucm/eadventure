package es.eucm.eadventure.common.impl.importer.subimporters.effects;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;

import es.eucm.eadventure.common.Importer;
import es.eucm.eadventure.common.data.chapter.effects.Effect;
import es.eucm.eadventure.common.impl.importer.interfaces.EffectsImporterFactory;
import es.eucm.eadventure.common.interfaces.AbstractFactory;
import es.eucm.eadventure.common.interfaces.MapProvider;
import es.eucm.eadventure.common.model.effects.EAdEffect;

public class EffectsImporterFactoryImpl extends
		AbstractFactory<Class<? extends Importer<?, ?>>> implements
		EffectsImporterFactory {

	private Injector injector;

	@Inject
	public EffectsImporterFactoryImpl(
			@Named("effects") MapProvider<Class<?>, Class<? extends Importer<?, ?>>> map,
			Injector injector) {
		super(map);
		this.injector = injector;
	}

	@Override
	public Importer<?, ?> getImporter(Class<?> clazz) {
		Class<? extends Importer<?, ?>> importerClass = get(clazz);
		if (importerClass != null) {
			return (Importer<?, ?>) injector.getInstance(importerClass);
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public EAdEffect getEffect(Effect effect) {
		Importer importer = getImporter(effect.getClass());
		if (importer != null)
			return (EAdEffect) importer.convert(effect);
		else
			return null;
	}

}
