package ead.engine.core.assets.multimedia;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.inject.Inject;
import ead.common.model.assets.multimedia.Music;
import ead.engine.core.assets.AbstractRuntimeAsset;
import ead.engine.core.assets.AssetHandler;
import ead.engine.core.assets.AssetHandlerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RuntimeMusic extends AbstractRuntimeAsset<Music> {

    private static final Logger logger = LoggerFactory.getLogger("RuntimeMusic");

    private FileHandle fh;

    private com.badlogic.gdx.audio.Music music;

    @Inject
    public RuntimeMusic(AssetHandler assetHandler) {
        super(assetHandler);
    }

    @Override
    public boolean loadAsset() {
        super.loadAsset();
        fh = ((AssetHandlerImpl) assetHandler).getFileHandle(descriptor
                .getUri());
        try {
            music = Gdx.audio.newMusic(fh);
        } catch ( Exception e ){
            logger.error("Error loading sound {}", descriptor.getUri(), e);
        }
        return true;
    }

    public void setVolume(float volume) {
        if (music != null ) {
            music.setVolume(volume);
        }
    }

    @Override
    public void freeMemory() {
        if ( music == null ){
            return;
        }
        if (isLoaded()) {
            super.freeMemory();
            music.dispose();
            music = null;
        }
    }

    public void play(boolean loop, float volume) {
        if ( music == null ){
            return;
        }
        music.setVolume(volume);
        music.setLooping(loop);
        music.play();
    }

    public void stop() {
        if ( music == null ){
            return;
        }
        music.stop();
    }

    @Override
    public void refresh() {
        FileHandle fh = ((AssetHandlerImpl) assetHandler)
                .getFileHandle(descriptor.getUri());
        if (!this.fh.path().equals(fh.path())) {
            this.freeMemory();
            this.loadAsset();
        }
    }

    public void setPause(boolean pause) {
        if ( pause ){
            music.pause();
        }
        else {
            this.music.play();
        }
    }
}
