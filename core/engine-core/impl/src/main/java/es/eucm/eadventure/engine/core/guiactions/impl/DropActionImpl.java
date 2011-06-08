package es.eucm.eadventure.engine.core.guiactions.impl;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.guievents.impl.EAdDropEvent;
import es.eucm.eadventure.common.model.params.guievents.EAdGUIEvent;
import es.eucm.eadventure.common.model.params.guievents.EAdMouseEvent.MouseActionType;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.guiactions.DropAction;

public class DropActionImpl implements DropAction {

	/**
	 * Virtual X coordinate where the action was performed.
	 */
	private int virtualX;

	/**
	 * Virtual Y coordinate where the action was performed.
	 */
	private int virtualY;
	
	private boolean consumed;
	
	private GameObject<? extends EAdElement> draggingElement;

	public DropActionImpl(int virtualX, int virtualY, GameObject<? extends EAdElement> draggingElement) {
		this.virtualX = virtualX;
		this.virtualY = virtualY;
		this.draggingElement = draggingElement;
		consumed = false;
	}

	
	@Override
	public EAdGUIEvent getGUIEvent() {
		return new EAdDropEvent(draggingElement.getElement());
	}

	@Override
	public boolean isConsumed() {
		return consumed;
	}

	@Override
	public void consume() {
		consumed = true;
	}

	/**
	 * @return the virtualX
	 */
	@Override
	public int getVirtualX() {
		return virtualX;
	}

	/**
	 * @return the virtualY
	 */
	@Override
	public int getVirtualY() {
		return virtualY;
	}


	@Override
	public MouseActionType getType() {
		return MouseActionType.DROP;
	}

}
