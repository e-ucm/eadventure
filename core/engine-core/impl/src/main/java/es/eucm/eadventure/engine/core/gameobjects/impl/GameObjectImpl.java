package es.eucm.eadventure.engine.core.gameobjects.impl;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;

public abstract class GameObjectImpl<T extends EAdElement> implements
		GameObject<T> {

	protected GameState gameState;

	protected T element;

	@Inject
	public GameObjectImpl(GameState gameState) {
		this.gameState = gameState;
	}

	@Override
	public void setElement(T element) {
		this.element = element;
	}

	@Override
	public T getElement() {
		return element;
	}
	
	@Override
	public String toString() {
		return element + "";
	}

}
