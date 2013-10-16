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
import es.eucm.ead.model.elements.extra.EAdMap;
import es.eucm.ead.model.interfaces.Element;
import es.eucm.ead.model.interfaces.Param;
import es.eucm.ead.model.params.text.EAdString;
import es.eucm.ead.model.params.variables.EAdVarDef;
import es.eucm.ead.model.params.variables.VarDef;

@Element
public class SceneElementDef extends ResourcedElement implements
		EAdSceneElementDef {

	public static final EAdVarDef<EAdSceneElement> VAR_SCENE_ELEMENT = new VarDef<EAdSceneElement>(
			"scene_element", EAdSceneElement.class, null);

	public static final EAdVarDef<EAdString> VAR_DOC_NAME = new VarDef<EAdString>(
			"doc_name", EAdString.class, null);

	public static final EAdVarDef<EAdString> VAR_DOC_DESC = new VarDef<EAdString>(
			"doc_desc", EAdString.class, null);

	public static final EAdVarDef<EAdString> VAR_DOC_DETAILED_DESC = new VarDef<EAdString>(
			"detailed_desc", EAdString.class, null);

	public static final EAdVarDef<EAdString> VAR_DOCUMENTATION = new VarDef<EAdString>(
			"documentation", EAdString.class, null);

	@Param
	private EAdMap<EAdVarDef<?>, Object> vars;

	public static final String appearance = "appearance";

	public static final String overAppearance = "overAppearance";

	public SceneElementDef() {
		super();
		vars = new EAdMap<EAdVarDef<?>, Object>();
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

	@Override
	public void setName(EAdString name) {
		setVarInitialValue(VAR_DOC_NAME, name);
	}

	@Override
	public void setDesc(EAdString description) {
		setVarInitialValue(VAR_DOC_DESC, description);
	}

	@Override
	public void setDetailDesc(EAdString detailedDescription) {
		setVarInitialValue(VAR_DOC_DETAILED_DESC, detailedDescription);
	}

	@Override
	public void setDoc(EAdString documentation) {
		setVarInitialValue(VAR_DOCUMENTATION, documentation);
	}

	/**
	 * Sets the initial appearance for the scene element
	 *
	 * @param appearance the initial appearance
	 */
	@Override
	public void setAppearance(EAdDrawable d) {
		addAsset(SceneElementDef.appearance, d);
	}

	@Override
	public void setOverAppearance(String bundle, EAdDrawable drawable) {
		addAsset(bundle, SceneElementDef.appearance, drawable);
	}

	@Override
	public void setOverAppearance(EAdDrawable drawable) {
		addAsset(SceneElementDef.overAppearance, drawable);
	}

	@Override
	public EAdDrawable getAppearance() {
		return (EAdDrawable) getResources().get(SceneElementDef.appearance);
	}

	@Override
	public EAdMap<EAdVarDef<?>, Object> getVars() {
		return vars;
	}

	@Override
	public <T> void setVarInitialValue(EAdVarDef<T> var, T value) {
		vars.put(var, value);
	}

	public EAdDrawable getAppearance(String currentBundle) {
		return (EAdDrawable) getAsset(currentBundle, SceneElementDef.appearance);
	}

	public void setVars(EAdMap<EAdVarDef<?>, Object> vars) {
		this.vars = vars;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getVarInitialValue(EAdVarDef<T> var) {
		if (vars.containsKey(var)) {
			return (T) vars.get(var);
		}
		return var.getInitialValue();
	}
}
