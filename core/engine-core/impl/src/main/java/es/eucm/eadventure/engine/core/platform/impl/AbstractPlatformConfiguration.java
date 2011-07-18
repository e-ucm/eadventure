package es.eucm.eadventure.engine.core.platform.impl;

import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;

/**
 * Abstract implementation for PlatformConfigurarion
 * 
 */
public abstract class AbstractPlatformConfiguration implements
		PlatformConfiguration {

	private int width;

	private int height;

	protected boolean fullscreen;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.platform.PlatformConfiguration#getWidth()
	 */
	@Override
	public int getWidth() {
		return width;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.platform.PlatformConfiguration#getHeight()
	 */
	@Override
	public int getHeight() {
		return height;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.platform.PlatformConfiguration#setWidth
	 * (int)
	 */
	@Override
	public void setWidth(int width) {
		this.width = width;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.platform.PlatformConfiguration#setHeight
	 * (int)
	 */
	@Override
	public void setHeight(int height) {
		this.height = height;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.platform.PlatformConfiguration#isFullscreen
	 * ()
	 */
	@Override
	public boolean isFullscreen() {
		return fullscreen;
	}

	@Override
	public int getVirtualWidth() {
		return (int) (getWidth() / getScale());
	}

	@Override
	public int getVirtualHeight() {
		return (int) (getHeight() / getScale());
	}
	
	public void setFullscreen(boolean fullscr) {
		fullscreen = fullscr;
	}

}
