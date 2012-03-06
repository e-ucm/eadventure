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

package ead.engine.core.platform.assets;

import android.graphics.Paint;
import android.graphics.Typeface;

import com.google.inject.Inject;

import ead.common.resources.assets.text.enums.FontStyle;
import ead.common.util.EAdRectangle;
import ead.engine.core.platform.AssetHandler;
import ead.engine.core.platform.assets.text.BasicRuntimeFont;

public class AndroidFont extends BasicRuntimeFont {

	private Typeface font;
	
	private int size;

	private Paint textPaint;
	
	@Inject
	public AndroidFont(AssetHandler assetHandler){
		super(assetHandler);
	}
	

	public boolean loadAsset() {
		this.font = Typeface.create(descriptor.getName(), getStyle(descriptor.getStyle()));
		textPaint = new Paint();
		textPaint.setTypeface(this.font);
		size = (int) descriptor.getSize();
		textPaint.setTextSize(size);
		return super.loadAsset();
	}

	private int getStyle(FontStyle style) {
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
	public int stringWidth(String string) {
		return (int) textPaint.measureText(string);
	}

	@Override
	public int lineHeight() {
		return (int) (Math.abs(textPaint.ascent()) + textPaint.descent());
	}

	@Override
	public EAdRectangle stringBounds(String string) {
		return new EAdRectangle(0, 0, stringWidth(string), lineHeight());
	}
	
	public Typeface getTextPaint(){
		return font;
	}
	
	public int size(){
		return size;
	}

}
