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

package ead.converter.subconverters;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ead.common.model.assets.drawable.EAdDrawable;
import ead.common.model.assets.drawable.basics.shapes.AbstractShape;
import ead.common.model.assets.multimedia.Music;
import ead.common.model.elements.BasicElement;
import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.conditions.EmptyCond;
import ead.common.model.elements.effects.ChangeSceneEf;
import ead.common.model.elements.effects.EmptyEffect;
import ead.common.model.elements.effects.PlayMusicEf;
import ead.common.model.elements.effects.TriggerMacroEf;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.events.WatchFieldEv;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.huds.MouseHud;
import ead.common.model.elements.operations.EAdField;
import ead.common.model.elements.predef.effects.MakeActiveElementEf;
import ead.common.model.elements.predef.effects.MoveActiveElementToMouseEf;
import ead.common.model.elements.scenes.*;
import ead.common.model.params.fills.ColorFill;
import ead.common.model.params.guievents.MouseGEv;
import ead.common.model.params.text.EAdString;
import ead.common.model.params.util.Position.Corner;
import ead.converter.EAdElementsCache;
import ead.converter.ModelQuerier;
import ead.converter.StringsConverter;
import ead.converter.UtilsConverter;
import ead.converter.resources.ResourcesConverter;
import ead.converter.subconverters.actors.ElementConverter;
import ead.converter.subconverters.actors.NPCConverter;
import ead.converter.subconverters.conditions.ConditionsConverter;
import ead.converter.subconverters.effects.EffectsConverter;
import ead.plugins.engine.bubbledescription.BubbleNameEv;
import es.eucm.eadventure.common.data.adventure.AdventureData;
import es.eucm.eadventure.common.data.chapter.ElementReference;
import es.eucm.eadventure.common.data.chapter.Exit;
import es.eucm.eadventure.common.data.chapter.ExitLook;
import es.eucm.eadventure.common.data.chapter.Trajectory;
import es.eucm.eadventure.common.data.chapter.elements.ActiveArea;
import es.eucm.eadventure.common.data.chapter.elements.Player;
import es.eucm.eadventure.common.data.chapter.resources.Resources;
import es.eucm.eadventure.common.data.chapter.scenes.Scene;

import java.awt.*;
import java.util.List;

@Singleton
public class SceneConverter {

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

	@Inject
	public SceneConverter(ResourcesConverter resourceConverter,
			EAdElementsCache elementsCache,
			TransitionConverter transitionConverter,
			RectangleConverter rectangleConverter,
			UtilsConverter utilsConverter, EffectsConverter effectConverter,
			ConditionsConverter conditionsConverter,
			StringsConverter stringsConverter, ModelQuerier modelQuerier,
			TrajectoryConverter trajectoryConverter,
			NPCConverter elementConverter) {
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
	}

	public EAdScene convert(Scene s) {

		SceneElement background = new SceneElement();
		BasicScene scene = new BasicScene(background);
		scene.setId(s.getId());

		addAppearance(scene, s);
		// XXX Information
		addReferences(scene, s);
		addActiveZones(scene, s);
		addExits(scene, s);

		// Add trajectory
		if (modelQuerier.getAventureData().getPlayerMode() == AdventureData.MODE_PLAYER_3RDPERSON) {
			scene.setTrajectoryDefinition(trajectoryConverter.convert(s
					.getTrajectory()));
		}

		return scene;
	}

	public void addAppearance(BasicScene scene, Scene s) {
		// Appearance tab
		SceneElement background = (SceneElement) scene.getBackground();
		// The foreground is only initialized if needed
		SceneElement foreground = null;
		// Resources blocks
		int i = 0;
		for (Resources r : s.getResources()) {
			// Background [SC - Bg]
			String backgroundPath = r
					.getAssetPath(Scene.RESOURCE_TYPE_BACKGROUND);
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
				background.setInitialBundle(utilsConverter
						.getResourceBundleId(i));
			}
			// Foreground [SC - Fg]
			String foregroundPath = r
					.getAssetPath(Scene.RESOURCE_TYPE_FOREGROUND);
			if (foregroundPath != null) {
				foregroundPath = utilsConverter.applyForegroundMask(
						foregroundPath, backgroundPath);
				if (foreground == null) {
					foreground = new SceneElement();
					foreground.setInitialEnable(false);
					foreground.setInitialZ(FOREGROUND_Z);
					foreground.setInitialBundle(utilsConverter
							.getResourceBundleId(i));
					scene.add(foreground);
				}
				foreground.setAppearance(utilsConverter.getResourceBundleId(i),
						new ead.common.model.assets.drawable.basics.Image(
								foregroundPath));
			}

			int finalWidth = (int) (d.getWidth() * scale);
			boolean hasScroll = finalWidth > 800;
			if (hasScroll) {

			}

			i++;
		}

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

