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

package ead.common.model.elements.scenes;

import ead.common.interfaces.Element;
import ead.common.interfaces.Param;
import ead.common.model.elements.EAdAction;
import ead.common.model.elements.ResourcedElementImpl;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.extra.EAdListImpl;
import ead.common.model.elements.scene.EAdSceneElement;
import ead.common.model.elements.scene.EAdSceneElementDef;
import ead.common.model.elements.variables.EAdVarDef;
import ead.common.model.elements.variables.VarDefImpl;
import ead.common.params.text.EAdString;
import ead.common.resources.annotation.Asset;
import ead.common.resources.annotation.Bundled;
import ead.common.resources.assets.AssetDescriptor;
import ead.common.resources.assets.drawable.Drawable;

@Element(runtime = SceneElementDefImpl.class, detailed = SceneElementDefImpl.class)
public class SceneElementDefImpl extends ResourcedElementImpl implements
		EAdSceneElementDef {

	public static final EAdVarDef<EAdSceneElement> VAR_SCENE_ELEMENT = new VarDefImpl<EAdSceneElement>(
			"scene_element", EAdSceneElement.class, null);

	@Param("actions")
	private EAdList<EAdAction> actions;

	@Bundled
	@Asset({ Drawable.class })
	public static final String appearance = "appearance";

	@Param("name")
	private EAdString name;

	@Param("desc")
	private EAdString desc;

	@Param("detailDesc")
	private EAdString detailDesc;

	@Param("doc")
	private EAdString doc;

	public SceneElementDefImpl() {
		super();
		this.actions = new EAdListImpl<EAdAction>(EAdAction.class);
		this.name = EAdString.newEAdString("name");
		this.desc = EAdString.newEAdString("desc");
		this.detailDesc = EAdString.newEAdString("detailDesc");
		this.doc = EAdString.newEAdString("doc");

	}

	public SceneElementDefImpl(AssetDescriptor appearance) {
		this();
		getResources().addAsset(getInitialBundle(),
				SceneElementDefImpl.appearance, appearance);
	}

	@Override
	public EAdList<EAdAction> getActions() {
		return actions;
	}

	@Override
	public EAdString getDesc() {
		return desc;
	}

	/**
	 * @return the detailedDescription
	 */
	public EAdString getDetailDesc() {
		return detailDesc;
	}

	/**
	 * @return the documentation
	 */
	public EAdString getDoc() {
		return doc;
	}

	/**
	 * @return the name
	 */
	public EAdString getName() {
		return name;
	}

	public void setName(EAdString name) {
		this.name = name;
	}

	public void setDesc(EAdString description) {
		this.desc = description;
	}

	public void setDetailDesc(EAdString detailedDescription) {
		this.detailDesc = detailedDescription;
	}

	public void setDoc(EAdString documentation) {
		this.doc = documentation;
	}

	public void setInitialAppearance(Drawable d) {
		this.getResources().addAsset(this.getInitialBundle(),
				SceneElementDefImpl.appearance, d);
	}
}
