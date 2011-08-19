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

package es.eucm.eadventure.common.elmentfactories.scenedemos;

import java.util.ArrayList;

import es.eucm.eadventure.common.interfaces.features.Oriented.Orientation;
import es.eucm.eadventure.common.model.actions.impl.EAdBasicAction;
import es.eucm.eadventure.common.model.behavior.EAdBehavior;
import es.eucm.eadventure.common.model.behaviors.impl.EAdBehaviorImpl;
import es.eucm.eadventure.common.model.conditions.impl.EmptyCondition;
import es.eucm.eadventure.common.model.conditions.impl.FlagCondition;
import es.eucm.eadventure.common.model.conditions.impl.ORCondition;
import es.eucm.eadventure.common.model.conditions.impl.VarCondition;
import es.eucm.eadventure.common.model.conditions.impl.VarCondition.Operator;
import es.eucm.eadventure.common.model.conditions.impl.VarValCondition;
import es.eucm.eadventure.common.model.effects.impl.EAdActorActionsEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdChangeAppearance;
import es.eucm.eadventure.common.model.effects.impl.EAdChangeScene;
import es.eucm.eadventure.common.model.effects.impl.sceneelements.EAdMoveSceneElement;
import es.eucm.eadventure.common.model.effects.impl.sceneelements.EAdMoveSceneElement.MovementSpeed;
import es.eucm.eadventure.common.model.effects.impl.text.EAdShowQuestion;
import es.eucm.eadventure.common.model.effects.impl.text.extra.Answer;
import es.eucm.eadventure.common.model.effects.impl.timedevents.EAdShowSceneElement;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeVarValueEffect;
import es.eucm.eadventure.common.model.elements.EAdChapter;
import es.eucm.eadventure.common.model.elements.EAdTimer;
import es.eucm.eadventure.common.model.elements.impl.EAdActorReferenceImpl;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicActor;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneImpl;
import es.eucm.eadventure.common.model.elements.impl.EAdTimerImpl;
import es.eucm.eadventure.common.model.elements.impl.extra.EAdButton;
import es.eucm.eadventure.common.model.elements.impl.extra.EAdCutscene;
import es.eucm.eadventure.common.model.elements.impl.extra.EAdSlide;
import es.eucm.eadventure.common.model.events.EAdConditionEvent;
import es.eucm.eadventure.common.model.events.EAdConditionEvent.ConditionedEvent;
import es.eucm.eadventure.common.model.events.EAdTimerEvent;
import es.eucm.eadventure.common.model.events.EAdTimerEvent.TimerEvent;
import es.eucm.eadventure.common.model.events.impl.EAdConditionEventImpl;
import es.eucm.eadventure.common.model.events.impl.EAdTimerEventImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.impl.EAdChapterImpl;
import es.eucm.eadventure.common.model.transitions.EAdTransition;
import es.eucm.eadventure.common.model.variables.impl.extra.EAdSceneElementVars;
import es.eucm.eadventure.common.model.variables.impl.operations.BooleanOperation;
import es.eucm.eadventure.common.model.variables.impl.operations.LiteralExpressionOperation;
import es.eucm.eadventure.common.model.variables.impl.vars.FloatVar;
import es.eucm.eadventure.common.params.fills.impl.EAdBorderedColor;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl.Corner;
import es.eucm.eadventure.common.resources.EAdBundleId;
import es.eucm.eadventure.common.resources.EAdString;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.CaptionImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.ImageImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.animation.Frame;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.animation.FramesAnimation;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.BezierShape;
import es.eucm.eadventure.common.resources.assets.drawable.compounds.impl.OrientedDrawableImpl;

public class BasicScene extends EAdSceneImpl implements SceneDemo {

	private EAdShowQuestion showQuestionEffect;

	//private EAdShowText showTextEffect;

	public EAdBasicActor buttonActor, panielActor, orientedActor;

	//private EAdMoveSceneElement move1, move2, move3, move4;

	private EAdChangeScene changeScreenEffect;

	private EAdActorReferenceImpl buttonReference, panielReference,
			orientedReference;

//	private EAdHighlightActorReference hle;
//
//	private EAdWaitEffect wait;

	public ArrayList<EAdTimer> timers;
	
	public EAdChapter chapter;
	
	public BasicScene( ){
		this( "BasicScene", new EAdChapterImpl("chapter1") );
	}
	
