package ead.common.model.elements.effects.sceneelements;

import ead.common.interfaces.Element;
import ead.common.interfaces.Param;

@Element
public class ChangeColorEf extends AbstractSceneElementEffect {

	@Param
	private float red;

	@Param
	private float green;

	@Param
	private float blue;

	public ChangeColorEf() {

	}

	public ChangeColorEf(float red, float green, float blue) {
		super();
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public float getRed() {
		return red;
	}

	public void setRed(float red) {
		this.red = red;
	}

	public float getGreen() {
		return green;
	}

	public void setGreen(float green) {
		this.green = green;
	}

	public float getBlue() {
		return blue;
	}

	public void setBlue(float blue) {
		this.blue = blue;
	}

}
