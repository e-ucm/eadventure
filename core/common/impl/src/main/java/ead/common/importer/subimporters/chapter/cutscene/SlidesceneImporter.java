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
import ead.common.model.elements.events.TimedEv;
import ead.common.model.elements.events.enums.TimedEventType;
import ead.common.model.elements.guievents.EAdMouseEvent;
import ead.common.model.elements.scene.EAdScene;
import ead.common.model.elements.scenes.SceneElementImpl;
import ead.common.model.elements.scenes.SceneImpl;
import ead.common.model.elements.transitions.EAdTransition;
import ead.common.model.elements.transitions.EmptyTransition;
import ead.common.resources.StringHandler;
import ead.common.resources.assets.drawable.basics.Image;
import ead.common.resources.assets.drawable.basics.animation.FramesAnimation;
import ead.common.resources.assets.multimedia.Sound;
import ead.common.resources.assets.multimedia.SoundImpl;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.resources.Resources;
import es.eucm.eadventure.common.data.chapter.scenes.Slidescene;

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

	@Inject
	public SlidesceneImporter(EffectsImporterFactory effectsImporter,
			EAdElementImporter<Conditions, EAdCondition> conditionsImporter,
			EAdElementFactory factory, ResourceImporter resourceImporter,
			StringHandler stringHandler) {
		this.factory = factory;
		this.resourceImporter = resourceImporter;
		this.stringHandler = stringHandler;
		this.effectsImporter = effectsImporter;
	}

	@Override
	public EAdScene init(Slidescene oldSlideScene) {
		EAdScene scene = new SceneImpl();
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
		cutscene.setReturnable(false);
		Resources res = oldSlides.getResources().get(0);
		String assetPath = res.getAssetPath(Slidescene.RESOURCE_TYPE_SLIDES);
		FramesAnimation asset = (FramesAnimation) resourceImporter
				.getAssetDescritptor(assetPath, FramesAnimation.class);
		ChangeSceneEf changeScene = getNextScene(oldSlides);

		EffectsMacro macro = effectsImporter.getMacroEffects(oldSlides
				.getEffects());
		if (macro != null) {
			TriggerMacroEf triggerMacro = new TriggerMacroEf();
			triggerMacro.putMacro(macro, EmptyCond.TRUE_EMPTY_CONDITION);
			changeScene.getNextEffects().add(triggerMacro);
		}
		
		

		EAdScene[] scenes = new EAdScene[asset.getFrameCount()];
		for (int i = 0; i < asset.getFrameCount(); i++) {
			if (i == 0)
				scenes[i] = cutscene;
			else
				scenes[i] = new SceneImpl();
			Image drawable = (Image) asset.getFrame(i).getDrawable();
			SceneElementImpl background = new SceneElementImpl(drawable);
			// Adjust scene background to 800x600 (restriction from old model)
			Dimension d = resourceImporter.getDimensionsForNewImage(drawable.getUri().getPath());
			float scaleX = 800.0f / d.width;
			float scaleY = 600.0f / d.height;
			
			background.setInitialScale(scaleX, scaleY);
			background.setId(scenes[i].getId() + "_background" );
			
			scenes[i].setBackground(background);
			scenes[i].setReturnable(false);
		}

		for (int i = 0; i < scenes.length; i++) {
			EAdEffect effect = null;
			if (i == scenes.length - 1) {
				effect = changeScene;
			} else {
				effect = new ChangeSceneEf();
				((ChangeSceneEf) effect).setNextScene(scenes[i + 1]);
			}

			scenes[i].getBackground().addBehavior(
					EAdMouseEvent.MOUSE_LEFT_CLICK, effect);
			
			if ( i != scenes.length - 1 ){
				EAdEvent changeEvent = getChangeSceneEvent(scenes[i + 1], asset.getFrame(i).getTime(), effect);
				scenes[i].getBackground().getEvents().add(changeEvent);
			}

		}

		for (Resources r : oldSlides.getResources()) {
			// Music is imported to chapter level. So, the chapter will
			// remain with the last sound track appeared in the scenes
			String musicPath = r.getAssetPath(Slidescene.RESOURCE_TYPE_MUSIC);

			if (musicPath != null) {
				Sound sound = (Sound) resourceImporter.getAssetDescritptor(
						musicPath, SoundImpl.class);
				chapter.getResources().addAsset(chapter.getInitialBundle(),
						EAdChapter.music, sound);
			}
		}

	}

	private EAdEvent getChangeSceneEvent(EAdScene eAdScene, int time, EAdEffect changeScene) {
		TimedEv event = new TimedEv( );
		event.setRepeats(1);
		event.setTime(time);
		event.addEffect(TimedEventType.START_TIME, changeScene);
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

}
