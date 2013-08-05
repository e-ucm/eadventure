package ead.plugins.engine.bubbledescription;

import es.eucm.ead.model.interfaces.Element;
import es.eucm.ead.model.interfaces.Param;
import es.eucm.ead.model.assets.drawable.basics.NinePatchImage;
import es.eucm.ead.model.assets.text.EAdFont;
import es.eucm.ead.model.elements.events.AbstractEvent;
import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.params.paint.EAdPaint;
import es.eucm.ead.model.params.text.EAdString;
import es.eucm.ead.model.params.variables.VarDef;

/**
 * An event that shows the name of the element under the mouse
 * 
 * 
 */
@Element
public class BubbleNameEv extends AbstractEvent {

	public static final VarDef<EAdString> VAR_BUBBLE_NAME = new VarDef<EAdString>(
			"hud_bubble_name", EAdString.class, null);

	public static final VarDef<EAdList> VAR_BUBBLE_OPERATIONS = new VarDef<EAdList>(
			"hud_bubble_operations", EAdList.class, null);

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
