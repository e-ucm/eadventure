package es.eucm.eadventure.engine.core.platform.impl.specialassetrenderers;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;

import es.eucm.eadventure.common.resources.assets.multimedia.Video;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.SpecialAssetRenderer;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;

public class VLCDesktopVideoRenderer implements SpecialAssetRenderer<Video, Component> {
	
	private static Logger logger = Logger.getLogger("VLCDesktopVideoRenderer");

	private EmbeddedMediaPlayer mediaPlayer;

	private CanvasVideoSurface videoSurface;
	
	protected boolean finished;

	private static MediaPlayerFactory mediaPlayerFactory;
	
	private static String vlcOptions = "";
	
	private String path;
	
	private AssetHandler assetHandler;

	@Inject
	public VLCDesktopVideoRenderer(AssetHandler assetHandler) {
		this.assetHandler = assetHandler;
		initilizeVariables();
	}
	
	@Override
	public Component getComponent(Video asset) {
		if (mediaPlayerFactory == null) {
			String[] options = {vlcOptions};
			mediaPlayerFactory = new MediaPlayerFactory (options);
		}
		
		Canvas canvas = new Canvas();
		canvas.setBackground(Color.black);
		
		mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
		videoSurface = mediaPlayerFactory.newVideoSurface(canvas);
		mediaPlayer.setVideoSurface(videoSurface);
		
		mediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventListener() {

			@Override
			public void backward(MediaPlayer arg0) { }

			@Override
			public void buffering(MediaPlayer arg0) { }

			@Override
			public void endOfSubItems(MediaPlayer arg0) { }

			@Override
			public void error(MediaPlayer arg0) { }

			@Override
			public void finished(MediaPlayer arg0) {
				finished = true;
			}

			@Override
			public void forward(MediaPlayer arg0) { }

			@Override
			public void lengthChanged(MediaPlayer arg0, long arg1) { }

			@Override
			public void mediaChanged(MediaPlayer arg0) { }

			@Override
			public void mediaDurationChanged(MediaPlayer arg0, long arg1) { }

			@Override
			public void mediaFreed(MediaPlayer arg0) { }

			@Override
			public void mediaMetaChanged(MediaPlayer arg0, int arg1) { }

			@Override
			public void mediaParsedChanged(MediaPlayer arg0, int arg1) { }

			@Override
			public void mediaStateChanged(MediaPlayer arg0, int arg1) { }

			@Override
			public void mediaSubItemAdded(MediaPlayer arg0, libvlc_media_t arg1) { }

			@Override
			public void newMedia(MediaPlayer arg0) { }

			@Override
			public void opening(MediaPlayer arg0) { }

			@Override
			public void pausableChanged(MediaPlayer arg0, int arg1) { }

			@Override
			public void paused(MediaPlayer arg0) { }

			@Override
			public void playing(MediaPlayer arg0) { }

			@Override
			public void positionChanged(MediaPlayer arg0, float arg1) { }

			@Override
			public void seekableChanged(MediaPlayer arg0, int arg1) { }

			@Override
			public void snapshotTaken(MediaPlayer arg0, String arg1) { }

			@Override
			public void stopped(MediaPlayer arg0) { }

			@Override
			public void subItemFinished(MediaPlayer arg0, int arg1) { }

			@Override
			public void subItemPlayed(MediaPlayer arg0, int arg1) { }

			@Override
			public void timeChanged(MediaPlayer arg0, long arg1) { }

			@Override
			public void titleChanged(MediaPlayer arg0, int arg1) { }
			
		});
		
		path = asset.getURI();
		if (assetHandler != null)
			path = assetHandler.getAbsolutePath(asset.getURI());

		finished = false;

		return canvas;
	}
	
	public static void initilizeVariables() {
		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("win")) {
			// exists?
			// not exists, extract
			// exists, use it!
			String pathLibvlc = null;
			String pathPlugins = null;
			System.setProperty("jna.library.path", pathLibvlc);
			System.setProperty("VLC_PLUGIN_PATH", pathPlugins);
			vlcOptions = "--no-video-title-show";
		} else if (os.contains("mac")) {
			String temp = "/Applications/VLC.app";
			if (!new File("/Applications/VLC.app/").exists()) {
				// not exists, extract
				//temp = ....;
			} else
				logger.log(Level.INFO, "VLC installed");
			String pathLibvlc = temp + "/Contents/MacOS/lib/";
			String pathPlugins = temp + "/Contents/MacOS/plugins/";
			System.setProperty("jna.library.path", pathLibvlc);
			System.setProperty("VLC_PLUGIN_PATH", pathPlugins);
			vlcOptions = "--vout=macosx";
		} else {
			logger.log(Level.SEVERE, "OS not supported by VLC video plugin");
		}
	}

	@Override
	public boolean isFinished() {
		return finished;
	}

	@Override
	public boolean start() {
		String [] mediaOptions ={};
		mediaPlayer.playMedia(path, mediaOptions);
		return true;
	}
	
}
