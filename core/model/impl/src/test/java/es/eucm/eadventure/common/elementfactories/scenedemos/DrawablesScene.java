package es.eucm.eadventure.common.elementfactories.scenedemos;

import es.eucm.eadventure.common.elementfactories.EAdElementsFactory;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.model.variables.impl.operations.ValueOperation;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.fills.impl.EAdLinearGradient;
import es.eucm.eadventure.common.params.fills.impl.EAdPaintImpl;
import es.eucm.eadventure.common.params.paint.EAdFill;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Image;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.CaptionImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.ImageImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.animation.FramesAnimation;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.RectangleShape;
import es.eucm.eadventure.common.resources.assets.drawable.compounds.ComposedDrawable;
import es.eucm.eadventure.common.resources.assets.drawable.compounds.StateDrawable;
import es.eucm.eadventure.common.resources.assets.drawable.compounds.impl.ComposedDrawableImpl;
import es.eucm.eadventure.common.resources.assets.drawable.compounds.impl.StateDrawableImpl;

public class DrawablesScene extends EmptyScene {

	public DrawablesScene() {
		setBackgroundFill(new EAdColor(240, 255, 255));
		int margin = 50;
		int x = margin;
		int y = margin;

		// eAdventure logo
		Image drawable = new ImageImpl("@drawable/eAdventureLogo.png");
		CaptionImpl caption = EAdElementsFactory.getInstance()
				.getCaptionFactory().createCaption("Image");
		caption.setPadding(0);
		ComposedDrawable composed = new ComposedDrawableImpl();
		composed.addDrawable(caption);
		composed.addDrawable(drawable, 0, 40);
		getElements().add(
				EAdElementsFactory.getInstance().getSceneElementFactory()
						.createSceneElement(composed, x, y));

		x += 200;

		// Shaded rectangle
		caption = EAdElementsFactory.getInstance().getCaptionFactory()
				.createCaption("Composed Drawable");
		caption.setPadding(0);

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

		getElements().add(
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

		getElements().add(
				EAdElementsFactory.getInstance().getSceneElementFactory()
						.createSceneElement(caption, x, y));
		EAdBasicSceneElement sceneElement = EAdElementsFactory.getInstance()
				.getSceneElementFactory()
				.createSceneElement(stateDrawable, x + 50, y + 40);
		sceneElement.addBehavior(
				EAdMouseEventImpl.MOUSE_RIGHT_CLICK,
				EAdElementsFactory
						.getInstance()
						.getEffectFactory()
						.getChangeVarValueEffect(
								new EAdFieldImpl<String>(sceneElement,
										EAdBasicSceneElement.VAR_STATE),
								new ValueOperation("assign",
										CommonStates.EAD_STATE_DEFAULT
												.toString())));
		sceneElement.addBehavior(
				EAdMouseEventImpl.MOUSE_LEFT_CLICK,
				EAdElementsFactory
						.getInstance()
						.getEffectFactory()
						.getChangeVarValueEffect(
								new EAdFieldImpl<String>(sceneElement,
										EAdBasicSceneElement.VAR_STATE),
								new ValueOperation("assign",
										CommonStates.EAD_STATE_TALKING
												.toString())));
		getElements().add(sceneElement);

		// Frames
		String uris[] = new String[8];
		for (int i = 1; i <= 8; i++)
			uris[i - 1] = "@drawable/paniel_wlr_0" + i + ".png";

		FramesAnimation animation = EAdElementsFactory.getInstance()
				.getDrawableFactory().getFramesAnimation(uris, 500);

		caption = EAdElementsFactory.getInstance().getCaptionFactory()
				.createCaption("Frames animation");
		caption.setPadding(0);

		getElements().add(
				EAdElementsFactory.getInstance().getSceneElementFactory()
						.createSceneElement(caption, margin, 180));

		EAdBasicSceneElement paniel = EAdElementsFactory.getInstance()
				.getSceneElementFactory()
				.createSceneElement(animation, margin, 220);
		paniel.setScale(0.8f);
		getElements().add(paniel);

	}

	@Override
	public String getSceneDescription() {
		return "A scene showing some drawables of eAdventure";
	}

	public String getDemoName() {
		return "Drawables Scene";
	}
}
