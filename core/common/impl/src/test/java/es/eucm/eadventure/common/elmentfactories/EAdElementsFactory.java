package es.eucm.eadventure.common.elmentfactories;

import es.eucm.eadventure.common.elmentfactories.assets.CaptionFactory;
import es.eucm.eadventure.common.elmentfactories.assets.ShapeFactory;
import es.eucm.eadventure.common.elmentfactories.effects.EffectFactory;
import es.eucm.eadventure.common.elmentfactories.events.EventsFactory;
import es.eucm.eadventure.common.elmentfactories.sceneelments.SceneElementFactory;

public class EAdElementsFactory {
	
	private static EAdElementsFactory instance = new EAdElementsFactory();
	
	public static EAdElementsFactory getInstance(){
		return instance;	
	}
	
	private SceneElementFactory sceneElementFactory = new SceneElementFactory();
	
	private ShapeFactory shapeFactory = new ShapeFactory();
	
	private EffectFactory effectFactory = new EffectFactory();
	
	private EventsFactory eventsFactory = new EventsFactory();
	
	private StringFactory stringFactory = new StringFactory();
	
	private CaptionFactory captionFactory = new CaptionFactory();

	public CaptionFactory getCaptionFactory() {
		return captionFactory;
	}

	public StringFactory getStringFactory() {
		return stringFactory;
	}

	public EventsFactory getEventsFactory() {
		return eventsFactory;
	}

	public EffectFactory getEffectFactory() {
		return effectFactory;
	}

	public SceneElementFactory getSceneElementFactory() {
		return sceneElementFactory;
	}

	public ShapeFactory getShapeFactory() {
		return shapeFactory;
	}
	
	

}
