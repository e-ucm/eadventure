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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.variables.EAdVar;
import es.eucm.eadventure.common.model.variables.impl.EAdVarImpl;
import es.eucm.eadventure.common.params.EAdFill;
import es.eucm.eadventure.common.params.EAdFont;
import es.eucm.eadventure.common.params.EAdFontImpl;
import es.eucm.eadventure.common.params.fills.impl.EAdBorderedColor;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.fills.impl.EAdLinearGradient;
import es.eucm.eadventure.common.params.geom.EAdPosition;
import es.eucm.eadventure.common.params.geom.EAdRectangle;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdRectangleImpl;
import es.eucm.eadventure.common.resources.EAdBundleId;
import es.eucm.eadventure.common.resources.EAdString;
import es.eucm.eadventure.common.resources.EAdURI;
import es.eucm.eadventure.common.resources.assets.impl.EAdURIImpl;

/**
 * Includes methods to generate an object of a given type from a string value
 */
public class ObjectFactory {

	private static final Logger logger = Logger.getLogger("ObjectFactory");

	private static Map<String, EAdElement> elementMap;

	private static Map<String, ArrayList<EAdVar<?>>> pendingVarMap;

	@SuppressWarnings("unchecked")
	public static Object getObject(String value, Class<?> fieldType) {
		if (fieldType == null)
			return value;
		if (fieldType == Integer.class || fieldType == int.class)
			return Integer.parseInt(value);
		else if (fieldType == Boolean.class || fieldType == boolean.class)
			return Boolean.parseBoolean(value);
		else if (fieldType == Float.class || fieldType == float.class)
			return Float.parseFloat(value);
		else if (fieldType == EAdPosition.class)
			return new EAdPositionImpl(value);
		else if (fieldType == EAdFont.class)
			return new EAdFontImpl(value);
		else if (fieldType == EAdRectangle.class)
			return new EAdRectangleImpl(value);
		else if (fieldType == EAdFill.class) {
			if (value.contains(":")) {
				return new EAdBorderedColor(value);
			} else if (value.contains(";")) {
				return new EAdLinearGradient(value);
			} else
				return new EAdColor(value);
		} else if (fieldType == EAdBorderedColor.class)
			return new EAdBorderedColor(value);
		else if (fieldType == EAdLinearGradient.class)
			return new EAdLinearGradient(value);
		else if (fieldType == EAdColor.class)
			return new EAdColor(value);
		else if (fieldType == EAdURI.class)
			return new EAdURIImpl(value);
		else if (fieldType == EAdString.class || fieldType == EAdString.class) {
			// TODO register?
			return new EAdString(value);
		} else if (fieldType == EAdBundleId.class)
			return new EAdBundleId(value);
		// else if (fieldType == EAdVar.class) {
		// EAdVar<?> o = NumberVar.valueOf(value);
		// if (o == null)
		// o = BooleanVar.valueOf(value);
		// if (o == null)
		// o = StringVar.valueOf(value);
		// String[] temp = value.split(";");
		// if (temp.length == 3 && temp[2] != null && !temp[2].equals("")) {
		// if (elementMap.containsKey(temp[2]))
		// ((EAdVarImpl<?>) o).setElement(elementMap.get(temp[2]));
		// else {
		// if (pendingVarMap.get(temp[2]) == null)
		// pendingVarMap.put(temp[2], new ArrayList<EAdVar<?>>());
		// pendingVarMap.get(temp[2]).add(o);
		// }
		// }
		//
		// return o;
		// }
		else if (fieldType == Class.class)
			try {
				return ClassLoader.getSystemClassLoader().loadClass(value);
			} catch (ClassNotFoundException e) {
				return Object.class;
			}
		else if (fieldType.isEnum())
			return Enum.valueOf(fieldType.asSubclass(Enum.class), value);
		else if (elementMap.containsKey(value) || fieldType == EAdElement.class)
			return elementMap.get(value);
		else
			return value;

	}

	public static void initilize() {
		elementMap = new HashMap<String, EAdElement>();
		pendingVarMap = new HashMap<String, ArrayList<EAdVar<?>>>();
	}

	public static void put(String id, EAdElement element) {
		logger.info("Added element id:" + id + "; element:" + element);
		elementMap.put(id, element);
		if (pendingVarMap.get(id) != null)
			for (EAdVar<?> var : pendingVarMap.get(id))
				((EAdVarImpl<?>) var).setElement(element);
	}
}
