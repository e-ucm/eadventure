package es.eucm.eadventure.common.model.effects.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.resources.assets.multimedia.Sound;

/**
 * An effect that plays a sound
 * 
 * 
 */
@Element(detailed = EAdPlaySoundEffect.class, runtime = EAdPlaySoundEffect.class)
public class EAdPlaySoundEffect extends AbstractEAdEffect {

	@Param("sound")
	private Sound sound;

	/**
	 * Creates a play sound effect
	 * 
	 * @param id
	 *            the effect id
	 * @param sound
	 *            the sound to be played
	 */
	public EAdPlaySoundEffect( Sound sound) {
		super();
		this.sound = sound;
	}
	
	public EAdPlaySoundEffect(){
		this( null );
	}
	
	public void setSound( Sound sound ){
		this.sound = sound;
	}

	/**
	 * Returns the sound to be played
	 * 
	 * @return
	 */
	public Sound getSound() {
		return sound;
	}

}
