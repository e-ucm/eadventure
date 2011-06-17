package es.eucm.eadventure.common.impl.importer.subimporters.effects;

import com.google.inject.Inject;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.ShowTextEffect;
import es.eucm.eadventure.common.model.effects.impl.text.EAdShowText;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.params.EAdBorderedColor;
import es.eucm.eadventure.common.model.params.EAdColor;
import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.common.resources.EAdString;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.impl.CaptionImpl;

public class ShowTextEffectImporter extends
		EffectImporter<ShowTextEffect, EAdShowText> {

	private StringHandler stringHandler;
	
	/**
	 * Aux var to generate ids
	 */
	private static int ID_GENERATOR = 0; 

	@Inject
	public ShowTextEffectImporter(StringHandler stringHandler, EAdElementImporter<Conditions,EAdCondition> conditionImporter ) {
		super( conditionImporter );
		this.stringHandler = stringHandler;
	}
	
	@Override
	public EAdShowText init(ShowTextEffect oldObject) {
		return new EAdShowText( "showTextEffect" + ID_GENERATOR++);
	}
	
	@Override
	public EAdShowText convert(ShowTextEffect oldObject, Object object) {
		EAdShowText showText = (EAdShowText) object;
		super.importConditions(oldObject, showText);
		
		EAdString text = new EAdString( stringHandler.getUniqueId() );
		stringHandler.addString(text, oldObject.getText());
		
		showText.setBlocking(true);
		showText.setOpaque(true);
		
		CaptionImpl c = new CaptionImpl( text );
		EAdColor center = EAdColor.valueOf( Integer.toHexString(oldObject.getRgbFrontColor()) + "ff");
		EAdColor border = EAdColor.valueOf( Integer.toHexString( oldObject.getRgbBorderColor() ) + "ff");
		c.setTextColor(new EAdBorderedColor( center, border ));
		
		showText.setCaption(c, oldObject.getX(), oldObject.getY());
		// TODO might be needed, check if imported text is positioned correctly
		// showText.getText().setPosition(new EAdPosition( EAdPosition.Corner.CENTER, oldObject.getX(), oldObject.getY() ));
		return showText;
	}

}
