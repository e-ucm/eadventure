package es.eucm.eadventure.common.model.elements;

import es.eucm.eadventure.common.interfaces.features.Evented;
import es.eucm.eadventure.common.interfaces.features.Resourced;
import es.eucm.eadventure.common.model.EAdElement;

/**
 * Most of {@link EAdElement} have resources and events. This interface
 * represents those elements
 * 
 */
public interface EAdGeneralElement extends EAdElement, Resourced, Evented {

}
