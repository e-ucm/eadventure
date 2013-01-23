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

package ead.importer.subimporters.chapter.scene;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import ead.common.model.elements.EAdChapter;
import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.conditions.ANDCond;
import ead.common.model.elements.conditions.EmptyCond;
import ead.common.model.elements.conditions.NOTCond;
import ead.common.model.elements.effects.EffectsMacro;
import ead.common.model.elements.effects.PlaySoundEf;
import ead.common.model.elements.effects.TriggerMacroEf;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.events.SceneElementEv;
import ead.common.model.elements.events.enums.SceneElementEvType;
import ead.common.model.elements.guievents.MouseGEv;
import ead.common.model.elements.scenes.BasicScene;
import ead.common.model.elements.scenes.EAdScene;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.elements.scenes.EAdSceneElementDef;
import ead.common.model.elements.scenes.GhostElement;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.common.model.elements.trajectories.NodeTrajectory;
import ead.common.model.elements.trajectories.SimpleTrajectory;
import ead.common.model.elements.variables.operations.BooleanOp;
import ead.common.model.predef.effects.MakeActiveElementEf;
import ead.common.model.predef.effects.MoveActiveElementToMouseEf;
import ead.common.model.predef.events.ScrollWithSceneElementEv;
import ead.common.params.text.EAdString;
import ead.common.resources.assets.drawable.basics.Image;
import ead.common.resources.assets.multimedia.EAdSound;
import ead.common.resources.assets.multimedia.Sound;
import ead.common.util.EAdPosition;
import ead.common.util.EAdPosition.Corner;
import ead.importer.EAdElementImporter;
import ead.importer.annotation.ImportAnnotator;
import ead.importer.interfaces.EAdElementFactory;
import ead.importer.interfaces.ResourceImporter;
import ead.tools.StringHandler;
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
public class SceneImporter implements EAdElementImporter<Scene, BasicScene> {

	private static final Logger logger = LoggerFactory
			.getLogger("SceneImporter");

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
	private EAdElementImporter<Trajectory, NodeTrajectory> trajectoryImporter;

	/**
	 * Barrier importer
	 */
	private EAdElementImporter<Barrier, EAdSceneElement> barrierImporter;

	private EAdElementFactory factory;

	protected ImportAnnotator annotator;

	private EAdElementImporter<Conditions, EAdCondition> conditionsImporter;

	@Inject
	public SceneImporter(
			StringHandler stringHandler,
			EAdElementImporter<Conditions, EAdCondition> conditionsImporter,
			ResourceImporter resourceImporter,
			EAdElementImporter<ElementReference, EAdSceneElement> referencesImporter,
			EAdElementFactory factory,
			EAdElementImporter<Exit, EAdSceneElement> exitsImporter,
			EAdElementImporter<Trajectory, NodeTrajectory> trajectoryImporter,
			EAdElementImporter<Barrier, EAdSceneElement> barrierImporter,
			ImportAnnotator annotator) {
		this.stringHandler = stringHandler;
		this.resourceImporter = resourceImporter;
		this.exitsImporter = exitsImporter;
		this.referencesImporter = referencesImporter;
		this.trajectoryImporter = trajectoryImporter;
		this.barrierImporter = barrierImporter;
		this.factory = factory;
		this.annotator = annotator;
		this.conditionsImporter = conditionsImporter;
	}

	@Override
	public BasicScene init(Scene oldScene) {
		BasicScene scene = new BasicScene();
		return scene;
	}

	@Override
	public BasicScene convert(Scene oldScene, Object object) {
		EAdChapter chapter = factory.getCurrentChapterModel();
		BasicScene scene = (BasicScene) object;
		chapter.getScenes().add(scene);

		importDocumentation(scene, oldScene);
		importResources(scene, oldScene, chapter);
		importSceneElements(scene, oldScene, chapter);

		return scene;
	}

	private void importSceneElements(BasicScene scene, Scene oldScene,
			EAdChapter chapter) {
		int substract = importActiveAreas(1, scene, oldScene.getActiveAreas());
		importExits(substract, scene, oldScene.getExits());
		importReferences(scene, oldScene.getItemReferences(), chapter);
		importReferences(scene, oldScene.getAtrezzoReferences(), chapter);
		importReferences(scene, oldScene.getCharacterReferences(), chapter);
		SceneElement playerReference = addPlayer(scene, oldScene, chapter);
		importTrajectory(scene, oldScene.getTrajectory(), oldScene
				.getBarriers(), playerReference);

	}

