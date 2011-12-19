package es.eucm.eadventure.common.elementfactories.demos.scenes;

import es.eucm.eadventure.common.model.effects.impl.EAdInterpolationEffect;
import es.eucm.eadventure.common.model.effects.impl.enums.InterpolationLoopType;
import es.eucm.eadventure.common.model.effects.impl.enums.InterpolationType;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeFieldValueEffect;
import es.eucm.eadventure.common.model.elements.EAdSceneElementDef;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneElementDefImpl;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent;
import es.eucm.eadventure.common.model.events.enums.SceneElementEventType;
import es.eucm.eadventure.common.model.events.impl.EAdSceneElementEventImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.model.variables.impl.operations.ValueOperation;
import es.eucm.eadventure.common.params.EAdFontImpl;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.fills.impl.EAdPaintImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl.Corner;
import es.eucm.eadventure.common.params.text.EAdString;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Image;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.CaptionImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.ImageImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.animation.Frame;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.animation.FramesAnimation;

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
		background.getResources().addAsset(background.getInitialBundle(), EAdSceneElementDefImpl.appearance, drawable);
		
		//250 x 200
		
		EAdBasicSceneElement b = getButton(optionUris);
		b.setPosition(new EAdPositionImpl(Corner.BOTTOM_LEFT, 75, 575));
		getComponents().add(b);
		
		b = getButton(option1Uris);
		b.setPosition(new EAdPositionImpl(Corner.BOTTOM_RIGHT, 725, 575));
		getComponents().add(b);

		b = getButton(option2Uris);
		b.setPosition(new EAdPositionImpl(Corner.TOP_RIGHT, 725, 150));
		getComponents().add(b);

		b = getButton(option3Uris);
		b.setPosition(new EAdPositionImpl(Corner.TOP_LEFT, 75, 150));
		getComponents().add(b);

		CaptionImpl caption = new CaptionImpl(new EAdString("Choose how to start a cough..."));
		caption.setFont(new EAdFontImpl(45.0f));
		caption.setTextPaint(new EAdPaintImpl(EAdColor.GREEN, EAdColor.WHITE));
		EAdSceneElementDef title = new EAdSceneElementDefImpl();
		title.getResources().addAsset(title.getInitialBundle(), EAdSceneElementDefImpl.appearance, caption);
		EAdBasicSceneElement titleRef = new EAdBasicSceneElement(title);
		titleRef.setPosition(20, 20);
		getComponents().add(titleRef);
		
		EAdSceneElementEvent event = new EAdSceneElementEventImpl();
		EAdInterpolationEffect effect = new EAdInterpolationEffect(
				new EAdFieldImpl<Integer>(titleRef,
						EAdBasicSceneElement.VAR_X), -50, 20, 2500,
				InterpolationLoopType.NO_LOOP, InterpolationType.BOUNCE_END);
		event.addEffect(SceneElementEventType.ADDED_TO_SCENE, effect);
		effect = new EAdInterpolationEffect(
				new EAdFieldImpl<Integer>(titleRef,
						EAdBasicSceneElement.VAR_Y), -50, 20, 2500,
				InterpolationLoopType.NO_LOOP, InterpolationType.BOUNCE_END);
		event.addEffect(SceneElementEventType.ADDED_TO_SCENE, effect);

		getBackground().getEvents().add(event);
	}
	
	private EAdBasicSceneElement getButton(String[] frames) {
		EAdSceneElementDef button = new EAdSceneElementDefImpl();
		button.getResources().addAsset(button.getInitialBundle(), EAdSceneElementDefImpl.appearance, getAnimation(frames));
		EAdBasicSceneElement buttonRef = new EAdBasicSceneElement(button);
		buttonRef.setPosition(20, 20);
		
		EAdField<Integer> scale2 = new EAdFieldImpl<Integer>(buttonRef, EAdBasicSceneElement.VAR_Z);
		EAdChangeFieldValueEffect e = new EAdChangeFieldValueEffect(scale2, new ValueOperation(1));
		
		EAdInterpolationEffect e2 = new EAdInterpolationEffect(
				new EAdFieldImpl<Float>(buttonRef,
						EAdBasicSceneElement.VAR_SCALE), 0.0f, 0.4f, 150,
				InterpolationLoopType.NO_LOOP, InterpolationType.LINEAR);

		buttonRef.addBehavior(EAdMouseEventImpl.MOUSE_ENTERED, e);
		buttonRef.addBehavior(EAdMouseEventImpl.MOUSE_ENTERED, e2);

		e = new EAdChangeFieldValueEffect(scale2, new ValueOperation(0));
		buttonRef.addBehavior(EAdMouseEventImpl.MOUSE_EXITED, e);
		EAdField<Float> scale = new EAdFieldImpl<Float>(buttonRef, EAdBasicSceneElement.VAR_SCALE);
		e = new EAdChangeFieldValueEffect(scale, new ValueOperation(1.0f));
		buttonRef.addBehavior(EAdMouseEventImpl.MOUSE_EXITED, e);

		
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
