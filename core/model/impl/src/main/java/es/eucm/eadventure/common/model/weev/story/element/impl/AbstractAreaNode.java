package es.eucm.eadventure.common.model.weev.story.element.impl;

import es.eucm.eadventure.common.model.weev.common.Area;

/**
 * Abstract extension of {@link AbstractNode} that implements {@link Area}
 */
public abstract class AbstractAreaNode extends AbstractNode implements Area {

	private Boolean retracted;

	private Integer width;

	private Integer height;
	
	@Override
	public void setRetracted(Boolean retracted) {
		this.retracted = retracted;
	}

	@Override
	public Boolean isRetracted() {
		return retracted;
	}

	@Override
	public Integer getWidth() {
		return width;
	}

	@Override
	public Integer getHeight() {
		return height;
	}

	@Override
	public void setWidth(Integer width) {
		this.width = width;
	}

	@Override
	public void setHeight(Integer height) {
		this.height = height;
	}

	@Override
	public Integer getMinWidth() {
		return 10;
	}

	@Override
	public Integer getMinHeight() {
		return 10;
	}

}
