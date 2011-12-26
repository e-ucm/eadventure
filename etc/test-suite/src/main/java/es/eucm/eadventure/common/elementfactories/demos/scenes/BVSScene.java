package es.eucm.eadventure.common.elementfactories.demos.scenes;

import es.eucm.eadventure.common.model.elements.effects.InterpolationEf;
import es.eucm.eadventure.common.model.elements.effects.enums.InterpolationLoopType;
import es.eucm.eadventure.common.model.elements.effects.enums.InterpolationType;
import es.eucm.eadventure.common.model.elements.effects.variables.ChangeFieldEf;
import es.eucm.eadventure.common.model.elements.events.SceneElementEv;
import es.eucm.eadventure.common.model.elements.events.enums.SceneElementEventType;
import es.eucm.eadventure.common.model.elements.guievents.EAdMouseEvent;
import es.eucm.eadventure.common.model.elements.scene.EAdSceneElementDef;
import es.eucm.eadventure.common.model.elements.scenes.SceneElementDefImpl;
import es.eucm.eadventure.common.model.elements.scenes.SceneElementImpl;
import es.eucm.eadventure.common.model.elements.variables.EAdField;
import es.eucm.eadventure.common.model.elements.variables.FieldImpl;
import es.eucm.eadventure.common.model.elements.variables.operations.ValueOp;
import es.eucm.eadventure.common.params.EAdFontImpl;
import es.eucm.eadventure.common.params.fills.EAdColor;
import es.eucm.eadventure.common.params.fills.EAdPaintImpl;
import es.eucm.eadventure.common.params.text.EAdString;
import es.eucm.eadventure.common.resources.assets.drawable.basics.CaptionImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Image;
import es.eucm.eadventure.common.resources.assets.drawable.basics.ImageImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.animation.Frame;
import es.eucm.eadventure.common.resources.assets.drawable.basics.animation.FramesAnimation;
import es.eucm.eadventure.common.util.EAdPosition;
import es.eucm.eadventure.common.util.EAdPosition.Corner;

public class BVSScene extends EmptyScene {

	private static String optionUris[] = new String[] {
		"@drawable/tos correcta 1_01.jpg", "@drawable/tos correcta 1_02.jpg"
	};

	private static String option1Uris[] = new String[] {
		"@drawable/tos incorrecta 1_01.jpg", "@drawable/tos incorrecta 1_02.jpg"
	};
	
	private static String option2Uris[] = new String[] {
		"@drawable/tos incorrecta 2_01.jpg", "@drawable/tos incorrecta 2_02.jpg"
	};

	private static String option3Uris[] = new String[] {
		"@drawable/tos incorrecta 3_01.jpg", "@drawable/tos incorrecta 3_02.jpg"
	};
	
	public BVSScene() {
		//NgCommon.init();
		EAdSceneElementDef background = getBackground().getDefinition();
		
		Image drawable = new ImageImpl("@drawable/backgroundSVB.jpg");	
		background.getResources().addAsset(background.getInitialBundle(), SceneElementDefImpl.appearance, drawable);
		
		//250 x 200
		
		SceneElementImpl b = getButton(optionUris);
		b.setPosition(new EAdPosition(Corner.BOTTOM_LEFT, 75, 575));
		getComponents().add(b);
		
		b = getButton(option1Uris);
		b.setPosition(new EAdPosition(Corner.BOTTOM_RIGHT, 725, 575));
		getComponents().add(b);

		b = getButton(option2Uris);
		b.setPosition(new EAdPosition(Corner.TOP_RIGHT, 725, 150));
		getComponents().add(b);

		b = getButton(option3Uris);
		b.setPosition(new EAdPosition(Corner.TOP_LEFT, 75, 150));
		getComponents().add(b);

		CaptionImpl caption = new CaptionImpl(new EAdString("Choose how to start a cough..."));
		caption.setFont(new EAdFontImpl(45.0f));
		caption.setTextPaint(new EAdPaintImpl(EAdColor.GREEN, EAdColor.WHITE));
		EAdSceneElementDef title = new SceneElementDefImpl();
		title.getResources().addAsset(title.getInitialBundle(), SceneElementDefImpl.appearance, caption);
		SceneElementImpl titleRef = new SceneElementImpl(title);
		titleRef.setPosition(20, 20);
		getComponents().add(titleRef);
		
		SceneElementEv event = new SceneElementEv();
		InterpolationEf effect = new InterpolationEf(
				new FieldImpl<Integer>(titleRef,
						SceneElementImpl.VAR_X), -50, 20, 2500,
				InterpolationLoopType.NO_LOOP, InterpolationType.BOUNCE_END);
		event.addEffect(SceneElementEventType.ADDED_TO_SCENE, effect);
		effect = new InterpolationEf(
				new FieldImpl<Integer>(titleRef,
						SceneElementImpl.VAR_Y), -50, 20, 2500,
				InterpolationLoopType.NO_LOOP, InterpolationType.BOUNCE_END);
		event.addEffect(SceneElementEventType.ADDED_TO_SCENE, effect);

		getBackground().getEvents().add(event);
	}
	
	private SceneElementImpl getButton(String[] frames) {
		EAdSceneElementDef button = new SceneElementDefImpl();
		button.getResources().addAsset(button.getInitialBundle(), SceneElementDefImpl.appearance, getAnimation(frames));
		SceneElementImpl buttonRef = new SceneElementImpl(button);
		buttonRef.setPosition(20, 20);
		
		EAdField<Integer> scale2 = new FieldImpl<Integer>(buttonRef, SceneElementImpl.VAR_Z);
		ChangeFieldEf e = new ChangeFieldEf(scale2, new ValueOp(1));
		
		InterpolationEf e2 = new InterpolationEf(
				new FieldImpl<Float>(buttonRef,
						SceneElementImpl.VAR_SCALE), 0.0f, 0.4f, 150,
				InterpolationLoopType.NO_LOOP, InterpolationType.LINEAR);

		buttonRef.addBehavior(EAdMouseEvent.MOUSE_ENTERED, e);
		buttonRef.addBehavior(EAdMouseEvent.MOUSE_ENTERED, e2);

		e = new ChangeFieldEf(scale2, new ValueOp(0));
		buttonRef.addBehavior(EAdMouseEvent.MOUSE_EXITED, e);
		EAdField<Float> scale = new FieldImpl<Float>(buttonRef, SceneElementImpl.VAR_SCALE);
		e = new ChangeFieldEf(scale, new ValueOp(1.0f));
		buttonRef.addBehavior(EAdMouseEvent.MOUSE_EXITED, e);

		
		return buttonRef;
	}
	
	private FramesAnimation getAnimation(String[] frames) {
		FramesAnimation fa = new FramesAnimation();
		for (String s : frames) {
			Frame f = new Frame(s);
			f.setTime(500);
			fa.addFrame(f);
		}
		return fa;
	}
	
	@Override
	public String getSceneDescription() {
		return "A scene with a character, with orientation and different states. Press the buttons to control the character.";
	}

	public String getDemoName() {
		return "Basic vital support Scene";
	}

	

	
}
