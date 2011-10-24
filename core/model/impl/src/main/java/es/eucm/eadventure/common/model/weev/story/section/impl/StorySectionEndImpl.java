package es.eucm.eadventure.common.model.weev.story.section.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.model.weev.story.element.impl.AbstractNamedNode;
import es.eucm.eadventure.common.model.weev.story.section.StorySectionEnd;

/**
 * Default implementation of {@link StorySectionEnd}
 */
@Element(detailed = StorySectionEndImpl.class, runtime = StorySectionEndImpl.class)
public class StorySectionEndImpl extends AbstractNamedNode implements
		StorySectionEnd {

}
