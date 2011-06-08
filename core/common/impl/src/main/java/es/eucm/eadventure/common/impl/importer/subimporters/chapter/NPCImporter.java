package es.eucm.eadventure.common.impl.importer.subimporters.chapter;

import java.util.HashMap;

import com.google.inject.Inject;

import es.eucm.eadventure.common.Importer;
import es.eucm.eadventure.common.data.chapter.Action;
import es.eucm.eadventure.common.data.chapter.elements.NPC;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.impl.importer.interfaces.ResourceImporter;
import es.eucm.eadventure.common.model.actions.EAdAction;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicActor;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.impl.ImageImpl;

public class NPCImporter extends ActorImporter<NPC> {

	@Inject
	public NPCImporter(StringHandler stringHandler,
			ResourceImporter resourceImporter,
			EAdElementFactory elementFactory,
			Importer<Action, EAdAction> actionImporter) {
		super(stringHandler, resourceImporter, elementFactory, actionImporter);
	}

	@Override
	public void initResourcesCorrespondencies() {
		// FIXME animations, other frames...

		properties = new HashMap<String, String>();
		properties.put(NPC.RESOURCE_TYPE_STAND_DOWN, EAdBasicActor.appearance);

		classes = new HashMap<String, Class<?>>();
		classes.put(NPC.RESOURCE_TYPE_STAND_DOWN, ImageImpl.class);

	}

}
