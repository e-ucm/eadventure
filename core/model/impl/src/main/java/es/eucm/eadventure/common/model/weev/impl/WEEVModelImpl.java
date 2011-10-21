package es.eucm.eadventure.common.model.weev.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.extra.impl.EAdListImpl;
import es.eucm.eadventure.common.model.impl.EAdElementImpl;
import es.eucm.eadventure.common.model.weev.Actor;
import es.eucm.eadventure.common.model.weev.WEEVModel;
import es.eucm.eadventure.common.model.weev.adaptation.AdaptationStructure;

/**
 * Default implementation of {@link WEEVModel}
 */
@Element(detailed = WEEVModelImpl.class, runtime = WEEVModelImpl.class)
public class WEEVModelImpl extends EAdElementImpl implements WEEVModel {

	private EAdList<Actor> actors;
	
	private Actor mainActor;
	
	private boolean firstPerson;
	
	private AdaptationStructure adaptationStructure;
	
	public WEEVModelImpl() {
		actors = new EAdListImpl<Actor>(Actor.class);
	}

	@Override
	public EAdList<Actor> getActors() {
		return actors;
	}

	@Override
	public Actor getMainActor() {
		return mainActor;
	}
	
	public void setMainActor(Actor mainActor) {
		this.mainActor = mainActor;
	}

	@Override
	public boolean isFirstPerson() {
		return firstPerson;
	}
	
	public void setFirstPerson(boolean firstPerson) {
		this.firstPerson = firstPerson;
	}

	@Override
	public AdaptationStructure getAdaptationStructure() {
		return adaptationStructure;
	}
	
	public void setAdaptationStructure(AdaptationStructure adaptationStructure) {
		this.adaptationStructure = adaptationStructure;
	}

}
