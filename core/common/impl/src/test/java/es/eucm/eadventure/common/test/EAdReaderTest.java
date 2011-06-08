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

package es.eucm.eadventure.common.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import junit.framework.TestCase;

import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;

import es.eucm.eadventure.common.impl.reader.EAdAdventureModelReader;
import es.eucm.eadventure.common.impl.reader.subparsers.AdventureHandler;
import es.eucm.eadventure.common.model.EAdAdventureModel;
import es.eucm.eadventure.common.model.impl.EAdAdventureModelImpl;

public class EAdReaderTest extends TestCase {

	private EAdAdventureModelReader reader;
	private File f;

	@Override
	public void setUp( ) {
		Injector injector = Guice.createInjector( new ConfigurationModule( ) );
		reader = injector.getInstance( EAdAdventureModelReader.class );

		f = new File( "src/test/resources/testAdventure2.xml" );
	}

	public class ConfigurationModule extends AbstractModule {
		@Override
		protected void configure( ) {
			bind( AdventureHandler.class );
			bind( EAdAdventureModel.class ).to( EAdAdventureModelImpl.class );
			bind( String.class ).annotatedWith(Names.named("classParam")).toInstance("class");
		}
	}

	@Test
	public void testFromIS( ) {
		EAdAdventureModel model = null;
		
		try {
			InputStream is = new FileInputStream( f );
			
			if ( is != null )
				model = reader.read( is );
			
			is.close( );
		}
		catch ( FileNotFoundException e ) {
			e.printStackTrace( );
			fail();
		}
		catch ( IOException e ) {
			e.printStackTrace();
			fail();
		}
		catch ( RuntimeException e) {
			e.printStackTrace();
			fail();
		}
		
		assertNotNull( model );
	}

	@Test
	public void testFromFile( ) {
		URI uri = f.toURI( );
		EAdAdventureModel model = null;
		try {
			model = reader.read( uri );
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		assertNotNull( model );
	}

}
