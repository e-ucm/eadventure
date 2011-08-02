package es.eucm.eadventure.common.model.effects.impl;

import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.resources.assets.multimedia.Sound;

/**
 * An effect that plays a sound
 * 
 * 
 */
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
	public EAdPlaySoundEffect(String id, Sound sound) {
		super(id);
		this.sound = sound;
	}
	
	public EAdPlaySoundEffect(String id){
		this( id, null );
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
