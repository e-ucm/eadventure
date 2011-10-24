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

package es.eucm.eadventure.common.model.elements.impl.extra;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.behaviors.impl.EAdBehaviorImpl;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.params.EAdString;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl.Corner;
import es.eucm.eadventure.common.predef.model.effects.EAdChangeAppearance;
import es.eucm.eadventure.common.resources.EAdBundleId;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Caption;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Image;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.CaptionImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.ImageImpl;
import es.eucm.eadventure.common.resources.assets.drawable.compounds.ComposedDrawable;
import es.eucm.eadventure.common.resources.assets.drawable.compounds.impl.ComposedDrawableImpl;
import es.eucm.eadventure.common.resources.assets.drawable.compounds.impl.DisplacedDrawableImpl;

@Element(runtime = EAdBasicSceneElement.class, detailed = EAdButton.class)
public class EAdButton extends EAdBasicSceneElement {

	@Param("text")
	private Caption text;

	public EAdButton(String id) {
		super(id);
		text = new CaptionImpl(EAdString.newEAdString("default"));
	}

	public void setText(Caption caption) {
		this.text = caption;
	}

	public Caption getText() {
		return text;
	}

	public void setUpNewInstance() {
		Image image = new ImageImpl("@drawable/button_normal.png");

		DisplacedDrawableImpl dd = new DisplacedDrawableImpl(this.text,
				new EAdPositionImpl(Corner.CENTER, -30, -40));

		ComposedDrawable cd = new ComposedDrawableImpl();
		cd.addDrawable(image);
		cd.addDrawable(dd);

		getResources().addAsset(getInitialBundle(),
				EAdBasicSceneElement.appearance, cd);

		EAdBundleId pressedBundle = new EAdBundleId("pressed");
		Image image2 = new ImageImpl("@drawable/button_pressed.png");
		ComposedDrawable cd2 = new ComposedDrawableImpl();
		cd2.addDrawable(image2);
		cd2.addDrawable(dd);

		getResources().addAsset(pressedBundle, EAdBasicSceneElement.appearance,
				cd2);

		behavior = new EAdBehaviorImpl(id + "_behavior");
		// behavior.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, new
		// EAdQuitGame(behavior, "menuButton_quitGame"));
		setBehavior(behavior);

		EAdChangeAppearance changeAppearance = new EAdChangeAppearance(
				"menuButton_enter", this, pressedBundle);
		behavior.addBehavior(EAdMouseEventImpl.MOUSE_ENTERED, changeAppearance);

		changeAppearance = new EAdChangeAppearance("menuButton_exit", this,
				this.getInitialBundle());
		behavior.addBehavior(EAdMouseEventImpl.MOUSE_EXITED, changeAppearance);
	}

}
