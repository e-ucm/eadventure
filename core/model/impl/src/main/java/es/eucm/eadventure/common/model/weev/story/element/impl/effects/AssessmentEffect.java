package es.eucm.eadventure.common.model.weev.story.element.impl.effects;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.weev.story.element.impl.AbstractEffect;
import es.eucm.eadventure.common.params.EAdString;

/**
 * Assessment of player effect.
 */
@Element(detailed = AssessmentEffect.class, runtime = AssessmentEffect.class)
public class AssessmentEffect extends AbstractEffect {

	/**
	 * The positive or negative type of the assessment
	 */
	public static enum Type {
		POSITIVE, NEGATIVE
	}

	/**
	 * The value (i.e. increment or decrement of points to the player) of the
	 * effect
	 */
	@Param(value = "value")
	private Integer value;

	/**
	 * The message in the log for the effect, the justification for the
	 * assessment
	 */
	@Param(value = "message")
	private EAdString message;

	/**
	 * The {@link Type} of this effect
	 */
	@Param(value = "type")
	private Type type;

	/**
	 * @param type
	 *            The {@link Type} of this effect
	 * @param value
	 *            The value (i.e. benefit or cost) of this assessment
	 */
	public AssessmentEffect(Type type, Integer value) {
		this.type = type;
		this.value = value;
		this.message = EAdString.newEAdString("message");
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public EAdString getMessage() {
		return message;
	}

}
