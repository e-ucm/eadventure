package ead.converter.subconverters.actors;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.elements.ResourcedElement;
import ead.common.model.elements.scenes.EAdSceneElementDef;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.converter.UtilsConverter;
import ead.converter.resources.ResourceConverter;
import es.eucm.eadventure.common.data.chapter.elements.Element;
import es.eucm.eadventure.common.data.chapter.elements.Item;

@Singleton
public class ItemConverter extends ElementConverter {

	@Inject
	public ItemConverter(ResourceConverter resourceConverter,
			UtilsConverter utilsConverter) {
		super(resourceConverter, utilsConverter);
	}

	public EAdSceneElementDef convert(Element a) {
		EAdSceneElementDef definition = super.convert(a);
		convert(a, Item.RESOURCE_TYPE_IMAGEOVER, definition,
				ResourcedElement.INITIAL_BUNDLE, SceneElementDef.overAppearance);
		return definition;
	}

	@Override
	public String getResourceType() {
		return Item.RESOURCE_TYPE_IMAGE;
	}

}
