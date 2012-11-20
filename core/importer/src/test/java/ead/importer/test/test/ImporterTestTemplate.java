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

package ead.importer.test.test;

import ead.common.model.EAdElement;
import ead.importer.EAdElementImporter;

public abstract class ImporterTestTemplate<OldType, NewType extends EAdElement> {
	//
	//	public static final Injector INJECTOR = Guice
	//			.createInjector(new ImporterConfigurationModule(), new CommonTestModule());
	//
	//	protected EAdElementImporter<OldType, NewType> importer;
	//	protected List<OldType> oldObjects;
	//
	public ImporterTestTemplate(
			Class<? extends EAdElementImporter<OldType, NewType>> importerClass) {
		//		importer = INJECTOR.getInstance(importerClass);
		//		oldObjects = new ArrayList<OldType>();
	}

	//
	//	@Before
	//	public void setUp() {
	//		addOldObjects();
	//	}
	//
	public abstract void addOldObjects();

	//
	//	public void addTestObject(OldType oldObject) {
	//		oldObjects.add(oldObject);
	//	}
	//
	public abstract boolean equals(OldType oldObject, NewType newObject);
	//
	//	public NewType createNewObject(OldType oldObject) {
	//		NewType newObject = importer.init(oldObject);
	//		newObject = importer.convert(oldObject, newObject);
	//		return newObject;
	//	}
	//
	//	@Test
	//	public void testConvert() {
	//		for (OldType oldObject : oldObjects) {
	//			NewType newObject = createNewObject(oldObject);
	//			assertTrue(this.equals(oldObject, newObject));
	//		}
	//
	//		finalTests();
	//	}
	//
	//	/**
	//	 * Some additional test to make when importation is finished
	//	 */
	//	public void finalTests() {
	//
	//	}

}
