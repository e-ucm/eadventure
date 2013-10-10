/**
 * eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the
 *    <e-UCM> research group.
 *
 *    Copyright 2005-2010 <e-UCM> research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    <e-UCM> is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure, version 2.0
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */

package es.eucm.ead.importer;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import es.eucm.ead.importer.resources.ResourcesConverter;
import es.eucm.ead.importer.subconverters.conditions.ConditionsConverter;
import es.eucm.ead.model.assets.drawable.EAdDrawable;
import es.eucm.ead.model.assets.drawable.compounds.StateDrawable;
import es.eucm.ead.model.assets.drawable.filters.FilteredDrawable;
import es.eucm.ead.model.assets.drawable.filters.MatrixFilter;
import es.eucm.ead.model.elements.BasicElement;
import es.eucm.ead.model.elements.EAdCondition;
import es.eucm.ead.model.elements.EAdElement;
import es.eucm.ead.model.elements.conditions.EmptyCond;
import es.eucm.ead.model.elements.effects.TriggerMacroEf;
import es.eucm.ead.model.elements.effects.variables.ChangeFieldEf;
import es.eucm.ead.model.elements.events.WatchFieldEv;
import es.eucm.ead.model.elements.huds.MouseHud;
import es.eucm.ead.model.elements.operations.BasicField;
import es.eucm.ead.model.elements.operations.EAdField;
import es.eucm.ead.model.elements.operations.ValueOp;
import es.eucm.ead.model.elements.predef.effects.ChangeAppearanceEf;
import es.eucm.ead.model.elements.scenes.EAdSceneElement;
import es.eucm.ead.model.interfaces.features.Evented;
import es.eucm.ead.model.interfaces.features.WithBehavior;
import es.eucm.ead.model.params.fills.ColorFill;
import es.eucm.ead.model.params.fills.Paint;
import es.eucm.ead.model.params.guievents.MouseGEv;
import es.eucm.ead.model.params.paint.EAdPaint;
import es.eucm.ead.model.params.util.Matrix;
import es.eucm.ead.model.params.variables.EAdVarDef;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.resources.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Singleton
public class UtilsConverter {

	static private Logger logger = LoggerFactory
			.getLogger(UtilsConverter.class);

	private ConditionsConverter conditionsConverter;

	private BasicElement cursor;

	private ResourcesConverter resourcesConverter;

	@Inject
	public UtilsConverter(ConditionsConverter conditionsConverter,
			ModelQuerier modelQuerier, ResourcesConverter resourcesConverter) {
		this.conditionsConverter = conditionsConverter;
		this.resourcesConverter = resourcesConverter;
		modelQuerier.setUtilsConverter(this);
		cursor = new BasicElement(MouseHud.CURSOR_ID);
	}

	/**
	 * Add conditions for the resources changes
	 *
	 * @param resources the list of resources
	 * @param e         the element affected
	 * @param stateVar  the variable controlling the state for the element
	 */
	public void addResourcesConditions(List<Resources> resources, EAdElement e,
			EAdVarDef<String> stateVar) {
		if (resources.size() > 1) {
			WatchFieldEv watchField = new WatchFieldEv();
			EAdField<String> stateField = new BasicField<String>(e, stateVar);
			// An effect that changes the state attending to the conditions
			TriggerMacroEf triggerMacro = new TriggerMacroEf();
			int i = 0;
			for (Resources r : resources) {
				EAdCondition cond = conditionsConverter.convert(r
						.getConditions());
				for (EAdField<?> f : conditionsConverter
						.getFieldsLastCondition()) {
					watchField.watchField(f);
				}
				ChangeFieldEf changeState = new ChangeFieldEf(stateField,
						new ValueOp(this.getResourceBundleId(i)));
				triggerMacro.putEffect(cond, changeState);
				i++;
			}

			watchField.addEffect(triggerMacro);
			((Evented) e).getEvents().add(watchField);

		}
	}

	public String getResourceBundleId(int index) {
		return "bundle" + index;
	}

	/**
	 * Watches the var in the definition
	 *
	 * @param sceneElement
	 * @param varState
	 */
	public void addWatchDefinitionField(EAdSceneElement sceneElement,
			EAdVarDef<String> varState) {
		WatchFieldEv watchField = new WatchFieldEv();
		watchField.watchField(new BasicField<String>(sceneElement
				.getDefinition(), varState));
		watchField.addEffect(new ChangeFieldEf(new BasicField<String>(
				sceneElement, varState), new BasicField<String>(sceneElement
				.getDefinition(), varState)));
		sceneElement.getEvents().add(watchField);
	}

	/**
	 * Simplifies a state drawables to its minimum
	 *
	 * @param drawable
	 * @return
	 */
	public EAdDrawable simplifyStateDrawable(StateDrawable drawable) {
		if (drawable.getDrawables().isEmpty()) {
			return null;
		} else if (drawable.getDrawablesCollection().size() == 1) {
			return drawable.getDrawable(drawable.getStates().iterator().next());
		}
		return drawable;
	}

