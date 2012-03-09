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

import ead.common.interfaces.features.Variabled;
import ead.common.model.EAdElement;
import ead.common.model.elements.extra.EAdList;
import ead.common.params.text.EAdString;

/**
 * Interface for an eAdventure game static model.
 */
public interface EAdAdventureModel extends EAdElement, Variabled {
	
	/**
	 * Default width for games
	 */
	public static final int DEFAULT_WIDTH = 800;
	
	/**
	 * Default height for games
	 */
	public static final int DEFAULT_HEIGHT = 600;

	/**
	 * Returns the chapters of the adventures.
	 * 
	 * @return the chapters in the adventure
	 */
	EAdList<EAdChapter> getChapters();

	/**
	 * @return the description of the adventure
	 */
	EAdString getDescription();

	/**
	 * @return the title of the adventure
	 */
	EAdString getTitle();

	/**
	 * Sets the inventory for the adventure
	 * 
	 * @param inventory
	 *            the inventory
	 */
	void setInventory(EAdInventory inventory);

	/**
	 * Returns the adventure inventory. {@code null} if the adventure does not
	 * require inventory
	 * 
	 * @return the inventory
	 */
	EAdInventory getInventory();
	
	/**
	 * Returns the width for this game
	 * @return
	 */
	int getGameWidth();
	
	/**
	 * Returns the height for this game
	 * @return
	 */
	int getGameHeight();

}
