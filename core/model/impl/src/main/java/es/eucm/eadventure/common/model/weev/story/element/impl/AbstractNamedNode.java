package es.eucm.eadventure.common.model.weev.story.element.impl;

import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.params.EAdString;

/**
 * Abstract extension of {@link AbstractNode} with a name {@link EAdString} field
 */
public abstract class AbstractNamedNode extends AbstractNode {

	@Param(value = "name")
	private EAdString name;
	
	public AbstractNamedNode() {
		name = EAdString.newEAdString("name");
	}
	
	public EAdString getName() {
		return name;
	}
}
