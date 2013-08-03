package ead.common.model.elements.effects;

import ead.common.interfaces.Element;
import ead.common.interfaces.Param;
import ead.common.model.assets.multimedia.Music;

@Element
public class PlayMusicEf extends AbstractEffect {

    @Param
    private Music music;

    @Param
    private float volume;

    @Param
    private boolean loop;

    public PlayMusicEf() {
    }

    public PlayMusicEf(Music music, float volume, boolean loop) {
        this.music = music;
        this.volume = volume;
        this.loop = loop;
    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }
}
