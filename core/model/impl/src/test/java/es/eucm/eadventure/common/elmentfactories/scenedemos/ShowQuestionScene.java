package es.eucm.eadventure.common.elmentfactories.scenedemos;

import es.eucm.eadventure.common.elmentfactories.EAdElementsFactory;
import es.eucm.eadventure.common.elmentfactories.StringFactory;
import es.eucm.eadventure.common.model.effects.impl.text.EAdShowQuestion;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;

public class ShowQuestionScene extends EmptyScene {

	public ShowQuestionScene() {
		EAdBasicSceneElement element = EAdElementsFactory.getInstance()
				.getSceneElementFactory()
				.createSceneElement("Launch show question", 10, 10);

		getElements().add(element);

		StringFactory stringFactory = EAdElementsFactory.getInstance()
				.getStringFactory();

		EAdShowQuestion effect = new EAdShowQuestion();
		effect.setQuestion(stringFactory.getString("A question has been made"));
		
		effect.addAnswer(stringFactory.getString("Answer 1"), effect);
		effect.addAnswer(stringFactory.getString("Answer 2"), effect);
		effect.addAnswer(stringFactory.getString("Answer 3"), null);
		
		effect.setUpNewInstance();

		element.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, effect);
	}

	@Override
	public String getDescription() {
		return "A scene to test show question effect";
	}

	public String getDemoName() {
		return "Show Question Scene";
	}

}
