package ead.plugins.engine.bubbledescription;

import ead.common.interfaces.Element;
import ead.common.interfaces.Param;
import ead.common.model.assets.drawable.basics.NinePatchImage;
import ead.common.model.assets.text.EAdFont;
import ead.common.model.elements.events.AbstractEvent;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.operations.EAdOperation;
import ead.common.model.params.paint.EAdPaint;
import ead.common.model.params.text.EAdString;
import ead.common.model.params.variables.VarDef;

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
