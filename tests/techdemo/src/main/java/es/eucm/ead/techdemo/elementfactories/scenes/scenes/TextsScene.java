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

package es.eucm.ead.techdemo.elementfactories.scenes.scenes;

import es.eucm.ead.model.assets.drawable.basics.Caption;
import es.eucm.ead.model.assets.drawable.compounds.ComposedDrawable;
import es.eucm.ead.model.assets.text.BasicFont;
import es.eucm.ead.model.assets.text.EAdFont;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.params.fills.ColorFill;
import es.eucm.ead.model.params.text.EAdString;

public class TextsScene extends EmptyScene {

	public TextsScene() {
		this.setId("TextsScene");
		this.setBackgroundFill(ColorFill.DARK_GRAY);

		ComposedDrawable drawable = new ComposedDrawable();
		int y = 0;
		int multiplier = 2;
		// Regular
		for (float size = 10.0f; size <= 20.0f; size += 1.0f) {
			drawable.addDrawable(createTestCaption(BasicFont.REGULAR), 0, y);
			y += size * multiplier;
		}
		drawable.addDrawable(createTestCaption(BasicFont.BIG), 0, y);

		this.add(new SceneElement(drawable));

	}

	private Caption createTestCaption(EAdFont font) {
		Caption c = new Caption(new EAdString("#txt#Test text"));
		c.setTextPaint(ColorFill.WHITE);
		c.setBubblePaint(ColorFill.RED);
		c.setPadding(0);
		c.setFont(font);
		return c;
	}

}
