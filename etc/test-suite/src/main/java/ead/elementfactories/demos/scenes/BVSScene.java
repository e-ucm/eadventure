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

package ead.elementfactories.demos.scenes;

import ead.common.model.elements.effects.InterpolationEf;
import ead.common.model.elements.effects.enums.InterpolationLoopType;
import ead.common.model.elements.effects.enums.InterpolationType;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.events.SceneElementEv;
import ead.common.model.elements.events.enums.SceneElementEvType;
import ead.common.model.elements.guievents.MouseGEv;
import ead.common.model.elements.scene.EAdSceneElementDef;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.common.model.elements.scenes.SceneElementImpl;
import ead.common.model.elements.variables.EAdField;
import ead.common.model.elements.variables.BasicField;
import ead.common.model.elements.variables.operations.ValueOp;
import ead.common.params.BasicFont;
import ead.common.params.fills.ColorFill;
import ead.common.params.fills.PaintFill;
import ead.common.params.text.EAdString;
import ead.common.resources.assets.drawable.basics.Caption;
import ead.common.resources.assets.drawable.basics.Image;
import ead.common.resources.assets.drawable.basics.animation.Frame;
import ead.common.resources.assets.drawable.basics.animation.FramesAnimation;
import ead.common.util.EAdPosition;
import ead.common.util.EAdPosition.Corner;

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
		
		Image drawable = new Image("@drawable/backgroundSVB.jpg");	
		background.getResources().addAsset(background.getInitialBundle(), SceneElementDef.appearance, drawable);
		
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

		Caption caption = new Caption(new EAdString("Choose how to start a cough..."));
		caption.setFont(new BasicFont(45.0f));
		caption.setTextPaint(new PaintFill(ColorFill.GREEN, ColorFill.WHITE));
		EAdSceneElementDef title = new SceneElementDef();
		title.getResources().addAsset(title.getInitialBundle(), SceneElementDef.appearance, caption);
		SceneElementImpl titleRef = new SceneElementImpl(title);
		titleRef.setPosition(20, 20);
		getComponents().add(titleRef);
		
		SceneElementEv event = new SceneElementEv();
		InterpolationEf effect = new InterpolationEf(
				new BasicField<Integer>(titleRef,
						SceneElementImpl.VAR_X), -50, 20, 2500,
				InterpolationLoopType.NO_LOOP, InterpolationType.BOUNCE_END);
		event.addEffect(SceneElementEvType.FIRST_UPDATE, effect);
		effect = new InterpolationEf(
				new BasicField<Integer>(titleRef,
						SceneElementImpl.VAR_Y), -50, 20, 2500,
				InterpolationLoopType.NO_LOOP, InterpolationType.BOUNCE_END);
		event.addEffect(SceneElementEvType.FIRST_UPDATE, effect);

		getBackground().getEvents().add(event);
	}
	
	private SceneElementImpl getButton(String[] frames) {
		EAdSceneElementDef button = new SceneElementDef();
		button.getResources().addAsset(button.getInitialBundle(), SceneElementDef.appearance, getAnimation(frames));
		SceneElementImpl buttonRef = new SceneElementImpl(button);
		buttonRef.setPosition(20, 20);
		
		EAdField<Integer> scale2 = new BasicField<Integer>(buttonRef, SceneElementImpl.VAR_Z);
		ChangeFieldEf e = new ChangeFieldEf(scale2, new ValueOp(1));
		
		InterpolationEf e2 = new InterpolationEf(
				new BasicField<Float>(buttonRef,
						SceneElementImpl.VAR_SCALE), 0.0f, 0.4f, 150,
				InterpolationLoopType.NO_LOOP, InterpolationType.LINEAR);

		buttonRef.addBehavior(MouseGEv.MOUSE_ENTERED, e);
		buttonRef.addBehavior(MouseGEv.MOUSE_ENTERED, e2);

		e = new ChangeFieldEf(scale2, new ValueOp(0));
		buttonRef.addBehavior(MouseGEv.MOUSE_EXITED, e);
		EAdField<Float> scale = new BasicField<Float>(buttonRef, SceneElementImpl.VAR_SCALE);
		e = new ChangeFieldEf(scale, new ValueOp(1.0f));
		buttonRef.addBehavior(MouseGEv.MOUSE_EXITED, e);

		
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
