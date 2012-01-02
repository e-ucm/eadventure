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

import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundMp3 extends Sound {

    /**
     * Format to decode the sound
     */
    private AudioFormat decodedFormat;

    /**
     * Input stream with the decoded mp3
     */
    private AudioInputStream audioInputStream;

    /**
     * MP3 file
     */
    private SourceDataLine line;

    /**
     * Filename of the mp3 file
     */
    private InputStream is;

    /**
     * Creates a new SoundMp3
     * <p>
     * If any error happens, an error message is printed to System.out and this
     * sound is disabled
     * 
     * @param filename
     *            path to the midi file
     * @param loop
     *            defines whether or not the sound must be played in a loop
     */
    public SoundMp3( InputStream is, boolean loop ) {
        super( loop );
        this.is = is;
    }

    /*
     *  (non-Javadoc)
     * @see es.eucm.eadventure.engine.multimedia.Sound#playOnce()
     */
    @Override
    public void playOnce( ) {

        try {
            // Open MP3 file
            AudioInputStream ais = AudioSystem.getAudioInputStream( is );

            AudioFormat baseFormat = ais.getFormat( );
            decodedFormat = new AudioFormat( AudioFormat.Encoding.PCM_SIGNED, // Encoding to use
            baseFormat.getSampleRate( ), // sample rate (same as base format)
            16, // sample size in bits (thx to Javazoom)
            baseFormat.getChannels( ), // # of Channels
            baseFormat.getChannels( ) * 2, // Frame Size
            baseFormat.getSampleRate( ), // Frame Rate
            false // Big Endian
            );

            audioInputStream = AudioSystem.getAudioInputStream( decodedFormat, ais );

            DataLine.Info info = new DataLine.Info( SourceDataLine.class, decodedFormat );
            line = (SourceDataLine) AudioSystem.getLine( info );
            line.open( decodedFormat );

            if( line != null ) {
                try {
                    line.open( decodedFormat );
                    byte[] data = new byte[ 4096 ];
                    line.start( );
                    int nBytesRead;
                    while( !stop && ( nBytesRead = audioInputStream.read( data, 0, data.length ) ) != -1 ) {
                        line.write( data, 0, nBytesRead );
                    }
                }
                catch( IOException e ) {
                    stopPlaying( );                    
                }
                catch( LineUnavailableException e ) {
                    stopPlaying( );
                }

            }
            else
                // If there was any error loading the sound, do nothing
                stopPlaying( );

        }
        catch( UnsupportedAudioFileException e ) {
            finalize( );
        }
        catch( IOException e ) {
            finalize( );
        }
        catch( LineUnavailableException e ) {
            finalize( );
        }

    }

    /*
     *  (non-Javadoc)
     * @see es.eucm.eadventure.engine.multimedia.Sound#stopPlaying()
     */
    @Override
    public synchronized void stopPlaying( ) {

        super.stopPlaying( );
        if( line != null ) {
            line.drain( );
            line.stop( );
            line.close( );
        }
        if( audioInputStream != null ) {
            try {
                audioInputStream.close( );
            }
            catch( IOException e ) {
                e.printStackTrace( );
            }
        }
    }

    /*
     *  (non-Javadoc)
     * @see java.lang.Object#finalize()
     */
    @Override
    public synchronized void finalize( ) {

        // Free resources
        line = null;
        audioInputStream = null;
    }

}