	public BasicScene(String id, EAdChapter chapter) {
		super(id);
		this.chapter = chapter;
		this.returnable = true;
		timers = new ArrayList<EAdTimer>();

		getBackground().getResources().addAsset(getBackground().getInitialBundle(), EAdBasicSceneElement.appearance, new ImageImpl("@drawable/Loading.png"));
		
		initPanielActor();
//		getActorReferences().add(panielReference);

		initButtonActor();

		initOrientedActor();

		initChangeScreen();

		initShowQuestion();

		this.panielReference.addBehavior(EAdMouseEventImpl.MOUSE_RIGHT_CLICK, showQuestionEffect);

		EAdTimer timer = new EAdTimerImpl("timer");
		((EAdTimerImpl) timer).setTime(new Integer(3000));
		timers.add(timer);
		
		BezierShape is = new BezierShape(40, 40);
		is.setFill(EAdBorderedColor.BLACK_ON_WHITE);
		is.lineTo(new EAdPositionImpl(200, 40));
		is.lineTo(new EAdPositionImpl(40, 200));
		EAdBasicSceneElement bse = new EAdBasicSceneElement("ide");
		bse.getResources().addAsset(bse.getInitialBundle(), EAdBasicSceneElement.appearance, is);

		is = new BezierShape(40, 40);
		is.setFill(EAdBorderedColor.WHITE_ON_BLACK);
		is.lineTo(new EAdPositionImpl(200, 40));
		is.lineTo(new EAdPositionImpl(40, 200));
		EAdBundleId bid = new EAdBundleId("other");
		bse.getResources().addAsset(bid, EAdBasicSceneElement.appearance, is);
		
		bse.addBehavior(EAdMouseEventImpl.MOUSE_ENTERED, new EAdChangeAppearance("id", bse, bid));
		bse.addBehavior(EAdMouseEventImpl.MOUSE_EXITED, new EAdChangeAppearance("id", bse, bse.getInitialBundle()));
		bse.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, new EAdChangeVarValueEffect("id", timer.timerStartedVar(), new BooleanOperation("id", EmptyCondition.TRUE_EMPTY_CONDITION)));
		//this.getSceneElements().add(bse);
		
		EAdTimerEvent timerEvent = new EAdTimerEventImpl("id", timer);
		bse.getEvents().add(timerEvent);
		timerEvent.addEffect(TimerEvent.TIMER_ENDED, 
				new EAdMoveSceneElement("id"));
//				new EAdChangeVarValueEffect("var", panielReference.getVars().getVar(EAdSceneElementVars.VAR_X), new LiteralExpressionOperation("id", "10")));
		
		
		EAdCutscene cutscene = new EAdCutscene("id");
//		cutscene.getBackground().getResources().addAsset(cutscene.getInitialBundle(), EAdBasicSceneElement.appearance, new ImageImpl("@drawable/slide1.png"));
		
		EAdSlide slide = new EAdSlide("id");
		slide.setTime(3000);
		slide.getBackground().getResources().addAsset(slide.getBackground().getInitialBundle(), EAdBasicSceneElement.appearance, new ImageImpl("@drawable/Slide1.png"));
		cutscene.addSlide(slide);

		EAdSlide slide2 = new EAdSlide("id");
		slide2.getBackground().getResources().addAsset(slide2.getBackground().getInitialBundle(), EAdBasicSceneElement.appearance, new ImageImpl("@drawable/slide2.png"));
		cutscene.addSlide(slide2);

		cutscene.setUpForEngine(chapter);
		
		this.getBackground().addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, new EAdChangeScene("id", cutscene, EAdTransition.DISPLACE));
		
		initBall();
		/*
		initShowText();
		initMoveActor();
		initReferences();
		initOtherEffects();

		getActorReferences().add(buttonReference);
		getActorReferences().add(orientedReference);
		
		EAdBehavior behavior = this.panielActor.getBehavior( );
		EAdHighlightActorReference highLight = new EAdHighlightActorReference( behavior, "highlight");
		highLight.setTime(1000);
		highLight.setActorReference(panielReference);
		behavior.addBehavior(EAdMouseEventImpl.MOUSE_ENTERED, highLight);
		*/
	}


