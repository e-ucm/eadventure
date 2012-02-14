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

package ead.elementfactories.assets;

import ead.common.params.BasicFont;
import ead.common.params.fills.ColorFill;
import ead.common.params.fills.LinearGradientFill;
import ead.common.params.fills.PaintFill;
import ead.common.params.paint.EAdFill;
import ead.common.params.text.EAdFont;
import ead.common.resources.assets.drawable.basics.Caption;
import ead.common.util.EAdURI;
import ead.elementfactories.EAdElementsFactory;

public class CaptionFactory {
	
	private EAdFill fill = new LinearGradientFill(ColorFill.GRAY, ColorFill.LIGHT_GRAY, 20, 20, true);
	
	private EAdFont droidFont = new BasicFont( new EAdURI( "@binary/DroidSans-Bold.ttf"), 20);

	public Caption createCaption(String text, EAdFill textFill,
			EAdFill bubbleFill, EAdFont font) {
		Caption caption = new Caption();
		EAdElementsFactory.getInstance().getStringFactory().setString(caption.getText(), text);

		caption.setTextPaint(textFill);
		caption.setBubblePaint(bubbleFill);
		caption.setFont(font);
		caption.setPadding(20);
		return caption;

	}

	public Caption createCaption(String text, EAdFill textFill,
			EAdFill bubbleFill) {
		return createCaption(text, textFill, bubbleFill, droidFont );
	}

	public Caption createCaption(String text) {
		return createCaption(text, PaintFill.WHITE_ON_BLACK, fill );
	}

}
