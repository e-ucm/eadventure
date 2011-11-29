package es.eucm.eadventure.common.model.effects.impl.enums;

import com.gwtent.reflection.client.Reflectable;

/**
 * <p>The modification to the state:
 * <li>PLACE_IN_INVENTORY: move the {@link EAdSceneElementDef} to the inventory (remove from scene)</li>
 * <li>PLACE_IN_SCENE; move the {@link EAdSceneElementDef} to the scene (remove from inventory)</li>
 * <li>REMOVE_SCENE_AND_INVENTORY: remove the {@link EAdSceneElementDef} from scene and inventory</li>
 * </p>
 *
 */
@Reflectable
public enum Modification {
	PLACE_IN_INVENTORY, PLACE_IN_SCENE, REMOVE_SCENE_AND_INVENTORY
}