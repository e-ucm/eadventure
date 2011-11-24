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

package es.eucm.eadventure.common.model.effects.impl.timedevents;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.impl.AbstractEAdEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdInterpolationEffect;
import es.eucm.eadventure.common.model.effects.impl.enums.InterpolationLoopType;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.events.enums.SceneElementEventType;
import es.eucm.eadventure.common.model.events.impl.EAdSceneElementEventImpl;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Caption;

/**
 * <p>
 * {@link EAdEffect} to show text on the screen
 * </p>
 * 
 */
@Element(runtime = EAdShowSceneElement.class, detailed = EAdShowSceneElement.class)
public class EAdShowSceneElement extends AbstractEAdEffect {

	public enum ShowTextAnimation {
		NONE, FADE_IN;
	}

	@Param("time")
	private int time;

	@Param("sceneElement")
	private EAdSceneElement sceneElement;

	public EAdShowSceneElement() {
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
			text.setVarInitialValue(EAdBasicSceneElement.VAR_ALPHA, 0.0f);
			EAdInterpolationEffect effect = new EAdInterpolationEffect(
					new EAdFieldImpl<Float>(text,
							EAdBasicSceneElement.VAR_ALPHA), 0.0f, 1.0f, 500,
					InterpolationLoopType.NO_LOOP);

			EAdSceneElementEventImpl event = new EAdSceneElementEventImpl();
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
		EAdBasicSceneElement text = new EAdBasicSceneElement();
		text.setId(this.id + "_caption");
		text.getResources().addAsset(text.getInitialBundle(),
				EAdBasicSceneElement.appearance, caption);
		text.setPosition(new EAdPositionImpl(x, y));
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
