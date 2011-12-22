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

package es.eucm.eadventure.engine.extra;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.model.elements.EAdAction;
import es.eucm.eadventure.common.model.elements.actions.ActionImpl;
import es.eucm.eadventure.common.model.elements.effects.ActorActionsEf;
import es.eucm.eadventure.common.model.elements.effects.enums.ChangeActorActions;
import es.eucm.eadventure.common.model.elements.guievents.MouseEventImpl;
import es.eucm.eadventure.common.model.elements.scenes.SceneElementDefImpl;
import es.eucm.eadventure.common.model.elements.scenes.SceneElementImpl;
import es.eucm.eadventure.common.model.predef.effects.ChangeAppearanceEf;
import es.eucm.eadventure.common.resources.assets.AssetDescriptor;

@Element(detailed = SceneElementImpl.class, runtime = SceneElementImpl.class)
public class AndroidAction extends SceneElementImpl {

	public AndroidAction(EAdAction eAdAction) {
		super();

		// TODO null?
		ActorActionsEf e = new ActorActionsEf( null,
				ChangeActorActions.HIDE_ACTIONS);
		this.addBehavior(MouseEventImpl.MOUSE_LEFT_CLICK, e);
		this.addBehavior(MouseEventImpl.MOUSE_RIGHT_CLICK, e);
		this.addBehavior(MouseEventImpl.MOUSE_LEFT_CLICK,
				eAdAction.getEffects());
		this.addBehavior(MouseEventImpl.MOUSE_RIGHT_CLICK,
				eAdAction.getEffects());

		AssetDescriptor asset = eAdAction.getAsset(
				eAdAction.getInitialBundle(), ActionImpl.appearance);
		this.getDefinition().getResources().addAsset(this.getDefinition().getInitialBundle(),
				SceneElementDefImpl.appearance, asset);

		if (eAdAction.getResources().getBundles()
				.contains(eAdAction.getHighlightBundle())) {
			getDefinition().getResources().addBundle(eAdAction.getHighlightBundle());
			getDefinition().getResources().addAsset(
					eAdAction.getHighlightBundle(),
					SceneElementDefImpl.appearance,
					eAdAction.getAsset(eAdAction.getHighlightBundle(),
							ActionImpl.appearance));
			this.addBehavior(MouseEventImpl.MOUSE_ENTERED,
					new ChangeAppearanceEf( this,
							eAdAction.getHighlightBundle()));
		} else
			getDefinition().getResources().addAsset(eAdAction.getHighlightBundle(),
					SceneElementDefImpl.appearance, asset);

		this.addBehavior(
				MouseEventImpl.MOUSE_EXITED,
				new ChangeAppearanceEf( this, getDefinition()
						.getInitialBundle()));
		
		
	}

}

