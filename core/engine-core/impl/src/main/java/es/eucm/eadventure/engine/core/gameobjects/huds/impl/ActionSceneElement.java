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

package es.eucm.eadventure.engine.core.gameobjects.huds.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.model.actions.EAdAction;
import es.eucm.eadventure.common.model.actions.impl.EAdBasicAction;
import es.eucm.eadventure.common.model.effects.impl.EAdActorActionsEffect;
import es.eucm.eadventure.common.model.effects.impl.enums.ChangeActorActions;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneElementDefImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.predef.model.effects.EAdChangeAppearance;
import es.eucm.eadventure.common.resources.assets.AssetDescriptor;

@Element(detailed = EAdBasicSceneElement.class, runtime = EAdBasicSceneElement.class)
public class ActionSceneElement extends EAdBasicSceneElement {

	public ActionSceneElement(EAdAction eAdAction) {
		super();
		this.setScale(0.8f);
		EAdActorActionsEffect e = new EAdActorActionsEffect( null,
				ChangeActorActions.HIDE_ACTIONS);
		this.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, e);
		this.addBehavior(EAdMouseEventImpl.MOUSE_RIGHT_CLICK, e);
		this.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK,
				eAdAction.getEffects());
		this.addBehavior(EAdMouseEventImpl.MOUSE_RIGHT_CLICK,
				eAdAction.getEffects());

		AssetDescriptor asset = eAdAction.getAsset(
				eAdAction.getInitialBundle(), EAdBasicAction.appearance);
		definition.getResources().addAsset(definition.getInitialBundle(),
				EAdSceneElementDefImpl.appearance, asset);

		if (eAdAction.getResources().getBundles()
				.contains(eAdAction.getHighlightBundle())) {
			definition.getResources().addBundle(eAdAction.getHighlightBundle());
			definition.getResources().addAsset(
					eAdAction.getHighlightBundle(),
					EAdSceneElementDefImpl.appearance,
					eAdAction.getAsset(eAdAction.getHighlightBundle(),
							EAdBasicAction.appearance));
			this.addBehavior(EAdMouseEventImpl.MOUSE_ENTERED,
					new EAdChangeAppearance( this,
							eAdAction.getHighlightBundle()));
		} else
			definition.getResources().addAsset(eAdAction.getHighlightBundle(),
					EAdSceneElementDefImpl.appearance, asset);

		this.addBehavior(
				EAdMouseEventImpl.MOUSE_EXITED,
				new EAdChangeAppearance(this, definition
						.getInitialBundle()));
		
		setVarInitialValue(EAdBasicSceneElement.VAR_NAME, eAdAction.getName());
		
		
	}

}
