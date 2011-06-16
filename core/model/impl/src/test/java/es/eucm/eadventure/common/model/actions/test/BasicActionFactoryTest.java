package es.eucm.eadventure.common.model.actions.test;

import es.eucm.eadventure.common.model.actions.EAdAction;
import es.eucm.eadventure.common.model.actions.impl.EAdBasicAction;
import es.eucm.eadventure.common.model.effects.impl.text.EAdShowText;
import es.eucm.eadventure.common.model.params.EAdBorderedColor;
import es.eucm.eadventure.common.model.params.EAdColor;
import es.eucm.eadventure.common.resources.EAdString;
import es.eucm.eadventure.common.resources.assets.drawable.impl.CaptionImpl;
import es.eucm.eadventure.common.resources.assets.drawable.impl.ImageImpl;

public class BasicActionFactoryTest {

	public static EAdAction getGrabAction() {
		EAdBasicAction action = new EAdBasicAction("action");
		action.getResources().addAsset(action.getInitialBundle(), EAdBasicAction.appearance,
				new ImageImpl("@drawable/grab.png"));
		
		EAdString hand = new EAdString("handAction");
		action.setName(hand);
		
		//TODO Should be created in another place
		EAdShowText actionEffect = new EAdShowText("effectAction");

		CaptionImpl caption = new CaptionImpl();
		caption.setBubbleColor(null);
		caption.setText(hand);
		caption.setTextColor(new EAdBorderedColor(new EAdColor(120, 20, 20),
				new EAdColor(34, 50, 60)));

		actionEffect.setCaption(caption, 40, 100);
		actionEffect.setLoops(400);
		
		action.getEffects().add(actionEffect);
		
		return action;
	}
}
