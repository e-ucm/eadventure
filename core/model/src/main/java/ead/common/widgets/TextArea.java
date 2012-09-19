package ead.common.widgets;

import ead.common.interfaces.Element;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.params.fills.Paint;
import ead.common.resources.assets.drawable.basics.shapes.RectangleShape;

@Element
public class TextArea extends SceneElement {

	public TextArea() {		
		this(100, 100);
	}

	public TextArea(int width, int height) {
		this.setId("textArea");
		this.setAppearance(new RectangleShape(width, height,
				Paint.BLACK_ON_WHITE));
	}

}
