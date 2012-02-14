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

package ead.engine.core.platform.specialassetrenderers;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import ead.common.resources.assets.multimedia.EAdVideo;
import ead.engine.core.platform.AssetHandler;
import ead.engine.core.platform.EngineConfiguration;
import ead.engine.core.platform.SpecialAssetRenderer;
import fr.hd3d.html5.video.client.VideoSource;
import fr.hd3d.html5.video.client.VideoWidget;

/**
 * Video {@link SpecialAssetRenderer} for PlayN (GWT) implementation.
 * <p>
 * This renderer uses a html5 video gwt widget
 * (http://code.google.com/p/gwt-html5-video) which replaces the panel with the
 * game in the html layout.
 * 
 */
public class PlayNVideoRenderer implements SpecialAssetRenderer<EAdVideo, Widget> {

	private static Logger logger = LoggerFactory.getLogger("PlayNVideoRenderer");

	private AssetHandler assetHandler;

	private EngineConfiguration platformConfiguration;

	private VideoWidget videoWidget;

	@Inject
	public PlayNVideoRenderer(AssetHandler assetHandler,
			EngineConfiguration platformConfiguration) {
		logger.info("New instance");
		this.assetHandler = assetHandler;
		this.platformConfiguration = platformConfiguration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.platform.SpecialAssetRenderer#getComponent
	 * (java.lang.Object)
	 * 
	 * DOCUMENTATION:
	 * http://code.google.com/p/gwt-html5-video/wiki/GettingStarted
	 */
	@Override
	public Widget getComponent(EAdVideo asset) {
		String path = assetHandler.getAbsolutePath(asset.getUri().getPath());
		// TODO Last parameter should be a capture of the vide
		
		if ( path.endsWith(".avi")){
			path = path.substring(0, path.length() - 3 ) + "webm";
		}
		videoWidget = new VideoWidget(true, false, "");
		List<VideoSource> sources = new ArrayList<VideoSource>();
		logger.info("New video widget: " + path);
		sources.add(new VideoSource(path));
		videoWidget.setSources(sources);
		videoWidget.setPixelSize(platformConfiguration.getWidth(),
				platformConfiguration.getHeight());
		return videoWidget;
	}

	@Override
	public boolean isFinished() {
		if (videoWidget == null)
			return false;
		return videoWidget.isEnded();
	}

	@Override
	public boolean start() {
		return true;
	}

	@Override
	public void reset() {
		if ( videoWidget.isEnded() ){
			videoWidget.setCurrentTime(0);
		}	
	}
}
