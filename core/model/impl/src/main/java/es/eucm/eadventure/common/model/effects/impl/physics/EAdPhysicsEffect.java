package es.eucm.eadventure.common.model.effects.impl.physics;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.effects.impl.AbstractEAdEffect;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.extra.impl.EAdListImpl;
import es.eucm.eadventure.common.model.variables.EAdVarDef;
import es.eucm.eadventure.common.model.variables.impl.EAdVarDefImpl;

@Element(detailed = EAdPhysicsEffect.class, runtime = EAdPhysicsEffect.class)
public class EAdPhysicsEffect extends AbstractEAdEffect {

	public enum PhType {
		STATIC, DYNAMIC
	};

	public enum PhShape {
		RECTANGULAR, CIRCULAR
	};

	public static final EAdVarDef<PhType> VAR_PH_TYPE = new EAdVarDefImpl<PhType>(
			"ph_type", PhType.class, PhType.STATIC);

	public static final EAdVarDef<PhShape> VAR_PH_SHAPE = new EAdVarDefImpl<PhShape>(
			"ph_shape", PhShape.class, PhShape.RECTANGULAR);

	public static final EAdVarDef<Float> VAR_PH_FRICTION = new EAdVarDefImpl<Float>(
			"ph_friction", Float.class, 0.3f);
	
	public static final EAdVarDef<Float> VAR_PH_RESTITUTION = new EAdVarDefImpl<Float>(
			"ph_restitution", Float.class, 0.1f);
	
	public static final EAdVarDef<Float> VAR_PH_DENSITY = new EAdVarDefImpl<Float>(
			"ph_restitution", Float.class, 0.001f);
		

	/**
	 * Elements that are affect by the physics
	 */
	@Param("elements")
	private EAdList<EAdSceneElement> elements;

	public EAdPhysicsEffect() {
		super("physicsEffect");
		elements = new EAdListImpl<EAdSceneElement>(EAdSceneElement.class);
		setBlocking(false);
		setQueueable(true);
	}

	public void addSceneElement(EAdSceneElement element) {
		this.elements.add(element);
	}

	public EAdList<EAdSceneElement> getElements() {
		return elements;
	}

}
