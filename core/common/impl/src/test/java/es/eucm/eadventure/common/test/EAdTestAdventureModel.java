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

package es.eucm.eadventure.common.test;

import es.eucm.eadventure.common.elmentfactories.scenedemos.BasicScene;
import es.eucm.eadventure.common.model.impl.EAdAdventureModelImpl;
import es.eucm.eadventure.common.model.impl.EAdChapterImpl;
import es.eucm.eadventure.common.resources.EAdString;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.impl.DefaultStringHandler;

public class EAdTestAdventureModel extends EAdAdventureModelImpl {

	private StringHandler stringHandler;

	public EAdTestAdventureModel() {
		// TODO Habrá que guardar también las claves para los strings
		stringHandler = new DefaultStringHandler();

		// Chapter 1
		EAdChapterImpl c = new EAdChapterImpl("chapter1");

		EAdString chapter1Description = new EAdString("chapter1_description");
		stringHandler.addString(chapter1Description,
				"A test chapter for reading and writing XML");
		c.setDescription(chapter1Description);

		EAdString chapter1Title = new EAdString("chapter1_title");
		stringHandler.addString(chapter1Title, "Test Chapter 1");
		c.setTitle(chapter1Title);
		
		this.getChapters().add(c);

		// Space

		BasicScene scene = new BasicScene("basicScene", c);
		c.getScenes().add(scene);
		
//		for (EAdTimer timer : space.timers)
//			c.getTimers().add(timer);

//		
//		if (space.panielActor != null)
//			c.getActors().add(space.panielActor);
//		if (space.orientedActor != null)
//			c.getActors().add(space.orientedActor);
//		if (space.buttonActor != null)
//			c.getActors().add(space.buttonActor);
	}


}
