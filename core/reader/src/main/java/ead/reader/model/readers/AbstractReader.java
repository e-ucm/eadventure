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

package ead.reader.model.readers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.reader.DOMTags;
import ead.reader.model.ObjectsFactory;
import ead.reader.model.XMLVisitor;
import ead.tools.xml.XMLNode;

public abstract class AbstractReader<T> implements Reader<T> {

	protected static final Logger logger = LoggerFactory
			.getLogger("ElementReader");

	protected ObjectsFactory elementsFactory;

	protected XMLVisitor xmlVisitor;

	public AbstractReader(ObjectsFactory elementsFactory, XMLVisitor xmlVisitor) {
		this.elementsFactory = elementsFactory;
		this.xmlVisitor = xmlVisitor;
	}

	/**
	 * Returns the class for the element contained in the given node
	 * @param node
	 * @return
	 */
	public Class<?> getNodeClass(XMLNode node) {
		String clazz = node.getAttributeValue(DOMTags.CLASS_AT);
		return clazz == null ? null : getNodeClass(clazz);
	}

	public Class<?> getNodeClass(String clazz) {
		clazz = translateClass(clazz);
		Class<?> c = null;
		try {
			c = elementsFactory.getClassFromName(clazz);
		} catch (NullPointerException e) {
			logger.error("Error resolving class {}", clazz, e);
		}
		return c;
	}

	/**
	 * Translate the class into its complete name
	 * @param clazz
	 * @return
	 */
	public String translateClass(String clazz) {
		return xmlVisitor.translateClazz(clazz);
	}

	public String translateField(String field) {
		return xmlVisitor.translateField(field);
	}

	public String translateParam(String param) {
		return xmlVisitor.translateParam(param);
	}

}
