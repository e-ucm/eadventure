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

package es.eucm.eadventure.common.impl.importer.subimporters.chapter.scene;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.ElementReference;
import es.eucm.eadventure.common.data.chapter.Exit;
import es.eucm.eadventure.common.data.chapter.Trajectory;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.elements.ActiveArea;
import es.eucm.eadventure.common.data.chapter.elements.Barrier;
import es.eucm.eadventure.common.data.chapter.elements.Player;
import es.eucm.eadventure.common.data.chapter.resources.Resources;
import es.eucm.eadventure.common.data.chapter.scenes.Scene;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.impl.importer.interfaces.ResourceImporter;
import es.eucm.eadventure.common.model.elements.EAdChapter;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.EAdSceneElementDef;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneImpl;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent;
import es.eucm.eadventure.common.model.events.enums.SceneElementEventType;
import es.eucm.eadventure.common.model.events.impl.EAdSceneElementEventImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.trajectories.impl.NodeTrajectoryDefinition;
import es.eucm.eadventure.common.model.trajectories.impl.SimpleTrajectoryDefinition;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl.Corner;
import es.eucm.eadventure.common.predef.model.effects.EAdMakeActiveElementEffect;
import es.eucm.eadventure.common.predef.model.effects.EAdMoveActiveElement;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.ImageImpl;
import es.eucm.eadventure.common.resources.assets.multimedia.Sound;
import es.eucm.eadventure.common.resources.assets.multimedia.impl.SoundImpl;

/**
 * Scenes importer
 * 
 */
public class SceneImporter implements EAdElementImporter<Scene, EAdSceneImpl> {

	/**
	 * String handler
	 */
	private StringHandler stringHandler;

	/**
	 * References importer
	 */
	private EAdElementImporter<ElementReference, EAdSceneElement> referencesImporter;

	/**
	 * Resources importer
	 */
	private ResourceImporter resourceImporter;

	/**
	 * Exits importer
	 */
	private EAdElementImporter<Exit, EAdSceneElement> exitsImporter;

	/**
	 * Trajectory importer
	 */
	private EAdElementImporter<Trajectory, NodeTrajectoryDefinition> trajectoryImporter;

	/**
	 * Barrier importer
	 */
	private EAdElementImporter<Barrier, EAdSceneElement> barrierImporter;

	/**
	 * Active areas importer
	 */
	private EAdElementImporter<ActiveArea, EAdSceneElement> activeAreasImporter;

	private EAdElementFactory factory;

	@Inject
	public SceneImporter(
			StringHandler stringHandler,
			EAdElementImporter<Conditions, EAdCondition> conditionsImporter,
			ResourceImporter resourceImporter,
			EAdElementImporter<ElementReference, EAdSceneElement> referencesImporter,
			EAdElementImporter<ActiveArea, EAdSceneElement> activeAreasImporter,
			EAdElementFactory factory,
			EAdElementImporter<Exit, EAdSceneElement> exitsImporter,
			EAdElementImporter<Trajectory, NodeTrajectoryDefinition> trajectoryImporter,
			EAdElementImporter<Barrier, EAdSceneElement> barrierImporter) {
		this.stringHandler = stringHandler;
		this.resourceImporter = resourceImporter;
		this.exitsImporter = exitsImporter;
		this.referencesImporter = referencesImporter;
		this.activeAreasImporter = activeAreasImporter;
		this.trajectoryImporter = trajectoryImporter;
		this.barrierImporter = barrierImporter;
		this.factory = factory;
	}

	@Override
	public EAdSceneImpl init(Scene oldScene) {
		EAdSceneImpl scene = new EAdSceneImpl();
		scene.setId(oldScene.getId());
		return scene;
	}

	@Override
	public EAdSceneImpl convert(Scene oldScene, Object object) {
		EAdChapter chapter = factory.getCurrentChapterModel();
		EAdSceneImpl scene = (EAdSceneImpl) object;
		chapter.getScenes().add(scene);

		importDocumentation(scene, oldScene);
		importResources(scene, oldScene, chapter);
		importSceneElements(scene, oldScene, chapter);

		return scene;
	}

	private void importSceneElements(EAdSceneImpl scene, Scene oldScene,
			EAdChapter chapter) {
		importExits(scene, oldScene.getExits());
		importAciveAreas(scene, oldScene.getActiveAreas());
		importReferences(scene, oldScene.getItemReferences(), chapter);
		importReferences(scene, oldScene.getAtrezzoReferences(), chapter);
		importReferences(scene, oldScene.getCharacterReferences(), chapter);
		EAdBasicSceneElement playerReference = addPlayer(scene, oldScene,
				chapter);
		importTrajectory(scene, oldScene.getTrajectory(),
				oldScene.getBarriers(), playerReference);

	}

