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

package es.eucm.eadventure.common.model.elements.impl.extra;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.EAdChapter;
import es.eucm.eadventure.common.model.conditions.impl.EmptyCondition;
import es.eucm.eadventure.common.model.conditions.impl.FlagCondition;
import es.eucm.eadventure.common.model.effects.impl.EAdChangeScene;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeVarValueEffect;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.EAdTransition;
import es.eucm.eadventure.common.model.elements.impl.EAdComposedScene;
import es.eucm.eadventure.common.model.elements.impl.EAdTimerImpl;
import es.eucm.eadventure.common.model.events.EAdConditionEvent.ConditionedEvent;
import es.eucm.eadventure.common.model.events.EAdEvent;
import es.eucm.eadventure.common.model.events.EAdTimerEvent.TimerEvent;
import es.eucm.eadventure.common.model.events.impl.EAdConditionEventImpl;
import es.eucm.eadventure.common.model.events.impl.EAdTimerEventImpl;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.extra.impl.EAdListImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.variables.impl.operations.BooleanOperation;
import es.eucm.eadventure.common.model.variables.impl.operations.LiteralExpressionOperation;

/**
 *
 */
@Element(detailed = EAdCutscene.class, runtime = EAdComposedScene.class)
public class EAdCutscene extends EAdComposedScene {

	EAdList<Integer> times;
	
	EAdList<EAdSlide> slides;
	
	@Param("nextScene")
	EAdScene nextScene;
	
	public EAdCutscene(String id) {
		super(id);
		times = new EAdListImpl<Integer>(Integer.class);
		slides = new EAdListImpl<EAdSlide>(EAdSlide.class);
	}
	
	
	public void addSlide(EAdSlide slide) {
		slides.add(slide);
		scenes.add(slide);
	}
	
	public void setUpForEngine(EAdChapter chapter) {
		for (int i = 0; i < slides.size() - 1; i++) {
			EAdSlide slide = slides.get(i);
			
			EAdChangeVarValueEffect e = new EAdChangeVarValueEffect("id", currentScene, new LiteralExpressionOperation("id", "[0] + 1", currentScene));
			slide.getBackground().getBehavior().addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, e);
			
			if (slide.getTime() == -1) {
				//TODO should be configuratble
				EAdChangeScene e2 = new EAdChangeScene("id", this, EAdTransition.DISPLACE);
				slide.getBackground().getBehavior().addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, e2);
			} else {
				EAdTimerImpl timer = new EAdTimerImpl("timer");
				timer.setTime(slide.getTime());
				chapter.getTimers().add(timer);
				
				EAdEvent event = new EAdConditionEventImpl("id", new FlagCondition(slide.sceneLoaded()));
				event.addEffect(ConditionedEvent.CONDITIONS_MET, new EAdChangeVarValueEffect("id", timer.timerStartedVar(), new BooleanOperation("id", EmptyCondition.TRUE_EMPTY_CONDITION)));
				events.add(event);
				
				EAdEvent event2 = new EAdTimerEventImpl("id", timer);
				event2.addEffect(TimerEvent.TIMER_ENDED, e);
				events.add(event2);
			}
		}
		//TODO should be configuratble
		EAdChangeScene e3 = new EAdChangeScene("id", nextScene, EAdTransition.DISPLACE);
		slides.get(slides.size() - 1).getBackground().getBehavior().addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, e3);
	}

	public void setNextScene(EAdScene nextScene) {
		this.nextScene = nextScene;
	}
	
	public EAdScene getNextScene() {
		return nextScene;
	}
	
}
