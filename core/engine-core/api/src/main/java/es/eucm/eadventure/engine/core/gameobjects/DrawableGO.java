package es.eucm.eadventure.engine.core.gameobjects;

import java.util.List;

import es.eucm.eadventure.common.interfaces.features.Positioned;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.Renderable;
import es.eucm.eadventure.engine.core.guiactions.GUIAction;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;
import es.eucm.eadventure.engine.core.util.EAdTransformation;

public interface DrawableGO<T> extends GameObject<T>, Positioned, Renderable {
	
	/**
	 * Process the action in the graphic interface (click, etc.)
	 * 
	 * @param action
	 *            the action to process
	 * @return {@code true} if he action has been processed by the object
	 */
	boolean processAction(GUIAction action);
	
	/**
	 * The the draggable element
	 * 
	 * @param mouseState
	 *            The mouse state
	 * @return The game object that is draggable
	 */
	DrawableGO<?> getDraggableElement(MouseState mouseState);
	
	/**
	 * Layout out the child game objects of this game objects
	 * 
	 * @param transformation
	 *            the transformation accumulated by this game object container
	 */
	void doLayout(EAdTransformation transformation);
	
	/**
	 * Returns the transformation (translation, rotation, scale, etc.) of this
	 * game object
	 * 
	 * @return the transformation
	 */
	EAdTransformation getTransformation();

	/**
	 * Returns if this game object is enable for user interactions
	 * 
	 * @return if this game object is enable for user interactions
	 */
	boolean isEnable();
	
	/**
	 * <p>
	 * Adds the assets used by this game object to the list and returns it
	 * </p>
	 * <p>
	 * This method is used to manage memory consumed by assets, allowing the
	 * releasing or pre-caching of assets as required.
	 * </p>
	 * 
	 * @param assetList
	 *            The list where to add the assets
	 * @param allAssets
	 *            If true all assets are added, if false only required ones are
	 * @return The list of assets with the ones of this game object added
	 */
	List<RuntimeAsset<?>> getAssets(List<RuntimeAsset<?>> assetList,
			boolean allAssets);

}
