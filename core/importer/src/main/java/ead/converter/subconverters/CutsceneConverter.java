package ead.converter.subconverters;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.assets.drawable.EAdDrawable;
import ead.common.model.elements.BasicElement;
import ead.common.model.elements.EAdElement;
import ead.common.model.elements.conditions.EmptyCond;
import ead.common.model.elements.effects.ChangeSceneEf;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.events.SceneElementEv;
import ead.common.model.elements.events.enums.SceneElementEvType;
import ead.common.model.elements.operations.BasicField;
import ead.common.model.elements.scenes.BasicScene;
import ead.common.model.elements.scenes.EAdScene;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.params.guievents.MouseGEv;
import ead.common.model.params.variables.VarDef;
import ead.converter.UtilsConverter;
import ead.converter.resources.ResourceConverter;
import es.eucm.eadventure.common.data.animation.Animation;
import es.eucm.eadventure.common.data.animation.Frame;
import es.eucm.eadventure.common.data.animation.Transition;
import es.eucm.eadventure.common.data.chapter.resources.Resources;
import es.eucm.eadventure.common.data.chapter.scenes.Cutscene;
import es.eucm.eadventure.common.data.chapter.scenes.Slidescene;

@Singleton
public class CutsceneConverter {

	public static final VarDef<Boolean> IN_CUTSCENE = new VarDef<Boolean>(
			"in_cutscene", Boolean.class, false);

	private ResourceConverter resourceConverter;

	private TransitionConverter transitionConverter;

	private UtilsConverter utilsConverter;

	@Inject
	public CutsceneConverter(ResourceConverter resourceConverter,
			TransitionConverter transitionConverter,
			UtilsConverter utilsConverter) {
		this.resourceConverter = resourceConverter;
		this.transitionConverter = transitionConverter;
		this.utilsConverter = utilsConverter;
	}

	public List<EAdScene> convert(Cutscene scene) {
		List<EAdScene> cutscene = null;
		if (scene instanceof Slidescene) {
			cutscene = convertSlidesScene((Slidescene) scene);
		} else {
			cutscene = convertVideoScene(scene);
		}
		int i = 0;
		for (EAdScene s : cutscene) {
			if (i == 0) {
				s.setId(scene.getId());
			} else {
				s.setId(scene.getId() + i);
			}
			i++;
		}
		return cutscene;
	}

	public List<EAdScene> convertSlidesScene(Slidescene cs) {
		List<EAdScene> cutscene = new ArrayList<EAdScene>();
		// XXX when more than one appearance, we need to create a series of
		// different slides
		for (Resources r : cs.getResources()) {
			// We import every slide as an independent scene
			String slidesPath = r.getAssetPath(Slidescene.RESOURCE_TYPE_SLIDES);
			if (slidesPath.endsWith(".eaa")) {
				Animation anim = resourceConverter.getAnimation(slidesPath);
				int i = 0;
				for (Frame f : anim.getFrames()) {
					EAdDrawable background = resourceConverter.getImage(f
							.getUri());
					// XXX Sound
					EAdScene scene = new BasicScene();
					scene.getBackground().getDefinition().setAppearance(
							background);

					// Link to next slide
					ChangeSceneEf nextSlide = new ChangeSceneEf();
					// If it's last frame, we link with the next scene
					if (i == anim.getFrames().size() - 1) {
						nextSlide.setNextScene(getNextScene(cs));
					}
					// Else, we link with the next slide
					else {
						nextSlide.setNextScene(new BasicElement(cs.getId()
								+ (i + 1)));
					}

					// Add transition, if any
					// For the last scene, we add the next transition
					if (i == anim.getFrames().size() - 1) {
						nextSlide.setTransition(transitionConverter
								.getTransition(cs.getTransitionType(), cs
										.getTransitionTime()));
					} else {
						// If the animation has transitions, we add it
						if (anim.isUseTransitions()) {
							Transition t = anim.getTransitions().get(i);
							nextSlide.setTransition(transitionConverter
									.getTransition(t.getType(), (int) t
											.getTime()));
						}
					}

					// Change to next slide when click
					scene.getBackground().addBehavior(
							MouseGEv.MOUSE_LEFT_PRESSED, nextSlide);
					i++;
					cutscene.add(scene);
				}
			}
			// Slides

			// XXX scene music
		}

		// Add conditioned resources
		for (EAdScene scene : cutscene) {
			utilsConverter.addResourcesConditions(cs.getResources(), scene
					.getBackground(), SceneElement.VAR_BUNDLE_ID);
		}

		// We add an event that sets the IN_CUTSCENE field for the use of others
		// (trigger cutscene effect, for example)
		EAdScene firstScene = cutscene.get(0);
		EAdScene lastScene = cutscene.get(cutscene.size() - 1);
		BasicField<Boolean> inCutscene = new BasicField<Boolean>(firstScene,
				IN_CUTSCENE);
		// Event for the first scene
		SceneElementEv event1 = new SceneElementEv();
		event1.addEffect(SceneElementEvType.ADDED, new ChangeFieldEf(
				inCutscene, EmptyCond.TRUE));
		firstScene.getEvents().add(event1);
		// Event for the last scene
		SceneElementEv event2 = new SceneElementEv();
		event2.addEffect(SceneElementEvType.REMOVED, new ChangeFieldEf(
				inCutscene, EmptyCond.FALSE));
		lastScene.getEvents().add(event2);

		return cutscene;
	}

	public List<EAdScene> convertVideoScene(Cutscene cs) {
		// XXX
		return null;
	}

	private EAdElement getNextScene(Cutscene cs) {
		EAdElement nextScene = null;
		switch (cs.getNext()) {
		case Slidescene.GOBACK:
			nextScene = null;
			break;
		case Slidescene.ENDCHAPTER:
			// XXX
			break;
		case Slidescene.NEWSCENE:
			nextScene = new BasicElement(cs.getTargetId());
			break;
		}
		return nextScene;
	}

}
