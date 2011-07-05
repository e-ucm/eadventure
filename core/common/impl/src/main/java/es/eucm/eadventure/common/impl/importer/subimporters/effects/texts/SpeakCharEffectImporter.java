package es.eucm.eadventure.common.impl.importer.subimporters.effects.texts;

import com.google.inject.Inject;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.SpeakCharEffect;
import es.eucm.eadventure.common.data.chapter.elements.NPC;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.model.effects.impl.text.EAdShowText;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.params.EAdBorderedColor;
import es.eucm.eadventure.common.model.params.EAdColor;
import es.eucm.eadventure.common.resources.EAdString;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.impl.CaptionImpl;

public class SpeakCharEffectImporter extends
		TextEffectImporter<SpeakCharEffect> {

	@Inject
	public SpeakCharEffectImporter(StringHandler stringHandler,
			EAdElementImporter<Conditions, EAdCondition> conditionImporter,
			EAdElementFactory factory) {
		super(stringHandler, conditionImporter, factory);
	}

	@Override
	public EAdShowText convert(SpeakCharEffect oldObject, Object object) {
		EAdShowText effect = super.convert(oldObject, object);

		EAdString text = stringHandler.addNewString(oldObject.getLine());

		NPC p = factory.getCurrentOldChapterModel().getCharacter(
				oldObject.getTargetId());
		CaptionImpl c = new CaptionImpl(text);
		EAdColor center = EAdColor.valueOf("0x" + p.getTextFrontColor().substring(1) + "ff");
		EAdColor border = EAdColor.valueOf("0x" + p.getTextBorderColor().substring(1) + "ff");
		c.setTextColor(new EAdBorderedColor(center, border));

		EAdColor bubbleCenter = EAdColor.valueOf("0x" + p.getBubbleBkgColor().substring(1) + "ff");
		EAdColor bubbleBorder = EAdColor.valueOf("0x" + p.getBubbleBorderColor().substring(1) + "ff");
		c.setBubbleColor(new EAdBorderedColor(bubbleCenter, bubbleBorder));
	
		//FIXME fix position
		effect.setCaption(c, 10, 10);

		return effect;
	}

}
