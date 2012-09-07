package ead.common.widgets;

import ead.common.model.elements.scenes.SceneElement;
import ead.common.params.fills.ColorFill;
import ead.common.params.text.EAdString;
import ead.common.resources.assets.drawable.basics.Caption;
import ead.common.resources.assets.text.BasicFont;

public class Label extends SceneElement {

	private Caption c;

	public Label(String string) {
		this(EAdString.newLiteralString(string));
	}

	public Label(EAdString string) {
		c = new Caption(string);
		c.setTextPaint(ColorFill.BLACK);
		c.setPadding(0);
		c.setBubblePaint(ColorFill.TRANSPARENT);
		c.setHasBubble(false);
		c.setFont(BasicFont.REGULAR);
		setAppearance(c);
	}

	public void setFontSize(float size) {
		c.getFont().setSize(size);
	}

	public void setColor(ColorFill color) {
		c.setTextPaint(color);
	}

	public void setBgColor(ColorFill color) {
		c.setHasBubble(true);
		c.setBubblePaint(color);
	}

	public Caption getCaption() {
		return c;
	}

}
