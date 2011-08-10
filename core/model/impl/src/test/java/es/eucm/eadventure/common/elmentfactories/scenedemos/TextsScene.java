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

import es.eucm.eadventure.common.elmentfactories.EAdElementsFactory;
import es.eucm.eadventure.common.elmentfactories.StringFactory.StringType;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.impl.text.EAdShowText.ShowTextAnimation;
import es.eucm.eadventure.common.params.EAdFontImpl;
import es.eucm.eadventure.common.params.fills.impl.EAdBorderedColor;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.CaptionImpl;
import es.eucm.eadventure.common.resources.assets.impl.EAdURIImpl;

public class TextsScene extends EmptyScene {
	
	public TextsScene( ){
		this.setBackgroundFill(EAdColor.DARK_GRAY);
		
		// Show text caption
		CaptionImpl caption = EAdElementsFactory.getInstance().getCaptionFactory().createCaption("Show text Á ÑÑÑ !!!! *Á", EAdBorderedColor.WHITE_ON_BLACK, EAdBorderedColor.BLACK_ON_WHITE, new EAdFontImpl( new EAdURIImpl( "@binary/DroidSans-Bold.ttf"), 20));
		EAdEffect effect = EAdElementsFactory.getInstance().getEffectFactory().getShowText("This text is showing through an EAdShowText effect", 400, 200, ShowTextAnimation.FADE_IN );
		getSceneElements().add(EAdElementsFactory.getInstance().getSceneElementFactory().createSceneElement(caption, 10, 10, effect));
		
		// Show question caption
		CaptionImpl caption2 = EAdElementsFactory.getInstance().getCaptionFactory().createCaption("Launch a question", EAdBorderedColor.WHITE_ON_BLACK, EAdBorderedColor.BLACK_ON_WHITE, new EAdFontImpl( new EAdURIImpl( "@binary/DroidSans-Bold.ttf"), 20));
		EAdEffect question = EAdElementsFactory.getInstance().getEffectFactory().getShowQuestion("I have a question and you have to answer, I'm afraid", 5 );
		getSceneElements().add(EAdElementsFactory.getInstance().getSceneElementFactory().createSceneElement(caption2, 10, 100, question));
		
		// Show text caption
		CaptionImpl caption3 = EAdElementsFactory.getInstance().getCaptionFactory().createCaption("Show very long text", EAdBorderedColor.WHITE_ON_BLACK, EAdBorderedColor.BLACK_ON_WHITE, new EAdFontImpl( new EAdURIImpl( "@binary/DroidSans-Bold.ttf"), 20));
		EAdEffect effect2 = EAdElementsFactory.getInstance().getEffectFactory().getShowText(StringType.VERY_LONG_STRING.getString(), 400, 200, ShowTextAnimation.FADE_IN, 100 );
		getSceneElements().add(EAdElementsFactory.getInstance().getSceneElementFactory().createSceneElement(caption3, 10, 200, effect2));
	}
	
	@Override
	public String getDescription() {
		return "A scene for test texts";
	}
	
	public String getDemoName(){
		return "Texts Scene";
	}

}
