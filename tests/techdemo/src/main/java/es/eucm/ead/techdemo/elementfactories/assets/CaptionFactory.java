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

package es.eucm.ead.techdemo.elementfactories.assets;

import es.eucm.ead.model.assets.drawable.basics.Caption;
import es.eucm.ead.model.assets.text.BasicFont;
import es.eucm.ead.model.assets.text.EAdFont;
import es.eucm.ead.model.params.fills.ColorFill;
import es.eucm.ead.model.params.fills.LinearGradientFill;
import es.eucm.ead.model.params.fills.Paint;
import es.eucm.ead.model.params.paint.EAdFill;
import es.eucm.ead.model.params.paint.EAdPaint;
import es.eucm.ead.model.params.text.EAdString;
import es.eucm.ead.techdemo.elementfactories.EAdElementsFactory;

public class CaptionFactory {

	private EAdFill fill = new LinearGradientFill(ColorFill.GRAY,
			ColorFill.LIGHT_GRAY, 20, 20, true);

	private EAdFont droidFont = BasicFont.BIG;

	public Caption createCaption(String text, EAdPaint textFill,
			EAdPaint bubbleFill, EAdFont font) {
		Caption caption = new Caption(new EAdString("someString"
				+ (int) (Math.random() * 9928109)));
		EAdElementsFactory.getInstance().getStringFactory().setString(
				caption.getText(), text);

		caption.setTextPaint(textFill);
		caption.setBubblePaint(bubbleFill);
		caption.setFont(font);
		caption.setPadding(20);
		return caption;

	}

	public Caption createCaption(String text, EAdPaint textFill,
			EAdFill bubbleFill) {
		return createCaption(text, textFill, bubbleFill, droidFont);
	}

	public Caption createCaption(String text) {
		return createCaption(text, Paint.WHITE_ON_BLACK, fill);
	}

}