	private void addReferences(BasicScene scene, Scene s) {
		addReferences(scene, s.getAtrezzoReferences());
		addReferences(scene, s.getItemReferences());
		addReferences(scene, s.getCharacterReferences());

		// Add player
		if (modelQuerier.getAventureData().getPlayerMode() == AdventureData.MODE_PLAYER_3RDPERSON) {
			SceneElement playerRef = new SceneElement(
					(EAdSceneElementDef) elementsCache.get(Player.IDENTIFIER));
			if (s.isAllowPlayerLayer() && s.getPlayerLayer() != -1) {
				playerRef.setInitialZ(s.getPlayerLayer());
			} else {
				playerRef.setInitialZ(PLAYER_Z);
			}

			playerRef.setInitialScale(s.getPlayerScale());
			playerRef.setPosition(Corner.BOTTOM_CENTER, s.getPositionX(), s
					.getPositionY());
			if (s.getTrajectory() != null) {
				Trajectory t = s.getTrajectory();
				playerRef.setInitialScale(t.getInitial().getScale());
				playerRef.setPosition(Corner.BOTTOM_CENTER, t.getInitial()
						.getX(), t.getInitial().getY());
			}
			scene.addAddedEffect(new MakeActiveElementEf(playerRef));
			scene.add(playerRef);

			scene.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED,
					new MoveActiveElementToMouseEf());
		}
	}

	private void addReferences(BasicScene scene,
			List<ElementReference> references) {
		for (ElementReference e : references) {
			EAdSceneElementDef def = (EAdSceneElementDef) elementsCache.get(e
					.getTargetId());
			SceneElement sceneElement = new SceneElement(def);
			sceneElement.setPosition(Corner.BOTTOM_CENTER, e.getX(), e.getY());
			sceneElement.setInitialZ(e.getLayer());
			sceneElement.setInitialScale(e.getScale());
			// XXX Influence area
			scene.add(sceneElement);

			// Add event to change appearance when required by the actor's
			// definition
			if (def.getResources().size() > 1) {
				utilsConverter.addWatchDefinitionField(sceneElement,
						SceneElement.VAR_BUNDLE_ID);
			}

			// Add visibility condition
			utilsConverter.addWatchCondition(sceneElement, sceneElement
					.getField(SceneElement.VAR_VISIBLE), e.getConditions());
		}
	}

	private void addExits(BasicScene scene, Scene s) {
		int i = 0;
		for (Exit e : s.getExits()) {
			AbstractShape shape = rectangleConverter.convert(e, EXIT_FILL);

			GhostElement exit = new GhostElement(shape);
			if (e.isRectangular()) {
				exit.setPosition(Corner.TOP_LEFT, e.getX(), e.getY());
			}

			EAdCondition cond = conditionsConverter.convert(e.getConditions());

			EAdEffect effectWhenClick;

			// Next scene
			ChangeSceneEf nextScene = new ChangeSceneEf();
			nextScene.setNextScene(new BasicElement(e.getNextSceneId()));
			nextScene.setTransition(transitionConverter.getTransitionExit(e
					.getTransitionType(), e.getTransitionTime()));

			// Add effects
			List<EAdEffect> effects = effectConverter.convert(e.getEffects());
			if (effects.size() > 0) {
				effectWhenClick = effects.get(0);
				effects.get(effects.size() - 1).getNextEffects().add(nextScene);
			} else {
				effectWhenClick = nextScene;
			}

			// Add next effects
			effects = effectConverter.convert(e.getPostEffects());
			if (effects.size() > 0) {
				nextScene.getNextEffects().add(effects.get(0));
			}

			// Set Z
			exit.setInitialZ(EXIT_Z + i);

			// Add appearance
			ExitLook exitLook = e.getDefaultExitLook();
			// Text
			if (!"".equals(exitLook.getExitText())) {
				EAdString text = stringsConverter.convert(exitLook
						.getExitText(), false);
				exit.setVarInitialValue(BubbleNameEv.VAR_BUBBLE_NAME, text);
			}
			// XXX For now, we use the default exit image
			utilsConverter.addCursorChange(exit, MouseHud.EXIT_CURSOR);

			// Add the exit to the scene
			scene.add(exit);

			// If it has not-effects
			if (e.isHasNotEffects()) {
				TriggerMacroEf triggerMacro = new TriggerMacroEf();
				// Add ACTIVE effects
				triggerMacro.putEffect(cond, effectWhenClick);
				// Add INACTIVE effects
				EAdList<EAdEffect> macro = new EAdList<EAdEffect>();
				effects = effectConverter.convert(e.getNotEffects());
				if (effects.size() > 0) {
					macro.add(effects.get(0));
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

			i++;
		}

	}

	private void addActiveZones(BasicScene scene, Scene s) {
		int i = 0;
		for (ActiveArea a : s.getActiveAreas()) {
			AbstractShape shape = rectangleConverter.convert(a,
					ACTIVE_AREA_FILL);
			GhostElement activeArea = new GhostElement(shape);
			// Add actions
			elementConverter.addActions(a, activeArea.getDefinition());
			elementConverter.addDescription(a, activeArea.getDefinition());

			activeArea.setId(a.getId());
			if (a.isRectangular()) {
				activeArea.setPosition(Corner.TOP_LEFT, a.getX(), a.getY());
			}
			// Set Z
			activeArea.setInitialZ(ACTIVE_AREA_Z + i);
			elementsCache.put(activeArea);
			// Add visibility condition
			utilsConverter.addWatchCondition(activeArea, activeArea
					.getField(SceneElement.VAR_VISIBLE), a.getConditions());

			scene.add(activeArea);
			i++;
		}

	}

	/**
	 * Foregrounds are imported os objects over the scene. A bundle could have an empty foreground. Then, the foreground
     * should be invisible.
     *
     * Also, music of the scene is converted (since it shares conditions with the foreground)
	 *
	 * @param scene
	 * @param resources
	 * @param foreground
	 */
	private void addForegroundMusicConditions(BasicScene scene,
                                              List<Resources> resources, SceneElement foreground) {
		WatchFieldEv watchField = new WatchFieldEv();
		boolean hasMusic = false;
		TriggerMacroEf triggerMacroVisible = new TriggerMacroEf();
		TriggerMacroEf triggerMacroMusic = new TriggerMacroEf();
		// Prepare visibility for foreground
		EAdField<Boolean> foregroundVisible = (foreground != null ? foreground
				.getField(SceneElement.VAR_VISIBLE) : null);
		ChangeFieldEf makeForegroundVisible = new ChangeFieldEf(
				foregroundVisible, EmptyCond.TRUE);
		ChangeFieldEf makeForegroundInvisible = new ChangeFieldEf(
				foregroundVisible, EmptyCond.FALSE);

		for (Resources r : resources) {
			EAdCondition cond = conditionsConverter.convert(r.getConditions());
			// Watch all the fields in the condition
			for (EAdField<?> field : conditionsConverter
					.getFieldsLastCondition()) {
				watchField.watchField(field);
			}

			if (foreground != null) {
				// Check if there is foreground in this bundle
				if (r.getAssetPath(Scene.RESOURCE_TYPE_FOREGROUND) == null) {
					triggerMacroVisible
							.putEffect(cond, makeForegroundInvisible);
				} else {
					triggerMacroVisible.putEffect(cond, makeForegroundVisible);
				}
			}

            // Check for music [SC - Music]
			String musicPath = r.getAssetPath(Scene.RESOURCE_TYPE_MUSIC);
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
