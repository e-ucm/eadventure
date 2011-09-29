package es.eucm.eadventure.common.model.effects.impl.physics;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.effects.EAdSceneElementEffect;
import es.eucm.eadventure.common.model.effects.impl.AbstractEAdEffect;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.variables.impl.operations.LiteralExpressionOperation;

@Element(detailed = PhApplyImpluse.class, runtime = PhApplyImpluse.class)
public class PhApplyImpluse extends AbstractEAdEffect implements EAdSceneElementEffect {
	
	@Param("xForce")
	private LiteralExpressionOperation xForce;
	
	@Param("yForce")
	private LiteralExpressionOperation yForce;

	@Param("element")
	private EAdSceneElement element;
	
	public PhApplyImpluse( ){
		this( null, null, null );
	}

	public PhApplyImpluse(EAdSceneElement element, LiteralExpressionOperation xForce, LiteralExpressionOperation yForce) {
		this.element = element;
		this.xForce = xForce;
		this.yForce = yForce;
		this.setQueueable(false);
	}

	public LiteralExpressionOperation getXForce() {
		return xForce;
	}
	
	public LiteralExpressionOperation getYForce() {
		return yForce;
	}
	
	public void setForce( LiteralExpressionOperation xForce, LiteralExpressionOperation yForce ){
		this.xForce = xForce;
		this.yForce = yForce;
	}

	@Override
	public EAdSceneElement getSceneElement() {
		return element;
	}

	@Override
	public void setSceneElement(EAdSceneElement sceneElement) {
		this.element = sceneElement;
	}

}
