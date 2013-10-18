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

package es.eucm.ead.tests.tools.tools;

import es.eucm.ead.model.elements.AdventureGame;
import es.eucm.ead.model.elements.Chapter;
import es.eucm.ead.model.elements.effects.ChangeSceneEf;
import es.eucm.ead.model.elements.scenes.Scene;
import es.eucm.ead.tools.ObjectVisitor;
import es.eucm.ead.tools.java.reflection.JavaReflectionClassLoader;
import es.eucm.ead.tools.reflection.ReflectionClassLoader;
import org.junit.Test;

public class ObjectVisitorTest {

	@Test
	public void testObjectVisitor() {
		ReflectionClassLoader.init(new JavaReflectionClassLoader());
		ObjectVisitor visitor = new ObjectVisitor();
		visitor.addListener(new ObjectVisitor.ObjectListener() {
			@Override
			public void visit(Object o) {
				if (o != null) {
					System.out.println(o.getClass().getName());
				} else {
					System.out.println(o);
				}
			}
		});
		AdventureGame model = new AdventureGame();
		model.setId("Ejemplo");
		Chapter chapter = new Chapter();
		Chapter chapter2 = new Chapter();
		chapter.setId("djk");
		chapter.addScene(new Scene());
		model.addChapter(chapter);
		model.addChapter(chapter);
		model.addChapter(chapter);
		model.addChapter(chapter2);
		ChangeSceneEf cs = new ChangeSceneEf();
		chapter.addInitEffect(cs);
		chapter2.addInitEffect(cs);
		visitor.visit(model);
	}
}
