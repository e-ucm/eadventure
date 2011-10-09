package es.eucm.eadventure.common.elementfactories.scenedemos;

import es.eucm.eadventure.common.model.effects.impl.EAdVarInterpolationEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdVarInterpolationEffect.InterpolationType;
import es.eucm.eadventure.common.model.effects.impl.EAdVarInterpolationEffect.LoopType;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeFieldValueEffect;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent.SceneElementEvent;
import es.eucm.eadventure.common.model.events.EAdSceneElementTimedEvent.SceneElementTimedEventType;
import es.eucm.eadventure.common.model.events.impl.EAdSceneElementEventImpl;
import es.eucm.eadventure.common.model.events.impl.EAdSceneElementTimedEventImpl;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.model.variables.impl.operations.MathOperation;
import es.eucm.eadventure.common.params.fills.impl.EAdBorderedColor;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl.Corner;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.CircleShape;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.RectangleShape;

public class DepthZScene extends EmptyScene {
	
	public DepthZScene(){
		
		int totalTime = 2000;
		
		EAdBasicSceneElement e1 = new EAdBasicSceneElement("e1", new RectangleShape( 50, 500, new EAdBorderedColor( EAdColor.RED, EAdColor.BLACK ) ));
		e1.setPosition(new EAdPositionImpl( Corner.CENTER, 400, 300 ));
		getElements().add(e1);
		
		EAdBasicSceneElement e2 = new EAdBasicSceneElement("e2", new CircleShape( 20, 20, 20, 20, new EAdBorderedColor( EAdColor.GREEN, EAdColor.BLACK ) ));
		e2.setPosition(new EAdPositionImpl( Corner.CENTER, 10, 300 ));
		getElements().add(e2);
		
		EAdField<Integer> xField = new EAdFieldImpl<Integer>(e2, EAdBasicSceneElement.VAR_X);
		EAdVarInterpolationEffect effect = new EAdVarInterpolationEffect("xInt", xField, 50, 750, totalTime, LoopType.REVERSE, InterpolationType.LINEAR);
		
		EAdSceneElementTimedEventImpl timedEvent = new EAdSceneElementTimedEventImpl("timed");
		timedEvent.setTime(totalTime);
		e2.setVarInitialValue(EAdBasicSceneElement.VAR_Z, 1);
		e2.setVarInitialValue(EAdBasicSceneElement.VAR_SCALE, 1.2f);
		
		EAdField<Integer> zField = new EAdFieldImpl<Integer>(e2, EAdBasicSceneElement.VAR_Z);
		EAdChangeFieldValueEffect changeZ = new EAdChangeFieldValueEffect("changeZ", zField, new MathOperation("- [0]", zField ));
		
		EAdField<Float> scaleField = new EAdFieldImpl<Float>(e2, EAdBasicSceneElement.VAR_SCALE);
		EAdChangeFieldValueEffect changeScale = new EAdChangeFieldValueEffect("changeSacle", scaleField, new MathOperation("1 / [0]", scaleField ));
		timedEvent.addEffect(SceneElementTimedEventType.START_TIME, changeScale);
		timedEvent.addEffect(SceneElementTimedEventType.START_TIME, changeZ);
		e2.getEvents().add(timedEvent);
		
		
		EAdSceneElementEvent event = new EAdSceneElementEventImpl("event");
		event.addEffect(SceneElementEvent.ADDED_TO_SCENE, effect);
		
		e2.getEvents().add(event);
		
		
	}
	
	@Override
	public String getDescription() {
		return "A scene showkin z depth";
	}

	public String getDemoName() {
		return "Depth Z Scene";
	}

}
