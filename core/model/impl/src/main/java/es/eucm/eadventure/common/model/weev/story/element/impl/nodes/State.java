package es.eucm.eadventure.common.model.weev.story.element.impl.nodes;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.model.weev.story.element.impl.AbstractNode;
import es.eucm.eadventure.common.model.weev.story.elements.Node;


/**
 * A state is the most generic kind of {@link Node} available
 */
@Element(detailed = State.class, runtime = State.class)
public class State extends AbstractNode implements Node {

}
