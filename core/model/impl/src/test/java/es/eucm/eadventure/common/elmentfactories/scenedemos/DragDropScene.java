package es.eucm.eadventure.common.elmentfactories.scenedemos;

import es.eucm.eadventure.common.model.conditions.impl.EmptyCondition;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeFieldValueEffect;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.model.variables.impl.operations.AssignOperation;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.fills.impl.EAdLinearGradient;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl.Corner;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.ImageImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.BallonShape;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.BallonShape.BalloonType;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.BezierShape;

public class DragDropScene extends EmptyScene {
	
	public DragDropScene(){
		setBackgroundFill(new EAdLinearGradient(EAdColor.LIGHT_GRAY, new EAdColor(245, 255, 245)));
		BezierShape shape = new BallonShape(0, 0, 100, 100, BalloonType.ROUNDED_RECTANGLE);
		shape.setFill(new EAdLinearGradient( EAdColor.RED, new EAdColor(200, 0, 0)));
		EAdBasicSceneElement e1 = new EAdBasicSceneElement("e1", new ImageImpl("@drawable/mole1.png"));
		e1.setDraggabe(EmptyCondition.TRUE_EMPTY_CONDITION);
		e1.setPosition(new EAdPositionImpl( Corner.CENTER, 0, 0));
		EAdField<Float> rotation = new EAdFieldImpl<Float>(e1, EAdBasicSceneElement.VAR_ROTATION);
		EAdChangeFieldValueEffect changeRotation1 = new EAdChangeFieldValueEffect("change", rotation, new AssignOperation((float) (Math.PI / 8.0f)));
		EAdChangeFieldValueEffect changeRotation2 = new EAdChangeFieldValueEffect("change", rotation, new AssignOperation(0.0f));
		e1.addBehavior(EAdMouseEventImpl.MOUSE_PRESSED, changeRotation1);
		e1.addBehavior(EAdMouseEventImpl.MOUSE_RELEASED, changeRotation2);
		
		getSceneElements().add(e1);
		
	}
	
	
	@Override
	public String getDescription() {
		return "A scene showing drag and drop";
	}

	public String getDemoName() {
		return "Drag & Drop Scene";
	}

}
