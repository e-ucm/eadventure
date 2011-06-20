package es.eucm.eadventure.plugin.box2d;

import java.util.ArrayList;

import es.eucm.eadventure.common.model.effects.impl.AbstractEAdEffect;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;

public class Box2DEffect extends AbstractEAdEffect {
	
	public EAdSceneElement ground;
	
	public ArrayList<EAdSceneElement> box = new ArrayList<EAdSceneElement>();

	public Box2DEffect(String id) {
		super(id);
	}

}
