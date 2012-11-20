/**
 * eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the
 *    <e-UCM> research group.
 *
 *    Copyright 2005-2010 <e-UCM> research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    <e-UCM> is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure, version 2.0
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */

package ead.importer.subimporters.effects;

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

import ead.importer.EAdElementImporter;
import ead.importer.interfaces.EffectsImporterFactory;
import ead.importer.subimporters.effects.texts.ShowTextEffectImporter;
import ead.importer.subimporters.effects.texts.SpeakCharEffectImporter;
import ead.importer.subimporters.effects.texts.SpeakPlayerEffectImporter;
import ead.importer.subimporters.effects.variables.ActivateFlagImporter;
import ead.importer.subimporters.effects.variables.DeactivateFlagImporter;
import ead.importer.subimporters.effects.variables.DecrementVarImporter;
import ead.importer.subimporters.effects.variables.IncrementVarImporter;
import ead.importer.subimporters.effects.variables.SetValueImporter;
import ead.tools.MapProvider;
import es.eucm.eadventure.common.data.chapter.effects.ActivateEffect;
import es.eucm.eadventure.common.data.chapter.effects.CancelActionEffect;
import es.eucm.eadventure.common.data.chapter.effects.ConsumeObjectEffect;
import es.eucm.eadventure.common.data.chapter.effects.DeactivateEffect;
import es.eucm.eadventure.common.data.chapter.effects.DecrementVarEffect;
import es.eucm.eadventure.common.data.chapter.effects.GenerateObjectEffect;
import es.eucm.eadventure.common.data.chapter.effects.HighlightItemEffect;
import es.eucm.eadventure.common.data.chapter.effects.IncrementVarEffect;
import es.eucm.eadventure.common.data.chapter.effects.MacroReferenceEffect;
import es.eucm.eadventure.common.data.chapter.effects.MoveNPCEffect;
import es.eucm.eadventure.common.data.chapter.effects.MoveObjectEffect;
import es.eucm.eadventure.common.data.chapter.effects.MovePlayerEffect;
import es.eucm.eadventure.common.data.chapter.effects.PlayAnimationEffect;
import es.eucm.eadventure.common.data.chapter.effects.PlaySoundEffect;
import es.eucm.eadventure.common.data.chapter.effects.RandomEffect;
import es.eucm.eadventure.common.data.chapter.effects.SetValueEffect;
import es.eucm.eadventure.common.data.chapter.effects.ShowTextEffect;
import es.eucm.eadventure.common.data.chapter.effects.SpeakCharEffect;
import es.eucm.eadventure.common.data.chapter.effects.SpeakPlayerEffect;
import es.eucm.eadventure.common.data.chapter.effects.TriggerBookEffect;
import es.eucm.eadventure.common.data.chapter.effects.TriggerConversationEffect;
import es.eucm.eadventure.common.data.chapter.effects.TriggerCutsceneEffect;
import es.eucm.eadventure.common.data.chapter.effects.TriggerLastSceneEffect;
import es.eucm.eadventure.common.data.chapter.effects.TriggerSceneEffect;
import es.eucm.eadventure.common.data.chapter.effects.WaitTimeEffect;

public class EffectsImporterModule extends AbstractModule implements
		MapProvider<Class<?>, Class<? extends EAdElementImporter<?, ?>>> {

	private Map<Class<?>, Class<? extends EAdElementImporter<?, ?>>> factoryMap;

	@Inject
	public EffectsImporterModule() {
		factoryMap = new LinkedHashMap<Class<?>, Class<? extends EAdElementImporter<?, ?>>>();
		factoryMap.put(ShowTextEffect.class, ShowTextEffectImporter.class);
		factoryMap.put(DeactivateEffect.class, DeactivateFlagImporter.class);
		factoryMap.put(ActivateEffect.class, ActivateFlagImporter.class);
		factoryMap.put(SetValueEffect.class, SetValueImporter.class);
		factoryMap.put(IncrementVarEffect.class, IncrementVarImporter.class);
		factoryMap.put(DecrementVarEffect.class, DecrementVarImporter.class);
		factoryMap.put(RandomEffect.class, RandomEffectImporter.class);
		factoryMap.put(TriggerConversationEffect.class,
				TriggerConversationImporter.class);
		factoryMap.put(TriggerCutsceneEffect.class,
				TriggerCutsceneImporter.class);
		factoryMap.put(TriggerSceneEffect.class, TriggerSceneImporter.class);
		factoryMap.put(TriggerLastSceneEffect.class,
				TriggerPreviousSceneImporter.class);
		factoryMap
				.put(SpeakPlayerEffect.class, SpeakPlayerEffectImporter.class);
		factoryMap.put(SpeakCharEffect.class, SpeakCharEffectImporter.class);
		factoryMap.put(MacroReferenceEffect.class, TriggerMacroImporter.class);
		factoryMap.put(CancelActionEffect.class,
				CancelActionEffectImporter.class);
		factoryMap.put(PlaySoundEffect.class, PlaySoundEffectImporter.class);
		factoryMap.put(ConsumeObjectEffect.class,
				ConsumeObjectEffectImporter.class);
		factoryMap.put(GenerateObjectEffect.class,
				GenerateObjectEffectImporter.class);
		factoryMap.put(MoveNPCEffect.class, MoveNPCEffectImporter.class);
		factoryMap.put(MoveObjectEffect.class, MoveObjectEffectImporter.class);
		factoryMap.put(MovePlayerEffect.class, MovePlayerEffectImporter.class);
		factoryMap.put(WaitTimeEffect.class, WaitEffectImporter.class);
		factoryMap
				.put(HighlightItemEffect.class, HighlightEffectImporter.class);
		factoryMap.put(PlayAnimationEffect.class,
				PlayAnimationEffectImporter.class);
		factoryMap
				.put(TriggerBookEffect.class, TriggerBookEffectImporter.class);
	}

	@Override
	public Map<Class<?>, Class<? extends EAdElementImporter<?, ?>>> getMap() {
		return factoryMap;
	}

	@Override
	protected void configure() {
		bind(
				new TypeLiteral<MapProvider<Class<?>, Class<? extends EAdElementImporter<?, ?>>>>() {
				}).annotatedWith(Names.named("effects")).to(
				EffectsImporterModule.class);
		bind(EffectsImporterFactory.class).to(EffectsImporterFactoryImpl.class);
	}

}
