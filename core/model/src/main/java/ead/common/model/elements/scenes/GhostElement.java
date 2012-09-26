package ead.common.model.elements.scenes;

import ead.common.interfaces.Element;
import ead.common.interfaces.Param;
import ead.common.resources.assets.drawable.EAdDrawable;

@Element
public class GhostElement extends SceneElement implements EAdGhostElement {

	@Param("interactionArea")
	private EAdDrawable interactionArea;
	
	public GhostElement( ){
		
	}

	public GhostElement(EAdDrawable interactionArea) {
		this.interactionArea = interactionArea;
	}

	@Override
	public EAdDrawable getInteractionArea() {
		return interactionArea;
	}

	@Override
	public void setInteractionArea(EAdDrawable drawable) {
		this.interactionArea = drawable;
	}

}
