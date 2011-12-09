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

	public EditionSceneElement(EAdSceneElement element, float scale) {
		
		EAdSceneElement proxy = null; 
		if (element instanceof EAdBasicSceneElement) {
			proxy = new BasicSceneElementReplica(element);
		} else if (element instanceof EAdComplexElement){
			proxy = new ComplexSceneElementReplica(element);
		}
		this.components.add(proxy);
		proxy.setVarInitialValue(EAdBasicSceneElement.VAR_X, (int) (scale * (Integer) proxy.getVars().get(EAdBasicSceneElement.VAR_X)));
		proxy.setVarInitialValue(EAdBasicSceneElement.VAR_Y, (int) (scale * (Integer) proxy.getVars().get(EAdBasicSceneElement.VAR_Y)));
		proxy.setVarInitialValue(EAdBasicSceneElement.VAR_SCALE, scale);
		
		EAdField<Float> rotation = new EAdFieldImpl<Float>(this,
				EAdBasicSceneElement.VAR_ROTATION);
		EAdChangeFieldValueEffect changeRotation = new EAdChangeFieldValueEffect(
				rotation,
				new ValueOperation(0.2f));
		EAdChangeFieldValueEffect restoreRotation = new EAdChangeFieldValueEffect(
				 rotation, new ValueOperation(0.0f));

		proxy.addBehavior(EAdMouseEventImpl.MOUSE_ENTERED, changeRotation);
		proxy.addBehavior(EAdMouseEventImpl.MOUSE_EXITED, restoreRotation);
	}
	

}
