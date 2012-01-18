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

package ead.common.importer.subimporters;

import com.google.inject.Inject;

import ead.common.EAdElementImporter;
import ead.common.importer.interfaces.EAdElementFactory;
import ead.common.model.elements.EAdAdventureModel;
import ead.common.model.elements.EAdAdventureModelImpl;
import ead.common.model.elements.EAdChapter;
import ead.common.model.elements.InventoryImpl;
import ead.common.resources.StringHandler;
import es.eucm.eadventure.common.data.adventure.AdventureData;
import es.eucm.eadventure.common.data.chapter.Chapter;

/**
 * An importer for old adventures
 * 
 * 
 */
public class AdventureImporter implements
		EAdElementImporter<AdventureData, EAdAdventureModel> {

	private EAdElementImporter<Chapter, EAdChapter> chapterImporter;

	private StringHandler stringsWriter;

	private EAdElementFactory factory;

	@Inject
	public AdventureImporter(
			EAdElementImporter<Chapter, EAdChapter> chapterImporter,
			StringHandler stringHandler, EAdElementFactory factory) {
		this.chapterImporter = chapterImporter;
		this.stringsWriter = stringHandler;
		this.factory = factory;
	}

	@Override
	public EAdAdventureModel init(AdventureData oldData) {
		EAdAdventureModelImpl model = new EAdAdventureModelImpl();
		return model;
	}

	@Override
	public EAdAdventureModel convert(AdventureData oldData, Object object) {
		factory.setOldDataModel(oldData);
		EAdAdventureModelImpl model = (EAdAdventureModelImpl) object;
		
		// FIXME positions for the inventory (among other thins in AdventureData)
		if (oldData.getInventoryPosition() != AdventureData.INVENTORY_NONE){
			model.setInventory(new InventoryImpl());
		}

		stringsWriter.setString(model.getTitle(), oldData.getTitle());
		stringsWriter.setString(model.getDescription(),
				oldData.getDescription());

		for (Chapter oldChapter : oldData.getChapters()) {
			EAdChapter newChapter = chapterImporter.init(oldChapter);

			if (newChapter != null) {
				model.getChapters().add(newChapter);
			}

			newChapter = chapterImporter.convert(oldChapter, newChapter);

		}

		return model;
	}

}
