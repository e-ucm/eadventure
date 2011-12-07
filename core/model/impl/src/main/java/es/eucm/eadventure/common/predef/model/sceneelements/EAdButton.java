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

package es.eucm.eadventure.common.predef.model.sceneelements;

import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdComplexElementImpl;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneElementDefImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.params.EAdFontImpl;
import es.eucm.eadventure.common.params.EAdString;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.fills.impl.EAdLinearGradient;
import es.eucm.eadventure.common.params.fills.impl.EAdPaintImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl.Corner;
import es.eucm.eadventure.common.predef.model.effects.EAdChangeAppearance;
import es.eucm.eadventure.common.resources.EAdBundleId;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.CaptionImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.RectangleShape;

public class EAdButton extends EAdComplexElementImpl {

	private CaptionImpl caption;

	public EAdButton() {
		super();
		setId("button");
		caption = new CaptionImpl();
		caption.setFont(new EAdFontImpl(12));
		caption.setTextPaint(EAdColor.BLACK);

		EAdBasicSceneElement text = new EAdBasicSceneElement(caption);
		text.setId("text");
		text.setPosition(Corner.CENTER, 100, 15);
		text.setVarInitialValue(EAdBasicSceneElement.VAR_ENABLE, Boolean.FALSE);

		createButton();
		getComponents().add(text);
	}

	public EAdString getLabel() {
		return caption.getText();
	}

	private void createButton() {
		EAdColor lightGray = new EAdColor(200, 200, 200);
		RectangleShape buttonBgNormal = new RectangleShape(200, 30);
		buttonBgNormal.setPaint(new EAdPaintImpl(new EAdLinearGradient(
				EAdColor.WHITE, lightGray, 0, 20), EAdColor.BLACK));
		RectangleShape buttonBgOver = new RectangleShape(200, 30);
		buttonBgOver.setPaint(new EAdPaintImpl(new EAdLinearGradient(lightGray,
				EAdColor.WHITE, 0, 20), EAdColor.BLACK));

		definition.getResources().addAsset(definition.getInitialBundle(),
				EAdSceneElementDefImpl.appearance, buttonBgNormal);
		EAdBundleId over = new EAdBundleId("over");
		definition.getResources().addBundle(over);
		definition.getResources().addAsset(over,
				EAdSceneElementDefImpl.appearance, buttonBgOver);
		setPosition(Corner.CENTER, 0, 0);

		EAdChangeAppearance changeAppearance = new EAdChangeAppearance(this,
				definition.getInitialBundle());
		EAdChangeAppearance changeAppearance2 = new EAdChangeAppearance(this,
				over);
		addBehavior(EAdMouseEventImpl.MOUSE_EXITED, changeAppearance);
		addBehavior(EAdMouseEventImpl.MOUSE_ENTERED, changeAppearance2);
	}

}
