package es.eucm.eadventure.engine.core.debuggers.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.variables.EAdVarDef;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.params.EAdFont;
import es.eucm.eadventure.common.params.EAdFontImpl;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.CaptionImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.RectangleShape;
import es.eucm.eadventure.common.resources.assets.drawable.compounds.ComposedDrawable;
import es.eucm.eadventure.common.resources.assets.drawable.compounds.impl.ComposedDrawableImpl;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.debuggers.EAdDebugger;
import es.eucm.eadventure.engine.core.gameobjects.DrawableGO;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.gameobjects.factories.SceneElementGOFactory;

public class FieldsDebugger implements EAdDebugger {

	private EAdElement element;

	private MouseState mouseState;

	private List<DrawableGO<?>> gos;

	private ValueMap valueMap;

	private EAdBasicSceneElement vars;

	private StringHandler stringHandler;

	private SceneElementGOFactory gameObjectFactory;

	private EAdFont font = new EAdFontImpl(12);

	private EAdColor color = new EAdColor(120, 120, 120, 50);

	@Inject
	public FieldsDebugger(MouseState mouseState, ValueMap valueMap,
			StringHandler stringHandler, SceneElementGOFactory gameObjectFactory) {
		this.mouseState = mouseState;
		this.valueMap = valueMap;
		this.stringHandler = stringHandler;
		this.gameObjectFactory = gameObjectFactory;
		gos = new ArrayList<DrawableGO<?>>();
		vars = new EAdBasicSceneElement("vars");
		vars.setVarInitialValue(EAdBasicSceneElement.VAR_ENABLE, false);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<DrawableGO<?>> getGameObjects() {
		if (mouseState.getGameObjectUnderMouse() != null
				&& mouseState.getGameObjectUnderMouse().getElement() != element
				&& mouseState.getGameObjectUnderMouse().getElement() instanceof EAdElement) {
			element = (EAdElement) mouseState.getGameObjectUnderMouse()
					.getElement();
			gos.clear();
			if (element != null) {
				Map<EAdVarDef<?>, Object> fields = valueMap
						.getElementVars(element);

				ComposedDrawable d = new ComposedDrawableImpl();
				RectangleShape shape = new RectangleShape(300, 20 * (fields
						.keySet().size() + 2));
				shape.setPaint(color);
				d.addDrawable(shape, -10, -10);
				CaptionImpl c = new CaptionImpl();
				stringHandler.setString(c.getText(), element + "");
				c.setFont(font);
				c.setTextPaint(EAdColor.RED);
				c.setBubblePaint(EAdColor.WHITE);
				c.setPadding(2);
				d.addDrawable(c, 0, 0);
				int yOffset = 20;
				for (EAdVarDef<?> var : fields.keySet()) {
					c = new CaptionImpl();
					stringHandler.setString(c.getText(), var.getName() + "=#0");
					c.getFields().add(new EAdFieldImpl(element, var));
					c.setFont(font);
					c.setTextPaint(EAdColor.WHITE);
					c.setBubblePaint(EAdColor.BLACK);
					c.setPadding(2);
					d.addDrawable(c, 0, yOffset);
					yOffset += 20;
				}

				vars.setPosition(10, 10);
				vars.getResources().addAsset(vars.getInitialBundle(),
						EAdBasicSceneElement.appearance, d);

				gos.add(gameObjectFactory.get(vars));

			}

		}

		for (GameObject<?> go : gos) {
			go.update();
		}
		return gos;
	}

}
