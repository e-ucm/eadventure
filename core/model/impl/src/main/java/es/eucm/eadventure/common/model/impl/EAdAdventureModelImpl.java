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

import es.eucm.eadventure.common.interfaces.CopyNotSupportedException;
import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.EAdAdventureModel;
import es.eucm.eadventure.common.model.EAdChapter;
import es.eucm.eadventure.common.model.EAdElementList;
import es.eucm.eadventure.common.model.EAdInventory;
import es.eucm.eadventure.common.model.events.EAdEvent;
import es.eucm.eadventure.common.model.impl.inventory.EAdBasicInventory;
import es.eucm.eadventure.common.resources.EAdBundleId;
import es.eucm.eadventure.common.resources.EAdResources;
import es.eucm.eadventure.common.resources.EAdString;
import es.eucm.eadventure.common.resources.assets.AssetDescriptor;
import es.eucm.eadventure.common.resources.impl.EAdResourcesImpl;

/**
 * The eAdventure game model.
 */
@Element(detailed = EAdAdventureModelImpl.class, runtime = EAdAdventureModelImpl.class)
public class EAdAdventureModelImpl implements EAdAdventureModel {
	
	/**
	 * Mode of the player.
	 */
	private PlayerMode playerMode;

	private EAdElementList<EAdChapter> chapters;

	@Param("description")
	private EAdString description;
	
	@Param("title")
	private EAdString title;

	@Param("inventory")
	private EAdInventory inventory;
	
	private EAdResources resources;
	
	private EAdElementList<EAdEvent> events;
	
	/**
	 * Constructs a {@link EAdAdventureModelImpl}.
	 */
	public EAdAdventureModelImpl() {
		resources = new EAdResourcesImpl(getClass());
		chapters = new EAdElementListImpl<EAdChapter>(EAdChapter.class);
		events = new EAdElementListImpl<EAdEvent>(EAdEvent.class);
		inventory = new EAdBasicInventory();
	}
	
	public EAdElementList<EAdChapter> getChapters() {
		return chapters;
	}
	
	public PlayerMode getPlayerMode() {
		return playerMode;
	}

	public void setPlayerMode(PlayerMode playerMode) {
		this.playerMode = playerMode;
	}
	
	public EAdAdventureModelImpl copy(){
		try {
			//TODO removed clone for GWT, should find other solution?
			//return (EAdAdventureModelImpl) super.clone();
			return null;
		} catch (Exception e) {
			throw new CopyNotSupportedException(e);
		}
	}
	
	public EAdAdventureModelImpl copy(boolean deepCopy){
		try {
			EAdAdventureModelImpl copy = (EAdAdventureModelImpl) this.copy();
			if(deepCopy){

			}
			return copy;
		} catch (ClassCastException e) {
			throw new CopyNotSupportedException(e);
		}
	}
	
	@Override
	public AssetDescriptor getAsset(String id) {
		return resources.getAsset(id);
	}

	@Override
	public AssetDescriptor getAsset(EAdBundleId bundleId, String id) {
		return resources.getAsset(id);
	}

	@Override
	public EAdBundleId getInitialBundle() {
		return resources.getInitialBundle();
	}

	@Override
	public EAdResources getResources() {
		return resources;
	}
	
	public String getId() {
		return "adventure";
	}

	/**
	 * @return the description
	 */
	@Override
	public EAdString getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(EAdString description) {
		this.description = description;
	}

	/**
	 * @return the title
	 */
	@Override
	public EAdString getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(EAdString title) {
		this.title = title;
	}
	
	/* (non-Javadoc)
	 * @see es.eucm.eadventure.common.model.EAdElement#getEvents()
	 */
	@Override
	public EAdElementList<EAdEvent> getEvents() {
		return events;
	}

	@Override
	public EAdInventory getInventory() {
		return inventory;
	}
	
	public void setInventory(EAdInventory inventory) {
		this.inventory = inventory;
	}
	
}
