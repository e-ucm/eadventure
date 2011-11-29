package es.eucm.eadventure.common.elementfactories.scenedemos;

import java.util.ArrayList;
import java.util.List;

import es.eucm.eadventure.common.elementfactories.EAdElementsFactory;
import es.eucm.eadventure.common.elementfactories.StringFactory;
import es.eucm.eadventure.common.elementfactories.scenedemos.normalguy.NgMainScreen;
import es.eucm.eadventure.common.model.effects.impl.EAdChangeScene;
import es.eucm.eadventure.common.model.effects.impl.text.EAdSpeakEffect;
import es.eucm.eadventure.common.model.elements.EAdSceneElementDef;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneElementDefImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.transitions.EAdTransition;
import es.eucm.eadventure.common.params.EAdFont;
import es.eucm.eadventure.common.params.EAdFontImpl;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.fills.impl.EAdPaintImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl.Corner;
import es.eucm.eadventure.common.params.paint.EAdFill;
import es.eucm.eadventure.common.predef.model.sceneelements.EAdButton;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.ImageImpl;

public class InitScene extends EmptyScene {

	private List<SceneDemo> sceneDemos;

	private EAdBasicSceneElement goBack;

	private EAdSceneElementDef infoButton;
	
	private EAdFill fill = new EAdColor(255, 255, 255, 200);
	
	private EAdFont font = new EAdFontImpl( 18 );

	private EAdPaintImpl speakPaint = new EAdPaintImpl(fill, EAdColor.LIGHT_GRAY, 5);

	public InitScene() {
		this.setBackground(new EAdBasicSceneElement(new ImageImpl("@drawable/techdemo-bg.png")));
		getBackground().setId("background");
		initList();
		initGOBackButton();
		initInfoButton();
		int y = 200;
		int x = 120;
		StringFactory sf = EAdElementsFactory.getInstance().getStringFactory();
		for (SceneDemo s : sceneDemos) {
			EAdButton b = new EAdButton();
			sf.setString(b.getLabel(), s.getDemoName());
			b.setPosition(x, y);
			b.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_PRESSED,
					new EAdChangeScene( s, EAdTransition.BASIC));
			this.getElements().add(b);
			s.getElements().add(goBack);
			EAdBasicSceneElement info = new EAdBasicSceneElement(infoButton);
			info.setPosition(Corner.BOTTOM_LEFT, 80, 590);
			EAdSpeakEffect effect = new EAdSpeakEffect();
			effect.setId("infoSpeak");
			effect.setColor(EAdColor.GRAY, speakPaint);
			effect.setFont(font);
			sf.setString(effect.getString(), s.getSceneDescription());
			info.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_PRESSED, effect);
			// info.setScale(0.5f);
			s.getElements().add(info);
			y += 45;
			if (y > 520) {
				y = 200;
				x += 210;
			}
		}
	}

	private void initInfoButton() {
		infoButton = new EAdSceneElementDefImpl();
		infoButton.setId("info");
		infoButton.getResources().addAsset(infoButton.getInitialBundle(),
				EAdBasicSceneElement.appearance,
				new ImageImpl("@drawable/infobutton.png"));
	}

	private void initGOBackButton() {
		goBack = new EAdBasicSceneElement(new ImageImpl(
				"@drawable/goback.png"));
		goBack.setId("goBack");
		goBack.setPosition(Corner.BOTTOM_LEFT, 10, 590);
		goBack.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_PRESSED,
				new EAdChangeScene( this, EAdTransition.BASIC));
		goBack.setScale(0.5f);

	}

	private void initList() {
		sceneDemos = new ArrayList<SceneDemo>();
		sceneDemos.add(new EmptyScene());
		sceneDemos.add(new ShapeScene());
		// sceneDemos.add(new TextsScene());
		sceneDemos.add(new CharacterScene());
		sceneDemos.add(new SpeakAndMoveScene());
		sceneDemos.add(new ComplexElementScene());
		// sceneDemos.add(new SoundScene());
		sceneDemos.add(new DrawablesScene());
		// sceneDemos.add(new MoleGame());
		sceneDemos.add(new ShowQuestionScene());
		// sceneDemos.add(new TrajectoriesScene());
		// sceneDemos.add(new PhysicsScene());
		sceneDemos.add(new PhysicsScene2());
		sceneDemos.add(new DragDropScene());
		sceneDemos.add(new PositionScene());
		sceneDemos.add(new DepthZScene());
		sceneDemos.add(new SharingEffectsScene());
		sceneDemos.add(new InventoryScene());
		// sceneDemos.add(new VideoScene());
		sceneDemos.add(new NgMainScreen());
		// sceneDemos.add(new NgRoom1());

	}

	@Override
	public String getSceneDescription() {
		return "A scene containing the demos scene";
	}

	public String getDemoName() {
		return "Scene Demo Chooser";
	}

}
