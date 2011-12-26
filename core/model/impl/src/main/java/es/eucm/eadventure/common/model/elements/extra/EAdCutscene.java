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

package es.eucm.eadventure.common.model.elements.extra;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.elements.EAdChapter;
import es.eucm.eadventure.common.model.elements.effects.ChangeSceneEf;
import es.eucm.eadventure.common.model.elements.effects.variables.ChangeFieldEf;
import es.eucm.eadventure.common.model.elements.extra.EAdList;
import es.eucm.eadventure.common.model.elements.guievents.EAdMouseEvent;
import es.eucm.eadventure.common.model.elements.scene.EAdScene;
import es.eucm.eadventure.common.model.elements.scenes.ComposedScene;
import es.eucm.eadventure.common.model.elements.transitions.EAdTransition;
import es.eucm.eadventure.common.model.elements.variables.EAdField;
import es.eucm.eadventure.common.model.elements.variables.FieldImpl;
import es.eucm.eadventure.common.model.elements.variables.operations.MathOp;

/**
 *
 */
@Element(detailed = EAdCutscene.class, runtime = EAdCutscene.class)
public class EAdCutscene extends ComposedScene {

	@Param("slides")
	private EAdList<EAdSlide> slides;

	@Param("nextScene")
	private EAdScene nextScene;

	public EAdCutscene() {
		super();
		slides = new EAdListImpl<EAdSlide>(EAdSlide.class);
	}

	public void addSlide(EAdSlide slide) {
		slides.add(slide);
		scenes.add(slide);
	}

	public void setUpForEngine(EAdChapter chapter) {
		EAdField<Integer> currentScene = new FieldImpl<Integer>(this,
				VAR_CURRENT_SCENE);
//		EAdField<Boolean> sceneLoaded = new EAdFieldImpl<Boolean>(this,
//				EAdSceneImpl.VAR_SCENE_LOADED);
		for (int i = 0; i < slides.size() - 1; i++) {
			EAdSlide slide = slides.get(i);

			ChangeFieldEf e = new ChangeFieldEf(
					currentScene, new MathOp(
							"[0] + 1", currentScene));
			slide.getBackground().getBehavior()
					.addBehavior(EAdMouseEvent.MOUSE_LEFT_CLICK, e);

			if (slide.getTime() == -1) {
				ChangeSceneEf e2 = new ChangeSceneEf( this,
						EAdTransition.DISPLACE);
				slide.getBackground().getBehavior()
						.addBehavior(EAdMouseEvent.MOUSE_LEFT_CLICK, e2);
			} else {
//				EAdTimerImpl timer = new EAdTimerImpl();
//				timer.setId("timer");
//				timer.setTime(slide.getTime());
////				chapter.getTimers().add(timer);
//
//				EAdEvent event = new EAdConditionEventImpl(
//						new OperationCondition(sceneLoaded, Boolean.TRUE, Comparator.EQUAL));
//				event.addEffect(ConditionedEventType.CONDITIONS_MET,
//						new EAdChangeFieldValueEffect(
//								new EAdFieldImpl<Boolean>(timer,
//										EAdTimerImpl.VAR_STARTED),
//								new BooleanOperation(
//										EmptyCondition.TRUE_EMPTY_CONDITION)));
//				events.add(event);

//				EAdEvent event2 = new EAdTimerEventImpl(timer);
//				event2.addEffect(TimerEventType.TIMER_ENDED, e);
//				events.add(event2);
			}
		}
		ChangeSceneEf e3 = new ChangeSceneEf(nextScene,
				EAdTransition.DISPLACE);
		slides.get(slides.size() - 1).getBackground().getBehavior()
				.addBehavior(EAdMouseEvent.MOUSE_LEFT_CLICK, e3);
	}

	public void setNextScene(EAdScene nextScene) {
		this.nextScene = nextScene;
	}

	public EAdScene getNextScene() {
		return nextScene;
	}

	public EAdList<EAdSlide> getSlides() {
		return slides;
	}

	
}