	private SceneElement addPlayer(BasicScene scene, Scene oldScene,
			EAdChapter chapter) {
		if (factory.isFirstPerson()) {
			return null;
		} else {
			EAdSceneElementDef player = (EAdSceneElementDef) factory
					.getElementById(Player.IDENTIFIER);
			SceneElement playerReference = new SceneElement(player);
			EAdPosition p = new EAdPosition(EAdPosition.Corner.BOTTOM_CENTER,
					oldScene.getPositionX(), oldScene.getPositionY());
			playerReference.setPosition(p);
			playerReference.setInitialScale(oldScene.getPlayerScale());

			// Make it active element of the scene
			MakeActiveElementEf effect = new MakeActiveElementEf(
					playerReference);

			SceneElementEv event = new SceneElementEv();
			event.addEffect(SceneElementEvType.FIRST_UPDATE, effect);
			playerReference.getEvents().add(event);

			int playerZ = oldScene.isAllowPlayerLayer() ? Math.max(0, oldScene
					.getPlayerLayer()) : 1;
			playerReference.setVarInitialValue(SceneElement.VAR_Z, playerZ);
			scene.getSceneElements().add(playerReference);

			scene.getBackground().addBehavior(MouseGEv.MOUSE_LEFT_CLICK,
					new MoveActiveElementToMouseEf());

			// Add move camera with character
			Dimension d = resourceImporter.getDimensionsForOldImage(oldScene
					.getResources().get(0).getAssetPath(
							Scene.RESOURCE_TYPE_BACKGROUND));
			scene.setBounds(d.width, 600);

			ScrollWithSceneElementEv scroll = new ScrollWithSceneElementEv(
					scene, playerReference);
			scene.getEvents().add(scroll);

			return playerReference;
		}

	}

	private void importTrajectory(BasicScene scene, Trajectory trajectory,
			List<Barrier> barriers, SceneElement playerReference) {
		if (trajectory == null) {
			scene.setTrajectoryDefinition(new SimpleTrajectory(true));
		} else {
			NodeTrajectory nodeDef = trajectoryImporter.init(trajectory);
			nodeDef = trajectoryImporter.convert(trajectory, nodeDef);
			scene.setTrajectoryDefinition(nodeDef);

			for (Barrier b : barriers) {
				EAdSceneElement barrier = barrierImporter.init(b);
				barrier = barrierImporter.convert(b, barrier);

				nodeDef.addBarrier(barrier);
				scene.getSceneElements().add(barrier);
			}

			playerReference.setPosition(new EAdPosition(Corner.BOTTOM_CENTER,
					nodeDef.getInitial().getX(), nodeDef.getInitial().getY()));
			playerReference.setInitialScale(nodeDef.getInitial().getScale());
		}

	}

	private int importActiveAreas(int substract, BasicScene scene,
			List<ActiveArea> list) {
		int i = 0;
		for (ActiveArea a : list) {
			SceneElement activeArea = (SceneElement) factory.getElementById(a
					.getId());
			activeArea.setPosition(new EAdPosition(EAdPosition.Corner.TOP_LEFT,
					a.getX(), a.getY()));
			activeArea.setVarInitialValue(SceneElement.VAR_Z, Integer.MAX_VALUE
					- substract - i);
			i++;
			if (activeArea != null)
				scene.getSceneElements().add(activeArea);
		}
		return substract - i;

	}

	private int importExits(int substract, BasicScene scene, List<Exit> list) {
		int i = 0;
		for (Exit e : list) {
			EAdSceneElement se = exitsImporter.init(e);
			se.setVarInitialValue(SceneElement.VAR_Z, Integer.MAX_VALUE
					- substract - i);
			se = exitsImporter.convert(e, se);
			i++;
			if (se != null) {
				scene.getSceneElements().add(se);
			}
		}
		return substract - i;

	}

	private void importReferences(BasicScene scene,
			List<ElementReference> references, EAdChapter chapter) {
		for (ElementReference oldRef : references) {
			EAdSceneElement newRef = referencesImporter.init(oldRef);
			newRef = referencesImporter.convert(oldRef, newRef);
			newRef.setVarInitialValue(SceneElement.VAR_Z, oldRef.getLayer());
			scene.getSceneElements().add(newRef);
		}

	}

