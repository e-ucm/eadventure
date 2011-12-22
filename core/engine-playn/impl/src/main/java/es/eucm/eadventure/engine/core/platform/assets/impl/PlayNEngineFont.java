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

package es.eucm.eadventure.engine.core.platform.assets.impl;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;

import es.eucm.eadventure.common.params.text.EAdFont;
import es.eucm.eadventure.common.util.EAdRectangleImpl;
import es.eucm.eadventure.engine.core.platform.RuntimeFont;

public class PlayNEngineFont implements RuntimeFont {

	private EAdFont font;

	private Element element;

	private static final String HEIGHT_TEXT = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZabcefghijklmnñopqrstuvwxyz1234567890!?";

	public PlayNEngineFont(EAdFont font) {
		this.font = font;
		Document doc = Document.get();

		if (element == null) {
			Element rootElement = doc.getBody();

			element = doc.createDivElement();
			element.getStyle().setVisibility(Style.Visibility.HIDDEN);
			element.getStyle().setPosition(Style.Position.ABSOLUTE);
			element.getStyle().setTop(-800, Unit.PX);
			element.getStyle().setOverflow(Style.Overflow.VISIBLE);
			rootElement.appendChild(element);
		}

	}

	@Override
	public EAdFont getEAdFont() {
		return font;
	}

	@Override
	public int stringWidth(String string) {
		prepareElement();
		element.setInnerText(string); 
		return element.getOffsetWidth();
	}

	@Override
	public int lineHeight() {
		prepareElement();
		return element.getOffsetHeight();
	}

	@Override
	public EAdRectangleImpl stringBounds(String string) {
		return new EAdRectangleImpl(0, 0, stringWidth(string), lineHeight());
	}
	
	private void prepareElement(){
		element.getStyle().setFontSize(font.getSize(), Unit.PX);
		element.getStyle().setFontWeight(Style.FontWeight.NORMAL);
		element.getStyle().setFontStyle(Style.FontStyle.NORMAL);
		element.setInnerText(HEIGHT_TEXT);
		switch (getStyle(font)) {
		case BOLD:
			element.getStyle().setFontWeight(Style.FontWeight.BOLD);
			break;
		case ITALIC:
			element.getStyle().setFontStyle(Style.FontStyle.ITALIC);
			break;
		}
	}

	private playn.core.Font.Style getStyle(EAdFont font) {
		switch (font.getStyle()) {
		case BOLD:
			return playn.core.Font.Style.BOLD;
		case ITALIC:
			return playn.core.Font.Style.ITALIC;
		default:
			return playn.core.Font.Style.PLAIN;
		}
	}

}
