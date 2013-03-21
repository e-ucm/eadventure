package ead.converter.subconverters.actors;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.assets.drawable.EAdDrawable;
import ead.common.model.elements.scenes.EAdSceneElementDef;
import ead.converter.UtilsConverter;
import ead.converter.resources.ResourceConverter;
import es.eucm.eadventure.common.data.chapter.elements.NPC;
import es.eucm.eadventure.common.data.chapter.resources.Resources;

@Singleton
public class NPCConverter extends ElementConverter {

	@Inject
	public NPCConverter(ResourceConverter resourceConverter,
			UtilsConverter utilsConverter) {
		super(resourceConverter, utilsConverter);
	}

	public EAdSceneElementDef convert(NPC npc) {
		EAdSceneElementDef def = super.convert(npc);
		return def;
	}

	@Override
	protected EAdDrawable getDrawable(Resources r, String resourceId) {
		// XXX All states and orientations
		return resourceConverter.getFramesAnimation(r.getAssetPath(resourceId));
	}

	@Override
	protected String getResourceType() {
		return NPC.RESOURCE_TYPE_STAND_UP;
	}

}
