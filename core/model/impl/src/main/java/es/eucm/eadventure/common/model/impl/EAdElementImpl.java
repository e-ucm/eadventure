package es.eucm.eadventure.common.model.impl;

import es.eucm.eadventure.common.interfaces.CopyNotSupportedException;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.EAdElement;

public abstract class EAdElementImpl implements EAdElement {

	private static int ID_GENERATOR = 0;

	@Param("id")
	protected String id;

	public EAdElementImpl() {
		this.id = "EAdElement" + ID_GENERATOR++
				+ Math.round(Math.random() * 1000);
	}

	public EAdElementImpl(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public EAdElement copy() {
		try {
			//TODO removed clone for GWT, should find other solution?
			//return (AbstractEAdElement) super.clone();
			return null;
		} catch (Exception e) {
			throw new CopyNotSupportedException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EAdElement copy(boolean deepCopy) {
		EAdElement copy = copy();
		if (deepCopy) {
			//TODO
		}
		return copy;
	}

}
