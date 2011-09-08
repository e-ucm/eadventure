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

package es.eucm.eadventure.common.impl.reader.subparsers.extra;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import es.eucm.eadventure.common.params.EAdFill;
import es.eucm.eadventure.common.params.EAdFontImpl;
import es.eucm.eadventure.common.params.EAdParam;
import es.eucm.eadventure.common.params.EAdString;
import es.eucm.eadventure.common.params.EAdURI;
import es.eucm.eadventure.common.params.EAdURIImpl;
import es.eucm.eadventure.common.params.fills.impl.EAdBorderedColor;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.fills.impl.EAdLinearGradient;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdRectangleImpl;
import es.eucm.eadventure.common.resources.EAdBundleId;

/**
 * Includes methods to generate an object of a given type from a string value
 */
public class ObjectFactory {

	private static final Logger logger = Logger.getLogger("ObjectFactory");
	
	private static Map<String, EAdParam> paramsMap = new HashMap<String, EAdParam>();

	@SuppressWarnings("unchecked")
	public static Object getObject(String value, Class<?> fieldType) {
		if ( EAdParam.class.isAssignableFrom(fieldType)){
			if ( paramsMap.containsKey(value)){
				return paramsMap.get(value);
			}
			else {
				EAdParam param = null;
				if (fieldType == EAdPositionImpl.class)
					param = new EAdPositionImpl(value);
				else if (fieldType == EAdFontImpl.class)
					param = new EAdFontImpl(value);
				else if (fieldType == EAdRectangleImpl.class)
					param = new EAdRectangleImpl(value);
				else if (EAdFill.class.isAssignableFrom(fieldType)) {
					if (value.contains(EAdBorderedColor.SEPARATOR)) {
						param = new EAdBorderedColor(value);
					} else if (value.contains(EAdLinearGradient.SEPARATOR)) {
						param = new EAdLinearGradient(value);
					} else
						param = new EAdColor(value);
				} else if (fieldType == EAdBorderedColor.class)
					param = new EAdBorderedColor(value);
				else if (fieldType == EAdLinearGradient.class)
					param = new EAdLinearGradient(value);
				else if (fieldType == EAdColor.class)
					param = new EAdColor(value);
				else if (fieldType == EAdURI.class)
					param = new EAdURIImpl(value);
				else if (fieldType == EAdString.class) {
					param = new EAdString(value);
				}
				return param;
			}
		}
		if (fieldType == null || fieldType == String.class )
			return value;
		if (fieldType == Integer.class || fieldType == int.class)
			return Integer.parseInt(value);
		else if (fieldType == Boolean.class || fieldType == boolean.class)
			return Boolean.parseBoolean(value);
		else if (fieldType == Float.class || fieldType == float.class)
			return Float.parseFloat(value);
		else if (fieldType == EAdBundleId.class)
			return new EAdBundleId(value);
		else if (fieldType == Class.class)
			try {
				return ClassLoader.getSystemClassLoader().loadClass(value);
			} catch (ClassNotFoundException e) {
				return Object.class;
			}
		else if (fieldType.isEnum())
			return Enum.valueOf(fieldType.asSubclass(Enum.class), value);
		else{
			logger.info("The field type was not recognised. The string is returned");
			return value;
		}

	}

	public static void initilize() {
		paramsMap.clear();
	}
}
