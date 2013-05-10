package ead.engine.core.events;

import com.badlogic.gdx.scenes.scene2d.InputEvent;

import ead.common.model.params.guievents.EAdGUIEvent;

public class DragEvent extends InputEvent {

	private EAdGUIEvent dragEvent;

	public DragEvent(EAdGUIEvent dragEvent) {
		this.dragEvent = dragEvent;
	}

	public EAdGUIEvent getDragEvent() {
		return dragEvent;
	}

	public void setDragEvent(EAdGUIEvent dragEvent) {
		this.dragEvent = dragEvent;
	}

}
