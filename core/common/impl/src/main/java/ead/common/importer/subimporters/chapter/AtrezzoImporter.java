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

package ead.common.importer.subimporters.chapter;

import java.util.HashMap;

import com.google.inject.Inject;

import ead.common.EAdElementImporter;
import ead.common.importer.interfaces.EAdElementFactory;
import ead.common.importer.interfaces.ResourceImporter;
import ead.common.model.elements.EAdAction;
import ead.common.model.elements.scene.EAdSceneElementDef;
import ead.common.model.elements.scenes.SceneElementDefImpl;
import ead.common.resources.StringHandler;
import ead.common.resources.assets.drawable.basics.ImageImpl;
import es.eucm.eadventure.common.data.chapter.Action;
import es.eucm.eadventure.common.data.chapter.elements.Atrezzo;


public class AtrezzoImporter extends ActorImporter<Atrezzo>{
	
	@Inject
	public AtrezzoImporter(StringHandler stringHandler,
			ResourceImporter resourceImporter,
			EAdElementFactory elementFactory,
			EAdElementImporter<Action, EAdAction> actionImporter, EAdElementFactory factory) {
		super(stringHandler, resourceImporter, elementFactory, actionImporter, factory);
	}

	protected void addActionsEffect(Atrezzo oldObject, EAdSceneElementDef actor){
		// Atrezzo doesn't need to add any action
	}

	@Override
	public void initResourcesCorrespondencies( ) {
		
		properties = new HashMap<String, String>();
		properties.put(Atrezzo.RESOURCE_TYPE_IMAGE, SceneElementDefImpl.appearance);
		
		objectClasses = new HashMap<String, Object>();
		objectClasses.put(Atrezzo.RESOURCE_TYPE_IMAGE, ImageImpl.class);
		
	}


}
