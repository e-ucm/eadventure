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

package es.eucm.eadventure.common.model.actions.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.actions.EAdAction;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.extra.impl.EAdListImpl;
import es.eucm.eadventure.common.model.impl.ResourcedElementImpl;
import es.eucm.eadventure.common.params.EAdString;
import es.eucm.eadventure.common.resources.EAdBundleId;
import es.eucm.eadventure.common.resources.annotation.Asset;
import es.eucm.eadventure.common.resources.annotation.Bundled;
import es.eucm.eadventure.common.resources.assets.drawable.Drawable;

@Element(runtime = EAdBasicAction.class, detailed = EAdBasicAction.class)
public class EAdBasicAction extends ResourcedElementImpl implements EAdAction {

	@Bundled
	@Asset({ Drawable.class })
	public static final String appearance = "appearance";

	@Param("name")
	private EAdString name;

	@Param("effects")
	private EAdList<EAdEffect> effects;

	@Param("highlightBundle")
	private EAdBundleId highlightBundle;

	@Param("condition")
	private EAdCondition condition;
	
	public EAdBasicAction() {
		super();
		effects = new EAdListImpl<EAdEffect>(EAdEffect.class);
		highlightBundle = new EAdBundleId(id + "_highlightBundle");
		name = EAdString.newEAdString("actionName");
		getResources().addBundle(highlightBundle);
	}

	@Override
	public EAdList<EAdEffect> getEffects() {
		return effects;
	}

	public EAdString getName() {
		return name;
	}

	@Override
	public EAdBundleId getNormalBundle() {
		return getInitialBundle();
	}
	
	@Override
	public EAdBundleId getHighlightBundle() {
		return highlightBundle;
	}

	@Override
	public EAdCondition getCondition() {
		return condition;
	}

	@Override
	public void setCondition(EAdCondition condition) {
		this.condition = condition;
		
	}

	public void setName(EAdString name) {
		this.name = name;
	}

	public void setHighlightBundle(EAdBundleId highlightBundle) {
		this.highlightBundle = highlightBundle;
	}

	
}
