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

package ead.common.model.elements;

import java.util.LinkedHashMap;
import java.util.Map;

import ead.common.interfaces.Element;
import ead.common.interfaces.Param;
import ead.common.model.EAdElement;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.extra.EAdListImpl;
import ead.common.model.elements.extra.EAdMap;
import ead.common.model.elements.extra.EAdMapImpl;
import ead.common.model.elements.variables.EAdVarDef;
import ead.common.params.text.EAdString;

/**
 * The eAdventure game model.
 */
@Element
public class BasicAdventureModel extends BasicElement implements EAdAdventureModel {

	@Param("description")
	public EAdString description;

	@Param("title")
	private EAdString title;

	@Param("chapters")
	private EAdList<EAdChapter> chapters;

	@Param("vars")
	private EAdMap<EAdVarDef<?>, Object> vars;

	@Param("inventory")
	private EAdInventory inventory;

	@Param("width")
	private int gameWidth;

	@Param("height")
	private int gameHeight;

	@Param("depthControlList")
	private EAdList<EAdElement> depthControlList;

	// This map is fulfilled with the values in ead.properties. That's why it
	// hasn't got a @Param annotation
	private Map<String, String> properties;

	/**
	 * Constructs a {@link BasicAdventureModel}.
	 */
	public BasicAdventureModel() {
		chapters = new EAdListImpl<EAdChapter>(EAdChapter.class);
		vars = new EAdMapImpl<EAdVarDef<?>, Object>(EAdVarDef.class,
				Object.class);
		description = EAdString.newRandomEAdString("desc");
		title = EAdString.newRandomEAdString("title");
		gameWidth = DEFAULT_WIDTH;
		gameHeight = DEFAULT_HEIGHT;
		depthControlList = new EAdListImpl<EAdElement>(EAdElement.class);
		properties = new LinkedHashMap<String, String>();
	}

	public EAdList<EAdChapter> getChapters() {
		return chapters;
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

	@Override
	public void setInventory(EAdInventory inventory) {
		this.inventory = inventory;
	}

	@Override
	public EAdInventory getInventory() {
		return inventory;
	}

	public EAdList<EAdElement> getDepthControlList() {
		return depthControlList;
	}

	@Override
	public int getGameWidth() {
		return gameWidth;
	}

	@Override
	public int getGameHeight() {
		return gameHeight;
	}

	public void setGameWidth(int width) {
		this.gameWidth = width;
	}

	public void setGameHeight(int height) {
		this.gameHeight = height;
	}

	@Override
	public Map<String, String> getProperties() {
		return properties;
	}

	@Override
	public void setProperty(String key, String value) {
		properties.put(key, value);
	}

}
