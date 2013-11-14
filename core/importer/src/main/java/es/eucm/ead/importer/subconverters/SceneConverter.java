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

package es.eucm.ead.importer.subconverters;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import es.eucm.ead.importer.EAdElementsCache;
import es.eucm.ead.importer.ModelQuerier;
import es.eucm.ead.importer.StringsConverter;
import es.eucm.ead.importer.UtilsConverter;
import es.eucm.ead.importer.resources.ResourcesConverter;
import es.eucm.ead.importer.subconverters.actors.ElementConverter;
import es.eucm.ead.importer.subconverters.actors.NPCConverter;
import es.eucm.ead.importer.subconverters.conditions.ConditionsConverter;
import es.eucm.ead.importer.subconverters.effects.EffectsConverter;
import es.eucm.ead.importer.testers.ConverterTester;
import es.eucm.ead.legacyplugins.model.LegacyVars;
import es.eucm.ead.model.assets.drawable.EAdDrawable;
import es.eucm.ead.model.assets.drawable.basics.shapes.AbstractShape;
import es.eucm.ead.model.assets.multimedia.Music;
import es.eucm.ead.model.elements.BasicElement;
import es.eucm.ead.model.elements.conditions.Condition;
import es.eucm.ead.model.elements.conditions.EmptyCond;
import es.eucm.ead.model.elements.effects.ChangeSceneEf;
import es.eucm.ead.model.elements.effects.Effect;
import es.eucm.ead.model.elements.effects.EmptyEffect;
import es.eucm.ead.model.elements.effects.PlayMusicEf;
import es.eucm.ead.model.elements.effects.TriggerMacroEf;
import es.eucm.ead.model.elements.effects.variables.ChangeFieldEf;
import es.eucm.ead.model.elements.events.WatchFieldEv;
import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.elements.huds.MouseHud;
import es.eucm.ead.model.elements.operations.ElementField;
import es.eucm.ead.model.elements.predef.effects.MakeActiveElementEf;
import es.eucm.ead.model.elements.predef.effects.MoveActiveElementToMouseEf;
import es.eucm.ead.model.elements.scenes.GhostElement;
import es.eucm.ead.model.elements.scenes.Scene;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.elements.scenes.SceneElementDef;
import es.eucm.ead.model.params.fills.ColorFill;
import es.eucm.ead.model.params.guievents.MouseGEv;
import es.eucm.ead.model.params.text.EAdString;
import es.eucm.ead.model.params.util.Position.Corner;
import es.eucm.eadventure.common.data.adventure.AdventureData;
import es.eucm.eadventure.common.data.chapter.ElementReference;
import es.eucm.eadventure.common.data.chapter.Exit;
import es.eucm.eadventure.common.data.chapter.ExitLook;
import es.eucm.eadventure.common.data.chapter.Trajectory;
import es.eucm.eadventure.common.data.chapter.elements.ActiveArea;
import es.eucm.eadventure.common.data.chapter.elements.Player;
import es.eucm.eadventure.common.data.chapter.resources.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.List;

@Singleton
public class SceneConverter {

	private static final Logger logger = LoggerFactory
			.getLogger(SceneConverter.class);

	private static final int EXIT_Z = 20000;

	private static final int ACTIVE_AREA_Z = 10000;

	private static final int PLAYER_Z = 5000;

	private static final int FOREGROUND_Z = Integer.MAX_VALUE / 2;

	private static final ColorFill EXIT_FILL = new ColorFill(255, 0, 0, 100);

	private static final ColorFill ACTIVE_AREA_FILL = new ColorFill(0, 255, 0,
			100);

	private TransitionConverter transitionConverter;

	private ResourcesConverter resourceConverter;

	private EAdElementsCache elementsCache;

	private RectangleConverter rectangleConverter;

	private UtilsConverter utilsConverter;

	private EffectsConverter effectConverter;

	private ConditionsConverter conditionsConverter;

	private StringsConverter stringsConverter;

	private ModelQuerier modelQuerier;

	private TrajectoryConverter trajectoryConverter;

	private ElementConverter elementConverter;

	private ConverterTester converterTester;

