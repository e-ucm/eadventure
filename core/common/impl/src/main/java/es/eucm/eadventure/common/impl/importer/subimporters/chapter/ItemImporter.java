package es.eucm.eadventure.common.impl.importer.subimporters.chapter;

import java.util.HashMap;

import com.google.inject.Inject;

import es.eucm.eadventure.common.Importer;
import es.eucm.eadventure.common.data.chapter.Action;
import es.eucm.eadventure.common.data.chapter.elements.Item;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.impl.importer.interfaces.ResourceImporter;
import es.eucm.eadventure.common.model.actions.EAdAction;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicActor;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.impl.ImageImpl;

public class ItemImporter extends ActorImporter<Item> {

	@Inject
	public ItemImporter(StringHandler stringHandler,
			ResourceImporter resourceImporter,
			EAdElementFactory elementFactory,
			Importer<Action, EAdAction> actionImporter) {
		super(stringHandler, resourceImporter, elementFactory, actionImporter);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initResourcesCorrespondencies() {

		// FIXME Item.RESOURCE_TYPE_ICON is ignored

		properties = new HashMap<String, String>();
		properties.put(Item.RESOURCE_TYPE_IMAGE, EAdBasicActor.appearance);

		classes = new HashMap<String, Class<?>>();
		classes.put(Item.RESOURCE_TYPE_IMAGE, ImageImpl.class);

	}

}
