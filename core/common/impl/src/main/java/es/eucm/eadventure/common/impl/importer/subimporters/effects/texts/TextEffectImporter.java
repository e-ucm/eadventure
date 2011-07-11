package es.eucm.eadventure.common.impl.importer.subimporters.effects.texts;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.AbstractEffect;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.impl.importer.subimporters.effects.EffectImporter;
import es.eucm.eadventure.common.model.effects.impl.text.EAdShowText;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.resources.StringHandler;

public abstract class TextEffectImporter<T extends AbstractEffect> extends
		EffectImporter<T, EAdShowText> {

	protected static int ID_GENERATOR = 0;

	protected StringHandler stringHandler;

	protected EAdElementFactory factory;

	public TextEffectImporter(StringHandler stringHandler,
			EAdElementImporter<Conditions, EAdCondition> conditionImporter,
			EAdElementFactory factory) {
		super(conditionImporter);
		this.stringHandler = stringHandler;
		this.factory = factory;
	}

	@Override
	public EAdShowText init(T oldObject) {
		return new EAdShowText("showText" + ID_GENERATOR++);
	}

	public EAdShowText convert(T oldObject, Object object) {
		EAdShowText showText = (EAdShowText) object;
		super.importConditions(oldObject, showText);

		showText.setBlocking(true);
		showText.setOpaque(true);
		showText.setQueueable(true);

		return showText;
	}

}
