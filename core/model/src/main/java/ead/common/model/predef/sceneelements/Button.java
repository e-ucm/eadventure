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

package ead.common.model.predef.sceneelements;

import ead.common.model.elements.guievents.MouseGEv;
import ead.common.model.elements.scenes.ComplexSceneElement;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.predef.effects.ChangeAppearanceEf;
import ead.common.params.fills.ColorFill;
import ead.common.params.fills.LinearGradientFill;
import ead.common.params.fills.Paint;
import ead.common.params.text.EAdString;
import ead.common.resources.EAdBundleId;
import ead.common.resources.assets.drawable.basics.Caption;
import ead.common.resources.assets.drawable.basics.shapes.RectangleShape;
import ead.common.resources.assets.text.BasicFont;
import ead.common.util.EAdPosition.Corner;

public class Button extends ComplexSceneElement {

	private Caption caption;

	public Button() {
		super();
		setId("button");
		caption = new Caption();
		caption.setFont(new BasicFont(12));
		caption.setTextPaint(ColorFill.BLACK);

		SceneElement text = new SceneElement(caption);
		text.setId("text");
		text.setPosition(Corner.CENTER, 100, 15);
		text.setVarInitialValue(SceneElement.VAR_ENABLE, Boolean.FALSE);

		createButton();
		getSceneElements().add(text);
	}

	public EAdString getLabel() {
		return caption.getText();
	}

	private void createButton() {
		ColorFill lightGray = new ColorFill(200, 200, 200);
		RectangleShape buttonBgNormal = new RectangleShape(200, 30);
		buttonBgNormal.setPaint(new Paint(new LinearGradientFill(
				ColorFill.WHITE, lightGray, 0, 20), ColorFill.BLACK));
		RectangleShape buttonBgOver = new RectangleShape(200, 30);
		buttonBgOver.setPaint(new Paint(new LinearGradientFill(lightGray,
				ColorFill.WHITE, 0, 20), ColorFill.BLACK));

		definition.getResources().addAsset(definition.getInitialBundle(),
				SceneElementDef.appearance, buttonBgNormal);
		EAdBundleId over = new EAdBundleId("over");
		definition.getResources().addBundle(over);
		definition.getResources().addAsset(over,
				SceneElementDef.appearance, buttonBgOver);
		setPosition(Corner.CENTER, 0, 0);

		ChangeAppearanceEf changeAppearance = new ChangeAppearanceEf(this,
				definition.getInitialBundle());
		ChangeAppearanceEf changeAppearance2 = new ChangeAppearanceEf(this,
				over);
		addBehavior(MouseGEv.MOUSE_EXITED, changeAppearance);
		addBehavior(MouseGEv.MOUSE_ENTERED, changeAppearance2);
	}

}
