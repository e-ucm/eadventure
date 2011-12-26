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

package es.eucm.eadventure.common.model.elements.effects.timedevents;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.elements.EAdEffect;
import es.eucm.eadventure.common.model.elements.effects.AbstractEffect;
import es.eucm.eadventure.common.model.elements.effects.InterpolationEf;
import es.eucm.eadventure.common.model.elements.effects.enums.InterpolationLoopType;
import es.eucm.eadventure.common.model.elements.effects.enums.ShowTextAnimation;
import es.eucm.eadventure.common.model.elements.events.SceneElementEv;
import es.eucm.eadventure.common.model.elements.events.enums.SceneElementEventType;
import es.eucm.eadventure.common.model.elements.scene.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.scenes.SceneElementImpl;
import es.eucm.eadventure.common.model.elements.variables.FieldImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Caption;
import es.eucm.eadventure.common.util.EAdPosition;

/**
 * <p>
 * {@link EAdEffect} to show text on the screen
 * </p>
 * 
 */
@Element(runtime = ShowSceneElementEf.class, detailed = ShowSceneElementEf.class)
public class ShowSceneElementEf extends AbstractEffect {

	@Param("time")
	private int time;

	@Param("sceneElement")
	private EAdSceneElement sceneElement;

	public ShowSceneElementEf() {
		super();
		setId("showTextEffect");
	}

	public void setSceneElement(EAdSceneElement text) {
		setSceneElement(text, ShowTextAnimation.NONE);
	}

	public void setSceneElement(EAdSceneElement text,
			ShowTextAnimation animation) {
		this.sceneElement = text;
		switch (animation) {
		case FADE_IN:
			text.setVarInitialValue(SceneElementImpl.VAR_ALPHA, 0.0f);
			InterpolationEf effect = new InterpolationEf(
					new FieldImpl<Float>(text,
							SceneElementImpl.VAR_ALPHA), 0.0f, 1.0f, 500,
					InterpolationLoopType.NO_LOOP);

			SceneElementEv event = new SceneElementEv();
			event.addEffect(
					SceneElementEventType.ADDED_TO_SCENE,
					effect);

			text.getEvents().add(event);
			break;
		}
	}

	public EAdSceneElement getSceneElement() {
		return sceneElement;
	}

	/**
	 * Helper method
	 * 
	 * @param caption
	 */
	public void setCaption(Caption caption, int x, int y,
			ShowTextAnimation animation) {
		SceneElementImpl text = new SceneElementImpl(caption);
		text.setId(this.id + "_caption");
		text.setPosition(new EAdPosition(x, y));
		setSceneElement(text, animation);
	}

	public void setCaption(Caption caption, int x, int y) {
		this.setCaption(caption, x, y, ShowTextAnimation.NONE);
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

}
