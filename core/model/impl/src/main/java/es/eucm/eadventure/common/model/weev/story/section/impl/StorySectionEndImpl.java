package es.eucm.eadventure.common.model.weev.story.section.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.weev.story.element.impl.AbstractNode;
import es.eucm.eadventure.common.model.weev.story.section.StorySectionEnd;
import es.eucm.eadventure.common.params.EAdString;

/**
 * Default implementation of {@link StorySectionEnd}
 */
@Element(detailed = StorySectionEndImpl.class, runtime = StorySectionEndImpl.class)
public class StorySectionEndImpl extends AbstractNode implements
		StorySectionEnd {

	@Param(value = "name")
	private EAdString name;
	
	public StorySectionEndImpl() {
		name = EAdString.newEAdString("name");
	}
	
	@Override
	public EAdString getName() {
		return name;
	}

}
