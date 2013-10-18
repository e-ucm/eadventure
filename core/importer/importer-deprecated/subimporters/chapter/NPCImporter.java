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
import java.util.LinkedHashMap;

import com.google.inject.Inject;

import es.eucm.ead.model.interfaces.features.enums.Orientation;
import es.eucm.ead.model.assets.drawable.EAdDrawable;
import es.eucm.ead.model.assets.drawable.basics.EAdBasicDrawable;
import es.eucm.ead.model.assets.drawable.basics.Image;
import es.eucm.ead.model.assets.drawable.basics.animation.Frame;
import es.eucm.ead.model.assets.drawable.basics.animation.FramesAnimation;
import es.eucm.ead.model.assets.drawable.compounds.EAdStateDrawable;
import es.eucm.ead.model.assets.drawable.compounds.StateDrawable;
import es.eucm.ead.model.assets.drawable.filters.FilteredDrawable;
import es.eucm.ead.model.assets.drawable.filters.MatrixFilter;
import es.eucm.ead.model.elements.enums.CommonStates;
import es.eucm.ead.model.elements.scenes.SceneElementDef;
import es.eucm.ead.model.params.util.Matrix;
import ead.importer.EAdElementImporter;
import ead.importer.annotation.ImportAnnotator;
import ead.importer.interfaces.EAdElementFactory;
import ead.importer.interfaces.ResourceImporter;
import es.eucm.ead.tools.StringHandler;
import es.eucm.eadventure.common.data.chapter.Action;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.elements.NPC;
import es.eucm.eadventure.common.data.chapter.resources.Resources;

public class NPCImporter extends ActorImporter<NPC> {

	@Inject
	public NPCImporter(StringHandler stringHandler,
			ResourceImporter resourceImporter,
			EAdElementFactory elementFactory,
			EAdElementImporter<Action, SceneElementDef> actionImporter,
			EAdElementFactory factory, ImportAnnotator annotator,
			EAdElementImporter<Conditions, Condition> conditionsImporter) {
		super(stringHandler, resourceImporter, elementFactory, actionImporter,
				factory, annotator, conditionsImporter);
	}

	@Override
	public void initResourcesCorrespondencies() {
		ArrayList<EAdStateDrawable> drawables = new ArrayList<EAdStateDrawable>();

		annotator.annotate(ImportAnnotator.Type.Entry,
				ImportAnnotator.Key.Role, "actor.npc");

		properties = new LinkedHashMap<String, String>();
		properties
				.put(NPC.RESOURCE_TYPE_STAND_DOWN, SceneElementDef.appearance);

		objectClasses = new LinkedHashMap<String, Object>();
		objectClasses.put(NPC.RESOURCE_TYPE_STAND_DOWN, drawables);

		for (Resources r : element.getResources()) {

			StateDrawable stateDrawable = new StateDrawable();

			StateDrawable stand = getOrientedAsset(r,
					NPC.RESOURCE_TYPE_STAND_UP, NPC.RESOURCE_TYPE_STAND_DOWN,
					NPC.RESOURCE_TYPE_STAND_RIGHT, NPC.RESOURCE_TYPE_STAND_LEFT);
			if (stand != null) {
				stateDrawable.addDrawable(CommonStates.DEFAULT.toString(),
						stand);
			}

			StateDrawable walk = getOrientedAsset(r, NPC.RESOURCE_TYPE_WALK_UP,
					NPC.RESOURCE_TYPE_WALK_DOWN, NPC.RESOURCE_TYPE_WALK_RIGHT,
					NPC.RESOURCE_TYPE_WALK_LEFT);
			if (walk != null) {
				stateDrawable
						.addDrawable(CommonStates.WALKING.toString(), walk);
			}

			StateDrawable talking = getOrientedAsset(r,
					NPC.RESOURCE_TYPE_SPEAK_UP, NPC.RESOURCE_TYPE_SPEAK_DOWN,
					NPC.RESOURCE_TYPE_SPEAK_RIGHT, NPC.RESOURCE_TYPE_SPEAK_LEFT);
			if (talking != null) {
				stateDrawable.addDrawable(CommonStates.TALKING.toString(),
						talking);
			}

			StateDrawable using = getOrientedAsset(r,
					NPC.RESOURCE_TYPE_USE_RIGHT, NPC.RESOURCE_TYPE_USE_LEFT,
					NPC.RESOURCE_TYPE_SPEAK_RIGHT, NPC.RESOURCE_TYPE_USE_LEFT);
			if (using != null) {
				stateDrawable.addDrawable(CommonStates.USING.toString(),
						using == null || using.getDrawables().isEmpty() ? stand
								: using);
			}

			drawables.add(stateDrawable);
		}
	}

	private StateDrawable getOrientedAsset(Resources r, String up, String down,
			String right, String left) {
		if (up == null && down == null && right == null && left == null) {
			return null;
		}

		StateDrawable oriented = new StateDrawable();
		EAdDrawable north = (EAdDrawable) resourceImporter.getAssetDescritptor(
				r.getAssetPath(up), Image.class);
		EAdDrawable south = (EAdDrawable) resourceImporter.getAssetDescritptor(
				r.getAssetPath(down), Image.class);
		EAdDrawable east = (EAdDrawable) resourceImporter.getAssetDescritptor(r
				.getAssetPath(right), Image.class);
		EAdDrawable west = (EAdDrawable) resourceImporter.getAssetDescritptor(r
				.getAssetPath(left), Image.class);

		// Fill north
		if (north == null) {
			north = south;
		}

		// Fill east
		if (east == null && west != null && west instanceof FramesAnimation) {
			east = mirrorAnimation((FramesAnimation) west);
		}

		if (east == null) {
			east = south;
		}

		// Fill west
		if (west == null && east != null) {
			west = mirrorAnimation((FramesAnimation) east);
		}

		if (west == null) {
			west = south;
		}

		if (north == south && south == west && west == east && east == null) {
			return null;
		}

		if (north != null) {
			oriented.setDrawable(Orientation.N, north);
		}

		if (south != null) {
			oriented.setDrawable(Orientation.S, south);
		}

		if (east != null) {
			oriented.setDrawable(Orientation.E, east);
		}

		if (west != null) {
			oriented.setDrawable(Orientation.W, west);
		}

		return oriented;
	}

	public FramesAnimation mirrorAnimation(FramesAnimation a) {
		FramesAnimation mirror = new FramesAnimation();

		for (Frame f : a.getFrames()) {
			Matrix m = new Matrix();
			m.scale(-1.0f, 1.0f, true);
			EAdBasicDrawable d = new FilteredDrawable(f.getDrawable(),
					new MatrixFilter(m, 1.0f, 0.0f));
			mirror.addFrame(new Frame(d, f.getTime()));
		}
		return mirror;
	}
}
