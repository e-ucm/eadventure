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

package es.eucm.eadventure.common.impl.reader.subparsers;

import org.xml.sax.Attributes;

import es.eucm.eadventure.common.impl.reader.subparsers.extra.AssetId;
import es.eucm.eadventure.common.resources.EAdBundleId;
import es.eucm.eadventure.common.resources.EAdResources;
import es.eucm.eadventure.common.resources.assets.AssetDescriptor;

/**
 * <p>
 * Parser for the resource element.
 * </p>
 * <p>
 * The resource element should be<br>
 * {@code	<bundle id="BUNDLE_ID">}<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 * {@code   <asset id="ASSET_ID" class="ASSETDESCRIPTOR_CLASS">ASSET_VALUE</asset>}
 * x N<br>
 * {@code	</bundle>}<br>
 * 
 * </p>
 */
public class BundleSubparser extends Subparser implements AssetId {

	/**
	 * The object where the asset belongs
	 */
	private EAdResources resources;

	private EAdBundleId bundleId;

	private String id;

	/**
	 * Constructor for the bundle parser.
	 * 
	 * @param bundle
	 *            The attributes of the bundle
	 */
	public BundleSubparser(EAdResources resources, Attributes attributes) {
		this.bundleId = new EAdBundleId(attributes.getValue("id"));
		this.resources = resources;
		if (bundleId.equals(resources.getInitialBundle())) {
			bundleId = resources.getInitialBundle();
		} else {
			resources.addBundle(bundleId);
		}
	}

	@Override
	public void characters(char[] buf, int offset, int len) {
	}

	@Override
	public void endElement() {
	}

	@Override
	public void addChild(Object element) {
		if (element instanceof AssetDescriptor)
			resources.addAsset(bundleId, id, (AssetDescriptor) element);
	}

	@Override
	public Object getObject() {
		return bundleId;
	}

	@Override
	public void setAssetId(String assetId) {
		this.id = assetId;
	}

}
