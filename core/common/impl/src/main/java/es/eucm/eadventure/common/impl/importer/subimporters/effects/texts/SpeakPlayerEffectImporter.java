package es.eucm.eadventure.common.impl.importer.subimporters.effects.texts;

import com.google.inject.Inject;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.SpeakPlayerEffect;
import es.eucm.eadventure.common.data.chapter.elements.Player;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.model.effects.impl.text.EAdShowText;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.params.EAdBorderedColor;
import es.eucm.eadventure.common.model.params.EAdColor;
import es.eucm.eadventure.common.resources.EAdString;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.impl.CaptionImpl;

public class SpeakPlayerEffectImporter extends
		TextEffectImporter<SpeakPlayerEffect> {

	@Inject
	public SpeakPlayerEffectImporter(StringHandler stringHandler,
			EAdElementImporter<Conditions, EAdCondition> conditionImporter,
			EAdElementFactory factory) {
		super(stringHandler, conditionImporter, factory);
	}

	@Override
	public EAdShowText convert(SpeakPlayerEffect oldObject, Object newElement) {
		EAdShowText effect = super.convert(oldObject, newElement);

		EAdString text = stringHandler.addNewString(oldObject.getLine());

		Player p = factory.getCurrentOldChapterModel().getPlayer();

		CaptionImpl c = new CaptionImpl(text);
		EAdColor center = EAdColor.valueOf("0x" + p.getTextFrontColor().substring(1) + "ff");
		EAdColor border = EAdColor.valueOf("0x" + p.getTextBorderColor().substring(1) + "ff");
		c.setTextColor(new EAdBorderedColor(center, border));

		EAdColor bubbleCenter = EAdColor.valueOf("0x" + p.getBubbleBkgColor().substring(1) + "ff");
		EAdColor bubbleBorder = EAdColor.valueOf("0x" + p.getBubbleBorderColor().substring(1)
				+ "ff");
		c.setBubbleColor(new EAdBorderedColor(bubbleCenter, bubbleBorder));
		
		
		
		// TODO Fix position
		effect.setCaption(c, 10, 10);

		return effect;
	}

}
