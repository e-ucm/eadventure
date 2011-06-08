package es.eucm.eadventure.common.model.effects.impl;

import es.eucm.eadventure.common.Element;
import es.eucm.eadventure.common.Param;
import es.eucm.eadventure.common.model.elements.EAdActor;

/**
 * <p>This effects modifies the state of an {@link EAdActor}</p>
 * <p>The state can change to the values specified by the 
 * modification value:
 * {@link Modification}
 *</p>
 */
@Element(detailed = EAdModifyActorState.class, runtime = EAdModifyActorState.class)
public class EAdModifyActorState extends AbstractEAdEffect {

	/**
	 * <p>The modification to the state:
	 * <li>PLACE_IN_INVENTORY: move the {@link EAdActor} to the inventory (remove from scene)</li>
	 * <li>PLACE_IN_SCENE; move the {@link EAdActor} to the scene (remove from inventory)</li>
	 * <li>REMOVE_SCENE_AND_INVENTORY: remove the {@link EAdActor} from scene and inventory</li>
	 * </p>
	 *
	 */
	public enum Modification {
		PLACE_IN_INVENTORY, PLACE_IN_SCENE, REMOVE_SCENE_AND_INVENTORY
	}
	
	@Param("modification")
	private Modification modification;
	
	@Param("actor")
	private EAdActor actor;
	
	public EAdModifyActorState(String id) {
		this(id, null, Modification.PLACE_IN_INVENTORY);
	}
	
	public EAdModifyActorState(String id, EAdActor actor, Modification modification) {
		super(id);
		this.modification = modification;
		this.actor = actor;
	}
	
	public void setModification(Modification modification) {
		this.modification = modification;
	}
	
	public Modification getModification() {
		return modification;
	}
	
	public void setActor(EAdActor actor) {
		this.actor = actor;
	}
	
	public EAdActor getActor() {
		return actor;
	}
	
}
