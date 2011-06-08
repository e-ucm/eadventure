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

package es.eucm.eadventure.common.model.effects.impl;

import es.eucm.eadventure.common.Element;
import es.eucm.eadventure.common.Param;
import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.resources.EAdBundleId;

@Element(runtime = EAdChangeAppearance.class, detailed = EAdChangeAppearance.class)
public class EAdChangeAppearance extends AbstractEAdEffect implements EAdEffect {

	@Param("element")
	private EAdElement element;
	
	@Param("bundleId")
	private EAdBundleId bundleId;
	
	public EAdChangeAppearance(String id) {
		super(id);
	}
	
	public EAdChangeAppearance(String id, EAdElement element, EAdBundleId bundleId) {
		super(id);
		this.element = element;
		this.bundleId = bundleId;
	}
	
	public void setElement(EAdElement element) {
		this.element = element;
	}
	
	public EAdElement getElement() {
		return element;
	}
	
	public void setBundleId(EAdBundleId bundleId) {
		this.bundleId = bundleId;
	}
	
	public EAdBundleId getBundleId() {
		return bundleId;
	}

	@Override
	public boolean isBlocking() {
		return false;
	}

	@Override
	public boolean isOpaque() {
		return false;
	}

}
