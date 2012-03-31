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

package ead.engine.core.platform.assets.specialassetrenderers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import ead.common.resources.assets.multimedia.EAdVideo;
import ead.engine.core.platform.EngineConfiguration;
import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.platform.assets.SpecialAssetRenderer;
import fr.hd3d.html5.video.client.VideoSource;
import fr.hd3d.html5.video.client.VideoSource.VideoType;
import fr.hd3d.html5.video.client.VideoWidget;
import fr.hd3d.html5.video.client.events.VideoErrorEvent;
import fr.hd3d.html5.video.client.handlers.VideoErrorHandler;

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
	
	private SimplePanel containerPanel;

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

		List<VideoSource> sources = new ArrayList<VideoSource>();
		//TODO Check if versions exist?
		if ( path.endsWith(".avi")){
			logger.info("Adding webm and mp4 for AVI video");
			sources.add(new VideoSource(changeExtension(path, "avi", "webm"), VideoType.WEBM));
			sources.add(new VideoSource(changeExtension(path, "avi", "mp4"), VideoType.MP4));
		} else if ( path.endsWith(".mpg")){
			logger.info("Adding webm and mp4 for MPG video");
			sources.add(new VideoSource(changeExtension(path, "mpg", "webm"), VideoType.WEBM));
			sources.add(new VideoSource(changeExtension(path, "mpg", "mp4"), VideoType.MP4));
		} else if (path.endsWith(".mp4")) {
			logger.info("Adding webm and mp4 for MP4 video");
			sources.add(new VideoSource(path, VideoType.MP4));
			sources.add(new VideoSource(changeExtension(path, "mp4", "webm"), VideoType.WEBM));
		} else if (path.endsWith(".webm")) {
			logger.info("Adding webm and mp4 for WEBM video");
			sources.add(new VideoSource(path, VideoType.WEBM));
			sources.add(new VideoSource(changeExtension(path, "webm", "mp4"), VideoType.MP4));
		}
		
		// TODO Last parameter should be a capture of the video or "Loading" message
		videoWidget = new VideoWidget(true, false, "");
		
		logger.info("New video widget: " + path);
		videoWidget.setSources(sources);
		videoWidget.setPixelSize(platformConfiguration.getWidth(),
				platformConfiguration.getHeight());
		videoWidget.addErrorHandler(new VideoErrorHandler() {
			@Override public void onError(VideoErrorEvent event) {
				logger.warn(event.toDebugString());
			}
		});
		
		containerPanel = new SimplePanel() {
			@Override public void onBrowserEvent(Event event) {
				switch (DOM.eventGetType(event)) {
	                case Event.ONMOUSEDOWN: {
	                	logger.info("Skipping video on user request");
	                	//TODO This should be a video or game configurable option
	    				videoWidget.setCurrentTime(videoWidget.getDuration());
	                }
				}
			}
		};
		containerPanel.sinkEvents(Event.MOUSEEVENTS);
		containerPanel.add(videoWidget);
		return containerPanel;
	}
	
	private static String changeExtension(String path, String oldExt, String newExt) {
		return path.substring(0, path.length() - oldExt.length() ) + newExt;
	}

	@Override
	public boolean isFinished() {
		boolean isFinished = videoWidget != null && videoWidget.isEnded();
		if (isFinished) {
			containerPanel.unsinkEvents(Event.MOUSEEVENTS);
		}
		return isFinished;
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
