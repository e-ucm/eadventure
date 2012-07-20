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

package ead.importer.subimporters.chapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.inject.Inject;

import ead.common.interfaces.features.enums.Orientation;
import ead.common.model.elements.EAdAction;
import ead.common.model.elements.enums.CommonStates;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.common.resources.assets.drawable.EAdDrawable;
import ead.common.resources.assets.drawable.basics.EAdBasicDrawable;
import ead.common.resources.assets.drawable.basics.Image;
import ead.common.resources.assets.drawable.basics.animation.Frame;
import ead.common.resources.assets.drawable.basics.animation.FramesAnimation;
import ead.common.resources.assets.drawable.compounds.EAdStateDrawable;
import ead.common.resources.assets.drawable.compounds.StateDrawable;
import ead.common.resources.assets.drawable.filters.FilteredDrawable;
import ead.common.resources.assets.drawable.filters.MatrixFilter;
import ead.common.util.BasicMatrix;
import ead.importer.EAdElementImporter;
import ead.importer.annotation.ImportAnnotator;
import ead.importer.interfaces.EAdElementFactory;
import ead.importer.interfaces.ResourceImporter;
import ead.tools.StringHandler;
import es.eucm.eadventure.common.data.chapter.Action;
import es.eucm.eadventure.common.data.chapter.elements.NPC;
import es.eucm.eadventure.common.data.chapter.resources.Resources;

public class NPCImporter extends ActorImporter<NPC> {

	@Inject
	public NPCImporter(StringHandler stringHandler,
			ResourceImporter resourceImporter,
			EAdElementFactory elementFactory,
			EAdElementImporter<Action, EAdAction> actionImporter,
			EAdElementFactory factory,
			ImportAnnotator annotator) {
		super(stringHandler, resourceImporter, elementFactory, actionImporter,
				factory, annotator);
	}

	@Override
	public void initResourcesCorrespondencies() {
		ArrayList<EAdStateDrawable> drawables = new ArrayList<EAdStateDrawable>();

		properties = new HashMap<String, String>();
		properties.put(NPC.RESOURCE_TYPE_STAND_DOWN,
				SceneElementDef.appearance);

		objectClasses = new HashMap<String, Object>();
		objectClasses.put(NPC.RESOURCE_TYPE_STAND_DOWN, drawables);

		for (Resources r : element.getResources()) {

			StateDrawable stateDrawable = new StateDrawable();

			StateDrawable stand = getOrientedAsset(r,
					NPC.RESOURCE_TYPE_STAND_UP, NPC.RESOURCE_TYPE_STAND_DOWN,
					NPC.RESOURCE_TYPE_STAND_RIGHT, NPC.RESOURCE_TYPE_STAND_LEFT);
			stateDrawable.addDrawable(
					CommonStates.EAD_STATE_DEFAULT.toString(), stand);

			StateDrawable walk = getOrientedAsset(r,
					NPC.RESOURCE_TYPE_WALK_UP, NPC.RESOURCE_TYPE_WALK_DOWN,
					NPC.RESOURCE_TYPE_WALK_RIGHT, NPC.RESOURCE_TYPE_WALK_LEFT);
			stateDrawable.addDrawable(
					CommonStates.EAD_STATE_WALKING.toString(),
					walk == null ? stand : walk);

			StateDrawable talking = getOrientedAsset(r,
					NPC.RESOURCE_TYPE_SPEAK_UP, NPC.RESOURCE_TYPE_SPEAK_DOWN,
					NPC.RESOURCE_TYPE_SPEAK_RIGHT, NPC.RESOURCE_TYPE_SPEAK_LEFT);
			stateDrawable.addDrawable(
					CommonStates.EAD_STATE_TALKING.toString(),
					talking == null ? stand : talking);

			StateDrawable using = getOrientedAsset(r,
					NPC.RESOURCE_TYPE_USE_RIGHT, NPC.RESOURCE_TYPE_USE_LEFT,
					NPC.RESOURCE_TYPE_SPEAK_RIGHT, NPC.RESOURCE_TYPE_USE_LEFT);
			stateDrawable.addDrawable(CommonStates.EAD_STATE_USING.toString(),
					using == null ? stand : using);

			drawables.add(stateDrawable);
		}
	}

	private StateDrawable getOrientedAsset(Resources r, String up,
			String down, String right, String left) {
		if (up == null && down == null && right == null && left == null)
			return null;

		StateDrawable oriented = new StateDrawable();
		EAdDrawable north = (EAdDrawable) resourceImporter.getAssetDescritptor(
				r.getAssetPath(up), Image.class);
		EAdDrawable south = (EAdDrawable) resourceImporter.getAssetDescritptor(
				r.getAssetPath(down), Image.class);
		EAdDrawable east = (EAdDrawable) resourceImporter.getAssetDescritptor(
				r.getAssetPath(right), Image.class);
		EAdDrawable west = (EAdDrawable) resourceImporter.getAssetDescritptor(
				r.getAssetPath(left), Image.class);

		// Fill north
		if (north == null) {
			north = south;
		}

		// Fill east
		if (east == null && west != null && west instanceof FramesAnimation) {
			east = mirrorAnimation( (FramesAnimation) west );
		}

		if (east == null) {
			east = south;
		}

		// Fill west
		if (west == null && east != null) {
			west = mirrorAnimation( (FramesAnimation) east );
		}

		if (west == null) {
			west = south;
		}

		oriented.setDrawable(Orientation.N, north);
		oriented.setDrawable(Orientation.S, south);
		oriented.setDrawable(Orientation.E, east);
		oriented.setDrawable(Orientation.W, west);

		return oriented;
	}
	
	public FramesAnimation mirrorAnimation( FramesAnimation a ){
		FramesAnimation mirror = new FramesAnimation();

		for (Frame f : a.getFrames()) {
			BasicMatrix m = new BasicMatrix();
			m.scale(-1.0f, 1.0f, true);
			EAdBasicDrawable d = new FilteredDrawable(f.getDrawable(), new MatrixFilter(m, 1.0f, 0.0f));
			mirror.addFrame(new Frame(d, f.getTime()));
		}
		return mirror;
	}
}
