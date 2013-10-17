package es.eucm.ead.legacyplugins.model;

import es.eucm.ead.model.assets.drawable.basics.NinePatchImage;
import es.eucm.ead.model.assets.text.EAdFont;
import es.eucm.ead.model.elements.events.AbstractEvent;
import es.eucm.ead.model.interfaces.Element;
import es.eucm.ead.model.interfaces.Param;
import es.eucm.ead.model.params.paint.EAdPaint;

/**
 * An event that shows the name of the element under the mouse
 * 
 * 
 */
@Element
public class BubbleNameEv extends AbstractEvent {

	@Param
	private NinePatchImage bubble;

	@Param
	private EAdFont font;

	@Param
	private EAdPaint textPaint;

	@Param
	private boolean followElement;

	public BubbleNameEv() {

	}

	public NinePatchImage getBubble() {
		return bubble;
	}

	public void setBubble(NinePatchImage bubble) {
		this.bubble = bubble;
	}

	public EAdFont getFont() {
		return font;
	}

	public void setFont(EAdFont font) {
		this.font = font;
	}

	public EAdPaint getTextPaint() {
		return textPaint;
	}

	public void setTextPaint(EAdPaint textPaint) {
		this.textPaint = textPaint;
	}

	public boolean isFollowElement() {
		return followElement;
	}

	public void setFollowElement(boolean followElement) {
		this.followElement = followElement;
	}

}
