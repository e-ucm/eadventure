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
import ead.common.model.elements.BasicElement;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.EAdElement;
import ead.common.model.elements.conditions.EmptyCond;
import ead.common.model.elements.effects.ChangeSceneEf;
import ead.common.model.elements.effects.QuitGameEf;
import ead.common.model.elements.effects.timedevents.WaitEf;
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
import ead.converter.resources.ResourcesConverter;
import ead.converter.subconverters.effects.EffectsConverter;
import es.eucm.eadventure.common.data.animation.Animation;
import es.eucm.eadventure.common.data.animation.Frame;
import es.eucm.eadventure.common.data.animation.Transition;
import es.eucm.eadventure.common.data.chapter.resources.Resources;
import es.eucm.eadventure.common.data.chapter.scenes.Cutscene;
import es.eucm.eadventure.common.data.chapter.scenes.Slidescene;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class CutsceneConverter {

    public static final VarDef<Boolean> IN_CUTSCENE = new VarDef<Boolean>(
            "in_cutscene", Boolean.class, false);

    private ResourcesConverter resourceConverter;

    private TransitionConverter transitionConverter;

    private UtilsConverter utilsConverter;

    private EffectsConverter effectsConverter;

    @Inject
    public CutsceneConverter(ResourcesConverter resourceConverter,
                             TransitionConverter transitionConverter,
                             UtilsConverter utilsConverter, EffectsConverter effectsConverter) {
        this.resourceConverter = resourceConverter;
        this.transitionConverter = transitionConverter;
        this.utilsConverter = utilsConverter;
        this.effectsConverter = effectsConverter;
    }

    public List<EAdScene> convert(Cutscene scene) {
        List<EAdScene> cutscene;
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
                    EAdDrawable background = utilsConverter.getBackground(f
                            .getUri(), true);

                    // XXX Sound
                    BasicScene scene = new BasicScene();
                    scene.getBackground().getDefinition().setAppearance(
                            background);
                    EAdEffect nextEffect = null;

                    if (cs.getNext() == Slidescene.ENDCHAPTER) {
                        // XXX Games with more than one chapter will fail
                        nextEffect = new QuitGameEf();
                    } else {
                        // Link to next slide
                        ChangeSceneEf nextSlide = new ChangeSceneEf();
                        // If it's last frame, we link with the next scene, ad
                        // we add the next effects
                        if (i == anim.getFrames().size() - 1) {
                            nextSlide.setNextScene(getNextScene(cs));
                            List<EAdEffect> effects = effectsConverter
                                    .convert(cs.getEffects());
                            if (effects.size() > 0) {
                                nextSlide.addNextEffect(effects.get(0));
                            }
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
                        nextEffect = nextSlide;
                    }

                    if (f.isWaitforclick()) {
                        // Change to next slide when click
                        scene.getBackground().addBehavior(
                                MouseGEv.MOUSE_LEFT_PRESSED, nextEffect);
                    }
                    else {
                        WaitEf wait = new WaitEf((int) f.getTime());
                        wait.addNextEffect(nextEffect);
                        scene.addAddedEffect(wait);
                    }
                    i++;
                    scene.setReturnable(false);
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
