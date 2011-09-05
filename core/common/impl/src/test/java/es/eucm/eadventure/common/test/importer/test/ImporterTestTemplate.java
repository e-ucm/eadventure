package es.eucm.eadventure.common.test.importer.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.impl.importer.ImporterConfigurationModule;
import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.test.CommonTestModule;

public abstract class ImporterTestTemplate<OldType, NewType extends EAdElement> {

	public static final Injector INJECTOR = Guice
			.createInjector(new ImporterConfigurationModule("project.eap"), new CommonTestModule());

	protected EAdElementImporter<OldType, NewType> importer;
	protected List<OldType> oldObjects;

	public ImporterTestTemplate(
			Class<? extends EAdElementImporter<OldType, NewType>> importerClass) {
		importer = INJECTOR.getInstance(importerClass);
		oldObjects = new ArrayList<OldType>();
	}

	@Before
	public void setUp() {
		addOldObjects();
	}

	public abstract void addOldObjects();

	public void addTestObject(OldType oldObject) {
		oldObjects.add(oldObject);
	}

	public abstract boolean equals(OldType oldObject, NewType newObject);

	public NewType createNewObject(OldType oldObject) {
		NewType newObject = importer.init(oldObject);
		newObject = importer.convert(oldObject, newObject);
		return newObject;
	}

	@Test
	public void testConvert() {
		for (OldType oldObject : oldObjects) {
			NewType newObject = createNewObject(oldObject);
			assertTrue(this.equals(oldObject, newObject));
		}

		finalTests();
	}

	/**
	 * Some additional test to make when importation is finished
	 */
	public void finalTests() {

	}

}
