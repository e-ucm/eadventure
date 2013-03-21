package ead.converter.subconverters.effects;

import java.util.ArrayList;
import java.util.List;

import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.effects.enums.MovementSpeed;
import ead.common.model.elements.effects.sceneelements.MoveSceneElementEf;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.converter.EAdElementsCache;
import ead.converter.subconverters.effects.EffectsConverter.EffectConverter;
import es.eucm.eadventure.common.data.chapter.effects.MoveNPCEffect;

public class MoveNPCConverter implements EffectConverter<MoveNPCEffect> {

	private EAdElementsCache elementsCache;

	public MoveNPCConverter(EAdElementsCache elementsCache) {
		this.elementsCache = elementsCache;
	}

	@Override
	public List<EAdEffect> convert(MoveNPCEffect e) {
		// XXX It doesn't work if there's more than one element with the definition
		ArrayList<EAdEffect> list = new ArrayList<EAdEffect>();
		SceneElementDef sceneElementDef = (SceneElementDef) elementsCache.get(e
				.getTargetId());
		MoveSceneElementEf moveSceneElement = new MoveSceneElementEf(
				sceneElementDef, e.getX(), e.getY(), MovementSpeed.NORMAL);
		list.add(moveSceneElement);
		return list;
	}
}
