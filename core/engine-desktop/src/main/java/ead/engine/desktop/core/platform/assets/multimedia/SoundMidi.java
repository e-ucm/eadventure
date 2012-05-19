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

package ead.engine.desktop.core.platform.assets.multimedia;

import java.io.IOException;
import java.io.InputStream;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

/**
 * This class is an implementation for Sound that is able to play midi sounds.
 */
public class SoundMidi extends Sound {

    /**
     * Sequencer for the Midi
     */
    private Sequencer sequencer;

    /**
     * Sequence containing the Midi
     */
    private Sequence sequence;

    /**
     * Creates a new SoundMidi.
     * <p>
     * If any error happens, an error message is printed to System.out and this
     * sound is disabled
     * 
     * @param filename
     *            path to the midi file
     * @param loop
     *            defines whether or not the sound must be played in a loop
     */
    public SoundMidi( InputStream is, boolean loop ) {

        super( loop );

        try {
            // Load sound from file
            sequence = MidiSystem.getSequence( is );
            sequencer = MidiSystem.getSequencer( );
            sequencer.open( );
            sequencer.setSequence( sequence );
            // Close the MIDI file
            is.close( );
        }
        catch( InvalidMidiDataException e ) {
            sequencer = null;
        }
        catch( IOException e ) {
            sequencer = null;
        }
        catch( MidiUnavailableException e ) {
            sequencer = null;
        }
    }

    /*
     *  (non-Javadoc)
     * @see es.eucm.eadventure.engine.multimedia.Sound#playOnce()
     */
    @Override
    public void playOnce( ) {

        if( sequencer != null ) {
            // Start playing the sound, and wait until it has finished
            sequencer.setTickPosition( sequencer.getLoopStartPoint( ) );
            sequencer.start( );
            while( sequencer.isRunning( ) )
                try {
                    sleep( 250 );
                }
                catch( InterruptedException e ) {
                }
        }
        else
            // If there was any error loading the sound, do nothing
            stopPlaying( );
    }

    /*
     *  (non-Javadoc)
     * @see es.eucm.eadventure.engine.multimedia.Sound#stopPlaying()
     */
    @Override
    public synchronized void stopPlaying( ) {

        super.stopPlaying( );
        // Stop playing the sound
        if( sequencer != null && sequencer.isOpen( ) ) {
            sequencer.stop( );
            sequencer.close( );
        }
    }

    /*
     *  (non-Javadoc)
     * @see java.lang.Object#finalize()
     */
    @Override
    public synchronized void finalize( ) {

        // Free resources
        if( sequencer != null && sequencer.isOpen( ) ) {
            sequencer.stop( );
            sequencer.close( );
        }
        sequencer = null;
        sequence = null;

    }

}
