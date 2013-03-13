package ead.plugins.engine.bubbledescription;

import com.google.inject.Inject;
import com.gwtent.reflection.client.Reflectable;

import ead.common.model.assets.drawable.basics.Caption;
import ead.common.model.assets.drawable.basics.EAdCaption;
import ead.common.model.assets.drawable.compounds.ComposedDrawable;
import ead.common.model.elements.operations.BasicField;
import ead.common.model.elements.operations.EAdField;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.params.text.EAdString;
import ead.common.model.params.util.Position.Corner;
import ead.engine.core.assets.AssetHandler;
import ead.engine.core.assets.drawables.RuntimeCaption;
import ead.engine.core.assets.drawables.RuntimeNinePatchImage;
import ead.engine.core.factories.SceneElementGOFactory;
import ead.engine.core.game.interfaces.GUI;
import ead.engine.core.game.interfaces.GameState;
import ead.engine.core.gameobjects.events.AbstractEventGO;
import ead.engine.core.gameobjects.sceneelements.SceneElementGO;

@Reflectable
public class BubbleNameGO extends AbstractEventGO<BubbleNameEv> {

	private final float TIME = 100.0f;

	private float scale = 0.0f;

	private boolean growing;

	private GUI gui;

	private SceneElementGO current;

	private SceneElementGOFactory sceneElementFactory;

	private SceneElementGO bubble;

	private EAdField<EAdString> currentDescription;

	private AssetHandler assetHandler;

	private RuntimeCaption caption;

	private RuntimeNinePatchImage ninePatch;

	@Inject
	public BubbleNameGO(GameState gameState, GUI gui,
			SceneElementGOFactory sceneElementFactory, AssetHandler assetHandler) {
		super(gameState);
		this.gui = gui;
		this.sceneElementFactory = sceneElementFactory;
		this.assetHandler = assetHandler;
	}

	@Override
	public void setElement(BubbleNameEv element) {
		super.setElement(element);
		currentDescription = new BasicField<EAdString>(element,
				BubbleNameEv.VAR_BUBBLE_NAME);
		SceneElement e = new SceneElement();
		e.setId("#engine.huds.namesbubble");
		e.setInitialZ(500);
		SceneElementGO hud = sceneElementFactory.get(e);
		gui.addHud(hud);
		// Bubble creation
		ComposedDrawable drawable = new ComposedDrawable();
		drawable.addDrawable(element.getBubble());
		Caption c = new Caption("[0]");
		c.setPadding(element.getBubble().getLeft());
		c.getOperations().add(currentDescription);
		c.setFont(element.getFont());
		c.setTextPaint(element.getTextPaint());
		drawable.addDrawable(c);
		SceneElement b = new SceneElement(drawable);
		b.setPosition(Corner.BOTTOM_CENTER, 0, 0);
		b.setInitialEnable(false);
		b.setInitialVisible(false);
		b.setInitialScale(0.0f);
		caption = (RuntimeCaption) assetHandler.getRuntimeAsset((EAdCaption) c);
		ninePatch = (RuntimeNinePatchImage) assetHandler
				.getRuntimeAsset(element.getBubble());
		bubble = sceneElementFactory.get(b);
		hud.addSceneElement(bubble);
	}

	@Override
	public void act(float delta) {
		SceneElementGO s = gui.getGameObjectUnderPointer();
		if (s != current) {
			current = s;
			if (current != null) {
				EAdString name = gameState.getValue(current.getElement(),
						BubbleNameEv.VAR_BUBBLE_NAME);
				gameState.setValue(currentDescription, name);
				if (name != null) {
					bubble.setVisible(true);
					bubble.setX(current.getCenterX());
					bubble.setY(current.getTop());
					scale = 0.0f;
					growing = true;
				} else {
					bubble.setVisible(false);
					scale = 0.0f;
				}
			} else {
				bubble.setVisible(false);
				scale = 0.0f;
			}
		}

		if (growing) {
			scale += 1.0f * (delta / TIME);
			if (scale >= 1.0f) {
				scale = 1.0f;
				growing = false;
			}
		}

		bubble.setScale(scale);
		ninePatch.setWidth(caption.getWidth());
		ninePatch.setHieght(caption.getHeight());
	}

}
