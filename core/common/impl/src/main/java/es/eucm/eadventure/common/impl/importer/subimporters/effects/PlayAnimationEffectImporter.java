package es.eucm.eadventure.common.impl.importer.subimporters.effects;

import com.google.inject.Inject;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.PlayAnimationEffect;
import es.eucm.eadventure.common.impl.importer.interfaces.ResourceImporter;
import es.eucm.eadventure.common.model.effects.impl.timedevents.EAdShowSceneElement;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.resources.assets.AssetDescriptor;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.ImageImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.animation.FramesAnimation;

public class PlayAnimationEffectImporter extends
		EffectImporter<PlayAnimationEffect, EAdShowSceneElement> {

	private ResourceImporter resourceImporter;

	@Inject
	public PlayAnimationEffectImporter(
			EAdElementImporter<Conditions, EAdCondition> conditionImporter,
			ResourceImporter resourceImporter) {
		super(conditionImporter);
		this.resourceImporter = resourceImporter;
	}

	@Override
	public EAdShowSceneElement init(PlayAnimationEffect oldObject) {
		return new EAdShowSceneElement("playAnimationEffect" + ID_GENERATOR++);
	}

	@Override
	public EAdShowSceneElement convert(PlayAnimationEffect oldObject,
			Object newElement) {
		EAdShowSceneElement effect = super.convert(oldObject, newElement);
		EAdBasicSceneElement element = new EAdBasicSceneElement("animation"
				+ ID_GENERATOR++);
		element.setPosition(new EAdPositionImpl(oldObject.getX(), oldObject
				.getY()));

		AssetDescriptor asset = resourceImporter.getAssetDescritptor(
				oldObject.getPath(), ImageImpl.class);
		if (asset instanceof FramesAnimation) {
			int time = 0;
			for (int i = 0; i < ((FramesAnimation) asset).getFrameCount(); i++) {
				time += ((FramesAnimation) asset).getFrame(i).getTime();
			}
			effect.setTime(time);
		} else {
			effect.setTime(1000);
		}

		element.getResources().addAsset(element.getInitialBundle(),
				EAdBasicSceneElement.appearance, asset);

		effect.setSceneElement(element);
		return effect;
	}

}
