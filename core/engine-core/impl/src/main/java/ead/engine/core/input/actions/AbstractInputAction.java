package ead.engine.core.input.actions;

import ead.common.model.elements.guievents.EAdGUIEvent;
import ead.engine.core.input.InputAction;

public class AbstractInputAction<T extends EAdGUIEvent> implements InputAction<T>{
	
	private boolean consumed;
	
	protected T event;
	
	public AbstractInputAction( T event ){
		this.event = event;
		consumed = false;
	}

	@Override
	public T getGUIEvent() {
		return event;
	}

	@Override
	public boolean isConsumed() {
		return consumed;
	}

	@Override
	public void consume() {
		consumed = true;
	}
	
	public String toString(){
		return event.toString();
	}

}
