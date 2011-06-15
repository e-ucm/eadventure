package es.eucm.eadventure.engine.core.platform.impl.assetrenderers;

import java.awt.Component;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.CannotRealizeException;
import javax.media.Codec;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.EndOfMediaEvent;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.PlugInManager;
import javax.media.PrefetchCompleteEvent;
import javax.media.RealizeCompleteEvent;
import javax.media.StopEvent;

import com.google.inject.Singleton;

import es.eucm.eadventure.common.resources.assets.multimedia.Video;
import es.eucm.eadventure.engine.core.platform.assets.impl.SpecialAssetRenderer;

@Singleton
public class DesktopVideoRenderer implements SpecialAssetRenderer<Video, Component> {

	private static Logger logger = Logger.getLogger("DesktopVideoRenderer");
	
	private boolean loaded = false;
	
	private boolean finished = false;
	
	private Player mediaPlayer;
	
	private static String CODEC_CLASS_NAME = "net.sourceforge.jffmpeg.VideoDecoder";
	
	@Override
	public Component getComponent(Video asset) {
		if (!loaded) {
	        try {
	            Codec video = (Codec) Class.forName( CODEC_CLASS_NAME ).newInstance( );
	            PlugInManager.addPlugIn( CODEC_CLASS_NAME, video.getSupportedInputFormats( ),  video.getSupportedOutputFormats( null ), PlugInManager.CODEC );
	            PlugInManager.commit( );
	            loaded = true;
	        }
	        catch( Exception e ) {
	        	logger.severe("Could not load video codec!");
	        }
		}
		Component video = null;
		
		Manager.setHint(Manager.LIGHTWEIGHT_RENDERER, true);
		try {
			MediaLocator mediaLocator = new MediaLocator("file://" + asset.getURI());
			mediaPlayer = Manager.createRealizedPlayer(mediaLocator);
			mediaPlayer.addControllerListener(new ControllerListener() {

				@Override
				public void controllerUpdate(ControllerEvent event) {
			        if( event instanceof RealizeCompleteEvent ) {
			            logger.info("RealizeCompleteEvent");
			            //realized = true;
			            //notify( );
			        }
			        else if( event instanceof EndOfMediaEvent ) {
			            logger.info("EndOfMediaEvent");
			            finished = true;
			        }
			        else if( event instanceof StopEvent ) {
			        	logger.info("StopEvent");
			            finished = true;
			            //notify( );
			        }
			        else if( event instanceof PrefetchCompleteEvent ) {
			            logger.info("PrefetchCompleteEvent");
			            //notify( );
			        }
			    }
				
			});
			video = mediaPlayer.getVisualComponent();
			
			if( video != null ) {
	            mediaPlayer.start( );
	        }
		} catch (NoPlayerException e) {
			logger.log(Level.SEVERE, "No player", e);
		} catch (MalformedURLException e) {
			logger.log(Level.SEVERE, "Malformed URL", e);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "IO Exception", e);
		} catch (CannotRealizeException e) {
			logger.log(Level.SEVERE, "Cannot realized player", e);
		}
		return video;
		
	}

	public boolean isFinished() {
		return finished;
	}
}
