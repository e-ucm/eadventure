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

package es.eucm.eadventure.common.impl.importer.subimporters.chapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.inject.Inject;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.Action;
import es.eucm.eadventure.common.data.chapter.elements.NPC;
import es.eucm.eadventure.common.data.chapter.resources.Resources;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.impl.importer.interfaces.ResourceImporter;
import es.eucm.eadventure.common.interfaces.features.Oriented.Orientation;
import es.eucm.eadventure.common.model.actions.EAdAction;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement.CommonStates;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.Drawable;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.ImageImpl;
import es.eucm.eadventure.common.resources.assets.drawable.compounds.OrientedDrawable;
import es.eucm.eadventure.common.resources.assets.drawable.compounds.StateDrawable;
import es.eucm.eadventure.common.resources.assets.drawable.compounds.impl.OrientedDrawableImpl;
import es.eucm.eadventure.common.resources.assets.drawable.compounds.impl.StateDrawableImpl;

public class NPCImporter extends ActorImporter<NPC> {

	@Inject
	public NPCImporter(StringHandler stringHandler,
			ResourceImporter resourceImporter,
			EAdElementFactory elementFactory,
			EAdElementImporter<Action, EAdAction> actionImporter) {
		super(stringHandler, resourceImporter, elementFactory, actionImporter);
	}

	@Override
	public void initResourcesCorrespondencies() {
		ArrayList<StateDrawable> drawables = new ArrayList<StateDrawable>();

		properties = new HashMap<String, String>();
		properties.put(NPC.RESOURCE_TYPE_STAND_DOWN,
				EAdBasicSceneElement.appearance);

		objectClasses = new HashMap<String, Object>();
		objectClasses.put(NPC.RESOURCE_TYPE_STAND_DOWN, drawables);

		for (Resources r : element.getResources()) {

			StateDrawableImpl stateDrawable = new StateDrawableImpl();

			OrientedDrawable stand = getOrientedAsset(r,
					NPC.RESOURCE_TYPE_STAND_UP, NPC.RESOURCE_TYPE_STAND_DOWN,
					NPC.RESOURCE_TYPE_STAND_RIGHT, NPC.RESOURCE_TYPE_STAND_LEFT);
			stateDrawable.addDrawable(
					CommonStates.EAD_STATE_DEFAULT.toString(), stand);

			OrientedDrawable walk = getOrientedAsset(r,
					NPC.RESOURCE_TYPE_WALK_UP, NPC.RESOURCE_TYPE_WALK_DOWN,
					NPC.RESOURCE_TYPE_WALK_RIGHT, NPC.RESOURCE_TYPE_WALK_LEFT);
			stateDrawable.addDrawable(
					CommonStates.EAD_STATE_WALKING.toString(),
					walk == null ? stand : walk);

			OrientedDrawable talking = getOrientedAsset(r,
					NPC.RESOURCE_TYPE_SPEAK_UP, NPC.RESOURCE_TYPE_SPEAK_DOWN,
					NPC.RESOURCE_TYPE_SPEAK_RIGHT, NPC.RESOURCE_TYPE_SPEAK_LEFT);
			stateDrawable.addDrawable(
					CommonStates.EAD_STATE_TALKING.toString(),
					talking == null ? stand : talking);

			OrientedDrawable using = getOrientedAsset(r,
					NPC.RESOURCE_TYPE_USE_RIGHT, NPC.RESOURCE_TYPE_USE_LEFT,
					NPC.RESOURCE_TYPE_SPEAK_RIGHT, NPC.RESOURCE_TYPE_USE_LEFT);
			stateDrawable.addDrawable(CommonStates.EAD_STATE_USING.toString(),
					using == null ? stand : using);

			drawables.add(stateDrawable);
		}

	}

	private OrientedDrawable getOrientedAsset(Resources r, String up,
			String down, String right, String left) {
		if (up == null && down == null && right == null && left == null)
			return null;

		OrientedDrawableImpl oriented = new OrientedDrawableImpl();
		Drawable north = (Drawable) resourceImporter.getAssetDescritptor(r.getAssetPath(up),
				ImageImpl.class);
		Drawable south = (Drawable) resourceImporter.getAssetDescritptor(r.getAssetPath(down),
				ImageImpl.class);
		Drawable east = (Drawable) resourceImporter.getAssetDescritptor(r.getAssetPath(right),
				ImageImpl.class);
		Drawable west = (Drawable) resourceImporter.getAssetDescritptor(r.getAssetPath(left),
				ImageImpl.class);

		oriented.setDrawable(Orientation.N, north);
		oriented.setDrawable(Orientation.S, south);
		oriented.setDrawable(Orientation.E, east);
		oriented.setDrawable(Orientation.W, west);

		return oriented;
	}
}
