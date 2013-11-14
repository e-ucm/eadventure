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
import es.eucm.ead.importer.ModelQuerier;
import es.eucm.ead.importer.UtilsConverter;
import es.eucm.ead.importer.resources.ResourcesConverter;
import es.eucm.ead.importer.subconverters.effects.EffectsConverter;
import es.eucm.ead.model.assets.drawable.EAdDrawable;
import es.eucm.ead.model.assets.multimedia.Video;
import es.eucm.ead.model.elements.BasicElement;
import es.eucm.ead.model.elements.conditions.EmptyCond;
import es.eucm.ead.model.elements.effects.ChangeChapterEf;
import es.eucm.ead.model.elements.effects.ChangeSceneEf;
import es.eucm.ead.model.elements.effects.Effect;
import es.eucm.ead.model.elements.effects.QuitGameEf;
import es.eucm.ead.model.elements.effects.timedevents.WaitEf;
import es.eucm.ead.model.elements.effects.variables.ChangeFieldEf;
import es.eucm.ead.model.elements.operations.ElementField;
import es.eucm.ead.model.elements.scenes.Scene;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.elements.scenes.VideoScene;
import es.eucm.ead.model.params.guievents.MouseGEv;
import es.eucm.eadventure.common.data.animation.Animation;
import es.eucm.eadventure.common.data.animation.Transition;
import es.eucm.eadventure.common.data.chapter.resources.Resources;
import es.eucm.eadventure.common.data.chapter.scenes.Cutscene;
import es.eucm.eadventure.common.data.chapter.scenes.Slidescene;
import es.eucm.eadventure.common.data.chapter.scenes.Videoscene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class CutsceneConverter {

	private static Logger logger = LoggerFactory
			.getLogger(CutsceneConverter.class);

	public static final String IN_CUTSCENE = "in_cutscene";
	private final ModelQuerier modelQuerier;

	private ResourcesConverter resourceConverter;

	private TransitionConverter transitionConverter;

	private UtilsConverter utilsConverter;

	private EffectsConverter effectsConverter;

	// Aux vars
	private Animation animation;
	private ArrayList<String> uris;

	private static final String[] EXTENSIONS = new String[] { "png", "jpg",
			"jpeg", "gif", "bmp" };

	@Inject
	public CutsceneConverter(ResourcesConverter resourceConverter,
			TransitionConverter transitionConverter,
			UtilsConverter utilsConverter, EffectsConverter effectsConverter,
			ModelQuerier modelQuerier) {
		this.resourceConverter = resourceConverter;
		this.transitionConverter = transitionConverter;
		this.utilsConverter = utilsConverter;
		this.effectsConverter = effectsConverter;
		this.modelQuerier = modelQuerier;
	}

	public List<Scene> convert(Cutscene scene) {
		List<Scene> cutscene;
		if (scene instanceof Slidescene) {
			cutscene = convertSlidesScene((Slidescene) scene);
		} else {
			cutscene = convertVideoScene(scene);
		}
		int i = 0;
		for (Scene s : cutscene) {
			if (i == 0) {
				s.setId(scene.getId());
			} else {
				s.setId(scene.getId() + i);
			}
			i++;
		}
		return cutscene;
	}

	public List<Scene> convertSlidesScene(Slidescene cs) {
		List<Scene> cutscene = new ArrayList<Scene>();
		// XXX when more than one appearance, we need to create a series of
		// different slides
		for (Resources r : cs.getResources()) {
			// We import every slide as an independent scene
			String slidesPath = r.getAssetPath(Slidescene.RESOURCE_TYPE_SLIDES);
			// [SS - Slides]
			prepareFrames(slidesPath);
			for (int i = 0; i < frames(); i++) {
				EAdDrawable background = utilsConverter.getBackground(uri(i),
						true);

				// XXX Sound
				Scene scene = new Scene(new SceneElement(background));
				Effect nextEffect;

				if (cs.getNext() == Slidescene.ENDCHAPTER) {
					// [SS - NextScene]
					nextEffect = generateEndChapter();
				} else {
					// Link to next slide
					ChangeSceneEf nextSlide;
					// If it's last frame, we link with the next scene, ad
					// we add the next effects
					if (i == frames() - 1) {
						// [SS - NextScene]
						nextSlide = generateNextScene(cs);
					} else {
						// Else, we link with the next slide
						nextSlide = new ChangeSceneEf(new BasicElement(cs
								.getId()
								+ (i + 1)));
					}

					// Add transition, if any
					// For the last scene, we add the next transition
					// [SS - Transition]
					if (i == frames() - 1) {
						nextSlide.setTransition(transitionConverter
								.getTransitionNextScene(cs.getTransitionType(),
										cs.getTransitionTime()));
					} else {
						// If the animation has transitions, we add it
						if (isUseTransitions()) {
							Transition t = getTransition(i);
							nextSlide.setTransition(transitionConverter
									.getTransition(t.getType(), (int) t
											.getTime()));
						}
					}
					nextEffect = nextSlide;
				}

				if (isWaitforclick(i)) {
					// Change to next slide when click
					scene.getBackground().addBehavior(
							MouseGEv.MOUSE_LEFT_PRESSED, nextEffect);
				} else {
					// Wait the time given by the frame
					WaitEf wait = new WaitEf(time(i));
					wait.addNextEffect(nextEffect);
					scene.addAddedEffect(wait);
				}
				scene.setReturnable(false);
				cutscene.add(scene);
			}
			// Slides

			// XXX scene music
		}

		// avoid empty cutscenes
		if (cutscene.isEmpty()) {
			logger.error("No scenes in cutscene {} (id {})", cs.getName(), cs
					.getId());
			return cutscene;
		}

		// Add conditioned resources
		for (Scene scene : cutscene) {
			utilsConverter.addResourcesConditions(cs.getResources(), scene
					.getBackground(), SceneElement.VAR_BUNDLE_ID);
		}

		return cutscene;
	}

	public List<Scene> convertVideoScene(Cutscene cs) {
		// [VI - Video]
		Videoscene vs = (Videoscene) cs;
		// XXX Not working when there's more than one resource
		Video video = resourceConverter.getVideo(vs.getResources().get(0)
				.getAssetPath(Videoscene.RESOURCE_TYPE_VIDEO));

		VideoScene videoScene = new VideoScene();
		videoScene.setVideo(video);

		// [VI - NextScene]
		Effect finalEffect;
		if (cs.getType() == Cutscene.ENDCHAPTER) {
			finalEffect = generateEndChapter();
		} else {
			finalEffect = generateNextScene(cs);
		}
		videoScene.addFinalEffect(finalEffect);

		ArrayList<Scene> scenes = new ArrayList<Scene>();
		scenes.add(videoScene);
		return scenes;
	}

	private BasicElement getNextScene(Cutscene cs) {
		BasicElement nextScene = null;
		switch (cs.getNext()) {
		// [CS - PrevScene]
		case Slidescene.GOBACK:
			nextScene = null;
			break;
		// [CS - NewScene]
		case Slidescene.NEWSCENE:
			nextScene = new BasicElement(cs.getTargetId());
			break;
		}
		return nextScene;
	}

	public Effect generateEndChapter() {
		Effect nextEffect;
		// [CS - ChapterEnds]
		int index = modelQuerier.getCurrentChapterIndex();
		int totalChapter = modelQuerier.getAventureData().getChapters().size();
		// Last chapter, quit the game
		if (index == totalChapter - 1) {
			nextEffect = new QuitGameEf();
		} else {
			// Go to next chapter
			nextEffect = new ChangeChapterEf(modelQuerier
					.generateChapterId(index + 1));
		}
		return nextEffect;
	}

	public ChangeSceneEf generateNextScene(Cutscene cs) {
		// [CS - PrevScene] [CS - NewScene]
		ChangeSceneEf nextScene = new ChangeSceneEf();
		nextScene.setNextScene(getNextScene(cs));
		List<Effect> effects = effectsConverter.convert(cs.getEffects());

		ElementField inCutscene = new ElementField(modelQuerier
				.getCurrentChapter(), IN_CUTSCENE + cs.getId(), false);
		ChangeFieldEf finalEffect = new ChangeFieldEf(inCutscene,
				EmptyCond.FALSE);
		if (effects.size() > 0) {
			nextScene.addNextEffect(effects.get(0));
			effects.get(effects.size() - 1).addNextEffect(finalEffect);
		} else {
			nextScene.addNextEffect(finalEffect);
		}
		return nextScene;
	}

	void prepareFrames(String slidesPath) {
		animation = null;
		if (slidesPath.endsWith(".eaa")) {
			animation = resourceConverter.getAnimation(slidesPath);
		} else {
			int frames = 0;
			uris = new ArrayList<String>();
			boolean done = false;
			while (!done) {
				String name = slidesPath + "_0" + (frames + 1);
				done = true;
				for (String ext : EXTENSIONS) {
					String uri = name + "." + ext;
					if (resourceConverter.fileExists(uri)) {
						done = false;
						uris.add(uri);
						break;
					}
					uri = name + "." + ext.toUpperCase();
					if (resourceConverter.fileExists(uri)) {
						done = false;
						uris.add(uri);
						break;
					}
				}
				frames++;
			}

		}
	}

	private int frames() {
		return animation == null ? uris.size() : animation.getFrames().size();
	}

	private String uri(int i) {
		return animation == null ? uris.get(i) : animation.getFrames().get(i)
				.getUri();
	}

	private boolean isWaitforclick(int i) {
		return animation == null
				|| animation.getFrames().get(i).isWaitforclick();
	}

	private int time(int i) {
		return (int) (animation == null ? 0 : animation.getFrames().get(i)
				.getTime());
	}

	private boolean isUseTransitions() {
		return animation != null && animation.isUseTransitions();
	}

	private Transition getTransition(int i) {
		return animation.getTransitions().get(i);
	}
}
