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

package es.eucm.eadventure.common.model.effects.impl.text;

import es.eucm.eadventure.common.Element;
import es.eucm.eadventure.common.Param;
import es.eucm.eadventure.common.model.effects.impl.AbstractEAdEffect;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.common.resources.assets.drawable.Caption;

/**
 * <p>
 * {@link EAdEffect} to show text on the screen
 * </p>
 * 
 */
@Element(runtime = EAdShowText.class, detailed = EAdShowText.class)
public class EAdShowText extends AbstractEAdEffect {

	@Param("text")
	private EAdBasicSceneElement text;

	/**
	 * How many times text must be shown entirely. If -1, text is shown until
	 * the effect is stopped
	 */
	// TODO This could be a boolean, as the loops are calculated based on the
	// text length or the TTS system used
	@Param("loops")
	private int loops;

	/**
	 * Creates an empty an non-blocking and non-opaque <b>Show Text Effect</b>
	 * with only one loop. Text for this effect must be set with
	 * {@link EAdShowText#setText(EAdTextImpl)}
	 * 
	 * @param id
	 *            Element's id
	 */
	public EAdShowText(String id) {
		super(id);
		loops = 1;
	}

	/**
	 * Creates an empty an non-blocking and non-opaque <b>Show Text Effect</b>
	 * with only one loop. Text for this effect must be set with
	 * {@link EAdShowText#setText(EAdTextImpl)}
	 */
	public EAdShowText() {
		this("showTextEffect");
	}

	/**
	 * Sets the loops for this effect
	 * 
	 * @param loops
	 *            How many times text must be shown entirely. If -1, text is
	 *            shown until the effect is stopped
	 */
	public void setLoops(int loops) {
		this.loops = loops;
	}

	/**
	 * @return How many times text must be shown entirely. If -1, text is shown
	 *         until the effect is stopped
	 */
	public int getLoops() {
		return loops;
	}

	public void setText(EAdBasicSceneElement text) {
		this.text = text;
	}

	public EAdBasicSceneElement getText() {
		return text;
	}

	/**
	 * Helper method
	 * 
	 * @param caption
	 */
	public void setCaption(Caption caption) {
		text = new EAdBasicSceneElement(this.id + "_caption");
		text.getResources().addAsset(text.getInitialBundle(),
				EAdBasicSceneElement.appearance, caption);
		text.setPosition(new EAdPosition(300, 300));
	}

}