//	private void initOtherEffects() {
//		hle = new EAdHighlightActorReference("hightLight");
//		hle.setTime(2000);
//		hle.setBlocking(false);
//		hle.setOpaque(false);
//
//		wait = new EAdWaitEffect("wait");
//		wait.setTime(2000);
//
//	}

	private void initShowQuestion() {
		showQuestionEffect = new EAdShowQuestion("question");
		CaptionImpl questionText = new CaptionImpl();
		questionText.setBubbleColor(EAdBorderedColor.WHITE_ON_BLACK );
		questionText.setText(new EAdString("question"));
		questionText.setTextColor(EAdBorderedColor.BLACK_ON_WHITE);
		EAdBasicSceneElement element = new EAdBasicSceneElement("question_element");
		element.getResources().addAsset(element.getInitialBundle(), EAdBasicSceneElement.appearance, questionText);
		showQuestionEffect.setQuestion(element);

		CaptionImpl captionImpl = new CaptionImpl();
		captionImpl.setText(new EAdString("answer1"));
		Answer answer = new Answer(showQuestionEffect.getId() + "_answer1");
		answer.getResources().addAsset(answer.getInitialBundle(), Answer.appearance, captionImpl);

		EAdChangeVarValueEffect changePos = new EAdChangeVarValueEffect("id");
		changePos.addVar(this.panielReference.getVars().getVar(EAdSceneElementVars.VAR_X));
		changePos.setOperation(new LiteralExpressionOperation("id", "[0] + 200", panielReference.getVars().getVar(EAdSceneElementVars.VAR_X)));
		answer.getMacro().getEffects().add(changePos);
		showQuestionEffect.getAnswers().add(answer);
		
		captionImpl = new CaptionImpl();
		captionImpl.setText(new EAdString("answer2"));
		answer = new Answer(showQuestionEffect.getId() + "_answer2");
		answer.getResources().addAsset(answer.getInitialBundle(), Answer.appearance, captionImpl);
		answer.getMacro().getEffects().add(changeScreenEffect);
		showQuestionEffect.getAnswers().add(answer);

		captionImpl = new CaptionImpl();
		captionImpl.setText(new EAdString("answer3"));
		answer = new Answer(showQuestionEffect.getId() + "_answer3");
		answer.getResources().addAsset(answer.getInitialBundle(), Answer.appearance, captionImpl);
		answer.getMacro().getEffects().add(changeScreenEffect);
		showQuestionEffect.getAnswers().add(answer);
		
		showQuestionEffect.setUpNewInstance();

	}
	
	private void initBall() {
		EAdButton button = new EAdButton("ball_button");
		button.setText(new CaptionImpl(new EAdString("start_ball")));
		button.setUpNewInstance();
		button.setPosition(new EAdPositionImpl(Corner.CENTER, 400, 400));
		//getSceneElements().add(button);

		EAdBasicSceneElement ball = new EAdBasicSceneElement("ball");
		ball.getResources().addAsset(ball.getInitialBundle(), EAdBasicSceneElement.appearance, new ImageImpl("@drawable/soccer-ball.png"));
		ball.setPosition(new EAdPositionImpl(Corner.CENTER, 300, 300));
		ball.setScale(0.4f);
		//getSceneElements().add(ball);
	
		FloatVar dirX = new FloatVar("ball_dirX");
		FloatVar dirY = new FloatVar("ball_dirY");

		EAdChangeVarValueEffect setVar = new EAdChangeVarValueEffect("i", dirX, new LiteralExpressionOperation("1", "1"));
		button.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, setVar);
		setVar = new EAdChangeVarValueEffect("i", dirY, new LiteralExpressionOperation("1", "1"));
		button.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, setVar);
		
		LiteralExpressionOperation leX = new LiteralExpressionOperation("id", "[0] + (abs(400*(1+[1]) - [0]*[1]) min abs(300*(1+[2]) - [3]*[2]))*[1] - 50*[1]", ball.getVars().getVar(EAdSceneElementVars.VAR_X), dirX, dirY, ball.getVars().getVar(EAdSceneElementVars.VAR_Y));
		LiteralExpressionOperation leY = new LiteralExpressionOperation("id", "[3] + (abs(400*(1+[1]) - [0]*[1]) min abs(300*(1+[2]) - [3]*[2]))*[2] - 50*[2]", ball.getVars().getVar(EAdSceneElementVars.VAR_X), dirX, dirY, ball.getVars().getVar(EAdSceneElementVars.VAR_Y));
		EAdMoveSceneElement move = new EAdMoveSceneElement(id, ball, leX, leY, MovementSpeed.FAST);
		button.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, move);
		
		EAdConditionEvent ce = new EAdConditionEventImpl("ev");
		ce.setCondition(new FlagCondition(move.animationEnded()));
		
		setVar = new EAdChangeVarValueEffect("i", dirX, new LiteralExpressionOperation("1", "0-[0]", dirX));
		setVar.setCondition(new ORCondition(new VarValCondition(ball.getVars().getVar(EAdSceneElementVars.VAR_X), 51, Operator.LESS_EQUAL), new VarValCondition(ball.getVars().getVar(EAdSceneElementVars.VAR_X), 749, Operator.GREATER_EQUAL)));
		ce.addEffect(ConditionedEvent.CONDITIONS_MET, setVar);
		setVar = new EAdChangeVarValueEffect("i", dirY, new LiteralExpressionOperation("1", "0-[0]", dirY));
		setVar.setCondition(new ORCondition(new VarValCondition(ball.getVars().getVar(EAdSceneElementVars.VAR_Y), 51, Operator.LESS_EQUAL), new VarValCondition(ball.getVars().getVar(EAdSceneElementVars.VAR_Y), 549, Operator.GREATER_EQUAL)));
		ce.addEffect(ConditionedEvent.CONDITIONS_MET, setVar);

		ce.addEffect(ConditionedEvent.CONDITIONS_MET, move);


		ball.getEvents().add(ce);
		
		
	}

	private void initButtonActor() {
		buttonActor = new EAdBasicActor("StartGame");
		buttonActor.getResources().addAsset(buttonActor.getInitialBundle(),
				EAdBasicActor.appearance, new ImageImpl("@drawable/start.png"));

		EAdBundleId id = new EAdBundleId("bundle");
		buttonActor.getResources().addBundle(id);
		buttonActor.getResources().addAsset(id, EAdBasicActor.appearance, new ImageImpl("@drawable/grab.png"));
		EAdConditionEvent event = new EAdConditionEventImpl("conditionEvent");
		event.setCondition(new VarValCondition(new FloatVar("f"), 1.0f, VarCondition.Operator.GREATER_EQUAL));
		EAdChangeAppearance app = new EAdChangeAppearance("cha");
		app.setElement(buttonActor);
		app.setBundleId(id);
		
		event.addEffect(EAdConditionEvent.ConditionedEvent.CONDITIONS_MET, app);
		buttonActor.getEvents().add(event);
		
		EAdString name = new EAdString("stringName");
		buttonActor.setName(name);
		
		buttonReference = new EAdActorReferenceImpl("id4", buttonActor);
		buttonReference.setPosition(new EAdPositionImpl(
				EAdPositionImpl.Corner.BOTTOM_CENTER, 200, 200));
		buttonReference.setScale(0.8f);
		
		//this.getSceneElements().add(buttonReference);
	}

	private void initPanielActor() {
		FramesAnimation animation = new FramesAnimation();

		for (int i = 1; i <= 8; i++)
			animation.addFrame(new Frame("@drawable/paniel_wlr_0"
					+ i + ".png"));

		panielActor = new EAdBasicActor("Paniel");
		panielActor.getResources().addAsset(panielActor.getInitialBundle(),
				EAdBasicActor.appearance, animation);
/*
		RectangleShape shape = new RectangleShape();
		shape.setColor(EAdColor.BLACK);
		shape.setHeight(200);
		shape.setWidth(200);
		panielActor.getResources().addAsset(panielActor.getInitialBundle(),
				EAdBasicActor.appearance, shape);
*/
		panielReference = new EAdActorReferenceImpl("id5", panielActor);
		panielReference.setPosition(new EAdPositionImpl(
				EAdPositionImpl.Corner.BOTTOM_CENTER, 400, 300));
		panielReference.setScale(0.5f);


		EAdString name = new EAdString("panielName");
		panielActor.setName(name);

		EAdString description = new EAdString("panielDescription");
		panielActor.setDescription(description);
		// Actions

		EAdBasicAction action = new EAdBasicAction("id2");
		action.getResources().addAsset(EAdBasicAction.appearance,
				new ImageImpl("@drawable/grab.png"));

		EAdString hand = new EAdString("handAction");
		action.setName(hand);
		
		EAdShowSceneElement actionEffect = new EAdShowSceneElement("effectAction");
		EAdBasicSceneElement actionText = new EAdBasicSceneElement("rr");
		
		actionText.setPosition(new EAdPositionImpl(100, 100));
		CaptionImpl caption = new CaptionImpl();
		caption.setBubbleColor(null);
		caption.setText(hand);
		caption.setTextColor(new EAdBorderedColor(new EAdColor(120, 20, 20),
				new EAdColor(34, 50, 60)));
		
		actionText.getResources().addAsset(actionText.getInitialBundle(), EAdBasicSceneElement.appearance, caption);
		actionEffect.setSceneElement(actionText);

		action.getEffects().add(actionEffect);

		panielActor.getActions().add(action);

		// Behavior
		EAdBehavior behavior = new EAdBehaviorImpl(panielReference.getId() + "_behavior");
//		addEffects(behavior);
		
		EAdChangeVarValueEffect changeVar = new EAdChangeVarValueEffect( "changevar");
		changeVar.addVar(new FloatVar("f"));
		LiteralExpressionOperation leo = new LiteralExpressionOperation( "idop", "[0] + 1", new FloatVar("f"));
		changeVar.setOperation(leo);
		
		behavior.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, changeVar);

		EAdActorActionsEffect showActions = new EAdActorActionsEffect( panielReference.getId()+ "_showActions", panielReference);
		behavior.addBehavior(EAdMouseEventImpl.MOUSE_DOUBLE_CLICK, showActions);

		panielReference.setBehavior(behavior);
		
		this.getSceneElements().add(panielReference);
	}

	private void initOrientedActor() {
		OrientedDrawableImpl anim = new OrientedDrawableImpl();
		anim.setDrawable(Orientation.N, new ImageImpl(
				"@drawable/going_up.jpg"));
		anim.setDrawable(Orientation.S, new ImageImpl(
				"@drawable/going_down.png"));
		anim.setDrawable(Orientation.W, new ImageImpl(
				"@drawable/going_left.png"));
		anim.setDrawable(Orientation.E, new ImageImpl(
				"@drawable/going_right.jpg"));
		orientedActor = new EAdBasicActor("Oriented");
		orientedActor.getResources().addAsset(orientedActor.getInitialBundle(),
				EAdBasicActor.appearance, anim);
		
		orientedActor.setName(new EAdString("orientedName"));
		
		orientedReference = new EAdActorReferenceImpl("oriented_ref",
				orientedActor);
		orientedReference.setPosition(new EAdPositionImpl(
				EAdPositionImpl.Corner.BOTTOM_CENTER, 50, 50));
		orientedReference.setInitialOrientation(Orientation.N);
		orientedReference.setScale(1.0f);
		
		//this.getSceneElements().add(orientedReference);

	}

	//private void initShowText() {
		/*
		showTextEffect = new EAdShowText(null, "id3");
		EAdString string = new EAdString("stringId");
		EAdTextImpl textImpl = new EAdTextImpl(showTextEffect, "idText");

		CaptionImpl caption = new CaptionImpl();

		
		caption.setMaximumHeight(200);
		caption.setMaximumWidth(400);
		caption.setMinimumHeight(200);
		caption.setMinimumWidth(100);
		showTextEffect.setText(textImpl);
		
		textImpl.setText(string);

		textImpl.setFont(EAdFont.REGULAR);
		textImpl.setTextColor(new EAdBorderedColor(EAdColor.BLACK,
				new EAdColor(80, 80, 80)));
		textImpl.setPosition(new EAdPosition(30, 50));
		*/
	//}

