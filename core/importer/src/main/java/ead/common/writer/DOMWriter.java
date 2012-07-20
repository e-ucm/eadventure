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

package ead.common.writer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ead.common.model.EAdElement;
import ead.common.model.elements.EAdAdventureModel;
import ead.common.model.elements.BasicAdventureModel;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.extra.EAdMap;
import ead.common.params.EAdParam;
import ead.common.resources.EAdResources;
import ead.common.resources.assets.AssetDescriptor;
import ead.reader.DOMTags;

import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract implementation of a DOMWriter
 *
 * @param <T>
 *            The type of the element writen by this DOMWriter
 */
public abstract class DOMWriter<T> {

	protected static final Logger logger = LoggerFactory.getLogger("DOMWriter");

	public static final boolean USE_PARAM_IDS = false;

	public static final boolean USE_DEFAULT_VALUES = true;

	/**
	 * The xml document
	 */
	protected static Document doc;

	/**
	 * Element map
	 */
	protected static Map<EAdElement, String> elementMap = new HashMap<EAdElement, String>();

	protected static ArrayList<EAdElement> mappedElement = new ArrayList<EAdElement>();

	/**
	 * A map to store repeated params and save some space in XML
	 */
	protected static Map<Object, String> paramsMap = new HashMap<Object, String>();

	public static DepthManager depthManager;

	/**
	 * A map to store repeated assets and save some space in XML
	 */
	protected static ArrayList<AssetDescriptor> mappedAsset = new ArrayList<AssetDescriptor>();
	
	protected static boolean error;

	public static void initMaps(EAdAdventureModel data) {
		elementMap.clear();
		mappedElement.clear();
		paramsMap.clear();
		mappedAsset.clear();
		depthManager = new DepthManager(((BasicAdventureModel) data).getDepthControlList());
		error = false;

		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.newDocument();
		} catch (ParserConfigurationException e) {
			logger.error("Error configuring parser for '{}'", data.getId(), e);
			error = true;
		}
	}
	
	public static Collection<Object> reorderKeys(Set<? extends Object> keys) {
		ArrayList<Object> reordered = new ArrayList<Object>(keys);
		Collections.sort(reordered, new Comparator<Object>() {
			@Override
			public int compare(Object a, Object b) {
				String ka = (a instanceof EAdElement) ? ((EAdElement)a).getId() :
						a.toString();
				String kb = (b instanceof EAdElement) ? ((EAdElement)b).getId() :
						b.toString();				
				return ka.compareTo(kb);
			}
		});		
		return reordered;
	}
	
	/**
	 * <p>
	 * Build the actual node created by this DOMWriter
	 * </p>
	 *
	 * @param data
	 *            The data to be placed in the node
	 * @param listClass
	 * @return The xml node created by the DOMWriter
	 */
	public abstract Element buildNode(T data, Class<?> listClass);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Element initNode(Object data, Class<?> listClass) {
		DOMWriter writer = getDOMWriter(data);
		return writer.buildNode(data, listClass);
	}

	@SuppressWarnings("rawtypes")
	public DOMWriter getDOMWriter(Object o) {
		if (o instanceof EAdElement) {
			return new ElementDOMWriter();
		} else if (o instanceof EAdList) {
			return new ListDOMWriter();
		} else if (o instanceof EAdMap) {
			return new MapDOMWriter();
		} else if (o instanceof EAdParam) {
			return new ParamDOMWriter();
		} else if (o instanceof EAdResources) {
			return new ResourcesDOMWriter();
		} else if (o instanceof AssetDescriptor) {
			return new AssetDOMWriter();
		} else {
			return new ParamDOMWriter();
		}
	}

	public String shortClass(String clazz) {
		String shortClass = clazz.startsWith(DOMTags.PACKAGE) ? clazz.substring(DOMTags.PACKAGE.length())
				: clazz;
		String alias = depthManager.getClassAliases().get(shortClass);
		if (alias == null) {
			alias = "" + convertToCode(depthManager.getClassAliases().keySet().size());
			depthManager.getAliasMap().put(alias, shortClass);
			depthManager.getClassAliases().put(shortClass, alias);
		}
		return alias;
	}

	public static String convertToCode(int val) {
		String code = "";
		if (val == 0)
			return "0";
		while (val > 0) {
			int t = val % 62;
			if (t < 10)
				code += t;
			else if (t < 36) {
				char c = (char) ('a' + t - 10);
				code += c;
			} else {
				char c = (char) ('A' + t - 36);
				code += c;
			}
			val = val / 62;
		}
		return code;
	}

	public static void clearMaps() {
		elementMap.clear();
		mappedElement.clear();
		paramsMap.clear();
		mappedAsset.clear();
		depthManager.clear();
	}

	public static boolean getError() {
		return error;
	}

}
