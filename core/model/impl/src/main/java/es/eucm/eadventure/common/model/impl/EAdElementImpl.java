package es.eucm.eadventure.common.model.impl;

import com.gwtent.reflection.client.Reflectable;

import es.eucm.eadventure.common.model.EAdElement;

@Reflectable
public abstract class EAdElementImpl implements EAdElement {

	protected String id;

	public EAdElementImpl() {
		this.id = "elem" + Math.round(Math.random() * 10000);
	}

	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public EAdElement copy() {
		// To implement in inherit classes
		return null;
	}

	@Override
	public EAdElement copy(boolean deepCopy) {
		// To implement in inherit classes
		return null;
	}
	
	public String toString(){
		return id;
	}

}