//	private void initMoveActor() {
//		move1 = new EAdMoveSceneElement("move");
//		
//		move1.setTargetCoordiantes(new LiteralExpressionOperation("id", "50"), new LiteralExpressionOperation("id", "400"));
//
//		move2 = new EAdMoveSceneElement("move2");
//		move2.setTargetCoordiantes(new LiteralExpressionOperation("id", "400"), new LiteralExpressionOperation("id", "400"));
//
//		move3 = new EAdMoveSceneElement("move3");
//
//		move3.setTargetCoordiantes(new LiteralExpressionOperation("id", "50"), new LiteralExpressionOperation("id", "50"));
//
//		move4 = new EAdMoveSceneElement("move4");
//		move4.setTargetCoordiantes(new LiteralExpressionOperation("id", "500"), new LiteralExpressionOperation("id", "500"));
//		move4.setSpeed(MovementSpeed.SLOW);
//		move4.setBlocking(false);
//		move4.setOpaque(false);
//
//	}

	private void initChangeScreen() {

		EAdSceneImpl space2 = new EAdSceneImpl("LoadingScreen2");
		space2.getBackground().getResources().addAsset(space2.getBackground().getInitialBundle(),
				EAdBasicSceneElement.appearance, new ImageImpl("@drawable/Creditos.jpg"));

		changeScreenEffect = new EAdChangeScene("changscree");
		changeScreenEffect.setNextScene(space2);
	}
	
	@Override
	public String getDescription() {
		return "A scene with basics elements";
	}
	
	public String getDemoName(){
		return "Basic Scene";
	}

}
