package es.eucm.eadventure.common.model.weev.story.element.impl.nodes;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.model.weev.story.element.impl.AbstractNamedNode;

/**
 * Timer node, represents a timer in the WEEV representation.
 * <p>
 * Timers can be used to trigger behavior after a set amount of time.
 */
@Element(detailed = Timer.class, runtime = Timer.class)
public class Timer extends AbstractNamedNode {

}
