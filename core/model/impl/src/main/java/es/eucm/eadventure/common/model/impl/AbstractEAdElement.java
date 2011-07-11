/**
 * eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the
 *    <e-UCM> research group.
 *
 *    Copyright 2005-2010 <e-UCM> research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    <e-UCM> is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure, version 2.0
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */

package es.eucm.eadventure.common.model.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import es.eucm.eadventure.common.interfaces.CopyNotSupportedException;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.EAdList;
import es.eucm.eadventure.common.model.events.EAdEvent;
import es.eucm.eadventure.common.resources.EAdBundleId;
import es.eucm.eadventure.common.resources.EAdResources;
import es.eucm.eadventure.common.resources.assets.AssetDescriptor;
import es.eucm.eadventure.common.resources.impl.EAdResourcesImpl;

/**
 * Abstract {@link es.eucm.eadventure.common.model.EAdElement} implementation.
 * 
 */
public abstract class AbstractEAdElement implements EAdElement {

	/**
	 * Resources of the eAdElement
	 */
	private EAdResources resources;

	@Param("id")
	protected String id;

	/**
	 * Events associated with this element
	 */
	protected EAdList<EAdEvent> events;

//	private static final Logger logger = LoggerFactory
//			.getLogger(AbstractEAdElement.class);
	private static final Logger logger = Logger.getLogger("AbstractEAdElement");

	/**
	 * Constructs a {@link AbstractEAdElement} with the specified parent
	 * element.
	 * 
	 * @param id
	 *            id of the element
	 * @throws NullPointerException
	 *             if {@code parent} is {@code null}.
	 */
	public AbstractEAdElement(String id) {
		this.id = id;
		resources = new EAdResourcesImpl(getClass());
		if (!(this instanceof EAdList))
			events = new EAdListImpl<EAdEvent>(EAdEvent.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AbstractEAdElement copy() {
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
	public AbstractEAdElement copy(boolean deepCopy) {
		AbstractEAdElement copy = copy();
		if (deepCopy) {
			//TODO
		}
		return copy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.common.model.EAdElement#getResources()
	 */
	@Override
	public EAdResources getResources() {
		return resources;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.common.model.EAdElement#getAsset(java.lang.String)
	 */
	@Override
	public AssetDescriptor getAsset(String id) {
		if (resources == null) {
			logger.log(Level.WARNING, "Element has no assets");
		}
		return resources.getAsset(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.common.model.EAdElement#getAsset(es.eucm.eadventure
	 * .common.resources.EAdBundleId, java.lang.String)
	 */
	@Override
	public AssetDescriptor getAsset(EAdBundleId bundleId, String id) {
		if (resources == null) {
			logger.log(Level.WARNING, "Element has no assets");
			return null;
		}
		return resources.getAsset(bundleId, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.common.model.EAdElement#getInitialBundle()
	 */
	@Override
	public EAdBundleId getInitialBundle() {
		return resources.getInitialBundle();
	}
	
	protected void setInitialBundle(EAdBundleId temp) {
		resources.setInitialBundle(temp);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.common.model.EAdElement#getId()
	 */
	@Override
	public String getId() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.common.model.EAdElement#getEvents()
	 */
	@Override
	public EAdList<EAdEvent> getEvents() {
		return events;
	}

}