	@Inject
	public SceneConverter(ResourcesConverter resourceConverter,
			EAdElementsCache elementsCache,
			TransitionConverter transitionConverter,
			RectangleConverter rectangleConverter,
			UtilsConverter utilsConverter, EffectsConverter effectConverter,
			ConditionsConverter conditionsConverter,
			StringsConverter stringsConverter, ModelQuerier modelQuerier,
			TrajectoryConverter trajectoryConverter,
			NPCConverter elementConverter, ConverterTester converterTester) {
		this.resourceConverter = resourceConverter;
		this.elementsCache = elementsCache;
		this.transitionConverter = transitionConverter;
		this.rectangleConverter = rectangleConverter;
		this.utilsConverter = utilsConverter;
		this.effectConverter = effectConverter;
		this.conditionsConverter = conditionsConverter;
		this.stringsConverter = stringsConverter;
		this.modelQuerier = modelQuerier;
		this.trajectoryConverter = trajectoryConverter;
		this.elementConverter = elementConverter;
		this.converterTester = converterTester;
	}

	public Scene convert(es.eucm.eadventure.common.data.chapter.scenes.Scene s) {

		SceneElement background = new SceneElement();
		Scene scene = new Scene(background);
		scene.setId(s.getId());
		background.setId(s.getId() + "$bg");

		logger.debug("Importing background...");
		addAppearance(scene, s);
		// XXX Information
		logger.debug("Importing references...");
		addReferences(scene, s);
		logger.debug("Importing active zones...");
		addActiveZones(scene, s);
		logger.debug("Importing exits...");
		addExits(scene, s);

		// Add trajectory
		if (modelQuerier.getAventureData().getPlayerMode() == AdventureData.MODE_PLAYER_3RDPERSON) {
			logger.debug("Importing trajectory...");
			scene.setTrajectoryDefinition(trajectoryConverter.convert(s
					.getTrajectory()));
		}

		return scene;
	}

	public void addAppearance(Scene scene,
			es.eucm.eadventure.common.data.chapter.scenes.Scene s) {
		// Appearance tab
		SceneElement background = scene.getBackground();
		// The foreground is only initialized if needed
		SceneElement foreground = null;
		// Resources blocks
		int i = 0;
		for (Resources r : s.getResources()) {
			logger.debug("---- Adding resources {}", i);
			// Background [SC - Bg]
			String backgroundPath = r
					.getAssetPath(es.eucm.eadventure.common.data.chapter.scenes.Scene.RESOURCE_TYPE_BACKGROUND);
			EAdDrawable drawable = utilsConverter.getBackground(backgroundPath);
			Dimension d = resourceConverter.getSize(backgroundPath);
			float scale = 1.0f;
			// If dimension is greater than 600, we have to scale
			if (d.getHeight() > 600) {
				scale = 600.0f / (float) d.getHeight();
			}

			background.setAppearance(utilsConverter.getResourceBundleId(i),
					drawable);
			if (i == 0) {
				background.setBundle(utilsConverter.getResourceBundleId(i));
			}
			// Foreground [SC - Fg]
			String foregroundPath = r
					.getAssetPath(es.eucm.eadventure.common.data.chapter.scenes.Scene.RESOURCE_TYPE_FOREGROUND);
			if (foregroundPath != null) {
				foregroundPath = utilsConverter.applyForegroundMask(
						foregroundPath, backgroundPath);
				if (foreground == null) {
					foreground = new SceneElement();
					foreground.setEnable(false);
					foreground.setZ(FOREGROUND_Z);
					foreground.setBundle(utilsConverter.getResourceBundleId(i));
					scene.add(foreground);
				}
				foreground.setAppearance(utilsConverter.getResourceBundleId(i),
						new es.eucm.ead.model.assets.drawable.basics.Image(
								foregroundPath));
			}

			int finalWidth = (int) (d.getWidth() * scale);
			// [GE - Arrows] [GE - Follow]
			scene.putProperty(LegacyVars.SCENE_WIDTH, finalWidth);

			i++;
		}

		logger.debug("---- Adding resources conditions");
		// Add conditioned resources
		utilsConverter.addResourcesConditions(s.getResources(), scene
				.getBackground(), SceneElement.VAR_BUNDLE_ID);
		if (foreground != null) {
			utilsConverter.addResourcesConditions(s.getResources(), foreground,
					SceneElement.VAR_BUNDLE_ID);
		}
		// [SC - Fg]
		this.addForegroundMusicConditions(scene, s.getResources(), foreground);
	}

