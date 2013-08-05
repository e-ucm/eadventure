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

package ead.common.model.elements.weev.story.element.impl.effects;

import es.eucm.ead.model.interfaces.Element;
import es.eucm.ead.model.interfaces.Param;
import es.eucm.ead.model.interfaces.features.Resourced;
import ead.common.model.elements.weev.story.element.impl.AbstractEffect;

/**
 * Change an element (e.g. actor, scene, etc.) appearance effect
 */
@Element
public class ChangeAppearanceEffect extends AbstractEffect {

	/**
	 * The {@link Resourced} element whose appearance can be changed
	 */
	@Param
	private Resourced element;

	/**
	 * @param element
	 *            The {@link Resourced} element whose appearance can be changed
	 * @param bundleId
	 *            The {@link EAdBundleId} of the appearance to be set
	 */
	public ChangeAppearanceEffect(Resourced element) {
		this.element = element;
	}

	public Resourced getElement() {
		return element;
	}

	public void setElement(Resourced element) {
		this.element = element;
	}

}
