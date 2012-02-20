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

package ead.common.model.elements.actions;

import ead.common.interfaces.Element;
import ead.common.interfaces.Param;
import ead.common.model.elements.EAdAction;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.ResourcedElement;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.extra.EAdListImpl;
import ead.common.params.text.EAdString;
import ead.common.resources.EAdBundleId;
import ead.common.resources.annotation.Asset;
import ead.common.resources.annotation.Bundled;
import ead.common.resources.assets.drawable.EAdDrawable;

@Element(runtime = ElementAction.class, detailed = ElementAction.class)
public class ElementAction extends ResourcedElement implements EAdAction {

	@Bundled
	@Asset({ EAdDrawable.class })
	public static final String appearance = "appearance";

	@Param("name")
	private EAdString name;

	@Param("effects")
	private EAdList<EAdEffect> effects;

	@Param("highlightBundle")
	private EAdBundleId highlightBundle;
	
	public ElementAction(){
		this( EAdString.newRandomEAdString("name"));
	}
	
	public ElementAction(EAdString name) {
		super();
		effects = new EAdListImpl<EAdEffect>(EAdEffect.class);
		highlightBundle = new EAdBundleId(id + "_highlight");
		this.name = name;
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

	public void setName(EAdString name) {
		this.name = name;
	}

	public void setHighlightBundle(EAdBundleId highlightBundle) {
		this.highlightBundle = highlightBundle;
	}

	
}