	/**
	 * Add a event to watch a condition, and set the field to the value of the
	 * condition
	 *
	 * @param field
	 * @param c
	 */
	public void addWatchCondition(EAdSceneElement sceneElement,
			EAdField<Boolean> field, Conditions c) {
		EAdCondition cond = conditionsConverter.convert(c);
		// If condition is true, the scene element is always visible, so no need
		// for watch event
		if (!cond.equals(EmptyCond.TRUE)) {
			WatchFieldEv watchField = new WatchFieldEv();
			for (EAdField<?> f : conditionsConverter.getFieldsLastCondition()) {
				watchField.watchField(f);
			}
			watchField.addEffect(new ChangeFieldEf(field, cond));
			sceneElement.getEvents().add(watchField);
		}
	}

	/**
	 * Add mouse enter and mouse exit behavior to change the cursor when is over
	 * the given element
	 *
	 * @param e        the element
	 * @param bundleId cursor bundle id
	 */
	public void addCursorChange(WithBehavior e, String bundleId) {
		e.addBehavior(MouseGEv.MOUSE_ENTERED, new ChangeAppearanceEf(cursor,
				MouseHud.EXIT_CURSOR));
		e.addBehavior(MouseGEv.MOUSE_EXITED, new ChangeAppearanceEf(cursor,
				MouseHud.DEFAULT_CURSOR));
	}

	/**
	 * Returns a string transforming the given old color
	 *
	 * @param oldColor
	 * @return
	 */
	public String getColor(String oldColor) {
		if (oldColor != null) {
			String color = oldColor;
			if (oldColor.startsWith("#")) {
				color = color.substring(1);
			}
			while (color.length() < 6) {
				color = "0" + color;
			}

			return "0x" + color + "FF";
		} else {
			return "0x00000000";
		}
	}

	/**
	 * Get the proper paint for two colors in the old format
	 *
	 * @param fillColor
	 * @param borderColor
	 * @return
	 */
	public EAdPaint getPaint(String fillColor, String borderColor) {
		return new Paint(new ColorFill(getColor(fillColor)), new ColorFill(
				getColor(borderColor)));
	}

	public EAdDrawable getBackground(String path) {
		return getBackground(path, false);
	}

	public EAdDrawable getBackground(String path, boolean adjust) {
		EAdDrawable drawable = resourcesConverter.getImage(path);
		Dimension d = resourcesConverter.getSize(path);
		// If dimension is greater than 600, we have to scale
		if (d.getHeight() > 600) {
			Matrix m = new Matrix();
			float scaleY = 600.0f / (float) d.getHeight();
			float scaleX = adjust ? 800.0f / (float) d.getWidth() : scaleY;
			float diff = 800.0f - scaleX * 800.0f;
			if (diff > 0) {
				m.translate(diff / 2.0f, 0.0f, true);
			}
			m.scale(scaleX, scaleY, true);
			MatrixFilter filter = new MatrixFilter(m, 0.0f, 0.0f);
			drawable = new FilteredDrawable(drawable, filter);
		}

		return drawable;
	}

	/**
	 * Takes the mask from a scene, and creates the resulting image of applying it to a background image.
	 * It writes the image in the foreground mask destination path
	 *
	 * @param foregroundPath
	 * @param backgroundPath
	 * @return the new path for the foreground
	 */
	public String applyForegroundMask(String foregroundPath,
			String backgroundPath) {
		BufferedImage foreground = resourcesConverter.loadImage(foregroundPath);
		BufferedImage background = resourcesConverter.loadImage(backgroundPath);

		String newUri = resourcesConverter.getPath(foregroundPath);

		if (!newUri.endsWith(".png")) {
			newUri += ".png";
		}

		if (foreground != null && background != null) {
			int width = foreground.getWidth();
			int height = foreground.getHeight();

			int[] backgroundPixels = background.getRGB(0, 0, width, height,
					null, 0, width);
			int[] maskPixels = foreground.getRGB(0, 0, width, height, null, 0,
					width);

			int[] resultPixels = new int[maskPixels.length];

			for (int i = 0; i < backgroundPixels.length; i++) {
				int color = backgroundPixels[i];
				int mask = maskPixels[i];

				if (mask != 0xffffffff) {
					resultPixels[i] = color;
				} else {
					resultPixels[i] = 0x00000000;
				}

			}
			BufferedImage result = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_ARGB);
			result.getRaster().setDataElements(0, 0, width, height,
					resultPixels);

			try {
				ImageIO.write(result, "png", new File(resourcesConverter
						.getProjectFolder(), newUri.substring(1)));
			} catch (IOException e) {
				logger.error("Error creating foreground image {}",
						foregroundPath, e);
			}

			foreground.flush();
			background.flush();
			result.flush();
		}
		return newUri;
	}
}
