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

package ead.elementfactories.demos.scenes;

import ead.common.model.elements.enums.CommonStates;
import ead.common.model.elements.guievents.EAdMouseEvent;
import ead.common.model.elements.scenes.SceneElementImpl;
import ead.common.model.elements.variables.EAdFieldImpl;
import ead.common.model.elements.variables.operations.ValueOp;
import ead.common.params.fills.EAdColor;
import ead.common.params.fills.EAdLinearGradient;
import ead.common.params.fills.EAdPaintImpl;
import ead.common.params.paint.EAdFill;
import ead.common.resources.assets.drawable.basics.CaptionImpl;
import ead.common.resources.assets.drawable.basics.ImageImpl;
import ead.common.resources.assets.drawable.basics.animation.FramesAnimation;
import ead.common.resources.assets.drawable.basics.shapes.RectangleShape;
import ead.common.resources.assets.drawable.compounds.ComposedDrawable;
import ead.common.resources.assets.drawable.compounds.ComposedDrawableImpl;
import ead.common.resources.assets.drawable.compounds.StateDrawable;
import ead.common.resources.assets.drawable.compounds.StateDrawableImpl;
import ead.elementfactories.EAdElementsFactory;

public class DrawablesScene extends EmptyScene {

	public DrawablesScene() {
		setBackgroundFill(new EAdColor(240, 255, 255));
		int margin = 50;
		int x = margin;
		int y = margin;

		// eAdventure logo
		ImageImpl drawable = new ImageImpl("@drawable/eAdventureLogo.png");
		CaptionImpl caption = EAdElementsFactory.getInstance()
				.getCaptionFactory().createCaption("Image");
		caption.setPadding(0);
		caption.setTextPaint(EAdColor.BLACK);
		ComposedDrawable composed = new ComposedDrawableImpl();
		composed.addDrawable(caption);
		composed.addDrawable(drawable, 0, 40);
		getComponents().add(
				EAdElementsFactory.getInstance().getSceneElementFactory()
						.createSceneElement(composed, x, y));

		x += 200;

		// Shaded rectangle
		caption = EAdElementsFactory.getInstance().getCaptionFactory()
				.createCaption("Composed Drawable");
		caption.setPadding(0);
		caption.setTextPaint(EAdColor.BLACK);

		int rWidth = 60;
		int rHeight = 40;

		RectangleShape rectangle = new RectangleShape(rWidth, rHeight);
		rectangle.setPaint(new EAdColor(255, 0, 0));

		RectangleShape rectangleShadow = new RectangleShape(rWidth, rHeight);
		rectangleShadow.setPaint(new EAdColor(100, 100, 100, 100));

		RectangleShape rectangleB = new RectangleShape(rWidth, rHeight);
		rectangleB
				.setPaint(new EAdPaintImpl(EAdColor.GREEN, EAdColor.BLACK, 2));

		EAdFill p = new EAdLinearGradient(EAdColor.BLUE, EAdColor.MAGENTA,
				rWidth, rHeight, false);
		RectangleShape rectangleG = new RectangleShape(rWidth, rHeight);
		rectangleG.setPaint(p);

		RectangleShape rectangle3 = new RectangleShape(rWidth, rHeight);
		rectangle3.setPaint(new EAdPaintImpl(
				new EAdLinearGradient(EAdColor.GREEN, EAdColor.YELLOW,
						rWidth / 2, rHeight / 2, false), EAdColor.RED));

		RectangleShape rectangle4 = new RectangleShape(rWidth, rHeight);
		rectangle4.setPaint(new EAdPaintImpl(null, EAdColor.BROWN, 4));

		RectangleShape rectangle5 = new RectangleShape(rWidth, rHeight);
		rectangle5.setPaint(new EAdPaintImpl(EAdColor.WHITE, p, 8));

		ComposedDrawable composedDrawable = new ComposedDrawableImpl();
		composedDrawable.addDrawable(rectangleShadow, 6, 6);
		composedDrawable.addDrawable(rectangle);
		composedDrawable.addDrawable(rectangleShadow, rWidth + 16, 6);
		composedDrawable.addDrawable(rectangleB, rWidth + 10, 0);
		composedDrawable.addDrawable(rectangleShadow, 2 * (rWidth + 10) + 6, 6);
		composedDrawable.addDrawable(rectangleG, 2 * (rWidth + 10), 0);
		// Row 2
		int height2 = rHeight + 10;
		composedDrawable.addDrawable(rectangleShadow, 6, 6 + height2);
		composedDrawable.addDrawable(rectangle3, 0, height2);
		composedDrawable.addDrawable(rectangleShadow, rWidth + 16, 6 + height2);
		composedDrawable.addDrawable(rectangle4, rWidth + 10, height2);
		composedDrawable.addDrawable(rectangleShadow, 2 * (rWidth + 10) + 6,
				6 + height2);
		composedDrawable.addDrawable(rectangle5, 2 * (rWidth + 10), height2);

		composed = new ComposedDrawableImpl();
		composed.addDrawable(caption);
		composed.addDrawable(composedDrawable, 0, 40);

		getComponents().add(
				EAdElementsFactory.getInstance().getSceneElementFactory()
						.createSceneElement(composed, x, y));

		x += 250;

		// State drawable
		RectangleShape rectangle1 = new RectangleShape(rHeight, rHeight);
		rectangle1.setPaint(new EAdPaintImpl(EAdColor.LIGHT_BROWN,
				EAdColor.LIGHT_BROWN, 8));

		RectangleShape rectangle2 = new RectangleShape(rHeight, rHeight);
		rectangle2.setPaint(new EAdPaintImpl(EAdColor.BLACK,
				EAdColor.LIGHT_BROWN, 8));

		StateDrawable stateDrawable = new StateDrawableImpl();
		stateDrawable.addDrawable(CommonStates.EAD_STATE_DEFAULT.toString(),
				rectangle1);
		stateDrawable.addDrawable(CommonStates.EAD_STATE_TALKING.toString(),
				rectangle2);

		caption = EAdElementsFactory.getInstance().getCaptionFactory()
				.createCaption("State Drawable");
		caption.setPadding(0);
		caption.setTextPaint(EAdColor.BLACK);

		getComponents().add(
				EAdElementsFactory.getInstance().getSceneElementFactory()
						.createSceneElement(caption, x, y));
		SceneElementImpl sceneElement = EAdElementsFactory.getInstance()
				.getSceneElementFactory()
				.createSceneElement(stateDrawable, x + 50, y + 40);
		sceneElement.addBehavior(
				EAdMouseEvent.MOUSE_RIGHT_CLICK,
				EAdElementsFactory
						.getInstance()
						.getEffectFactory()
						.getChangeVarValueEffect(
								new EAdFieldImpl<String>(sceneElement,
										SceneElementImpl.VAR_STATE),
								new ValueOp(CommonStates.EAD_STATE_DEFAULT
												.toString())));
		sceneElement.addBehavior(
				EAdMouseEvent.MOUSE_LEFT_CLICK,
				EAdElementsFactory
						.getInstance()
						.getEffectFactory()
						.getChangeVarValueEffect(
								new EAdFieldImpl<String>(sceneElement,
										SceneElementImpl.VAR_STATE),
								new ValueOp(CommonStates.EAD_STATE_TALKING
												.toString())));
		getComponents().add(sceneElement);

		// Frames
		String uris[] = new String[8];
		for (int i = 1; i <= 8; i++)
			uris[i - 1] = "@drawable/paniel_wlr_0" + i + ".png";

		FramesAnimation animation = EAdElementsFactory.getInstance()
				.getDrawableFactory().getFramesAnimation(uris, 500);

		caption = EAdElementsFactory.getInstance().getCaptionFactory()
				.createCaption("Frames animation");
		caption.setPadding(0);
		caption.setTextPaint(EAdColor.BLACK);

		getComponents().add(
				EAdElementsFactory.getInstance().getSceneElementFactory()
						.createSceneElement(caption, margin, 180));

		SceneElementImpl paniel = EAdElementsFactory.getInstance()
				.getSceneElementFactory()
				.createSceneElement(animation, margin, 220);
		paniel.setScale(0.8f);
		getComponents().add(paniel);

	}

	@Override
	public String getSceneDescription() {
		return "A scene showing some drawables of eAdventure";
	}

	public String getDemoName() {
		return "Drawables Scene";
	}
}
