package ead.engine.core.gameobjects.widgets;

import com.google.inject.Inject;

import ead.common.model.elements.guievents.enums.KeyEventType;
import ead.common.model.elements.guievents.enums.MouseGEvType;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.resources.assets.drawable.basics.Caption;
import ead.common.widgets.TextArea;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.factories.EventGOFactory;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.SceneElementGO;
import ead.engine.core.gameobjects.sceneelements.SceneElementGOImpl;
import ead.engine.core.input.InputAction;
import ead.engine.core.input.actions.KeyInputAction;
import ead.engine.core.input.actions.MouseInputAction;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.util.EAdTransformation;
import ead.tools.StringHandler;

public class TextAreaGO extends SceneElementGOImpl<TextArea> {

	private String currentText;

	private Caption textCaption;

	private SceneElementGO<?> textElement;

	private StringHandler stringHandler;

	@Inject
	public TextAreaGO(AssetHandler assetHandler,
			SceneElementGOFactory sceneElementFactory, GUI gui,
			GameState gameState, EventGOFactory eventFactory,
			StringHandler stringHandler) {
		super(assetHandler, sceneElementFactory, gui, gameState, eventFactory);
		this.stringHandler = stringHandler;
	}

	@Override
	public void setElement(TextArea element) {
		super.setElement(element);
		textCaption = new Caption(stringHandler.generateNewString());
		textCaption.setPreferredHeight(this.getHeight());
		textCaption.setPreferredWidth(this.getWidth());
		textElement = sceneElementFactory.get(new SceneElement(textCaption));
		textElement.getElement().setId("textCaption");
		textElement.setEnabled(false);
	}

	@Override
	public boolean processAction(InputAction<?> action) {
		super.processAction(action);
		if (action instanceof KeyInputAction) {
			KeyInputAction keyAction = (KeyInputAction) action;
			if (keyAction.getType() == KeyEventType.KEY_TYPED) {
				switch (keyAction.getKeyCode()) {
				case BACKSPACE:
					if (currentText.length() > 0) {
						currentText = currentText.substring(0,
								currentText.length() - 2);
					}
					break;				
				default:
					if (keyAction.getCharacter() != null) {
						currentText += keyAction.getCharacter();
					}
				}
				stringHandler.setString(textCaption.getLabel(), currentText);
			}
			action.consume();
			return true;
		} else if (action instanceof MouseInputAction) {
			MouseInputAction mouseAction = (MouseInputAction) action;
			if (mouseAction.getType() == MouseGEvType.PRESSED) {
				gameState.setActiveElement(element);
				action.consume();
				return true;
			}
		}
		return false;

	}

	public void update() {
		super.update();
		textElement.update();
	}

	@Override
	public void doLayout(EAdTransformation transformation) {
		super.doLayout(transformation);
		gui.addElement(textElement, transformation);
	}

}