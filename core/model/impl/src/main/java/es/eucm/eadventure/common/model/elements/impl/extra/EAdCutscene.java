package es.eucm.eadventure.common.model.elements.impl.extra;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.EAdChapter;
import es.eucm.eadventure.common.model.EAdElementList;
import es.eucm.eadventure.common.model.conditions.impl.EmptyCondition;
import es.eucm.eadventure.common.model.conditions.impl.FlagCondition;
import es.eucm.eadventure.common.model.effects.impl.EAdChangeScene;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeVarValueEffect;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.impl.EAdComposedScene;
import es.eucm.eadventure.common.model.elements.impl.EAdTimerImpl;
import es.eucm.eadventure.common.model.events.EAdConditionEvent.ConditionedEvent;
import es.eucm.eadventure.common.model.events.EAdEvent;
import es.eucm.eadventure.common.model.events.EAdTimerEvent.TimerEvent;
import es.eucm.eadventure.common.model.events.impl.EAdConditionEventImpl;
import es.eucm.eadventure.common.model.events.impl.EAdTimerEventImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.impl.EAdElementListImpl;
import es.eucm.eadventure.common.model.variables.impl.operations.BooleanOperation;
import es.eucm.eadventure.common.model.variables.impl.operations.LiteralExpressionOperation;

/**
 *
 */
@Element(detailed = EAdCutscene.class, runtime = EAdComposedScene.class)
public class EAdCutscene extends EAdComposedScene {

	EAdElementList<Integer> times;
	
	EAdElementList<EAdSlide> slides;
	
	@Param("nextScene")
	EAdScene nextScene;
	
	public EAdCutscene(String id) {
		super(id);
		times = new EAdElementListImpl<Integer>(Integer.class);
		slides = new EAdElementListImpl<EAdSlide>(EAdSlide.class);
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
				EAdChangeScene e2 = new EAdChangeScene("id", this);
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
		EAdChangeScene e3 = new EAdChangeScene("id", nextScene);
		slides.get(slides.size() - 1).getBackground().getBehavior().addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, e3);
	}

	public void setNextScene(EAdScene nextScene) {
		this.nextScene = nextScene;
	}
	
	public EAdScene getNextScene() {
		return nextScene;
	}
	
}
