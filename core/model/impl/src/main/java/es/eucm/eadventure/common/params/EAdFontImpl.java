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

package es.eucm.eadventure.common.params;

import es.eucm.eadventure.common.resources.EAdURI;
import es.eucm.eadventure.common.resources.assets.impl.EAdURIImpl;

/**
 * EAdFont represents a text font and its metrics
 * 
 */
public class EAdFontImpl implements EAdFont {

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
	private Style style;

	/**
	 * URI to the *.ttf file for the name font
	 */
	private EAdURI uri;

	/**
	 * Regular EAdFont
	 */
	public static final EAdFontImpl REGULAR = new EAdFontImpl(25.0f);

	public static final EAdFontImpl REGULAR_BOLD = new EAdFontImpl(null, 25.0f,
			Style.BOLD);

	/**
	 * Big EAdFont
	 */
	public static final EAdFontImpl BIG = new EAdFontImpl(35.0f);

	public EAdFontImpl(float size) {
		this("Arial", size);
	}

	public EAdFontImpl(EAdURI uri, float size) {
		this(uri.getPath(), size);
		this.uri = uri;
	}

	@Override
	public boolean isTTF() {
		return uri != null;
	}

	@Override
	public EAdURI getURI() {
		return uri;
	}

	public EAdFontImpl(String data) {
		parse(data);
	}

	public EAdFontImpl(String name, float size) {
		this(name, size, Style.PLAIN);
	}

	public EAdFontImpl(String name, float size, Style style) {
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
	public boolean equals(Object o) {
		if (o != null && o instanceof EAdFontImpl) {
			EAdFontImpl temp = (EAdFontImpl) o;
			if (temp.name.equals(name) && temp.size == size
					&& temp.style.equals(style))
				return true;
		}
		return false;
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
	public Style getStyle() {
		return style;
	}

	/**
	 * @param style
	 *            the style to set
	 */
	public void setStyle(Style style) {
		this.style = style;
	}

	@Override
	public String toStringData() {
		String data = "";
		if (uri != null)
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
			uri = new EAdURIImpl(uriData[1]);
		}

		size = Float.parseFloat(strings[1]);
		style = Style.valueOf(strings[2]);

	}

}
