package es.eucm.ead.tests.tools.tools;

import es.eucm.ead.model.elements.BasicAdventureModel;
import es.eucm.ead.model.elements.BasicChapter;
import es.eucm.ead.model.elements.effects.ChangeSceneEf;
import es.eucm.ead.model.elements.scenes.BasicScene;
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
		BasicAdventureModel model = new BasicAdventureModel();
		model.setId("Ejemplo");
		BasicChapter chapter = new BasicChapter();
		BasicChapter chapter2 = new BasicChapter();
		chapter.setId("djk");
		chapter.addScene(new BasicScene());
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
