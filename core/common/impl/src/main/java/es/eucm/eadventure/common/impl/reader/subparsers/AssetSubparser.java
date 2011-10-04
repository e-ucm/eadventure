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

import java.lang.reflect.Constructor;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.xml.sax.Attributes;

import es.eucm.eadventure.common.impl.reader.subparsers.extra.AssetId;
import es.eucm.eadventure.common.impl.reader.subparsers.extra.ObjectFactory;
import es.eucm.eadventure.common.impl.writer.DOMWriter;
import es.eucm.eadventure.common.resources.assets.AssetDescriptor;

/**
 * <p>
 * Parser for the resource element.
 * </p>
 * <p>
 * The asset element should be<br>
 * {@code <asset id="ASSET_ID" class="ASSETDESCRIPTOR_CLASS">ASSET_VALUE</asset>}
 * <br>
 * </p>
 */
public class AssetSubparser extends Subparser<AssetDescriptor> {

	private static final Logger logger = Logger.getLogger("AssetSubparser");

	/**
	 * Constructor for the asset parser.
	 * 
	 * @param object
	 *            The object where the asset belongs
	 * @param bundle
	 *            The attributes of the bundle
	 */
	public AssetSubparser(Object parent, Object assetId, Attributes attributes) {
		super(parent, attributes, AssetDescriptor.class);
		if (assetId instanceof AssetId) {
			((AssetId) assetId)
					.setAssetId(attributes.getValue(DOMWriter.ID_AT));
		}
		if (clazz != null) {
			String uniqueId = attributes.getValue(DOMWriter.UNIQUE_ID_AT);
			Class<?> c = null;
			try {
				c = ClassLoader.getSystemClassLoader().loadClass(clazz);
				Constructor<?> con = c.getConstructor();
				element = (AssetDescriptor) con.newInstance(new Object[] {});
				ObjectFactory.addAsset(uniqueId, element);
			} catch (NoSuchMethodException e1) {
				logger.info("You must define a constructor without parameters for the class "
						+ c + " in order to make work XML read and write");

			} catch (Exception e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}
}
