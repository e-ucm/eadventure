package ead.converter.subconverters;

import com.google.inject.Singleton;

import ead.common.model.elements.transitions.DisplaceTransition;
import ead.common.model.elements.transitions.EAdTransition;
import ead.common.model.elements.transitions.EmptyTransition;
import ead.common.model.elements.transitions.FadeInTransition;
import ead.common.model.elements.transitions.enums.DisplaceTransitionType;
import es.eucm.eadventure.common.data.animation.Transition;

@Singleton
public class TransitionConverter {

	public EAdTransition getTransition(int type, int time) {
		switch (type) {
		case Transition.TYPE_FADEIN:
			return new FadeInTransition(time);
		case Transition.TYPE_HORIZONTAL:
			return new DisplaceTransition(time,
					DisplaceTransitionType.HORIZONTAL, true);
		case Transition.TYPE_VERTICAL:
			return new DisplaceTransition(time,
					DisplaceTransitionType.VERTICAL, true);
		default:
			return EmptyTransition.instance();
		}
	}

}
