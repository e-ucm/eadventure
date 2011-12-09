package es.eucm.eadventure.editor.view.generics.scene.impl;

import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeFieldValueEffect;
import es.eucm.eadventure.common.model.elements.EAdComplexElement;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdComplexElementImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.model.variables.impl.operations.ValueOperation;

public class EditionSceneElement extends EAdComplexElementImpl {

	public EditionSceneElement(EAdSceneElement element) {
		if (element instanceof EAdBasicSceneElement) {
			this.components.add(new BasicSceneElementReplica(element));
		} else if (element instanceof EAdComplexElement){
			this.components.add(new ComplexSceneElementReplica(element));
		}
		
		EAdField<Float> rotation = new EAdFieldImpl<Float>(this,
				EAdBasicSceneElement.VAR_ROTATION);
		EAdChangeFieldValueEffect changeRotation = new EAdChangeFieldValueEffect(
				rotation,
				new ValueOperation(2.0f));
		EAdChangeFieldValueEffect restoreRotation = new EAdChangeFieldValueEffect(
				 rotation, new ValueOperation(0.0f));

		this.addBehavior(EAdMouseEventImpl.MOUSE_ENTERED, changeRotation);
		this.addBehavior(EAdMouseEventImpl.MOUSE_EXITED, restoreRotation);
		
	}
	
	

}
