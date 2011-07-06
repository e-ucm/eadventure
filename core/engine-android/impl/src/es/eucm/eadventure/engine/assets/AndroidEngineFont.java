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

package es.eucm.eadventure.engine.assets;

import java.io.File;

import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import es.eucm.eadventure.common.model.params.EAdFont;
import es.eucm.eadventure.common.model.params.EAdFont.Style;
import es.eucm.eadventure.common.model.params.EAdRectangle;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.RuntimeFont;

public class AndroidEngineFont implements RuntimeFont {

	private EAdFont eadFont;

	private Typeface font;
	
	private Paint textPaint;

	public AndroidEngineFont(EAdFont font, AssetHandler assetHandler) {
		this.eadFont = font;
		if (eadFont.isTTF()) {
				this.font = Typeface.createFromFile(
						new File(assetHandler
								.getAbsolutePath(eadFont.getURI().getURI())));
		} else
			this.font = Typeface.create(font.getName(), getStyle(font.getStyle()));
		textPaint = new Paint();
	}

	private int getStyle(Style style) {
		switch (style) {
		case BOLD:
			return Typeface.BOLD;
		case ITALIC:
			return Typeface.ITALIC;
		case PLAIN:
		default:
			return Typeface.NORMAL;
		}
	}

	public Typeface getFont() {
		return font;
	}

	@Override
	public EAdFont getEAdFont() {
		return eadFont;
	}

	@Override
	public int stringWidth(String string) {
		Rect bounds = new Rect();
		textPaint.getTextBounds(string,0,string.length(),bounds);
		return bounds.width();
	}

	@Override
	public int lineHeight() {
		Rect bounds = new Rect();
		textPaint.getTextBounds("b",0,"b".length(),bounds);
		return bounds.height();
	}

	@Override
	public EAdRectangle stringBounds(String string) {
		Rect bounds = new Rect();
		textPaint.getTextBounds(string,0,string.length(),bounds);
		return new EAdRectangle(0, 0, bounds.width(), bounds.height());
	}

}
