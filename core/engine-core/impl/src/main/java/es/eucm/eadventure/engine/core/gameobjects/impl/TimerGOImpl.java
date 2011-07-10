package es.eucm.eadventure.engine.core.gameobjects.impl;

import java.util.List;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.elements.EAdTimer;
import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.gameobjects.TimerGO;
import es.eucm.eadventure.engine.core.guiactions.GUIAction;
import es.eucm.eadventure.engine.core.GameLoop;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;

public class TimerGOImpl extends AbstractGameObject<EAdTimer> implements TimerGO {

	private double passedTime;

	@Inject
	public TimerGOImpl(AssetHandler assetHandler, StringHandler stringHandler,
			GameObjectFactory gameObjectFactory, GUI gui, GameState gameState,
			ValueMap valueMap, PlatformConfiguration platformConfiguration) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState, valueMap,
				platformConfiguration);
	}

	@Override
	public boolean processAction(GUIAction action) {
		return false;
	}

	@Override
	public void setElement(EAdTimer element) {
		super.setElement(element);
		valueMap.setValue(element.timerStartedVar(), Boolean.FALSE);
		valueMap.setValue(element.timerEndedVar(), Boolean.FALSE);
	}

	@Override
	public GameObject<?> getDraggableElement(MouseState mouseState) {
		return null;
	}

	@Override
	public void doLayout(int offsetX, int offsetY) {
	}

	@Override
	public EAdPosition getPosition() {
		return null;
	}

	@Override
	public List<RuntimeAsset<?>> getAssets(List<RuntimeAsset<?>> assetList,
			boolean allAssets) {
		return null;
	}

	@Override
	public void update(GameState gameState) {
		if (valueMap.getValue(element.timerEndedVar()))
			valueMap.setValue(element.timerEndedVar(), Boolean.FALSE);
		if (valueMap.getValue(element.timerStartedVar())) {
			passedTime += GameLoop.SKIP_MILLIS_TICK;
			if (passedTime > element.getTime()) {
				valueMap.setValue(element.timerEndedVar(), Boolean.TRUE);
				//TODO should now if restart
				valueMap.setValue(element.timerStartedVar(), Boolean.FALSE);
			}
		}
	}

}
