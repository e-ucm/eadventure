package es.eucm.eadventure.common.predef.model.sceneelements;

import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdComplexElementImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.params.EAdFontImpl;
import es.eucm.eadventure.common.params.EAdString;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.fills.impl.EAdLinearGradient;
import es.eucm.eadventure.common.params.fills.impl.EAdPaintImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl.Corner;
import es.eucm.eadventure.common.predef.model.effects.EAdChangeAppearance;
import es.eucm.eadventure.common.resources.EAdBundleId;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.CaptionImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.RectangleShape;

public class EAdButton extends EAdComplexElementImpl {

	private CaptionImpl caption;

	public EAdButton() {
		super("button");
		caption = new CaptionImpl();
		caption.setFont(new EAdFontImpl(12));
		caption.setTextPaint(EAdColor.BLACK);

		EAdBasicSceneElement text = new EAdBasicSceneElement("text", caption);
		text.setPosition(Corner.CENTER, 100, 15);
		text.setVarInitialValue(EAdBasicSceneElement.VAR_ENABLE, Boolean.FALSE);

		createButton();
		getElements().add(text);
	}

	public EAdString getLabel() {
		return caption.getText();
	}

	private void createButton() {
		EAdColor lightGray = new EAdColor(200, 200, 200);
		RectangleShape buttonBgNormal = new RectangleShape(200, 30);
		buttonBgNormal.setPaint(new EAdPaintImpl(new EAdLinearGradient(
				EAdColor.WHITE, lightGray, 0, 20), EAdColor.BLACK));
		RectangleShape buttonBgOver = new RectangleShape(200, 30);
		buttonBgOver.setPaint(new EAdPaintImpl(new EAdLinearGradient(
				lightGray, EAdColor.WHITE, 0, 20), EAdColor.BLACK));

		getResources().addAsset(getInitialBundle(),
				EAdBasicSceneElement.appearance, buttonBgNormal);
		EAdBundleId over = new EAdBundleId("over");
		getResources().addBundle(over);
		getResources().addAsset(over, EAdBasicSceneElement.appearance,
				buttonBgOver);
		setPosition(Corner.CENTER, 0, 0);

		
		EAdChangeAppearance changeAppearance = new EAdChangeAppearance(
				"change", this, getInitialBundle());
		EAdChangeAppearance changeAppearance2 = new EAdChangeAppearance(
				"change", this, over);
		addBehavior(EAdMouseEventImpl.MOUSE_EXITED, changeAppearance);
		addBehavior(EAdMouseEventImpl.MOUSE_ENTERED, changeAppearance2);
	}

}
