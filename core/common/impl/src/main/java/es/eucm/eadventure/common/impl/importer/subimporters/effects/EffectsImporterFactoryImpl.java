package es.eucm.eadventure.common.impl.importer.subimporters.effects;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.effects.Effect;
import es.eucm.eadventure.common.impl.importer.interfaces.EffectsImporterFactory;
import es.eucm.eadventure.common.interfaces.AbstractFactory;
import es.eucm.eadventure.common.interfaces.MapProvider;
import es.eucm.eadventure.common.model.effects.EAdEffect;

public class EffectsImporterFactoryImpl extends
		AbstractFactory<Class<? extends EAdElementImporter<?, ?>>> implements
		EffectsImporterFactory {

	private Injector injector;

	@Inject
	public EffectsImporterFactoryImpl(
			@Named("effects") MapProvider<Class<?>, Class<? extends EAdElementImporter<?, ?>>> map,
			Injector injector) {
		super(map);
		this.injector = injector;
	}

	@Override
	public EAdElementImporter<?, ?> getImporter(Class<?> clazz) {
		Class<? extends EAdElementImporter<?, ?>> importerClass = get(clazz);
		if (importerClass != null) {
			return (EAdElementImporter<?, ?>) injector.getInstance(importerClass);
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public EAdEffect getEffect(Effect effect) {
		EAdElementImporter importer = getImporter(effect.getClass());
		if (importer != null) {
			EAdEffect newEffect = (EAdEffect) importer.init(effect);
			return (EAdEffect) importer.convert(effect, newEffect);
		} else
			return null;
	}

}
