package es.eucm.eadventure.engine.core.platform.impl.extra;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.model.actions.EAdAction;
import es.eucm.eadventure.common.model.actions.impl.EAdBasicAction;
import es.eucm.eadventure.common.model.effects.impl.EAdActorActionsEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdChangeAppearance;
import es.eucm.eadventure.common.model.effects.impl.EAdActorActionsEffect.Change;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.resources.assets.AssetDescriptor;

@Element(detailed = EAdBasicSceneElement.class, runtime = EAdBasicSceneElement.class)
public class DesktopAction extends EAdBasicSceneElement {

	public DesktopAction(EAdAction eAdAction) {
		super("action");

		// TODO null?
		EAdActorActionsEffect e = new EAdActorActionsEffect("", null,
				Change.HIDE_ACTIONS);
		this.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, e);
		this.addBehavior(EAdMouseEventImpl.MOUSE_RIGHT_CLICK, e);
		this.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK,
				eAdAction.getEffects());
		this.addBehavior(EAdMouseEventImpl.MOUSE_RIGHT_CLICK,
				eAdAction.getEffects());

		AssetDescriptor asset = eAdAction.getAsset(
				eAdAction.getInitialBundle(), EAdBasicAction.appearance);
		this.getResources().addAsset(this.getInitialBundle(),
				EAdBasicSceneElement.appearance, asset);

		if (eAdAction.getResources().getBundles()
				.contains(eAdAction.getHighlightBundle())) {
			this.getResources().addBundle(eAdAction.getHighlightBundle());
			this.getResources().addAsset(
					eAdAction.getHighlightBundle(),
					EAdBasicSceneElement.appearance,
					eAdAction.getAsset(eAdAction.getHighlightBundle(),
							EAdBasicAction.appearance));
			this.addBehavior(EAdMouseEventImpl.MOUSE_ENTERED,
					new EAdChangeAppearance("action_mouseEnter", this,
							eAdAction.getHighlightBundle()));
		} else
			this.getResources().addAsset(eAdAction.getHighlightBundle(),
					EAdBasicSceneElement.appearance, asset);

		this.addBehavior(
				EAdMouseEventImpl.MOUSE_EXITED,
				new EAdChangeAppearance("action_mouseExit", this, this
						.getInitialBundle()));
	}

}
