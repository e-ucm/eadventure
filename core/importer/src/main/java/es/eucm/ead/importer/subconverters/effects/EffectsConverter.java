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
import es.eucm.ead.importer.*;
import es.eucm.ead.model.elements.BasicElement;
import es.eucm.ead.model.elements.EAdCondition;
import es.eucm.ead.model.elements.EAdEffect;
import es.eucm.ead.model.elements.conditions.EmptyCond;
import es.eucm.ead.model.elements.effects.ChangeSceneEf;
import es.eucm.ead.model.elements.effects.EmptyEffect;
import es.eucm.ead.model.elements.effects.sceneelements.ChangeColorEf;
import es.eucm.ead.model.elements.effects.variables.ChangeFieldEf;
import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.elements.operations.BasicField;
import es.eucm.ead.model.elements.operations.EAdField;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.importer.resources.ResourcesConverter;
import es.eucm.ead.importer.subconverters.conditions.ConditionsConverter;
import es.eucm.ead.importer.subconverters.effects.variables.*;
import es.eucm.eadventure.common.data.chapter.effects.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Singleton
public class EffectsConverter {

	private static final Logger logger = LoggerFactory
			.getLogger("EffectsConverter");
	private ResourcesConverter resourcesConverter;

	private ModelQuerier modelQuerier;

	private EAdElementsCache elementsCache;

	private Map<Class<?>, EffectConverter<?>> converters;

	private ConditionsConverter conditionConverter;

	private StringsConverter stringsConverter;

	private UtilsConverter utilsConverter;

	private static EAdField<Boolean> ghostEffectsVisible = new BasicField<Boolean>(
			new BasicElement(AdventureConverter.EFFECTS_GHOST_ID),
			SceneElement.VAR_VISIBLE);
	public static ChangeFieldEf showGhostEffects = new ChangeFieldEf(
			ghostEffectsVisible, EmptyCond.TRUE);
	public static ChangeFieldEf hideGhostEffects = new ChangeFieldEf(
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
				modelQuerier));
		ChangeSceneConverter changeSceneConverter = new ChangeSceneConverter();
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
	public List<EAdEffect> convert(AbstractEffect e) {
		EffectConverter converter = converters.get(e.getClass());
		if (converter == null) {
			logger.warn("No effect converter for {}", e.getClass());
		} else {
			List<EAdEffect> effects = converter.convert(e);
			if (e.getConditions() != null) {
				EAdCondition cond = conditionConverter.convert(e
						.getConditions());
				effects.get(0).setCondition(cond);
			}
			for (EAdEffect effect : effects) {
				// Effects are always executed (conditions may prevent its
				// execution later)
				effect.setNextEffectsAlways(true);
			}
			return effects;
		}
		return Collections.emptyList();
	}

	public interface EffectConverter<T extends Effect> {
		List<EAdEffect> convert(T e);
	}

	/**
	 * Returns a list of effects. This effects are already concatenated one to
	 * the other, so most of cases you'll only need to use the first element of
	 * the list (For example, to add it to scene element with an addBehavior
	 * method)
	 * 
	 * You'll also use the last effect of the list when you need to add more
	 * affects AFTER all the effects on the list has been executed
	 * 
	 * @param ef
	 * @return
	 */
	public EAdList<EAdEffect> convert(Effects ef) {
		EAdList<EAdEffect> effects = new EAdList<EAdEffect>();
		EAdEffect effect = null;
		for (AbstractEffect e : ef.getEffects()) {
			List<EAdEffect> nextEffects = convert(e);
			if (nextEffects.size() > 0) {
				effects.addAll(nextEffects);
				if (effect != null) {
					EAdEffect nextEffect = simplifyEffects(nextEffects).get(0);
					effect.getNextEffects().add(nextEffect);
				}
				effect = nextEffects.get(nextEffects.size() - 1);
			}
		}

		boolean nextShow = false;
		int i = 0;
		for (EAdEffect e : effects) {
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

	private List<EAdEffect> simplifyEffects(List<EAdEffect> nextEffects) {
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

		ArrayList<EAdEffect> list = new ArrayList<EAdEffect>();

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
							EAdEffect e = nextEffects.get(j);
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

	private boolean isNotQueueable(EAdEffect e) {
		return e.getSimultaneousEffects().size() == 0
				&& e.getNextEffects().size() == 1
				&& (e instanceof ChangeFieldEf || e instanceof ChangeColorEf);
	}
}
