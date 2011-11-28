package es.eucm.eadventure.common.elementfactories.actions;

import es.eucm.eadventure.common.elementfactories.EAdElementsFactory;
import es.eucm.eadventure.common.model.actions.EAdAction;
import es.eucm.eadventure.common.model.actions.impl.EAdBasicAction;
import es.eucm.eadventure.common.model.effects.impl.text.EAdSpeakEffect;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.ImageImpl;

public class ActionsFactory {
	
	public EAdAction getBasicAction( ){
		EAdBasicAction action = new EAdBasicAction();
		action.getResources().addAsset(action.getInitialBundle(),
				EAdBasicAction.appearance,
				new ImageImpl("@drawable/examine-normal.png"));
		action.getResources().addAsset(action.getHighlightBundle(),
				EAdBasicAction.appearance,
				new ImageImpl("@drawable/examine-pressed.png"));

		EAdSpeakEffect speak = new EAdSpeakEffect();

		EAdElementsFactory.getInstance().getStringFactory()
				.setString(speak.getString(), "The action was triggered!");

		
		action.getEffects().add(speak);
		
		return action;
	}

}
