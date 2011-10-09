package es.eucm.eadventure.common.elementfactories.scenedemos;

import es.eucm.eadventure.common.elementfactories.EAdElementsFactory;
import es.eucm.eadventure.common.model.effects.impl.EAdVarInterpolationEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdVarInterpolationEffect.InterpolationType;
import es.eucm.eadventure.common.model.effects.impl.EAdVarInterpolationEffect.LoopType;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdComposedElementImpl;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent.SceneElementEvent;
import es.eucm.eadventure.common.model.events.impl.EAdSceneElementEventImpl;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.params.fills.impl.EAdBorderedColor;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl.Corner;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.RectangleShape;

public class ComplexElementScene extends EmptyScene {

	public ComplexElementScene() {
		EAdComposedElementImpl complex = new EAdComposedElementImpl("complex");
		RectangleShape rectangle = new RectangleShape(400, 400);
		rectangle.setFill(EAdBorderedColor.BLACK_ON_WHITE);
		complex.getResources().addAsset(complex.getInitialBundle(),
				EAdBasicSceneElement.appearance, rectangle);
		complex.setBounds(400, 400);
		complex.setPosition(new EAdPositionImpl(Corner.CENTER, 400, 300));

		EAdBasicSceneElement e = EAdElementsFactory
				.getInstance()
				.getSceneElementFactory()
				.createSceneElement(new RectangleShape(30, 30, EAdColor.RED),
						new RectangleShape(50, 50, EAdColor.BLUE), 40, 40);
		
		e.setPosition(new EAdPositionImpl(Corner.CENTER, 50, 50));

		complex.getElements().add(e);

		getElements().add(complex);

		EAdField<Float> rotation = new EAdFieldImpl<Float>(complex,
				EAdBasicSceneElement.VAR_ROTATION);

		EAdVarInterpolationEffect effect = new EAdVarInterpolationEffect(
				"effect");
		effect.setInterpolation(rotation, 0, 2 * (float) Math.PI, 10000,
				LoopType.RESTART, InterpolationType.LINEAR);

		EAdSceneElementEvent event = new EAdSceneElementEventImpl();
		event.addEffect(SceneElementEvent.ADDED_TO_SCENE, effect);

		complex.getEvents().add(event);

		EAdField<Float> rotation2 = new EAdFieldImpl<Float>(e,
				EAdBasicSceneElement.VAR_ROTATION);

		EAdVarInterpolationEffect effect2 = new EAdVarInterpolationEffect(
				"effect");
		effect2.setInterpolation(rotation2, 0, 2 * (float) Math.PI, 1000,
				LoopType.RESTART, InterpolationType.LINEAR);

		EAdSceneElementEvent event2 = new EAdSceneElementEventImpl();
		event2.addEffect(SceneElementEvent.ADDED_TO_SCENE, effect2);

		e.getEvents().add(event2);

		EAdField<Float> scale = new EAdFieldImpl<Float>(complex,
				EAdBasicSceneElement.VAR_SCALE);

		EAdVarInterpolationEffect effect3 = new EAdVarInterpolationEffect(
				"effect");
		effect3.setInterpolation(scale, 0.5f, 2.0f, 5000, LoopType.REVERSE,
				InterpolationType.LINEAR);

		event2.addEffect(SceneElementEvent.ADDED_TO_SCENE, effect3);

	}

	@Override
	public String getDescription() {
		return "A scene to show complex elements";
	}

	public String getDemoName() {
		return "Complex Element Scene";
	}

}
