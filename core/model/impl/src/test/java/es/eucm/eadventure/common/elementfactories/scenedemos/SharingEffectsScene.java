package es.eucm.eadventure.common.elementfactories.scenedemos;

import es.eucm.eadventure.common.model.effects.impl.sceneelements.EAdMakeActiveElementEffect;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeFieldValueEffect;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.model.variables.impl.SystemFields;
import es.eucm.eadventure.common.model.variables.impl.operations.MathOperation;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.RectangleShape;

public class SharingEffectsScene extends EmptyScene {

	public SharingEffectsScene( ){
		EAdBasicSceneElement b = new EAdBasicSceneElement( "button", new RectangleShape( 50, 50, EAdColor.RED ) );
		
		EAdField<Float> field = new EAdFieldImpl<Float>( SystemFields.ACTIVE_ELEMENT, EAdBasicSceneElement.VAR_ROTATION );
		EAdChangeFieldValueEffect effect = new EAdChangeFieldValueEffect("change", field, new MathOperation("[0] + 0.1", field) );
		b.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, effect);
		
		b.setPosition(20, 20);
		this.getElements().add(b);
		
		for ( int i = 0; i < 4; i++)
			for ( int j = 0; j < 4; j++ ){
				EAdBasicSceneElement e = new EAdBasicSceneElement("e" + i + "" + j, new RectangleShape( 50, 50, EAdColor.BLUE ) );
				e.setPosition(i * 60 + 10, j * 60 + 60);
				EAdMakeActiveElementEffect ef = new EAdMakeActiveElementEffect( "makeActive" );
				ef.setSceneElement(e);
				e.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, ef);
				getElements().add(e);
			}
				
	}
	
	@Override
	public String getSceneDescription() {
		return "A scene in which elements share effects";
	}

	public String getDemoName() {
		return "Sharing Effects Scene";
	}
}
