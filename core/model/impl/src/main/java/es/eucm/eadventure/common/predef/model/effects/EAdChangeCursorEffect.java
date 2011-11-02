package es.eucm.eadventure.common.predef.model.effects;

import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeFieldValueEffect;
import es.eucm.eadventure.common.model.variables.impl.SystemFields;
import es.eucm.eadventure.common.model.variables.impl.operations.ValueOperation;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Image;

/**
 * Effect that changes the cursor (this effect only works in some platforms)
 * 
 */
public class EAdChangeCursorEffect extends EAdChangeFieldValueEffect {
	
	public EAdChangeCursorEffect(Image image){
		super("changeCursorEffect");
		addField(SystemFields.MOUSE_CURSOR);
		setOperation(new ValueOperation(image));
	}
	
	

}
