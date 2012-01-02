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

package ead.common.importer.subimporters.chapter.scene;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;

import ead.common.EAdElementImporter;
import ead.common.importer.interfaces.EAdElementFactory;
import ead.common.importer.interfaces.ResourceImporter;
import ead.common.model.elements.EAdChapter;
import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.events.SceneElementEv;
import ead.common.model.elements.events.enums.SceneElementEventType;
import ead.common.model.elements.guievents.EAdMouseEvent;
import ead.common.model.elements.scene.EAdSceneElement;
import ead.common.model.elements.scene.EAdSceneElementDef;
import ead.common.model.elements.scenes.SceneElementDefImpl;
import ead.common.model.elements.scenes.SceneElementImpl;
import ead.common.model.elements.scenes.SceneImpl;
import ead.common.model.elements.trajectories.NodeTrajectoryDefinition;
import ead.common.model.elements.trajectories.SimpleTrajectoryDefinition;
import ead.common.model.predef.effects.MakeActiveElementEf;
import ead.common.model.predef.effects.MoveActiveElementEf;
import ead.common.model.predef.events.ScrollWithSceneElementEv;
import ead.common.resources.StringHandler;
import ead.common.resources.assets.drawable.basics.ImageImpl;
import ead.common.resources.assets.multimedia.Sound;
import ead.common.resources.assets.multimedia.SoundImpl;
import ead.common.util.EAdPosition;
import ead.common.util.EAdPosition.Corner;
import es.eucm.eadventure.common.data.chapter.ElementReference;
import es.eucm.eadventure.common.data.chapter.Exit;
import es.eucm.eadventure.common.data.chapter.Trajectory;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.elements.ActiveArea;
import es.eucm.eadventure.common.data.chapter.elements.Barrier;
import es.eucm.eadventure.common.data.chapter.elements.Player;
import es.eucm.eadventure.common.data.chapter.resources.Resources;
import es.eucm.eadventure.common.data.chapter.scenes.Scene;

/**
 * Scenes importer
 * 
 */
public class SceneImporter implements EAdElementImporter<Scene, SceneImpl> {

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

	private EAdElementFactory factory;

	@Inject
	public SceneImporter(
			StringHandler stringHandler,
			EAdElementImporter<Conditions, EAdCondition> conditionsImporter,
			ResourceImporter resourceImporter,
			EAdElementImporter<ElementReference, EAdSceneElement> referencesImporter,
			EAdElementFactory factory,
			EAdElementImporter<Exit, EAdSceneElement> exitsImporter,
			EAdElementImporter<Trajectory, NodeTrajectoryDefinition> trajectoryImporter,
			EAdElementImporter<Barrier, EAdSceneElement> barrierImporter) {
		this.stringHandler = stringHandler;
		this.resourceImporter = resourceImporter;
		this.exitsImporter = exitsImporter;
		this.referencesImporter = referencesImporter;
		this.trajectoryImporter = trajectoryImporter;
		this.barrierImporter = barrierImporter;
		this.factory = factory;
	}

	@Override
	public SceneImpl init(Scene oldScene) {
		SceneImpl scene = new SceneImpl();
		scene.setId(oldScene.getId());
		return scene;
	}

	@Override
	public SceneImpl convert(Scene oldScene, Object object) {
		EAdChapter chapter = factory.getCurrentChapterModel();
		SceneImpl scene = (SceneImpl) object;
		chapter.getScenes().add(scene);

		importDocumentation(scene, oldScene);
		importResources(scene, oldScene, chapter);
		importSceneElements(scene, oldScene, chapter);

		return scene;
	}

	private void importSceneElements(SceneImpl scene, Scene oldScene,
			EAdChapter chapter) {
		importExits(scene, oldScene.getExits());
		importAciveAreas(scene, oldScene.getActiveAreas());
		importReferences(scene, oldScene.getItemReferences(), chapter);
		importReferences(scene, oldScene.getAtrezzoReferences(), chapter);
		importReferences(scene, oldScene.getCharacterReferences(), chapter);
		SceneElementImpl playerReference = addPlayer(scene, oldScene,
				chapter);
		importTrajectory(scene, oldScene.getTrajectory(),
				oldScene.getBarriers(), playerReference);

	}

