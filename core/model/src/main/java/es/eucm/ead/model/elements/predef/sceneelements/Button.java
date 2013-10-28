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

package es.eucm.ead.model.elements.predef.sceneelements;

import es.eucm.ead.model.assets.drawable.basics.Caption;
import es.eucm.ead.model.assets.drawable.basics.shapes.RectangleShape;
import es.eucm.ead.model.assets.text.BasicFont;
import es.eucm.ead.model.elements.scenes.GroupElement;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.elements.scenes.SceneElementDef;
import es.eucm.ead.model.params.fills.ColorFill;
import es.eucm.ead.model.params.fills.LinearGradientFill;
import es.eucm.ead.model.params.fills.Paint;
import es.eucm.ead.model.params.text.EAdString;
import es.eucm.ead.model.params.util.Position.Corner;

public class Button extends GroupElement {

	public Button() {

	}

	public Button(EAdString string) {
		super();
		Caption caption = new Caption(string);
		caption.setFont(BasicFont.REGULAR);
		caption.setTextPaint(ColorFill.BLACK);

		SceneElement text = new SceneElement(caption);
		text.setPosition(Corner.CENTER, 100, 15);
		text.putProperty(SceneElement.VAR_ENABLE, Boolean.FALSE);

		createButton();
		getSceneElements().add(text);
	}

	private void createButton() {
		ColorFill lightGray = new ColorFill(200, 200, 200);
		RectangleShape buttonBgNormal = new RectangleShape(200, 30);
		buttonBgNormal.setPaint(new Paint(new LinearGradientFill(
				ColorFill.WHITE, lightGray, 0, 20), ColorFill.BLACK));
		RectangleShape buttonBgOver = new RectangleShape(200, 30);
		buttonBgOver.setPaint(new Paint(new LinearGradientFill(lightGray,
				ColorFill.WHITE, 0, 20), ColorFill.BLACK));
		SceneElementDef def = new SceneElementDef(buttonBgNormal, buttonBgOver);
		this.setDefinition(def);
		setPosition(Corner.CENTER, 0, 0);
	}

}
