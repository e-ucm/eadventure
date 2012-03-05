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

package ead.engine.core.gameobjects.huds;

import ead.common.interfaces.Element;
import ead.common.model.elements.EAdAction;
import ead.common.model.elements.actions.ElementAction;
import ead.common.model.elements.effects.ActorActionsEf;
import ead.common.model.elements.effects.enums.ChangeActorActions;
import ead.common.model.elements.guievents.MouseGEv;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.predef.effects.ChangeAppearanceEf;
import ead.common.resources.assets.AssetDescriptor;

@Element(detailed = SceneElement.class, runtime = SceneElement.class)
public class ActionSceneElement extends SceneElement {

	public ActionSceneElement(EAdAction eAdAction) {
		super();
		this.setInitialScale(0.8f);
		this.setId("action" + eAdAction.getId());
		ActorActionsEf e = new ActorActionsEf( null,
				ChangeActorActions.HIDE_ACTIONS);
		this.addBehavior(MouseGEv.MOUSE_LEFT_CLICK, e);
		this.addBehavior(MouseGEv.MOUSE_RIGHT_CLICK, e);
		this.addBehavior(MouseGEv.MOUSE_LEFT_CLICK,
				eAdAction.getEffects());
		this.addBehavior(MouseGEv.MOUSE_RIGHT_CLICK,
				eAdAction.getEffects());

		AssetDescriptor asset = eAdAction.getAsset(
				eAdAction.getInitialBundle(), ElementAction.appearance);
		definition.getResources().addAsset(definition.getInitialBundle(),
				SceneElementDef.appearance, asset);

		if (eAdAction.getResources().getBundles()
				.contains(eAdAction.getHighlightBundle())) {
			definition.getResources().addBundle(eAdAction.getHighlightBundle());
			definition.getResources().addAsset(
					eAdAction.getHighlightBundle(),
					SceneElementDef.appearance,
					eAdAction.getAsset(eAdAction.getHighlightBundle(),
							ElementAction.appearance));
			this.addBehavior(MouseGEv.MOUSE_ENTERED,
					new ChangeAppearanceEf( this,
							eAdAction.getHighlightBundle()));
		} else
			definition.getResources().addAsset(eAdAction.getHighlightBundle(),
					SceneElementDef.appearance, asset);

		this.addBehavior(
				MouseGEv.MOUSE_EXITED,
				new ChangeAppearanceEf(this, definition
						.getInitialBundle()));
		
		setVarInitialValue(SceneElement.VAR_NAME, eAdAction.getName());
		
		
	}

}
