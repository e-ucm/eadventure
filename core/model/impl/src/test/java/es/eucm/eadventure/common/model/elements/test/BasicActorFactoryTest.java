package es.eucm.eadventure.common.model.elements.test;

import es.eucm.eadventure.common.model.actions.test.BasicActionFactoryTest;
import es.eucm.eadventure.common.model.elements.EAdActor;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicActor;
import es.eucm.eadventure.common.resources.EAdString;
import es.eucm.eadventure.common.resources.assets.drawable.animation.impl.Frame;
import es.eucm.eadventure.common.resources.assets.drawable.animation.impl.FramesAnimation;

public class BasicActorFactoryTest {

	public static EAdActor getActor() {
		EAdBasicActor basicActor = new EAdBasicActor("Actor_id");

		FramesAnimation animation = new FramesAnimation();
		for (int i = 1; i <= 8; i++)
			animation.addFrame(new Frame("@drawable/paniel_wlr_0"
					+ i + ".png"));

		basicActor.getResources().addAsset(basicActor.getInitialBundle(),
				EAdBasicActor.appearance, animation);

		EAdString name = new EAdString("panielName");
		basicActor.setName(name);

		EAdString description = new EAdString("panielDescription");
		basicActor.setDescription(description);

		basicActor.getActions().add(BasicActionFactoryTest.getGrabAction());
		
		return basicActor;
	}
	
}