	private SceneElementImpl addPlayer(SceneImpl scene, Scene oldScene,
			EAdChapter chapter) {
		if (factory.isFirstPerson()) {
			return null;
		} else {
			EAdSceneElementDef player = (EAdSceneElementDef) factory
					.getElementById(Player.IDENTIFIER);
			SceneElementImpl playerReference = new SceneElementImpl(
					player);
			EAdPosition p = new EAdPosition(
					EAdPosition.Corner.BOTTOM_CENTER,
					oldScene.getPositionX(), oldScene.getPositionY());
			playerReference.setPosition(p);
			playerReference.setScale(oldScene.getPlayerScale());

			// Make it active element of the scene
			MakeActiveElementEf effect = new MakeActiveElementEf(
					playerReference);
			
			SceneElementEv event = new SceneElementEv();
			event.setId("makeAcitveCharacter");
			event.addEffect(SceneElementEventType.ADDED_TO_SCENE, effect);
			playerReference.getEvents().add(event);

			int layer = oldScene.getPlayerLayer();

			if (layer <= 0) {
				scene.getComponents().add(playerReference);
			} else {
				scene.getComponents().add(playerReference, layer - 1);
			}

			scene.getBackground().addBehavior(
					EAdMouseEvent.MOUSE_LEFT_CLICK,
					new MoveActiveElementEf());
			
			// Add move camera with character
			Dimension d = resourceImporter.getDimensions(oldScene.getResources().get(0).getAssetPath(Scene.RESOURCE_TYPE_BACKGROUND));
			scene.setBounds(d.width, 600);
			
			ScrollWithSceneElementEv scroll = new ScrollWithSceneElementEv( scene, playerReference );
			scene.getEvents().add(scroll);

			return playerReference;
		}

	}

	private void importTrajectory(SceneImpl scene, Trajectory trajectory,
			List<Barrier> barriers, SceneElementImpl playerReference) {
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
				scene.getComponents().add(barrier);
			}

			playerReference.setPosition(new EAdPosition(
					Corner.BOTTOM_CENTER, nodeDef.getInitial().getX(), nodeDef
							.getInitial().getY()));
			playerReference.setScale(nodeDef.getInitial().getScale());
		}

	}

	private void importAciveAreas(SceneImpl scene, List<ActiveArea> list) {
		for (ActiveArea a : list) {
			SceneElementImpl activeArea = (SceneElementImpl) factory.getElementById(a
					.getId());
			activeArea.setPosition(new EAdPosition(EAdPosition.Corner.TOP_LEFT, a.getX(), a.getY()));
			activeArea.setVarInitialValue(SceneElementImpl.VAR_Z, Integer.MAX_VALUE - 2 );
			if (activeArea != null)
				scene.getComponents().add(activeArea);
		}

	}

	private void importExits(SceneImpl scene, List<Exit> list) {
		for (Exit e : list) {
			EAdSceneElement se = exitsImporter.init(e);
			se.setVarInitialValue(SceneElementImpl.VAR_Z, Integer.MAX_VALUE - 1);
			se = exitsImporter.convert(e, se);
			if (se != null)
				scene.getComponents().add(se);
		}

	}

	private void importReferences(SceneImpl scene,
			List<ElementReference> references, EAdChapter chapter) {
		for (ElementReference oldRef : references) {
			EAdSceneElement newRef = referencesImporter.init(oldRef);
			newRef = referencesImporter.convert(oldRef, newRef);
			newRef.setVarInitialValue(SceneElementImpl.VAR_Z,
					oldRef.getLayer());
			scene.getComponents().add(newRef);
		}

	}

	private void importResources(SceneImpl scene, Scene oldScene,
			EAdChapter chapter) {

		Map<String, String> resourcesStrings = new HashMap<String, String>();
		resourcesStrings.put(Scene.RESOURCE_TYPE_BACKGROUND,
				SceneElementDefImpl.appearance);

		Map<String, Object> resourcesClasses = new HashMap<String, Object>();
		resourcesClasses.put(Scene.RESOURCE_TYPE_BACKGROUND, ImageImpl.class);

		resourceImporter.importResources(scene.getBackground().getDefinition(),
				oldScene.getResources(), resourcesStrings, resourcesClasses);

		scene.getBackground().setId("background");

		for (Resources r : oldScene.getResources()) {
			String foregroundPath = r
					.getAssetPath(Scene.RESOURCE_TYPE_FOREGROUND);
			if (foregroundPath != null) {
				ImageImpl image = (ImageImpl) resourceImporter
						.getAssetDescritptor(foregroundPath, ImageImpl.class);
				SceneElementImpl foreground = new SceneElementImpl(
						image);
				foreground.setId("foreground");
				foreground.setVarInitialValue(SceneElementImpl.VAR_Z, Integer.MAX_VALUE);
				scene.getComponents().add(foreground);
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

	private void importDocumentation(SceneImpl scene, Scene oldScene) {
		stringHandler.setString(scene.getDefinition().getName(),
				oldScene.getName());
		stringHandler.setString(scene.getDefinition().getDoc(),
				oldScene.getDocumentation());
	}

}
