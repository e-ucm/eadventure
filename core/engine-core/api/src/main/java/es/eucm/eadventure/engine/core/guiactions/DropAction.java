package es.eucm.eadventure.engine.core.guiactions;

/**
 * <p>
 * Action triggered when an element is dropped in the GUI
 * </p>
 * 
 */
public interface DropAction extends MouseAction {

	/**
	 * Get the x coordinate of the mouse action in the virtual screen
	 * 
	 * @return the x coordinate
	 */
	public int getVirtualX();

	/**
	 * Get the y coordinate of the mouse action in the virtual screen
	 * 
	 * @return the y coordinate
	 */
	public int getVirtualY();

}