	private EAdBasicSceneElement addPlayer(EAdSceneImpl scene, Scene oldScene,
			EAdChapter chapter) {
		if (factory.isFirstPerson()) {
			return null;
		} else {
			EAdSceneElementDef player = (EAdSceneElementDef) factory
					.getElement(Player.IDENTIFIER, factory
							.getCurrentOldChapterModel().getPlayer());
			EAdBasicSceneElement playerReference = new EAdBasicSceneElement(
					player);
			EAdPositionImpl p = new EAdPositionImpl(
					EAdPositionImpl.Corner.BOTTOM_CENTER,
					oldScene.getPositionX(), oldScene.getPositionY());
			playerReference.setPosition(p);
			playerReference.setScale(oldScene.getPlayerScale());

			// Make it active element of the scene
			EAdMakeActiveElementEffect effect = new EAdMakeActiveElementEffect(
					playerReference);

			EAdSceneElementEvent event = new EAdSceneElementEventImpl();
			event.setId("makeAcitveCharacter");
			event.addEffect(SceneElementEventType.ADDED_TO_SCENE, effect);
			playerReference.getEvents().add(event);

			scene.getElements().add(playerReference);

			scene.getBackground().addBehavior(
					EAdMouseEventImpl.MOUSE_LEFT_CLICK,
					new EAdMoveActiveElement());

			playerReference.setPosition(new EAdPositionImpl(
					Corner.BOTTOM_CENTER, oldScene.getPositionX(), oldScene
							.getPositionY()));

			return playerReference;
		}

	}

	private void importTrajectory(EAdSceneImpl scene, Trajectory trajectory,
			List<Barrier> barriers, EAdBasicSceneElement playerReference) {
		if (trajectory == null) {
			scene.setTrajectoryDefinition(new SimpleTrajectoryDefinition(true));
		} else {
			NodeTrajectoryDefinition nodeDef = trajectoryImporter
					.init(trajectory);
			nodeDef = trajectoryImporter.convert(trajectory, nodeDef);
			scene.setTrajectoryDefinition(nodeDef);

			for (Barrier b : barriers) {
				EAdSceneElement barrier = barrierImporter.init(b);
				barrier = barrierImporter.convert(b, barrier);

				nodeDef.addBarrier(barrier);
				scene.getElements().add(barrier);
			}

			playerReference.setPosition(new EAdPositionImpl(
					Corner.BOTTOM_CENTER, nodeDef.getInitial().getX(), nodeDef
							.getInitial().getY()));
			playerReference.setScale(nodeDef.getInitial().getScale());
		}

	}

	private void importAciveAreas(EAdSceneImpl scene, List<ActiveArea> list) {
		for (ActiveArea a : list) {
			EAdSceneElement se = activeAreasImporter.init(a);
			se = activeAreasImporter.convert(a, se);
			if (se != null)
				scene.getElements().add(se);
		}

	}

	private void importExits(EAdSceneImpl scene, List<Exit> list) {
		for (Exit e : list) {
			EAdSceneElement se = exitsImporter.init(e);
			se = exitsImporter.convert(e, se);
			if (se != null)
				scene.getElements().add(se);
		}

	}

	private void importReferences(EAdSceneImpl scene,
			List<ElementReference> references, EAdChapter chapter) {
		for (ElementReference oldRef : references) {
			EAdSceneElement newRef = referencesImporter.init(oldRef);
			newRef = referencesImporter.convert(oldRef, newRef);
			scene.getElements().add(newRef);
		}

	}

	private void importResources(EAdSceneImpl scene, Scene oldScene,
			EAdChapter chapter) {


		Map<String, String> resourcesStrings = new HashMap<String, String>();
		resourcesStrings.put(Scene.RESOURCE_TYPE_BACKGROUND,
				EAdBasicSceneElement.appearance);

		Map<String, Object> resourcesClasses = new HashMap<String, Object>();
		resourcesClasses.put(Scene.RESOURCE_TYPE_BACKGROUND, ImageImpl.class);

		resourceImporter.importResources(scene.getBackground(),
				oldScene.getResources(), resourcesStrings, resourcesClasses);

		for (Resources r : oldScene.getResources()) {
			String foregroundPath = r.getAssetPath(Scene.RESOURCE_TYPE_FOREGROUND);
			if ( foregroundPath != null ){
				ImageImpl image = (ImageImpl) resourceImporter.getAssetDescritptor(foregroundPath, ImageImpl.class);
				EAdBasicSceneElement foreground = new EAdBasicSceneElement( image );
				foreground.setId("foreground");
				foreground.setVarInitialValue(EAdBasicSceneElement.VAR_Z, -100);
				scene.getElements().add(foreground);
			}
			// Music is imported to chapter level. So, the chapter will
			// remain with the last sound track appeared in the scenes
			String musicPath = r.getAssetPath(Scene.RESOURCE_TYPE_MUSIC);

			if (musicPath != null) {
				Sound sound = new SoundImpl(musicPath);
				chapter.getResources().addAsset(chapter.getInitialBundle(),
						EAdChapter.music, sound);
			}
		}

	}

	private void importDocumentation(EAdSceneImpl scene, Scene oldScene) {
		stringHandler.setString(scene.getName(), oldScene.getName());
		stringHandler.setString(scene.getDoc(),
				oldScene.getDocumentation());
	}

}
