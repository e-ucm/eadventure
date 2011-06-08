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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;

import es.eucm.eadventure.common.EAdRuntimeException;
import es.eucm.eadventure.common.impl.reader.subparsers.extra.ObjectFactory;
import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.resources.EAdBundleId;
import es.eucm.eadventure.common.resources.annotation.ParamenterName;
import es.eucm.eadventure.common.resources.assets.AssetDescriptor;

/**
 * Parser for the asset element. The asset element should be
 * {@code <asset name="RESOURCE_NAME">@type/value</asset>} when there is no
 * bundle and
 * {@code <asset name="RESOURCE_NAME" bundle="BUNDLE_NAME">@type/value</asset>}
 * when there is a bundle.
 */
public class AssetDescriptorSubparser extends Subparser {

	/**
	 * The current string being parsed (in this case, the path of the asset)
	 */
	private StringBuffer currentString;

	/**
	 * The name of the asset
	 */
	private String name;

	/**
	 * The bundle the asset belongs to
	 */
	private String bundle;

	/**
	 * The class of the asset descriptor
	 */
	private String className;

	/**
	 * The object where the asset belongs
	 */
	private EAdElement object;

	private Attributes attributes;

	private static final Logger logger = LoggerFactory
			.getLogger("ParamSubparser");

	/**
	 * Constructor for the asset parser.
	 * 
	 * @param object
	 *            The object where the asset belongs
	 * @param bundle
	 *            The attributes of the bundle
	 */
	public AssetDescriptorSubparser(EAdElement object, Attributes attributes) {
		currentString = new StringBuffer();
		this.object = object;
		this.name = attributes.getValue("name");
		this.bundle = attributes.getValue("bundle");
		this.className = attributes.getValue("class");
		this.attributes = attributes;
	}

	@Override
	public void characters(char[] buf, int offset, int len) {
		currentString.append(new String(buf, offset, len));
	}

	@Override
	public void endElement() {
		String value = currentString.toString().trim();

		try {
			Class<?> clazz = ClassLoader.getSystemClassLoader().loadClass(className);
			Constructor<?> con = clazz.getConstructors()[0];
			Object[] objects = new Object[con.getParameterTypes().length];
			objects[0] = value;
			for (int i = 1; i < con.getParameterTypes().length; i++) {
				String name = null;
				for (int j = 0; j < con.getParameterAnnotations()[i].length; j++)
					if (con.getParameterAnnotations()[i][j] instanceof ParamenterName)
						name = ((ParamenterName) con.getParameterAnnotations()[i][j])
								.value();

				if (name == null) {
					logger.error("Asset has no parameter name! :" + className);
					throw (new EAdRuntimeException(
							"Asset has no parameter name! :" + className));
				}

				String stringValue = attributes.getValue(name);
				objects[i] = ObjectFactory.getObject(stringValue,
						con.getParameterTypes()[i]);
			}

			AssetDescriptor descriptor = (AssetDescriptor) con
					.newInstance(objects);

			if (bundle == null)
				object.getResources().addAsset(name, descriptor);
			else {
				EAdBundleId bundleId = new EAdBundleId(bundle);
				object.getResources().addAsset(bundleId, name, descriptor);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

	@Override
	public void addChild(Object element) {
		//DO NOTHING
		logger.error("Tried to add child to assset descriptor " + element);
	}

}
