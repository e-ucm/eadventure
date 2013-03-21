package ead.converter.subconverters.effects;

import java.util.ArrayList;
import java.util.List;

import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.effects.InterpolationEf;
import ead.common.model.elements.effects.enums.InterpolationLoopType;
import ead.common.model.elements.effects.enums.InterpolationType;
import ead.common.model.elements.effects.sceneelements.ChangeColorEf;
import ead.common.model.elements.operations.BasicField;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.converter.EAdElementsCache;
import ead.converter.subconverters.effects.EffectsConverter.EffectConverter;
import es.eucm.eadventure.common.data.chapter.effects.HighlightItemEffect;

public class HighlightItemConverter implements
		EffectConverter<HighlightItemEffect> {

	private EAdElementsCache elementsCache;

	public HighlightItemConverter(EAdElementsCache elementsCache) {
		this.elementsCache = elementsCache;
	}

	@Override
	public List<EAdEffect> convert(HighlightItemEffect e) {
		ArrayList<EAdEffect> list = new ArrayList<EAdEffect>();

		float red = 0.0f;
		float green = 0.0f;
		float blue = 0.0f;

		switch (e.getHighlightType()) {
		case HighlightItemEffect.HIGHLIGHT_BLUE:
			blue = 1.0f;
			break;
		case HighlightItemEffect.HIGHLIGHT_GREEN:
			green = 1.0f;
			break;
		case HighlightItemEffect.HIGHLIGHT_RED:
			red = 1.0f;
			break;
		case HighlightItemEffect.HIGHLIGHT_BORDER:
			// XXX Highlight border
			red = blue = green = 0.5f;
			break;
		}

		ChangeColorEf changeColor = new ChangeColorEf(red, green, blue);
		changeColor.setSceneElement(elementsCache.get(e.getTargetId()));
		list.add(changeColor);

		if (e.isHighlightAnimated()) {
			SceneElementDef def = (SceneElementDef) elementsCache.get(e
					.getTargetId());
			BasicField<EAdSceneElement> sceneElementField = new BasicField<EAdSceneElement>(
					def, SceneElementDef.VAR_SCENE_ELEMENT);
			InterpolationEf interpolation = new InterpolationEf(
					sceneElementField, SceneElement.VAR_SCALE, 1.0f, 2.0f,
					1000, 0, InterpolationLoopType.REVERSE, 1,
					InterpolationType.LINEAR);
			InterpolationEf interpolation2 = new InterpolationEf(
					sceneElementField, SceneElement.VAR_SCALE, 2.0f, 1.0f,
					1000, 0, InterpolationLoopType.REVERSE, 1,
					InterpolationType.LINEAR);
			changeColor.getNextEffects().add(interpolation);
			interpolation.getNextEffects().add(interpolation2);
			list.add(interpolation);
			list.add(interpolation2);
		}

		return list;
	}
}
