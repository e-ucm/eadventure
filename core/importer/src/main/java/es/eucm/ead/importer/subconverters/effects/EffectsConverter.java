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

package es.eucm.ead.importer.subconverters.effects;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import es.eucm.ead.importer.AdventureConverter;
import es.eucm.ead.importer.EAdElementsCache;
import es.eucm.ead.importer.ModelQuerier;
import es.eucm.ead.importer.StringsConverter;
import es.eucm.ead.importer.UtilsConverter;
import es.eucm.ead.importer.resources.ResourcesConverter;
import es.eucm.ead.importer.subconverters.conditions.ConditionsConverter;
import es.eucm.ead.importer.subconverters.effects.variables.ActivateFlagConverter;
import es.eucm.ead.importer.subconverters.effects.variables.DeactivateFlagConverter;
import es.eucm.ead.importer.subconverters.effects.variables.DecrementVarConverter;
import es.eucm.ead.importer.subconverters.effects.variables.IncrementVarConverter;
import es.eucm.ead.importer.subconverters.effects.variables.RandomEffectConverter;
import es.eucm.ead.importer.subconverters.effects.variables.SetValueConverter;
import es.eucm.ead.model.elements.BasicElement;
import es.eucm.ead.model.elements.conditions.Condition;
import es.eucm.ead.model.elements.conditions.EmptyCond;
import es.eucm.ead.model.elements.effects.ChangeSceneEf;
import es.eucm.ead.model.elements.effects.Effect;
import es.eucm.ead.model.elements.effects.EmptyEffect;
import es.eucm.ead.model.elements.effects.sceneelements.ChangeColorEf;
import es.eucm.ead.model.elements.effects.variables.ChangeFieldEf;
import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.elements.operations.ElementField;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.eadventure.common.data.chapter.effects.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class EffectsConverter {

	static private Logger logger = LoggerFactory
			.getLogger(EffectsConverter.class);
	private ResourcesConverter resourcesConverter;

	private ModelQuerier modelQuerier;

	private EAdElementsCache elementsCache;

	private Map<Class<?>, EffectConverter<?>> converters;

	private ConditionsConverter conditionConverter;

	private StringsConverter stringsConverter;

	private UtilsConverter utilsConverter;

	private ElementField ghostEffectsVisible = new ElementField(
			new BasicElement(AdventureConverter.EFFECTS_GHOST_ID),
			SceneElement.VAR_VISIBLE);
	public ChangeFieldEf showGhostEffects = new ChangeFieldEf(
			ghostEffectsVisible, EmptyCond.TRUE);
	public ChangeFieldEf hideGhostEffects = new ChangeFieldEf(
			ghostEffectsVisible, EmptyCond.FALSE);

	@Inject
	public EffectsConverter(ModelQuerier modelQuerier,
			ConditionsConverter conditionsConverter,
			EAdElementsCache elementsCache, StringsConverter stringsConverter,
			UtilsConverter utilsConverter, ResourcesConverter resourcesConverter) {
		this.modelQuerier = modelQuerier;
		this.conditionConverter = conditionsConverter;
		this.elementsCache = elementsCache;
		this.utilsConverter = utilsConverter;
		this.stringsConverter = stringsConverter;
		this.resourcesConverter = resourcesConverter;
		modelQuerier.setEffectsConverter(this);
		setConverters();
	}

	private void setConverters() {
		converters = new HashMap<Class<?>, EffectConverter<?>>();
		converters.put(MacroReferenceEffect.class, new TriggerMacroConverter(
				modelQuerier, elementsCache));
		ChangeSceneConverter changeSceneConverter = new ChangeSceneConverter(
				this, modelQuerier);
		converters.put(TriggerSceneEffect.class, changeSceneConverter);
		converters.put(TriggerCutsceneEffect.class, changeSceneConverter);
		converters.put(TriggerLastSceneEffect.class, changeSceneConverter);
		converters.put(DeactivateEffect.class, new DeactivateFlagConverter(
				modelQuerier));
		converters.put(ActivateEffect.class, new ActivateFlagConverter(
				modelQuerier));
		converters.put(SetValueEffect.class,
				new SetValueConverter(modelQuerier));
		converters.put(IncrementVarEffect.class, new IncrementVarConverter(
				modelQuerier));
		converters.put(DecrementVarEffect.class, new DecrementVarConverter(
				modelQuerier));
		MoveEffectConverter moveEffectConverter = new MoveEffectConverter(
				elementsCache);
		converters.put(MoveNPCEffect.class, moveEffectConverter);
		converters.put(MovePlayerEffect.class, moveEffectConverter);
		converters.put(MoveObjectEffect.class, moveEffectConverter);
		converters.put(WaitTimeEffect.class, new WaitConverter());
		converters.put(HighlightItemEffect.class, new HighlightItemConverter(
				elementsCache));
		converters.put(TriggerConversationEffect.class,
				new TriggerConversationConverter(modelQuerier));

		SpeakEffectConverter speakConverter = new SpeakEffectConverter(
				modelQuerier, stringsConverter, utilsConverter);
		converters.put(ShowTextEffect.class, speakConverter);
		converters.put(SpeakCharEffect.class, speakConverter);
		converters.put(SpeakPlayerEffect.class, speakConverter);
		converters.put(RandomEffect.class, new RandomEffectConverter(this));
		converters.put(PlaySoundEffect.class, new PlaySoundConverter(
				resourcesConverter));

		// factoryMap.put(CancelActionEffect.class,
		// CancelActionEffectImporter.class);
		// factoryMap.put(ConsumeObjectEffect.class,
		// ConsumeObjectEffectImporter.class);
		// factoryMap.put(GenerateObjectEffect.class,
		// GenerateObjectEffectImporter.class);

		// factoryMap.put(MoveObjectEffect.class,
		// MoveObjectEffectImporter.class);
		// factoryMap.put(MovePlayerEffect.class,
		// MovePlayerEffectImporter.class);

		// factoryMap.put(PlayAnimationEffect.class,
		// PlayAnimationEffectImporter.class);
		// factoryMap
		// .put(TriggerBookEffect.class, TriggerBookEffectImporter.class);

	}

	@SuppressWarnings( { "rawtypes", "unchecked" })
	public List<Effect> convert(AbstractEffect e) {
		EffectConverter converter = converters.get(e.getClass());
		if (converter == null) {
			logger.warn("No effect converter for {}", e.getClass());
		} else {
			List<Effect> effects = converter.convert(e);
			for (Effect effect : effects) {
				if (effect.getId() == null) {
					effect.setId("ef#"
							+ BasicElement.classToString(effect.getClass())
							+ "$"
							+ elementsCache.newReference(BasicElement
									.classToString(effect.getClass())));
				}
			}
			if (e.getConditions() != null) {
				Condition cond = conditionConverter.convert(e.getConditions());
				effects.get(0).setCondition(cond);
			}
			for (es.eucm.ead.model.elements.effects.Effect effect : effects) {
				// Effects are always executed (conditions may prevent its
				// execution later)
				effect.setNextEffectsAlways(true);
			}
			return effects;
		}
		return Collections.emptyList();
	}

	public interface EffectConverter<T extends es.eucm.eadventure.common.data.chapter.effects.Effect> {
		List<Effect> convert(T e);
	}

	/**
	 * Returns a list of effects. This effects are already concatenated one to
	 * the other, so most of cases you'll only need to use the first element of
	 * the list (For example, to add it to scene element with an addBehavior
	 * method)
	 * <p/>
	 * You'll also use the last effect of the list when you need to add more
	 * affects AFTER all the effects on the list has been executed
	 *
	 * @param ef
	 * @return
	 */
	public EAdList<Effect> convert(Effects ef) {
		EAdList<Effect> effects = new EAdList<Effect>();
		Effect effect = null;
		for (AbstractEffect e : ef.getEffects()) {
			List<Effect> nextEffects = convert(e);
			if (nextEffects.size() > 0) {
				effects.addAll(nextEffects);
				if (effect != null) {
					Effect nextEffect = simplifyEffects(nextEffects).get(0);
					effect.addNextEffect(nextEffect);
				}
				effect = nextEffects.get(nextEffects.size() - 1);
			}
		}

		boolean nextShow = false;
		int i = 0;
		for (Effect e : effects) {
			if (i == 0) {
				e.addSimultaneousEffect(showGhostEffects);
			}

			if (nextShow) {
				nextShow = false;
				e.addSimultaneousEffect(showGhostEffects);
			}
			if (e instanceof ChangeSceneEf) {
				nextShow = true;
				e.addNextEffect(hideGhostEffects);
			}

			if (i == effects.size() - 1) {
				e.addNextEffect(hideGhostEffects);
			}
			i++;
		}

		return effects;
	}

	private List<Effect> simplifyEffects(List<Effect> nextEffects) {
		if (nextEffects.size() < 2) {
			return nextEffects;
		}

		int i = 0;
		boolean hasNotQueue = false;
		while (!hasNotQueue && i < nextEffects.size()) {
			hasNotQueue = isNotQueueable(nextEffects.get(i++));
		}

		if (!hasNotQueue) {
			return nextEffects;
		}

		ArrayList<Effect> list = new ArrayList<Effect>();

		int index = 0;
		while (i < nextEffects.size()) {
			while (i < nextEffects.size()
					&& isNotQueueable(nextEffects.get(i++))) {
				;
				if (i - index == 0) {
					list.add(nextEffects.get(i));
				} else {
					if (i == 0 && i - index > 2) {
						EmptyEffect effect = new EmptyEffect();
						for (int j = i; j < index; j++) {
							Effect e = nextEffects.get(j);
							e.getNextEffects().clear();
							effect.addNextEffect(e);
							list.add(effect);
						}
					} else {
						for (int j = i; j < index; j++) {
							list.add(nextEffects.get(j));
						}
					}

				}
				index = i;
			}
		}

		for (int j = index; j < i; j++) {
			list.add(nextEffects.get(j));
		}
		return list;
	}

	private boolean isNotQueueable(Effect e) {
		return e.getSimultaneousEffects().size() == 0
				&& e.getNextEffects().size() == 1
				&& (e instanceof ChangeFieldEf || e instanceof ChangeColorEf);
	}
}
