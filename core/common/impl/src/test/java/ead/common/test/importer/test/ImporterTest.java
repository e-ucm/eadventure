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

package ead.common.test.importer.test;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import ead.common.importer.EAdventure1XImporter;
import ead.common.importer.ImporterConfigurationModule;
import ead.common.model.elements.EAdAdventureModel;
import es.eucm.eadventure.common.data.adventure.AdventureData;

public class ImporterTest extends TestCase {

	private EAdventure1XImporter importer;

	@Before
    @Override
	public void setUp( ) {
		Injector injector = Guice.createInjector( new ImporterConfigurationModule(  ) );
		importer = injector.getInstance( EAdventure1XImporter.class );
	}

	@Test
	public void testImportGame( ) {
		EAdAdventureModel data = importer.importGame( null, null  );
		assertNotNull( data );
		AdventureData oldData = importer.loadGame(  );
		assertNotNull( oldData );
	}

	@Test
	public void testImportGameFromZip( ){
		EAdAdventureModel data = importer.importGame(  null, "src/test/resources/Chocolate.ead" );
		assertNotNull( data );
	}

}
