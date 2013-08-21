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

package es.eucm.ead.tools.xml;

import org.xml.sax.Attributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * General interface for XML nodes
 * 
 */
public class XMLNode {

	private String tag;

	private String text;

	private Map<String, String> attributes;

	private List<XMLNode> children;

	public XMLNode(String tag) {
		this.tag = tag;
		this.text = "";
		this.attributes = new HashMap<String, String>();
		this.children = new ArrayList<XMLNode>();
	}

	public XMLNode(String tag, Attributes attributes) {
		this(tag);
		for (int i = 0; i < attributes.getLength(); i++) {
			String key = attributes.getLocalName(i);
			String value = attributes.getValue(i);
			this.attributes.put(key, value);
		}
	}

	/**
	 * @return the text contained by this node
	 */
	public String getNodeText() {
		return text;
	}

	/**
	 * @return the node name
	 */
	public String getNodeName() {
		return tag;
	}

	/**
	 *
	 * @return whether this node has child nodes or not
	 */
	public boolean hasChildNodes() {
		return children.size() > 0;
	}

	/**
	 * Returns the first child node
	 *
	 * @return
	 */
	public XMLNode getFirstChild() {
		return children.size() > 0 ? children.get(0) : null;
	}

	/**
	 * Sets the contained text in this node
	 *
	 * @param text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Sets the value for the attribute
	 *
	 * @param key
	 * @param value
	 */
	public void setAttribute(String key, String value) {
		this.attributes.put(key, value);
	}

	/**
	 * Appends a child to this node
	 *
	 * @param node
	 */
	public void append(XMLNode node) {
		this.children.add(node);
	}

	/**
	 * Returns the value for a given attribute. Returns null if the attribute is
	 * not present in the node
	 *
	 * @param atttributeName
	 *            the attribute name
	 * @return
	 */
	public String getAttributeValue(String atttributeName) {
		return attributes.get(atttributeName);
	}

	/**
	 * Returns the number of attributes contained by this node
	 * @return
	 */
	public int getAttributesLength() {
		return attributes.size();
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public List<XMLNode> getChildren() {
		return children;
	}

	public void copy(XMLNode n) {
		this.attributes = n.attributes;
		this.text = n.text;
		this.children = n.children;
	}

	public void setChildren(List<XMLNode> children) {
		this.children = children;
	}
}
