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

package es.eucm.ead.tools.java.xml.sax;

import es.eucm.ead.tools.xml.XMLNode;
import es.eucm.ead.tools.xml.XMLNodeList;
import org.xml.sax.Attributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaxXMLNode implements XMLNode {

	private String tag;

	private String text;

	private Map<String, String> attributes;

	private SaxXMLNodeList children;

	public SaxXMLNode(String tag) {
		this.tag = tag;
		this.text = "";
		this.attributes = new HashMap<String, String>();
		this.children = new SaxXMLNodeList();
	}

	public SaxXMLNode(String tag, Attributes attributes) {
		this(tag);
		for (int i = 0; i < attributes.getLength(); i++) {
			String key = attributes.getLocalName(i);
			String value = attributes.getValue(i);
			this.attributes.put(key, value);
		}
	}

	@Override
	public String getNodeText() {
		return text;
	}

	@Override
	public XMLNodeList getChildNodes() {
		return children;
	}

	@Override
	public String getNodeName() {
		return tag;
	}

	@Override
	public boolean hasChildNodes() {
		return children.getLength() > 0;
	}

	@Override
	public XMLNode getFirstChild() {
		return children.getLength() > 0 ? children.item(0) : null;
	}

	@Override
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public void setAttribute(String key, String value) {
		this.attributes.put(key, value);
	}

	@Override
	public void append(XMLNode node) {
		this.children.append((SaxXMLNode) node);
	}

	@Override
	public String getAttributeValue(String atttributeName) {
		return attributes.get(atttributeName);
	}

	@Override
	public int getAttributesLength() {
		return attributes.size();
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public List<SaxXMLNode> getNodes() {
		return children.getNodes();
	}
}
