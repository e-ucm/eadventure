package es.eucm.ead.tests.importer;

import es.eucm.ead.importer.AdventureConverter;
import es.eucm.ead.model.elements.effects.ChangeSceneEf;
import es.eucm.ead.tools.ObjectVisitor;

public class ConverterObjectVisitor {

	public static final String TEST_FOLDER = "/home/eva/repositories/eadventure-legacy/Projects/Dama Boba";

	public static void main(String args[]) {
		AdventureConverter converter = new AdventureConverter();
		converter.convert(TEST_FOLDER, null);

		ObjectVisitor objectVisitor = new ObjectVisitor();
		objectVisitor.addListener(new ObjectVisitor.ObjectListener() {
			@Override
			public void visit(Object o) {
				if (o instanceof ChangeSceneEf) {
					System.out.println(((ChangeSceneEf) o).getNextScene());
				}
			}
		});
		objectVisitor.visit(converter.getModel());
	}
}
