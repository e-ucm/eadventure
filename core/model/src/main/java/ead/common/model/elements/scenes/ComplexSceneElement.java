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
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.extra.EAdListImpl;
import ead.common.model.elements.scene.EAdComplexSceneElement;
import ead.common.model.elements.scene.EAdSceneElement;
import ead.common.model.elements.scene.EAdSceneElementDef;
import ead.common.model.elements.variables.EAdVarDef;
import ead.common.model.elements.variables.VarDef;
import ead.common.resources.assets.drawable.EAdDrawable;

/**
 * Represents an scene element that is compound with other scene elements. This
 * element acts as container. Its state (position, scale, rotation...) affects
 * its contained elements
 * 
 * 
 */
@Element
public class ComplexSceneElement extends SceneElement implements
		EAdComplexSceneElement {

	/**
	 * A variable defining if this container must update its width to the
	 * minimum width to contain all its components
	 */
	public static final EAdVarDef<Boolean> VAR_AUTO_SIZE_HORIZONTAL = new VarDef<Boolean>(
			"autoSizeH", Boolean.class, Boolean.TRUE);

	/**
	 * A variable defining if this container must update its height to the
	 * minimum height to contain all its components
	 */
	public static final EAdVarDef<Boolean> VAR_AUTO_SIZE_VERTICAL = new VarDef<Boolean>(
			"autoSizeV", Boolean.class, Boolean.TRUE);

	@Param("sceneElements")
	protected EAdList<EAdSceneElement> sceneElements;

	/**
	 * Creates an empty complex scene element
	 * 
	 * @param id
	 *            the id for the element
	 */
	public ComplexSceneElement() {
		super();
		sceneElements = new EAdListImpl<EAdSceneElement>(EAdSceneElement.class);
	}

	public ComplexSceneElement(EAdSceneElementDef definition) {
		this();
		this.setDefinition(definition);
	}

	public ComplexSceneElement(EAdDrawable asset ) {
		this(new SceneElementDef( asset) );
	}

	/**
	 * Sets the bounds for this container. Values equal or less than zero are
	 * interpreted as infinitum
	 * 
	 * @param width
	 *            the width
	 */
	public void setBounds(int width, int height) {
		setVarInitialValue(VAR_AUTO_SIZE_HORIZONTAL, width <= 0);
		setVarInitialValue(VAR_WIDTH, width);
		setVarInitialValue(VAR_AUTO_SIZE_VERTICAL, height <= 0);
		setVarInitialValue(VAR_HEIGHT, height);

	}

	@Override
	public EAdList<EAdSceneElement> getSceneElements() {
		return sceneElements;
	}
}
