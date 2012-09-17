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
import ead.common.model.elements.ResourcedElement;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.extra.EAdListImpl;
import ead.common.model.elements.scene.EAdSceneElement;
import ead.common.model.elements.scene.EAdSceneElementDef;
import ead.common.model.elements.variables.EAdVarDef;
import ead.common.model.elements.variables.VarDef;
import ead.common.params.text.EAdString;
import ead.common.resources.EAdBundleId;
import ead.common.resources.EAdResources;
import ead.common.resources.annotation.Asset;
import ead.common.resources.annotation.Bundled;
import ead.common.resources.assets.AssetDescriptor;
import ead.common.resources.assets.drawable.EAdDrawable;

@Element
public class SceneElementDef extends ResourcedElement implements
		EAdSceneElementDef {

	public static final EAdVarDef<EAdSceneElement> VAR_SCENE_ELEMENT = new VarDef<EAdSceneElement>(
			"scene_element", EAdSceneElement.class, null);

	@Param("actions")
	private EAdList<EAdAction> actions;

	@Bundled
	@Asset({ EAdDrawable.class })
	public static final String appearance = "appearance";

	@Param("name")
	private EAdString name;

	@Param("desc")
	private EAdString desc;

	@Param("detailDesc")
	private EAdString detailDesc;

	@Param("doc")
	private EAdString doc;

	public SceneElementDef() {
		super();
		this.actions = new EAdListImpl<EAdAction>(EAdAction.class);
		this.name = EAdString.newRandomEAdString("name");
		this.desc = EAdString.newRandomEAdString("desc");
		this.detailDesc = EAdString.newRandomEAdString("detailDesc");
		this.doc = EAdString.newRandomEAdString("doc");

	}

	public SceneElementDef(AssetDescriptor appearance) {
		this();
		getResources().addAsset(getInitialBundle(),
				SceneElementDef.appearance, appearance);
	}

	public void setActions(EAdList<EAdAction> actions) {
		this.actions = actions;
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

	public void setInitialAppearance(EAdDrawable d) {
		this.getResources().addAsset(this.getInitialBundle(),
				SceneElementDef.appearance, d);
	}

	/**
	 * Sets the initial appearance for the scene element
	 *
	 * @param appearance
	 *            the initial appearance
	 */
	public void setAppearance(EAdDrawable appearance) {
		setAppearance(getInitialBundle(), appearance);
	}

	/**
	 * Sets the appearance in the given bundle
	 *
	 * @param bundle
	 *            the bundle id
	 * @param appearance
	 *            the appearance
	 */
	public void setAppearance(EAdBundleId bundle, EAdDrawable appearance) {
		EAdResources resources = getResources();
		if (!resources.getBundles().contains(bundle)) {
			resources.addBundle(bundle);
		}
		resources.addAsset(bundle, SceneElementDef.appearance, appearance);
	}

	@Override
	public EAdDrawable getAppearance() {
		return (EAdDrawable) getResources().getAsset(getInitialBundle(), SceneElementDef.appearance);
	}

	@Override
	public EAdDrawable getAppearance(EAdBundleId bundle) {
		return (EAdDrawable) getResources().getAsset(bundle, SceneElementDef.appearance);
	}
}
