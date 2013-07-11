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

package ead.converter;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ead.common.interfaces.features.Evented;
import ead.common.interfaces.features.WithBehavior;
import ead.common.model.assets.drawable.EAdDrawable;
import ead.common.model.assets.drawable.compounds.StateDrawable;
import ead.common.model.assets.drawable.filters.FilteredDrawable;
import ead.common.model.assets.drawable.filters.MatrixFilter;
import ead.common.model.elements.BasicElement;
import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.EAdElement;
import ead.common.model.elements.conditions.EmptyCond;
import ead.common.model.elements.effects.TriggerMacroEf;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.events.WatchFieldEv;
import ead.common.model.elements.huds.MouseHud;
import ead.common.model.elements.operations.BasicField;
import ead.common.model.elements.operations.EAdField;
import ead.common.model.elements.operations.ValueOp;
import ead.common.model.elements.predef.effects.ChangeAppearanceEf;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.params.fills.ColorFill;
import ead.common.model.params.fills.Paint;
import ead.common.model.params.guievents.MouseGEv;
import ead.common.model.params.paint.EAdPaint;
import ead.common.model.params.util.Matrix;
import ead.common.model.params.variables.EAdVarDef;
import ead.converter.resources.ResourcesConverter;
import ead.converter.subconverters.conditions.ConditionsConverter;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.resources.Resources;

import java.awt.*;
import java.util.List;

@Singleton
public class UtilsConverter {

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
	 * @param e
	 *            the element
	 * @param bundleId
	 *            cursor bundle id
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
		EAdDrawable drawable = resourcesConverter.getImage(path);
		Dimension d = resourcesConverter.getSize(path);
		// If dimension is greater than 600, we have to scale
		if (d.getHeight() > 600) {
			Matrix m = new Matrix();
			float scale = 600.0f / (float) d.getHeight();
			float diff = 800.0f - scale * 800.0f;
			if (diff > 0) {
				m.translate(diff / 2.0f, 0.0f, true);
			}
			m.scale(scale, scale, true);
			MatrixFilter filter = new MatrixFilter(m, 0.0f, 0.0f);
			drawable = new FilteredDrawable(drawable, filter);
		}

		return drawable;
	}
}
