package es.eucm.ead.legacyplugins.model.elements;

import es.eucm.ead.model.assets.text.EAdFont;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.interfaces.Element;
import es.eucm.ead.model.interfaces.Param;
import es.eucm.ead.model.params.paint.EAdPaint;

@Element
public class ClockDisplay extends SceneElement {

	@Param
	private EAdPaint color;

	@Param
	private EAdFont font;

	public EAdPaint getColor() {
		return color;
	}

	public void setColor(EAdPaint color) {
		this.color = color;
	}

	public EAdFont getFont() {
		return font;
	}

	public void setFont(EAdFont font) {
		this.font = font;
	}
}
