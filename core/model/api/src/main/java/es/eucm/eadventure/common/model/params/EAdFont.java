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

package es.eucm.eadventure.common.model.params;

/**
 * EAdFont represents a text font and its metrics
 * 
 */
public class EAdFont {

	public static enum Style {BOLD, PLAIN, ITALIC};
	
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
	 * Regular EAdFont
	 */
	public static final EAdFont REGULAR = new EAdFont(25.0f);
	
	public static final EAdFont REGULAR_BOLD = new EAdFont("Arial", 25.0f, Style.BOLD);
	
	/**
	 * Big EAdFont
	 */
	public static final EAdFont BIG = new EAdFont(35.0f);
	
	public EAdFont(float size) {
		this("Arial", size);
	}

	public EAdFont(String name, float size) {
		this(name, size, Style.PLAIN);
	}
	
	public EAdFont(String name, float size, Style style) {
		setName(name);
		setSize(size);
		setStyle(style);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 * name;size;style
	 */
	@Override
	public String toString() {
		return name + ";" + size + ";" + style;
	}
	
	public static EAdFont valueOf(String string) {
		String[] strings = string.split(";");
		String name = strings[0];
		float size = Float.parseFloat(strings[1]);
		Style style = Style.valueOf(strings[2]);
		return new EAdFont(name, size, style);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o != null && o instanceof EAdFont) {
			EAdFont temp = (EAdFont) o;
			if (temp.name.equals(name)
					&& temp.size == size
					&& temp.style.equals(style))
				return true;
		}
		return false;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the size
	 */
	public float getSize() {
		return size;
	}

	/**
	 * @param size the size to set
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
	 * @param style the style to set
	 */
	public void setStyle(Style style) {
		this.style = style;
	}
	
}