	private void addReferences(Scene scene,
			es.eucm.eadventure.common.data.chapter.scenes.Scene s) {
		addReferences(scene, s.getAtrezzoReferences());
		addReferences(scene, s.getItemReferences());
		addReferences(scene, s.getCharacterReferences());

		// Add player
		if (modelQuerier.getAventureData().getPlayerMode() == AdventureData.MODE_PLAYER_3RDPERSON) {
			SceneElement playerRef = new SceneElement(
					(SceneElementDef) elementsCache.get(Player.IDENTIFIER));
			// [SC - Player Layer]
			if (s.isAllowPlayerLayer() && s.getPlayerLayer() != -1) {
				playerRef.setZ(s.getPlayerLayer());
			} else {
				playerRef.setZ(PLAYER_Z);
			}

			playerRef.setScale(s.getPlayerScale());
			playerRef.setPosition(Corner.BOTTOM_CENTER, s.getPositionX(), s
					.getPositionY());
			if (s.getTrajectory() != null) {
				Trajectory t = s.getTrajectory();
				playerRef.setScale(t.getInitial().getScale());
				playerRef.setPosition(Corner.BOTTOM_CENTER, t.getInitial()
						.getX(), t.getInitial().getY());
			}
			scene.addAddedEffect(new MakeActiveElementEf(playerRef));
			scene.add(playerRef);

			scene.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED,
					new MoveActiveElementToMouseEf());
		}
	}

	private void addReferences(Scene scene, List<ElementReference> references) {
		for (ElementReference e : references) {
			SceneElementDef def = (SceneElementDef) elementsCache.get(e
					.getTargetId());
			SceneElement sceneElement = new SceneElement(def);
			String sceneElementId = e.getTargetId() + "$"
					+ elementsCache.newReference(e.getTargetId());
			converterTester.checkBundles(e, sceneElementId);
			sceneElement.setId(sceneElementId);
			sceneElement.setPosition(Corner.BOTTOM_CENTER, e.getX(), e.getY());
			// [ER - Layer]
			sceneElement.setZ(e.getLayer());
			sceneElement.setScale(e.getScale());
			// XXX Influence area
			scene.add(sceneElement);

			// Add event to change appearance when required by the actor's
			// definition
			if (def.getResources().size() > 1) {
				utilsConverter.addWatchDefinitionField(sceneElement,
						SceneElement.VAR_BUNDLE_ID);
			}

			// Add visibility condition
			// [ER - Conditions]
			utilsConverter.addWatchCondition(sceneElement, new ElementField(
					sceneElement, SceneElement.VAR_VISIBLE, true), e
					.getConditions());
		}
	}

	private void addExits(Scene scene,
			es.eucm.eadventure.common.data.chapter.scenes.Scene s) {
		int i = 0;
		for (Exit e : s.getExits()) {
			AbstractShape shape = rectangleConverter.convert(e, EXIT_FILL);

			GhostElement exit = new GhostElement(shape);
			exit.setId(s.getId() + "$ex" + i);
			if (e.isRectangular()) {
				exit.setPosition(Corner.TOP_LEFT, e.getX(), e.getY());
			}

			// [EXIT - CondInactive] [EXIT - Conditions]
			Condition cond = conditionsConverter.convert(e.getConditions());

			Effect effectWhenClick;

			// Next scene
			// [EXIT - Next]
			ChangeSceneEf nextScene = new ChangeSceneEf();
			nextScene.setId(exit.getId() + "$cs");
			nextScene.setNextEffectsAlways(true);
			nextScene.setNextScene(new BasicElement(e.getNextSceneId()));
			// [EXIT - Transition]
			nextScene.setTransition(transitionConverter.getTransitionExit(e
					.getTransitionType(), e.getTransitionTime()));

			// Add effects
			// [EXIT - Effects]
			List<Effect> effects = effectConverter.convert(e.getEffects());
			if (effects.size() > 0) {
				effectWhenClick = effects.get(0);
				effects.get(effects.size() - 1).addNextEffect(nextScene);
			} else {
				effectWhenClick = nextScene;
			}

			// Add next effects
			// [EXIT - PostEffects]
			List<Effect> postEffects = effectConverter.convert(e
					.getPostEffects());
			if (postEffects.size() > 0) {
				nextScene.addNextEffect(postEffects.get(0));
			}

			// Set Z
			exit.setZ(EXIT_Z + i);

			// Add appearance
			ExitLook exitLook = e.getDefaultExitLook();
			// Text
			if (!"".equals(exitLook.getExitText())) {
				EAdString text = stringsConverter.convert(exitLook
						.getExitText(), false);
				exit.putProperty(LegacyVars.BUBBLE_NAME, text);
			}
			// XXX For now, we use the default exit image
			utilsConverter.addCursorChange(exit, MouseHud.EXIT_CURSOR);

			// Add the exit to the scene
			scene.add(exit);

			List<Effect> notEffects = null;
			// [EXIT - NotEffects]
			// If it has not-effects
			if (e.isHasNotEffects()) {
				TriggerMacroEf triggerMacro = new TriggerMacroEf();
				// Add ACTIVE effects
				triggerMacro.putEffect(cond, effectWhenClick);
				// Add INACTIVE effects
				EAdList<Effect> macro = new EAdList<Effect>();
				notEffects = effectConverter.convert(e.getNotEffects());
				if (notEffects.size() > 0) {
					macro.add(notEffects.get(0));
				}
				// The macro only executes if the first condition fails
				triggerMacro.putEffects(EmptyCond.TRUE, macro);
				exit.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, triggerMacro);

			} else {
				if (!cond.equals(EmptyCond.TRUE)) {
					EmptyEffect empty = new EmptyEffect();
					empty.setCondition(cond);
					empty.addNextEffect(effectWhenClick);
					effectWhenClick = empty;
				}
				exit.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, effectWhenClick);
				// Add visibility condition
				utilsConverter.addWatchCondition(exit, exit
						.getField(SceneElement.VAR_VISIBLE), e.getConditions());
			}
			// Add tests
			converterTester.checkExit(s.getId(), e, exit, effects, nextScene,
					postEffects, notEffects);
			i++;
		}

	}

	private void addActiveZones(Scene scene,
			es.eucm.eadventure.common.data.chapter.scenes.Scene s) {
		int i = 0;
		for (ActiveArea a : s.getActiveAreas()) {
			// [AA - Shape]
			AbstractShape shape = rectangleConverter.convert(a,
					ACTIVE_AREA_FILL);
			GhostElement activeArea = new GhostElement(shape);
			// Add actions
			// [AA - Actions]
			elementConverter.addActions(a, activeArea.getDefinition());
			elementConverter.addDescription(a, activeArea.getDefinition());

			// [AA - Id]
			activeArea.setId(a.getId());
			if (a.isRectangular()) {
				activeArea.setPosition(Corner.TOP_LEFT, a.getX(), a.getY());
			}
			// Set Z
			activeArea.setZ(ACTIVE_AREA_Z + i);
			elementsCache.put(activeArea);
			// Add visibility condition
			// [AA - Conditions]
			utilsConverter.addWatchCondition(activeArea, activeArea
					.getField(SceneElement.VAR_VISIBLE), a.getConditions());

			scene.add(activeArea);
			i++;
		}

	}

	/**
	 * Foregrounds are imported os objects over the scene. A bundle could have an empty foreground. Then, the foreground
	 * should be invisible.
	 * <p/>
	 * Also, music of the scene is converted (since it shares conditions with the foreground)
	 *
	 * @param scene
	 * @param resources
	 * @param foreground
	 */
	private void addForegroundMusicConditions(Scene scene,
			List<Resources> resources, SceneElement foreground) {
		WatchFieldEv watchField = new WatchFieldEv();
		boolean hasMusic = false;
		TriggerMacroEf triggerMacroVisible = new TriggerMacroEf();
		TriggerMacroEf triggerMacroMusic = new TriggerMacroEf();
		// Prepare visibility for foreground
		ChangeFieldEf makeForegroundVisible = null;
		ChangeFieldEf makeForegroundInvisible = null;
		if (foreground != null) {
			ElementField foregroundVisible = (foreground
					.getField(SceneElement.VAR_VISIBLE));
			makeForegroundVisible = new ChangeFieldEf(foregroundVisible,
					EmptyCond.TRUE);
			makeForegroundInvisible = new ChangeFieldEf(foregroundVisible,
					EmptyCond.FALSE);
		}

		for (Resources r : resources) {
			Condition cond = conditionsConverter.convert(r.getConditions());
			// Watch all the fields in the condition
			for (ElementField field : conditionsConverter
					.getFieldsLastCondition()) {
				watchField.watchField(field);
			}

			if (foreground != null) {
				// Check if there is foreground in this bundle
				if (r
						.getAssetPath(es.eucm.eadventure.common.data.chapter.scenes.Scene.RESOURCE_TYPE_FOREGROUND) == null) {
					triggerMacroVisible
							.putEffect(cond, makeForegroundInvisible);
				} else {
					triggerMacroVisible.putEffect(cond, makeForegroundVisible);
				}
			}

			// Check for music [SC - Music]
			String musicPath = r
					.getAssetPath(es.eucm.eadventure.common.data.chapter.scenes.Scene.RESOURCE_TYPE_MUSIC);
			if (musicPath != null) {
				hasMusic = true;
				Music music = resourceConverter.getMusic(musicPath);
				PlayMusicEf playMusic = new PlayMusicEf(music, 1.0f, true);
				triggerMacroMusic.putEffect(cond, playMusic);
			}
		}

		if (hasMusic) {
			scene.addAddedEffect(triggerMacroMusic);
		}
		if (foreground != null) {
			scene.addAddedEffect(triggerMacroVisible);
		}

	}

}
