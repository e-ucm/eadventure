package es.eucm.eadventure.common.elementfactories.scenedemos;

import es.eucm.eadventure.common.model.effects.impl.EAdInterpolationEffect;
import es.eucm.eadventure.common.model.effects.impl.enums.InterpolationType;
import es.eucm.eadventure.common.model.effects.impl.enums.InterpolationLoopType;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeFieldValueEffect;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent;
import es.eucm.eadventure.common.model.events.enums.SceneElementEventType;
import es.eucm.eadventure.common.model.events.enums.SceneElementTimedEventType;
import es.eucm.eadventure.common.model.events.impl.EAdSceneElementEventImpl;
import es.eucm.eadventure.common.model.events.impl.EAdTimedEventImpl;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.model.variables.impl.operations.MathOperation;
import es.eucm.eadventure.common.params.fills.impl.EAdPaintImpl;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl.Corner;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.CircleShape;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.RectangleShape;

public class DepthZScene extends EmptyScene {
	
	public DepthZScene(){
		
		int totalTime = 2000;
		
		EAdBasicSceneElement e1 = new EAdBasicSceneElement(new RectangleShape( 50, 500, new EAdPaintImpl( EAdColor.RED, EAdColor.BLACK ) ));
		e1.setPosition(new EAdPositionImpl( Corner.CENTER, 400, 300 ));
		getComponents().add(e1);
		
		EAdBasicSceneElement e2 = new EAdBasicSceneElement( new CircleShape( 20, 20, 20, 20, new EAdPaintImpl( EAdColor.GREEN, EAdColor.BLACK ) ));
		e2.setPosition(new EAdPositionImpl( Corner.CENTER, 10, 300 ));
		getComponents().add(e2);
		
		EAdField<Integer> xField = new EAdFieldImpl<Integer>(e2, EAdBasicSceneElement.VAR_X);
		EAdInterpolationEffect effect = new EAdInterpolationEffect(xField, 50, 750, totalTime, InterpolationLoopType.REVERSE, InterpolationType.LINEAR);
		
		EAdTimedEventImpl timedEvent = new EAdTimedEventImpl();
		timedEvent.setTime(totalTime);
		e2.setVarInitialValue(EAdBasicSceneElement.VAR_Z, 1);
		e2.setVarInitialValue(EAdBasicSceneElement.VAR_SCALE, 1.2f);
		
		EAdField<Integer> zField = new EAdFieldImpl<Integer>(e2, EAdBasicSceneElement.VAR_Z);
		EAdChangeFieldValueEffect changeZ = new EAdChangeFieldValueEffect( zField, new MathOperation("- [0]", zField ));
		changeZ.setId("changeZ");
		
		EAdField<Float> scaleField = new EAdFieldImpl<Float>(e2, EAdBasicSceneElement.VAR_SCALE);
		EAdChangeFieldValueEffect changeScale = new EAdChangeFieldValueEffect( scaleField, new MathOperation("1 / [0]", scaleField ));
		changeScale.setId("changeSacle");
		timedEvent.addEffect(SceneElementTimedEventType.START_TIME, changeScale);
		timedEvent.addEffect(SceneElementTimedEventType.START_TIME, changeZ);
		e2.getEvents().add(timedEvent);
		
		
		EAdSceneElementEvent event = new EAdSceneElementEventImpl();
		event.addEffect(SceneElementEventType.ADDED_TO_SCENE, effect);
		
		e2.getEvents().add(event);
		
		
	}
	
	@Override
	public String getSceneDescription() {
		return "A scene showing z depth";
	}

	public String getDemoName() {
		return "Depth Z Scene";
	}

}
