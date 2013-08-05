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

package es.eucm.ead.model.elements.effects.timedevents;

import es.eucm.ead.model.interfaces.Element;
import es.eucm.ead.model.interfaces.Param;
import es.eucm.ead.model.assets.drawable.basics.EAdCaption;
import es.eucm.ead.model.elements.EAdEffect;
import es.eucm.ead.model.elements.effects.AbstractEffect;
import es.eucm.ead.model.elements.effects.InterpolationEf;
import es.eucm.ead.model.elements.effects.enums.InterpolationLoopType;
import es.eucm.ead.model.elements.effects.enums.ShowTextAnimation;
import es.eucm.ead.model.elements.events.SceneElementEv;
import es.eucm.ead.model.elements.events.enums.SceneElementEvType;
import es.eucm.ead.model.elements.operations.BasicField;
import es.eucm.ead.model.elements.scenes.EAdSceneElement;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.params.util.Position;

/**
 * <p>
 * {@link EAdEffect} to show text on the screen
 * </p>
 * 
 */
@Element
public class ShowSceneElementEf extends AbstractEffect {

	@Param
	private int time;

	@Param
	private EAdSceneElement sceneElement;

	public ShowSceneElementEf() {
		super();
	}

	public void setSceneElement(EAdSceneElement text) {
		setSceneElement(text, ShowTextAnimation.NONE);
	}

	public void setSceneElement(EAdSceneElement text,
			ShowTextAnimation animation) {
		this.sceneElement = text;
		switch (animation) {
		case FADE_IN:
			text.setVarInitialValue(SceneElement.VAR_ALPHA, 0.0f);
			InterpolationEf effect = new InterpolationEf(new BasicField<Float>(
					text, SceneElement.VAR_ALPHA), 0.0f, 1.0f, 500,
					InterpolationLoopType.NO_LOOP);

			SceneElementEv event = new SceneElementEv();
			event.addEffect(SceneElementEvType.INIT, effect);

			text.getEvents().add(event);
			break;
		default:
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
	public void setCaption(EAdCaption caption, int x, int y,
			ShowTextAnimation animation) {
		SceneElement text = new SceneElement(caption);
		text.setPosition(new Position(x, y));
		setSceneElement(text, animation);
	}

	public void setCaption(EAdCaption caption, int x, int y) {
		this.setCaption(caption, x, y, ShowTextAnimation.NONE);
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

}
