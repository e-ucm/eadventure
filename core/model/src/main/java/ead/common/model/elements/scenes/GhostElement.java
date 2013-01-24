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
import ead.common.model.assets.drawable.EAdDrawable;

/**
 * <p>
 * Use ghost elements whenever you want to create a scene element whose
 * interaction area is different from its appearance.
 * </p>
 * <p>
 * You also can use ghost elements with a null interaction area to create
 * elements that don't interfere with user interaction at all (such a mouse
 * pointer, a score board...)
 * </p>
 * 
 */
@Element
public class GhostElement extends SceneElement implements EAdGhostElement {

	@Param
	private EAdDrawable interactionArea;

	@Param
	private boolean catchAll;

	public GhostElement() {

	}

	/**
	 * Creates a ghost element
	 * 
	 * @param appearance
	 *            the appearance
	 * @param interactionArea
	 *            the interaction area. Could be {@code null}
	 */
	public GhostElement(EAdDrawable appearance, EAdDrawable interactionArea) {
		super(appearance);
		this.interactionArea = interactionArea;
	}

	public GhostElement(EAdDrawable interactionArea) {
		this.interactionArea = interactionArea;
	}

	@Override
	public EAdDrawable getInteractionArea() {
		return interactionArea;
	}

	@Override
	public void setInteractionArea(EAdDrawable drawable) {
		this.interactionArea = drawable;
	}

	public boolean isCatchAll() {
		return catchAll;
	}

	public void setCatchAll(boolean catchAll) {
		this.catchAll = catchAll;
	}

}
