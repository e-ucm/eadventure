package es.eucm.eadventure.common.elmentfactories.scenedemos;

import es.eucm.eadventure.common.elmentfactories.EAdElementsFactory;
import es.eucm.eadventure.common.model.elements.EAdSceneElement.CommonStates;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.variables.impl.extra.EAdSceneElementVars;
import es.eucm.eadventure.common.model.variables.impl.operations.AssignOperation;
import es.eucm.eadventure.common.params.fills.impl.EAdBorderedColor;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.fills.impl.EAdLinearGradient;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Caption;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Image;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.ImageImpl;
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
		Caption caption = EAdElementsFactory.getInstance().getCaptionFactory()
				.createCaption("Image");
		ComposedDrawable composed = new ComposedDrawableImpl();
		composed.addDrawable(caption);
		composed.addDrawable(drawable, 0, 40);
		getSceneElements().add(
				EAdElementsFactory.getInstance().getSceneElementFactory()
						.createSceneElement(composed, x, y));

		x += 200;

		// Shaded rectangle
		caption = EAdElementsFactory.getInstance().getCaptionFactory()
				.createCaption("Composed Drawable");
		RectangleShape rectangle = new RectangleShape(190, 40);
		rectangle.setFill(new EAdBorderedColor(EAdColor.GREEN, EAdColor.BLACK,
				2));

		RectangleShape rectangleShadow = new RectangleShape(190, 40);
		rectangleShadow.setFill(new EAdColor(100, 100, 100, 100));

		ComposedDrawable composedDrawable = new ComposedDrawableImpl();
		composedDrawable.addDrawable(rectangleShadow, 8, 8);
		composedDrawable.addDrawable(rectangle);

		composed = new ComposedDrawableImpl();
		composed.addDrawable(caption);
		composed.addDrawable(composedDrawable, 0, 40);

		getSceneElements().add(
				EAdElementsFactory.getInstance().getSceneElementFactory()
						.createSceneElement(composed, x, y));

		x += 220;

		// State drawable
		RectangleShape rectangle1 = new RectangleShape(190, 40);
		rectangle1.setFill(new EAdBorderedColor(EAdColor.GREEN, EAdColor.BLACK,
				1));

		RectangleShape rectangle2 = new RectangleShape(190, 40);
		rectangle2.setFill(new EAdLinearGradient(new EAdColor(0, 255, 0, 100),
				EAdColor.BLACK, false));
		
		StateDrawable stateDrawable = new StateDrawableImpl();
		stateDrawable.addDrawable(CommonStates.EAD_STATE_DEFAULT.toString(),
				rectangle1);
		stateDrawable.addDrawable(CommonStates.EAD_STATE_TALKING.toString(),
				rectangle2);

		
		getSceneElements().add(EAdElementsFactory.getInstance()
				.getSceneElementFactory().createSceneElement("State Drawable", x, y));
		EAdBasicSceneElement sceneElement = EAdElementsFactory.getInstance()
				.getSceneElementFactory().createSceneElement(stateDrawable, x, y + 40);
		sceneElement.addBehavior(
				EAdMouseEventImpl.MOUSE_RIGHT_CLICK,
				EAdElementsFactory
						.getInstance()
						.getEffectFactory()
						.getChangeVarValueEffect(
								sceneElement.getVars().getVar(
										EAdSceneElementVars.VAR_STATE),
								new AssignOperation("assign",
										CommonStates.EAD_STATE_DEFAULT
												.toString())));
		sceneElement.addBehavior(
				EAdMouseEventImpl.MOUSE_LEFT_CLICK,
				EAdElementsFactory
						.getInstance()
						.getEffectFactory()
						.getChangeVarValueEffect(
								sceneElement.getVars().getVar(
										EAdSceneElementVars.VAR_STATE),
								new AssignOperation("assign",
										CommonStates.EAD_STATE_TALKING
												.toString())));
		getSceneElements().add(sceneElement);

	}
}
