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

package ead.engine.core.gdx.html.platform.assets;

import ead.common.model.assets.multimedia.EAdVideo;
import ead.engine.core.assets.SpecialAssetRenderer;

public class GWTVideoRenderer implements SpecialAssetRenderer<EAdVideo, Object> {

	private boolean isStream;
	private String source;

	/**
	 * Static var used to notify when video ends 
	 */
	private static boolean videoEnded = true;

	public GWTVideoRenderer() {
		export();
	}

	@Override
	public Object getComponent(EAdVideo asset) {
		isStream = asset.isStream();
		source = asset.getUri();
		return this;
	}

	@Override
	public boolean isFinished() {
		return videoEnded;
	}

	@Override
	public boolean start() {
		videoEnded = false;
		if (isStream) {
			playYouTubeVideo(source);
		} else {
			playVideo("assets/" + source.substring(1));
		}
		return true;
	}

	@Override
	public void reset() {

	}

	public void hide() {
		if (isStream) {
			hideYouTubeVideo();
		} else {
			hideVideo();
		}
	}

	public static void videoEnded() {
		videoEnded = true;
	}

	public native final void playVideo(String source) /*-{ 
																		var player = $wnd._V_("nVideo");
																		$wnd.$("#nVideoContainer").css("visibility", "visible");		
																		player.src(source);
																		player.play();
																		}-*/;

	public native final void playYouTubeVideo(String source) /*-{ 		
																				$wnd.$("#ytplayer").css("visibility", "visible");
																				ytplayer.loadVideoByUrl(source);
																				}-*/;

	public static native final void hideVideo() /*-{
																$wnd.$("#nVideoContainer").css("visibility", "hidden");
																}-*/;

	public static native final void hideYouTubeVideo() /*-{
																		$wnd.$("#ytplayer").css("visibility", "hidden");
																		}-*/;

	public static native final void export()/*-{
															$wnd.eadVideoEnded=$entry(@ead.engine.core.gdx.html.platform.assets.GWTVideoRenderer::videoEnded());
															}-*/;

}
