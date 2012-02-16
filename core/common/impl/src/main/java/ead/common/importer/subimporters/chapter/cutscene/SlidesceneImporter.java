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
import ead.common.importer.interfaces.EAdElementFactory;
import ead.common.importer.interfaces.EffectsImporterFactory;
import ead.common.importer.interfaces.ResourceImporter;
import ead.common.importer.subimporters.effects.TriggerSceneImporter;
import ead.common.model.elements.EAdChapter;
import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.EAdEvent;
import ead.common.model.elements.conditions.EmptyCond;
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
import ead.common.model.elements.transitions.DisplaceTransition;
import ead.common.model.elements.transitions.EAdTransition;
import ead.common.model.elements.transitions.EmptyTransition;
import ead.common.model.elements.transitions.FadeInTransition;
import ead.common.model.elements.transitions.enums.DisplaceTransitionType;
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
public class SlidesceneImporter implements
		EAdElementImporter<Slidescene, EAdScene> {

	private EAdElementFactory factory;

	private ResourceImporter resourceImporter;

	private StringHandler stringHandler;

	private EffectsImporterFactory effectsImporter;

	private ImageLoaderFactory imageLoader;

	private InputStreamCreator inputStreamCreator;

	private Animation animation;

	private FramesAnimation frames;

	@Inject
	public SlidesceneImporter(EffectsImporterFactory effectsImporter,
			EAdElementImporter<Conditions, EAdCondition> conditionsImporter,
			EAdElementFactory factory, ResourceImporter resourceImporter,
			StringHandler stringHandler, ImageLoaderFactory imageLoader,
			InputStreamCreator inputStreamCreator) {
		this.imageLoader = imageLoader;
		this.inputStreamCreator = inputStreamCreator;
		this.factory = factory;
		this.resourceImporter = resourceImporter;
		this.stringHandler = stringHandler;
		this.effectsImporter = effectsImporter;
	}

	@Override
	public EAdScene init(Slidescene oldSlideScene) {
		EAdScene scene = new BasicScene();
		scene.setId(oldSlideScene.getId() + "_slide_1");
		return scene;
	}

	@Override
	public EAdScene convert(Slidescene oldSlideScene, Object object) {
		EAdChapter chapter = factory.getCurrentChapterModel();
		EAdScene cutscene = (EAdScene) object;

		importDocumentation(cutscene, oldSlideScene);
		importResources(cutscene, oldSlideScene, chapter);

		return cutscene;
	}

	private void importResources(EAdScene cutscene, Slidescene oldSlides,
			EAdChapter chapter) {
		// FIXME music is not imported for animations
		cutscene.setReturnable(false);
		Resources res = oldSlides.getResources().get(0);
		String assetPath = res.getAssetPath(Slidescene.RESOURCE_TYPE_SLIDES);

		ChangeFieldEf hideInventory = new ChangeFieldEf(
				SystemFields.SHOW_INVENTORY, BooleanOp.FALSE_OP);

		// hide inventory event
		addHideInventoryEvent(cutscene, hideInventory);

		// Change scene after slide show
		ChangeSceneEf changeNextScene = getNextScene(oldSlides);
		EffectsMacro macro = effectsImporter.getMacroEffects(oldSlides
				.getEffects());
		if (macro != null) {
			TriggerMacroEf triggerMacro = new TriggerMacroEf();
			triggerMacro.putMacro(macro, EmptyCond.TRUE_EMPTY_CONDITION);
			changeNextScene.getNextEffects().add(triggerMacro);
		}

		initAnimation(assetPath);
		// generate slides properties
		List<Image> backgroundImages = generateBgImages();
		List<EAdTransition> transitions = generateTransitions();
		List<Integer> times = generateTimes();

		EAdScene[] scenes = new EAdScene[backgroundImages.size()];
		for (int i = 0; i < scenes.length; i++) {
			if (i == 0)
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
			ChangeSceneEf effect = null;
			if (i == scenes.length - 1) {
				effect = changeNextScene;
				ChangeFieldEf showInventory = new ChangeFieldEf(
						SystemFields.SHOW_INVENTORY, BooleanOp.TRUE_OP);
				effect.getNextEffects().add(showInventory);
			} else {
				effect = new ChangeSceneEf();
				((ChangeSceneEf) effect).setNextScene(scenes[i + 1]);
			}
			effect.setTransition(transitions.get(i));

			scenes[i].getBackground().addBehavior(MouseGEv.MOUSE_LEFT_CLICK,
					effect);

			if (i != scenes.length - 1) {
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
				times.add(new Long(animation.getFrame(i).getTime()).intValue());
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

	private void addHideInventoryEvent(EAdScene cutscene, ChangeFieldEf field) {
		SceneElementEv bgEvent = new SceneElementEv();
		bgEvent.addEffect(SceneElementEvType.ADDED_TO_SCENE, field);
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

	private ChangeSceneEf getNextScene(Slidescene oldSlides) {
		EAdScene nextScene = null;
		EAdTransition transition = EmptyTransition.instance();
		switch (oldSlides.getNext()) {
		case Slidescene.GOBACK:
			nextScene = null;
			break;
		case Slidescene.ENDCHAPTER:
			// FIXME end chapter slide scene
			nextScene = null;
			break;
		case Slidescene.NEWSCENE:
			nextScene = (EAdScene) factory.getElementById(oldSlides
					.getTargetId());
			transition = TriggerSceneImporter.getTransition(
					oldSlides.getTransitionType(),
					oldSlides.getTransitionTime());
			break;
		}
		ChangeSceneEf changeScene = new ChangeSceneEf();
		changeScene.setNextScene(nextScene);
		changeScene.setTransition(transition);
		return changeScene;
	}

	private void importDocumentation(EAdScene scene, Slidescene oldScene) {
		stringHandler.setString(scene.getDefinition().getDoc(),
				oldScene.getDocumentation());
		stringHandler.setString(scene.getDefinition().getName(),
				oldScene.getName());
	}

	public static EAdTransition getTransition(int type, int time) {
		switch (type) {
		case Transition.TYPE_FADEIN:
			return new FadeInTransition(time);
		case Transition.TYPE_HORIZONTAL:
			return new DisplaceTransition(time,
					DisplaceTransitionType.HORIZONTAL, true);
		case Transition.TYPE_VERTICAL:
			return new DisplaceTransition(time,
					DisplaceTransitionType.VERTICAL, true);
		default:
			return EmptyTransition.instance();
		}
	}

}
