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

import java.util.Map.Entry;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.elements.EAdAdventureModel;
import es.eucm.eadventure.common.model.elements.EAdChapter;
import es.eucm.eadventure.common.model.elements.PlayerMode;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.extra.EAdMap;
import es.eucm.eadventure.common.model.extra.impl.EAdListImpl;
import es.eucm.eadventure.common.model.extra.impl.EAdMapImpl;
import es.eucm.eadventure.common.model.variables.EAdVarDef;
import es.eucm.eadventure.common.params.EAdString;

/**
 * The eAdventure game model.
 */
@Element(detailed = EAdAdventureModelImpl.class, runtime = EAdAdventureModelImpl.class)
public class EAdAdventureModelImpl implements EAdAdventureModel {

	/**
	 * Mode of the player.
	 */
	@Param("playerMode")
	private PlayerMode playerMode;

	@Param("description")
	public EAdString description;

	@Param("title")
	private EAdString title;

	@Param("chapters")
	private EAdList<EAdChapter> chapters;

	@Param("vars")
	private EAdMap<EAdVarDef<?>, Object> vars;

	/**
	 * Constructs a {@link EAdAdventureModelImpl}.
	 */
	public EAdAdventureModelImpl() {
		chapters = new EAdListImpl<EAdChapter>(EAdChapter.class);
		vars = new EAdMapImpl<EAdVarDef<?>, Object>(EAdVarDef.class,
				Object.class);
		description = EAdString.newEAdString("adventureDescription");
		title = EAdString.newEAdString("adventureTitle");
	}

	public EAdList<EAdChapter> getChapters() {
		return chapters;
	}

	public PlayerMode getPlayerMode() {
		return playerMode;
	}

	public void setPlayerMode(PlayerMode playerMode) {
		this.playerMode = playerMode;
	}

	public EAdAdventureModelImpl copy() {
		return copy(false);
	}

	public EAdAdventureModelImpl copy(boolean deepCopy) {
		EAdAdventureModelImpl copy = new EAdAdventureModelImpl();
		if (deepCopy) {
			for (EAdChapter c : this.getChapters()) {
				copy.getChapters().add((EAdChapter) c.copy(deepCopy));
			}
			copy.setPlayerMode(getPlayerMode());

			for (Entry<EAdVarDef<?>, Object> entry : getVars().entrySet())
				copy.getVars().put(entry.getKey(), entry.getValue());
		}
		return copy;

	}

	public String getId() {
		return "adventure";
	}
	
	@Override
	public void setId(String id) {
	}


	/**
	 * @return the description
	 */
	@Override
	public EAdString getDescription() {
		return description;
	}

	/**
	 * @return the title
	 */
	@Override
	public EAdString getTitle() {
		return title;
	}

	@Override
	public EAdMap<EAdVarDef<?>, Object> getVars() {
		return vars;
	}

	@Override
	public <T> void setVarInitialValue(EAdVarDef<T> var, T value) {
		vars.put(var, value);
	}
	
	public void setTitle(EAdString title) {
		this.title = title;
	}

	public void setDescription(EAdString description) {
		this.description = description;
	}

}
