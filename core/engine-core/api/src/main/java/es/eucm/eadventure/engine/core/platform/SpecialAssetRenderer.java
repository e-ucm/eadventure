package es.eucm.eadventure.engine.core.platform;

/**
 * Renderer for special assets (e.g. video), which need to be rendered
 * independently of the rest of the elements in the game.
 * 
 * @param <S>
 *            The type of the asset
 * @param <T>
 *            The type of the component used to render the asset
 */
public interface SpecialAssetRenderer<S, T> {

	/**
	 * The platform-dependent component or element used to rendered the asset
	 * 
	 * @param asset
	 *            the asset to be rendered
	 * @return
	 */
	T getComponent(S asset);

	/**
	 * @return true if reproduction of asset has finished
	 */
	boolean isFinished();

	/**
	 * @return start the reproduction of the asset
	 */
	boolean start();

}
