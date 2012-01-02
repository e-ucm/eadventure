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

package ead.engine.core.platform.assets.sound;

/**
 * This abstract class defines any kind of sound event managed in eAdventure.
 * <p>
 * Any concrete class for a concrete sound format must implement the playOnce()
 * method. stopPlaying() method should also be overriden, calling this class'
 * stopPlaying() method to stop the play loop and actually stopping the sound
 * currently being played.
 * <p>
 * The sound is played in a new thread, so execution is not stopped while
 * playing the sound.
 */
public abstract class Sound extends Thread {

    /**
     * Stores if the sound file must play as a loop
     */
    private boolean loop;

    /**
     * Stores if the sound file is stopped
     */
    protected boolean stop;

    /**
     * Creates a new Sound.
     * 
     * @param loop
     *            defines whether or not the sound must be played in a loop
     */
    public Sound( boolean loop ) {

        super( );
        this.loop = loop;
        stop = false;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Thread#run()
     */
    @Override
    public void run( ) {

        boolean playedAtLeastOnce = false;
        while( !stop && ( loop || !playedAtLeastOnce ) ) {
            playOnce( );
            playedAtLeastOnce = true;
        }
        finalize( );
    }

    /**
     * Starts playing the sound.
     */
    public void startPlaying( ) {

        start( );
    }

    /**
     * Plays the sound just once. This method shouldn't be called manually.
     * Instead, create a new Sound with loop = false, and call startPlaying().
     */
    public abstract void playOnce( );

    /**
     * Stops playing the sound.
     */
    public void stopPlaying( ) {

        stop = true;
    }

    /**
     * Finalice and released the resources used for the sound
     */
    @Override
    public abstract void finalize( );

    /**
     * Returns whether the sound is still playing
     * 
     * @return true if the sound is playing, false otherwise
     */
    public boolean isPlaying( ) {

        return isAlive( );
    }

}
