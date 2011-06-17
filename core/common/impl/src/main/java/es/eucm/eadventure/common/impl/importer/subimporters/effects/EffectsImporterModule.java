package es.eucm.eadventure.common.impl.importer.subimporters.effects;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.effects.ActivateEffect;
import es.eucm.eadventure.common.data.chapter.effects.DeactivateEffect;
import es.eucm.eadventure.common.data.chapter.effects.DecrementVarEffect;
import es.eucm.eadventure.common.data.chapter.effects.IncrementVarEffect;
import es.eucm.eadventure.common.data.chapter.effects.RandomEffect;
import es.eucm.eadventure.common.data.chapter.effects.SetValueEffect;
import es.eucm.eadventure.common.data.chapter.effects.ShowTextEffect;
import es.eucm.eadventure.common.data.chapter.effects.TriggerConversationEffect;
import es.eucm.eadventure.common.data.chapter.effects.TriggerLastSceneEffect;
import es.eucm.eadventure.common.impl.importer.interfaces.EffectsImporterFactory;
import es.eucm.eadventure.common.impl.importer.subimporters.effects.variables.ActivateFlagImporter;
import es.eucm.eadventure.common.impl.importer.subimporters.effects.variables.DeactivateFlagImporter;
import es.eucm.eadventure.common.impl.importer.subimporters.effects.variables.DecrementVarImporter;
import es.eucm.eadventure.common.impl.importer.subimporters.effects.variables.IncrementVarImporter;
import es.eucm.eadventure.common.impl.importer.subimporters.effects.variables.SetValueImporter;
import es.eucm.eadventure.common.interfaces.MapProvider;

public class EffectsImporterModule extends AbstractModule implements MapProvider<Class<?>, Class<? extends EAdElementImporter<?, ?>>>{

	private Map<Class<?>, Class<? extends EAdElementImporter<?, ?>>> factoryMap;
	
	@Inject
	public EffectsImporterModule() {
		factoryMap = new HashMap<Class<?>, Class<? extends EAdElementImporter<?, ?>>>();
		factoryMap.put(ShowTextEffect.class, ShowTextEffectImporter.class);
		factoryMap.put(DeactivateEffect.class, DeactivateFlagImporter.class);
		factoryMap.put(ActivateEffect.class, ActivateFlagImporter.class);
		factoryMap.put(SetValueEffect.class, SetValueImporter.class);
		factoryMap.put(IncrementVarEffect.class, IncrementVarImporter.class);
		factoryMap.put(DecrementVarEffect.class, DecrementVarImporter.class);
		factoryMap.put(RandomEffect.class, RandomEffectImporter.class);
		factoryMap.put(TriggerConversationEffect.class, TriggerConversationImporter.class);
		factoryMap.put(TriggerLastSceneEffect.class, TriggerPreviousSceneImporter.class);
	}
	
	@Override
	public Map<Class<?>, Class<? extends EAdElementImporter<?, ?>>> getMap() {
		return factoryMap;
	}

	@Override
	protected void configure() {
		bind(new TypeLiteral<MapProvider<Class<?>, Class<? extends EAdElementImporter<?, ?>>>>() {}).annotatedWith(Names.named("effects")).to(EffectsImporterModule.class);
		bind(EffectsImporterFactory.class).to(EffectsImporterFactoryImpl.class);
	}

	
	
}
