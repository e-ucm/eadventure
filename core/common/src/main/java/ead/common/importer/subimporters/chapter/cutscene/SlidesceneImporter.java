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

package ead.common.importer.subimporters.chapter.cutscene;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import ead.common.EAdElementImporter;
import ead.common.importer.annotation.ImportAnnotator;
import ead.common.importer.interfaces.EAdElementFactory;
import ead.common.importer.interfaces.EffectsImporterFactory;
import ead.common.importer.interfaces.ResourceImporter;
import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.EAdEvent;
import ead.common.model.elements.effects.ChangeSceneEf;
import ead.common.model.elements.effects.EffectsMacro;
import ead.common.model.elements.effects.TriggerMacroEf;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.events.SceneElementEv;
import ead.common.model.elements.events.TimedEv;
import ead.common.model.elements.events.enums.SceneElementEvType;
import ead.common.model.elements.events.enums.TimedEvType;
import ead.common.model.elements.guievents.MouseGEv;
import ead.common.model.elements.scene.EAdScene;
import ead.common.model.elements.scenes.BasicScene;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.transitions.EAdTransition;
import ead.common.model.elements.transitions.EmptyTransition;
import ead.common.model.elements.variables.SystemFields;
import ead.common.model.elements.variables.operations.BooleanOp;
import ead.common.resources.assets.drawable.basics.Image;
import ead.common.resources.assets.drawable.basics.animation.FramesAnimation;
import ead.common.util.StringHandler;
import es.eucm.eadventure.common.data.animation.Animation;
import es.eucm.eadventure.common.data.animation.Frame;
import es.eucm.eadventure.common.data.animation.ImageLoaderFactory;
import es.eucm.eadventure.common.data.animation.Transition;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.resources.Resources;
import es.eucm.eadventure.common.data.chapter.scenes.Slidescene;
import es.eucm.eadventure.common.loader.InputStreamCreator;
import es.eucm.eadventure.common.loader.Loader;

/**
 * Scenes importer
 * 
 */
public class SlidesceneImporter extends CutsceneImporter<Slidescene> {

	private ImageLoaderFactory imageLoader;

	private InputStreamCreator inputStreamCreator;

	private Animation animation;

	private FramesAnimation frames;

	private EAdElementImporter<Conditions, EAdCondition> conditionsImporter;

	@Inject
	public SlidesceneImporter(EffectsImporterFactory effectsImporter,
			EAdElementImporter<Conditions, EAdCondition> conditionsImporter,
			EAdElementFactory factory, ResourceImporter resourceImporter,
			StringHandler stringHandler, ImageLoaderFactory imageLoader,
			InputStreamCreator inputStreamCreator,
			ImportAnnotator annotator) {
		super(stringHandler, factory, effectsImporter, resourceImporter, annotator);
		this.imageLoader = imageLoader;
		this.inputStreamCreator = inputStreamCreator;
		this.conditionsImporter = conditionsImporter;
	}

	@Override
	public EAdScene init(Slidescene oldSlideScene) {
		EAdScene scene = new BasicScene();
		scene.setId(oldSlideScene.getId() + "_slide_1");
		return scene;
	}

	@Override
	public EAdScene convert(Slidescene oldSlideScene, Object object) {
		EAdScene cutscene = super.convert(oldSlideScene, object);
		return cutscene;
	}

	protected void importResources(Slidescene oldSlides, EAdScene cutscene) {
		// Hide inventory event
		addHideInventoryEvent(cutscene);
		EAdEffect changeNextScene = getEndEffect(oldSlides);

		if (oldSlides.getResources().size() == 1) {
			createSceneFromSlides(cutscene, oldSlides, oldSlides.getResources()
					.get(0), changeNextScene);
		} else {
			// When there's more than one appearance, we create a series of
			// scene for every animation.
			// Then, the initial cutscene decides which appearance to launch
			TriggerMacroEf triggerMacro = new TriggerMacroEf();
			for (Resources res : oldSlides.getResources()) {
				EAdScene scene = new BasicScene();
				createSceneFromSlides(scene, oldSlides, res, changeNextScene);
				EffectsMacro macro = new EffectsMacro();
				macro.getEffects().add(new ChangeSceneEf(scene));
				EAdCondition c = conditionsImporter.init(res.getConditions());
				c = conditionsImporter.convert(res.getConditions(), c);
				triggerMacro.putMacro(macro, c);
			}
			SceneElementEv event = new SceneElementEv();
			event.addEffect(SceneElementEvType.ADDED_TO_SCENE, triggerMacro);
			cutscene.getEvents().add(event);
		}

	}