	private void importResources(BasicScene scene, Scene oldScene,
			EAdChapter chapter) {

		Map<String, String> resourcesStrings = new LinkedHashMap<String, String>();
		resourcesStrings.put(Scene.RESOURCE_TYPE_BACKGROUND,
				SceneElementDef.appearance);

		Map<String, Object> resourcesClasses = new LinkedHashMap<String, Object>();
		resourcesClasses.put(Scene.RESOURCE_TYPE_BACKGROUND, Image.class);

		resourceImporter.importResources(scene.getBackground().getDefinition(),
				oldScene.getResources(), resourcesStrings, resourcesClasses);

		// Music variables
		List<String> musics = new ArrayList<String>();
		List<EAdCondition> conditions = new ArrayList<EAdCondition>();
		EAdCondition lastCondition = null;
		boolean end = false;

		for (Resources r : oldScene.getResources()) {

			EAdCondition c = conditionsImporter.init(r.getConditions());
			c = conditionsImporter.convert(r.getConditions(), c);

			// This condition is to detect if the resources blocks has an empty
			// condition. If so, subsequent resources blocks are unreachable
			if (c.equals(EmptyCond.TRUE_EMPTY_CONDITION)) {
				end = true;
			}

			if (lastCondition != null) {
				c = new ANDCond(new NOTCond(lastCondition), c);
				lastCondition = c;
			}
			conditions.add(c);

			String foregroundPath = r
					.getAssetPath(Scene.RESOURCE_TYPE_FOREGROUND);
			if (foregroundPath != null) {

				Image image = (Image) resourceImporter.getAssetDescritptor(
						foregroundPath, Image.class);

				String path = image.getUri().getPath();
				if (path.endsWith(".jpg") || path.endsWith(".JPG")) {
					image = new Image(path.substring(0, path.length() - 3)
							+ "png");
				}

				String backgroundPath = r
						.getAssetPath(Scene.RESOURCE_TYPE_BACKGROUND);
				applyForegroundMask(image.getUri().getPath(), foregroundPath,
						backgroundPath);

				SceneElement foreground = new GhostElement(image, null);
				foreground.setVarInitialValue(SceneElement.VAR_Z,
						Integer.MAX_VALUE);
				foreground.setInitialEnable(false);

				ChangeFieldEf changeVisibility = new ChangeFieldEf(foreground
						.getField(SceneElement.VAR_VISIBLE), new BooleanOp(c));

				SceneElementEv event = new SceneElementEv(
						SceneElementEvType.ALWAYS, changeVisibility);

				foreground.getEvents().add(event);
				scene.getSceneElements().add(foreground);
			}

			musics.add(r.getAssetPath(Scene.RESOURCE_TYPE_MUSIC));

			if (end) {
				break;
			}
		}

		importSceneMusic(musics, resourceImporter, scene, conditions);

	}

	public static void importSceneMusic(List<String> musicPaths,
			ResourceImporter resourceImporter, EAdScene scene,
			List<EAdCondition> conditions) {

		TriggerMacroEf triggerMacro = new TriggerMacroEf();
		int i = 0;
		for (String musicPath : musicPaths) {
			EAdCondition condition = conditions.get(i);
			EAdSound sound = null;
			if (musicPath != null) {

				sound = (EAdSound) resourceImporter.getAssetDescritptor(
						musicPath, Sound.class);
			}
			PlaySoundEf playSound = new PlaySoundEf(sound, true);
			triggerMacro.putMacro(new EffectsMacro(playSound), condition);
			i++;
		}

		SceneElementEv event = new SceneElementEv();

		event.addEffect(SceneElementEvType.ADDED_TO_SCENE, triggerMacro);
		scene.getEvents().add(event);
	}

	private void applyForegroundMask(String finalPath, String foregroundPath,
			String backgroundPath) {
		BufferedImage foreground = resourceImporter.getOldImage(foregroundPath);
		BufferedImage background = resourceImporter.getOldImage(backgroundPath);

		int width = foreground.getWidth();
		int height = foreground.getHeight();

		int[] backgroundPixels = background.getRGB(0, 0, width, height, null,
				0, width);
		int[] maskPixels = foreground.getRGB(0, 0, width, height, null, 0,
				width);

		int[] resultPixels = new int[maskPixels.length];

		for (int i = 0; i < backgroundPixels.length; i++) {
			int color = backgroundPixels[i];
			int mask = maskPixels[i];

			if (mask != 0xffffffff) {
				resultPixels[i] = color;
			} else {
				resultPixels[i] = 0x00000000;
			}

		}
		BufferedImage result = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);
		result.getRaster().setDataElements(0, 0, width, height, resultPixels);
		String newUri = resourceImporter.getURI(foregroundPath);

		try {
			ImageIO.write(result, "png", new File(resourceImporter
					.getNewProjecFolder(), newUri.substring(1)));
		} catch (IOException e) {
			logger.error("Error creating foreground image {}", e);
		}

		foreground.flush();
		background.flush();
		result.flush();

	}

	private void importDocumentation(EAdScene scene, Scene oldScene) {
		EAdString doc = stringHandler.generateNewString();
		stringHandler.setString(doc, oldScene.getDocumentation());
		scene.getDefinition().setDoc(doc);

		EAdString name = stringHandler.generateNewString();
		stringHandler.setString(name, oldScene.getName());
		scene.getDefinition().setName(name);
	}

}
