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

package es.eucm.ead.model.elements.scenes;

import es.eucm.ead.model.assets.AssetDescriptor;
import es.eucm.ead.model.assets.drawable.EAdDrawable;
import es.eucm.ead.model.elements.ResourcedElement;
import es.eucm.ead.model.interfaces.Element;
import es.eucm.ead.model.params.text.EAdString;

@Element
public class SceneElementDef extends ResourcedElement {

	public static final String VAR_DOC_NAME = "doc_name";

	public static final String VAR_DOC_DESC = "doc_desc";

	public static final String VAR_DOC_DETAILED_DESC = "detailed_desc";

	public static final String VAR_DOCUMENTATION = "documentation";

	public static final String appearance = "appearance";

	public static final String overAppearance = "overAppearance";

	public SceneElementDef() {
		super();
	}

	public void setAppearance(String bundle, EAdDrawable drawable) {
		addAsset(bundle, SceneElementDef.appearance, drawable);
	}

	public SceneElementDef(AssetDescriptor appearance) {
		this();
		addAsset(SceneElementDef.appearance, appearance);
	}

	public SceneElementDef(EAdDrawable appearance, EAdDrawable overAppearance) {
		this(appearance);
		addAsset(SceneElementDef.overAppearance, overAppearance);
	}

	public void setName(EAdString name) {
		putProperty(VAR_DOC_NAME, name);
	}

	public void setDesc(EAdString description) {
		putProperty(VAR_DOC_DESC, description);
	}

	public void setDetailDesc(EAdString detailedDescription) {
		putProperty(VAR_DOC_DETAILED_DESC, detailedDescription);
	}

	public void setDoc(EAdString documentation) {
		putProperty(VAR_DOCUMENTATION, documentation);
	}

	/**
	 * Sets the initial appearance for the scene element
	 *
	 * @param d the initial appearance
	 */
	public void setAppearance(EAdDrawable d) {
		addAsset(SceneElementDef.appearance, d);
	}

	public void setOverAppearance(String bundle, EAdDrawable drawable) {
		addAsset(bundle, SceneElementDef.appearance, drawable);
	}

	public void setOverAppearance(EAdDrawable drawable) {
		addAsset(SceneElementDef.overAppearance, drawable);
	}

	public EAdDrawable getAppearance() {
		return (EAdDrawable) getResources().get(SceneElementDef.appearance);
	}

	public EAdDrawable getAppearance(String currentBundle) {
		return (EAdDrawable) getAsset(currentBundle, SceneElementDef.appearance);
	}
}