	private void createSceneFromSlides(EAdScene cutscene, Slidescene oldSlides,
			Resources res, EAdEffect changeNextScene) {
		String assetPath = res.getAssetPath(Slidescene.RESOURCE_TYPE_SLIDES);

		// Change scene after slide show

		// FIXME music is not imported for animations
		initAnimation(assetPath);
		// generate slides properties
		List<Image> backgroundImages = generateBgImages();
		List<EAdTransition> transitions = generateTransitions();
		List<Integer> times = generateTimes();

		EAdScene[] scenes = new EAdScene[backgroundImages.size()];
		for (int i = 0; i < scenes.length; i++) {
			if (i == 0 && oldSlides.getResources().size() == 1)
				scenes[i] = cutscene;
			else
				scenes[i] = new BasicScene();

			Image drawable = backgroundImages.get(i);
			SceneElement background = new SceneElement(drawable);
			// Adjust scene background to 800x600 (restriction from old
			// model)
			Dimension d = resourceImporter.getDimensionsForNewImage(drawable
					.getUri().getPath());
			float scaleX = 800.0f / d.width;
			float scaleY = 600.0f / d.height;
			background.setInitialScale(scaleX, scaleY);

			background.setId(scenes[i].getId() + "_background");
			scenes[i].setBackground(background);
			scenes[i].setReturnable(false);
		}

		for (int i = 0; i < scenes.length; i++) {
			EAdEffect effect = null;
			if (i == scenes.length - 1) {
				effect = changeNextScene;
				ChangeFieldEf showInventory = new ChangeFieldEf(
						SystemFields.SHOW_INVENTORY, BooleanOp.TRUE_OP);
				effect.getNextEffects().add(showInventory);
			} else {
				effect = new ChangeSceneEf();
				((ChangeSceneEf) effect).setNextScene(scenes[i + 1]);
			}

			if (effect instanceof ChangeSceneEf)
				((ChangeSceneEf) effect).setTransition(transitions.get(i));

			scenes[i].getBackground().addBehavior(MouseGEv.MOUSE_LEFT_PRESSED,
					effect);

			if (i != scenes.length - 1 && times.get(i) != -1 ) {
				EAdEvent changeEvent = getChangeSceneEvent(scenes[i + 1],
						times.get(i), effect);
				scenes[i].getBackground().getEvents().add(changeEvent);
			}

		}
	}

	private void initAnimation(String assetPath) {
		if (assetPath.endsWith(".eaa")) {
			animation = Loader.loadAnimation(inputStreamCreator, assetPath,
					imageLoader);

		} else {
			frames = (FramesAnimation) resourceImporter.getAssetDescritptor(
					assetPath, FramesAnimation.class);
		}

	}

	private List<Integer> generateTimes() {
		ArrayList<Integer> times = new ArrayList<Integer>();
		if (animation != null) {
			for (int i = 0; i < animation.getFrames().size(); i++) {
				if (animation.getFrame(i).isWaitforclick()) {
					// Minus one means that no timed event for changing the
					// scene must be added
					times.add(-1);
				} else {
					times.add(new Long(animation.getFrame(i).getTime())
							.intValue());
				}
			}
		} else {
			for (ead.common.resources.assets.drawable.basics.animation.Frame f : frames
					.getFrames()) {
				times.add(f.getTime());
			}
		}
		return times;
	}

	private List<EAdTransition> generateTransitions() {
		ArrayList<EAdTransition> transitions = new ArrayList<EAdTransition>();
		if (animation != null) {
			for (int i = 0; i < animation.getFrames().size(); i++) {
				Transition t = animation.getTranstionForFrame(i);
				if (t != null) {
					transitions.add(getTransition(t.getType(),
							new Long(t.getTime()).intValue()));
				} else {
					transitions.add(EmptyTransition.instance());
				}
			}
		} else {
			for (int i = 0; i < frames.getFrameCount(); i++) {
				transitions.add(EmptyTransition.instance());
			}
		}
		return transitions;
	}

	private List<Image> generateBgImages() {
		ArrayList<Image> images = new ArrayList<Image>();
		if (animation != null) {
			for (Frame f : animation.getFrames()) {
				String uri = resourceImporter.getURI(f.getUri());
				images.add(new Image(uri));
			}
		} else {
			for (int i = 0; i < frames.getFrameCount(); i++) {
				images.add((Image) frames.getFrame(i).getDrawable());
			}
		}
		return images;
	}

	private void addHideInventoryEvent(EAdScene cutscene) {
		ChangeFieldEf hideInventory = new ChangeFieldEf(
				SystemFields.SHOW_INVENTORY, BooleanOp.FALSE_OP);
		SceneElementEv bgEvent = new SceneElementEv();
		bgEvent.addEffect(SceneElementEvType.ADDED_TO_SCENE, hideInventory);
		cutscene.getEvents().add(bgEvent);
	}

	private EAdEvent getChangeSceneEvent(EAdScene eAdScene, int time,
			EAdEffect changeScene) {
		TimedEv event = new TimedEv();
		event.setRepeats(1);
		event.setTime(time);
		event.addEffect(TimedEvType.END_TIME, changeScene);
		return event;
	}

	@Override
	protected void importConfiguration(EAdScene scene, EAdEffect endEffect) {

	}

}
