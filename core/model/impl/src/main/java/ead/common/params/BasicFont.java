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

package ead.common.params;

import ead.common.params.text.EAdFont;
import ead.common.params.text.enums.FontStyle;
import ead.common.util.EAdURI;


/**
 * EAdFont represents a text font and its metrics
 * 
 * 
 * {@link EAdURI}
 */
public class BasicFont extends AbstractParam implements EAdFont {

	/**
	 * Name of the font
	 */
	private String name;

	/**
	 * Size of the font
	 */
	private float size;

	/**
	 * Style of the font
	 */
	private FontStyle style;

	/**
	 * URI to the *.ttf file for the name font
	 */
	private EAdURI uri;

	/**
	 * Regular EAdFont
	 */
	public static final BasicFont REGULAR = new BasicFont(25.0f);

	public static final BasicFont REGULAR_BOLD = new BasicFont(null, 25.0f,
			FontStyle.BOLD);

	/**
	 * Big EAdFont
	 */
	public static final BasicFont BIG = new BasicFont(35.0f);

	public BasicFont(float size) {
		this("Arial", size);
	}

	public BasicFont(EAdURI uri, float size) {
		this(uri.getPath(), size);
		this.uri = uri;
	}

	@Override
	public boolean isTTF() {
		return uri != null;
	}

	@Override
	public EAdURI getUri() {
		return uri;
	}

	public BasicFont(String data) {
		parse(data);
	}

	public BasicFont(String name, float size) {
		this(name, size, FontStyle.PLAIN);
	}

	public BasicFont(String name, float size, FontStyle style) {
		setName(name);
		setSize(size);
		setStyle(style);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString() name;size;style
	 */
	@Override
	public String toString() {
		return toStringData();
	}

	@Override
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public float getSize() {
		return size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(float size) {
		this.size = size;
	}

	/**
	 * @return the style
	 */
	public FontStyle getStyle() {
		return style;
	}

	/**
	 * @param style
	 *            the style to set
	 */
	public void setStyle(FontStyle style) {
		this.style = style;
	}

	@Override
	public String toStringData() {
		String data = "";
		if (uri == null)
			data += name;
		else
			data += "uri;" + uri.getPath();
		data += ":" + size + ":" + style;
		return data;
	}

	@Override
	public void parse(String data) {
		String[] strings = data.split(":");
		name = strings[0];
		if (name.startsWith("uri")) {
			String[] uriData = name.split(";");
			uri = new EAdURI(uriData[1]);
		}

		size = Float.parseFloat(strings[1]);
		style = FontStyle.valueOf(strings[2]);
	}

	@Override
	public void setUri(EAdURI uri) {
		this.uri = uri;
	}
	
	public int hashCode(){
		return toStringData().hashCode();
	}
	
	public boolean equals(Object o){
		return o.equals(toString());
	}

}
