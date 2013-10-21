/**
 * eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the
 *    <e-UCM> research group.
 *
 *    Copyright 2005-2010 <e-UCM> research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    <e-UCM> is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure, version 2.0
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */

package es.eucm.ead.importer.subconverters;

import com.google.inject.Singleton;
import es.eucm.ead.model.elements.transitions.DisplaceTransition;
import es.eucm.ead.model.elements.transitions.Transition;
import es.eucm.ead.model.elements.transitions.EmptyTransition;
import es.eucm.ead.model.elements.transitions.FadeInTransition;
import es.eucm.ead.model.elements.transitions.enums.DisplaceTransitionType;
import es.eucm.eadventure.common.data.chapter.Exit;
import es.eucm.eadventure.common.data.chapter.NextScene;

@Singleton
public class TransitionConverter {

	public Transition getTransition(int type, int time) {
		switch (type) {
		case es.eucm.eadventure.common.data.animation.Transition.TYPE_FADEIN:
			return new FadeInTransition(time);
		case es.eucm.eadventure.common.data.animation.Transition.TYPE_HORIZONTAL:
			return new DisplaceTransition(time,
					DisplaceTransitionType.HORIZONTAL, true);
		case es.eucm.eadventure.common.data.animation.Transition.TYPE_VERTICAL:
			return new DisplaceTransition(time,
					DisplaceTransitionType.VERTICAL, true);
		default:
			return EmptyTransition.instance();
		}
	}

	public Transition getTransitionExit(int type, int time) {
		switch (type) {
		case Exit.FADE_IN:
			return new FadeInTransition(time);
		case Exit.BOTTOM_TO_TOP:
			return new DisplaceTransition(time,
					DisplaceTransitionType.VERTICAL, false);
		case Exit.TOP_TO_BOTTOM:
			return new DisplaceTransition(time,
					DisplaceTransitionType.VERTICAL, true);
		case Exit.LEFT_TO_RIGHT:
			return new DisplaceTransition(time,
					DisplaceTransitionType.HORIZONTAL, true);
		case Exit.RIGHT_TO_LEFT:
			return new DisplaceTransition(time,
					DisplaceTransitionType.HORIZONTAL, false);
		default:
			return EmptyTransition.instance();
		}
	}

	public Transition getTransitionNextScene(int type, int time) {
		switch (type) {
		case NextScene.FADE_IN:
			return new FadeInTransition(time);
		case NextScene.TOP_TO_BOTTOM:
			return new DisplaceTransition(time,
					DisplaceTransitionType.VERTICAL, true);
		case NextScene.BOTTOM_TO_TOP:
			return new DisplaceTransition(time,
					DisplaceTransitionType.VERTICAL, false);
		case NextScene.RIGHT_TO_LEFT:
			return new DisplaceTransition(time,
					DisplaceTransitionType.HORIZONTAL, false);
		case NextScene.LEFT_TO_RIGHT:
			return new DisplaceTransition(time,
					DisplaceTransitionType.HORIZONTAL, true);
		default:
			return EmptyTransition.instance();
		}
	}

}
