package es.eucm.eadventure.engine.core.gameobjects.impl.sceneelements;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.common.model.variables.EAdVar;
import es.eucm.eadventure.common.resources.EAdBundleId;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.AssetDescriptor;
import es.eucm.eadventure.common.resources.assets.drawable.OrientedDrawable;
import es.eucm.eadventure.common.resources.assets.drawable.StateDrawable;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.gameobjects.SceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.AbstractGameObject;
import es.eucm.eadventure.engine.core.guiactions.GUIAction;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.DrawableAsset;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;

public abstract class SceneElementGOImpl<T extends EAdSceneElement> extends
		AbstractGameObject<T> implements SceneElementGO<T> {

	private static final Logger logger = Logger.getLogger("SceneElementGOImpl");

	protected EAdPosition position;

	protected float scale;

	protected Orientation orientation;

	protected String state;

	protected float rotation;

	private int width;

	private int height;

	private float alpha;

	protected boolean visible;

	@Inject
	public SceneElementGOImpl(AssetHandler assetHandler,
			StringHandler stringHandler, GameObjectFactory gameObjectFactory,
			GUI gui, GameState gameState, ValueMap valueMap,
			PlatformConfiguration platformConfiguration) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState,
				valueMap, platformConfiguration);
		logger.info("New instance");
		visible = true;
	}

	@Override
	public abstract boolean processAction(GUIAction action);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.gameobjects.impl.AbstractGameObject#setElement
	 * (es.eucm.eadventure.common.model.EAdElement)
	 * 
	 * Should be implemented to get position, scale, orientation and other
	 * values
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void setElement(T element) {
		super.setElement(element);
		this.position = new EAdPosition(element.getPosition());
		element.positionXVar().setInitialValue(position.getX());
		element.positionYVar().setInitialValue(position.getY());

		// If element is a clone, sets its vars to its initial values, if not,
		// it preserves the values contained by the valueMap
		if (element.isClone()) {
			for (EAdVar var : element.getVars()) {
				valueMap.setValue(var, var.getInitialValue());
			}
		}

		visible = valueMap.getValue(element.visibleVar());
		rotation = valueMap.getValue(element.rotationVar());
		scale = valueMap.getValue(element.scaleVar());
		alpha = valueMap.getValue(element.alphaVar());
		orientation = valueMap.getValue(element.orientationVar());
		state = valueMap.getValue(element.stateVar());
	}

	@Override
	public GameObject<?> getDraggableElement(MouseState mouseState) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.gameobjects.impl.AbstractGameObject#doLayout
	 * ()
	 * 
	 * Should layout all sub-elements and resource
	 */
	@Override
	public void doLayout(int offsetX, int offsetY) {
		super.doLayout(offsetX, offsetY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.gameobjects.impl.AbstractGameObject#update
	 * (es.eucm.eadventure.engine.core.GameState)
	 * 
	 * Should update the state of all sub-elements and resources
	 */
	@Override
	public void update(GameState state) {
		super.update(state);
		this.getAsset().update(state);
		this.position.setX(valueMap.getValue(element.positionXVar()));
		this.position.setY(valueMap.getValue(element.positionYVar()));
		this.rotation = valueMap.getValue(element.rotationVar());
		this.alpha = valueMap.getValue(element.alphaVar());
		this.orientation = valueMap.getValue(element.orientationVar());
		this.state = valueMap.getValue(element.stateVar());
	}

	@Override
	public T getElement() {
		return super.getElement();
	}

	@Override
	public EAdPosition getPosition() {
		return position;
	}

	@Override
	public void setPosition(EAdPosition position) {
		this.position = position;
	}

	@Override
	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;

	}

	@Override
	public Orientation getOrientation() {
		return orientation;
	}

	@Override
	public AssetDescriptor getCurrentAssetDescriptor() {
		AssetDescriptor a = element.getResources().getAsset(getCurrentBundle(),
				EAdBasicSceneElement.appearance);

		return getCurrentAssetDescriptor(a);
	}

	protected AssetDescriptor getCurrentAssetDescriptor(AssetDescriptor a) {
		if (a == null)
			return null;

		// Check state
		if (a instanceof StateDrawable) {
			StateDrawable stateDrawable = (StateDrawable) a;
			return getCurrentAssetDescriptor(stateDrawable.getDrawable(state));
		}
		// Check orientation
		else if (a instanceof OrientedDrawable) {
			return getCurrentAssetDescriptor(((OrientedDrawable) a)
					.getDrawable(orientation));
		} else {
			return a;
		}
	}

	@Override
	public DrawableAsset<?> getAsset() {
		DrawableAsset<?> r = (DrawableAsset<?>) assetHandler
				.getRuntimeAsset(getCurrentAssetDescriptor());
		if (!r.isLoaded())
			r.loadAsset();
		return r;
	}

	@Override
	public DrawableAsset<?> getRenderAsset() {
		DrawableAsset<?> r = getAsset();
		if (r instanceof DrawableAsset && r.isLoaded()) {
			setWidth(r.getWidth());
			setHeight(r.getHeight());
			return r.getDrawable();
		}
		return r;
	}

	@Override
	public List<RuntimeAsset<?>> getAssets(List<RuntimeAsset<?>> assetList,
			boolean allAssets) {

		if (!isVisible() && !allAssets) {
			return assetList;
		}

		List<EAdBundleId> bundles = new ArrayList<EAdBundleId>();
		if (allAssets)
			bundles.addAll(getElement().getResources().getBundles());
		else
			bundles.add(getCurrentBundle());

		for (EAdBundleId bundle : bundles) {
			AssetDescriptor a = getElement().getResources().getAsset(bundle,
					EAdBasicSceneElement.appearance);
			getAssetsRecursively(a, assetList, allAssets);
		}
		return assetList;
	}

	protected void getAssetsRecursively(AssetDescriptor a,
			List<RuntimeAsset<?>> assetList, boolean allAssets) {
		if (a == null)
			return;

		if (a instanceof StateDrawable) {
			if (!allAssets)
				getAssetsRecursively(((StateDrawable) a).getDrawable(state),
						assetList, allAssets);
			else {
				for (String s : ((StateDrawable) a).getStates()) {
					getAssetsRecursively(((StateDrawable) a).getDrawable(s),
							assetList, allAssets);
				}

			}

		} else if (a instanceof OrientedDrawable) {
			if (!allAssets)
				getAssetsRecursively(
						((OrientedDrawable) a).getDrawable(orientation),
						assetList, allAssets);
			else {

				for (Orientation o : Orientation.values()) {
					getAssetsRecursively(((OrientedDrawable) a).getDrawable(o),
							assetList, allAssets);
				}
			}
		} else
			assetList.add(assetHandler.getRuntimeAsset(a));
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public float getScale() {
		return scale;
	}

	@Override
	public void setScale(float scale) {
		this.scale = scale;
	}

	public boolean isVisible() {
		return valueMap.getValue(element.visibleVar());
	}

	public void setWidth(int width) {
		this.width = width;
		valueMap.setValue(element.widthVar(), width);
	}

	public void setHeight(int height) {
		this.height = height;
		valueMap.setValue(element.heightVar(), height);
	}

	@Override
	public float getRotation() {
		return rotation;
	}

	@Override
	public int getCenterX() {
		return (int) ((position.getJavaX(width) + width / 2) * scale);
	}

	@Override
	public int getCenterY() {
		return (int) ((position.getJavaY(height) + height / 2) * scale);
	}

	@Override
	public int getY() {
		return (int) ((position.getJavaY(height) + height) * scale);
	}

	public float getAlpha() {
		return alpha;
	}

}
