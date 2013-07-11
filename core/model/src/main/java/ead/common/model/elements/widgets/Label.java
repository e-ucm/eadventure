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

package ead.common.model.elements.widgets;

import ead.common.model.assets.drawable.basics.Caption;
import ead.common.model.assets.text.BasicFont;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.params.fills.ColorFill;
import ead.common.model.params.text.EAdString;

public class Label extends SceneElement {

	private Caption c;

	public Label(String string) {
		this(new EAdString(string));
	}

	public Label(EAdString string) {
		c = new Caption(string);
		c.setTextPaint(ColorFill.BLACK);
		c.setPadding(0);
		c.setBubblePaint(ColorFill.TRANSPARENT);
		c.setHasBubble(false);
		c.setFont(BasicFont.REGULAR);
		setAppearance(c);
	}

	public void setFontSize(float size) {
		c.getFont().setSize(size);
	}

	public void setColor(ColorFill color) {
		c.setTextPaint(color);
	}

	public void setBgColor(ColorFill color) {
		c.setHasBubble(true);
		c.setBubblePaint(color);
	}

	public Caption getCaption() {
		return c;
	}

}
