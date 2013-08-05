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

package ead.writer.model.writers;

import java.util.ArrayList;

import es.eucm.ead.model.params.EAdParam;
import es.eucm.ead.model.params.guievents.EAdGUIEvent;
import es.eucm.ead.model.params.paint.EAdPaint;
import es.eucm.ead.model.params.variables.VarDef;
import ead.reader.DOMTags;
import ead.tools.xml.XMLNode;
import ead.writer.model.ModelVisitor;

public class ParamWriter extends AbstractWriter<Object> {

	private ArrayList<String> params;

	public ParamWriter(ModelVisitor modelVisitor) {
		super(modelVisitor);
		params = new ArrayList<String>();
	}

	@Override
	public XMLNode write(Object o) {
		if (o == null) {
			return null;
		}

		XMLNode node = modelVisitor.newNode(DOMTags.PARAM_TAG);
		if (o != null) {
			String translatedClass = translateClass(o.getClass());
			node.setAttribute(DOMTags.CLASS_AT, translatedClass);
			String value;
			if (o instanceof VarDef || o instanceof EAdPaint
					|| o instanceof Enum || o instanceof EAdGUIEvent) {
				value = translateParam(o.toString());
			} else if (o instanceof EAdParam) {
				value = ((EAdParam) o).toStringData();
			} else if (o instanceof Class) {
				value = ((Class<?>) o).getName();
			} else if (o instanceof Boolean) {
				value = ((Boolean) o).booleanValue() ? "t" : "f";
			} else if (o instanceof Float) {
				value = o.toString();
				if (value.endsWith(".0")) {
					value = value.substring(0, value.length() - 2);
				}
			} else {
				value = o.toString();
			}
			node.setText(value);
			if (!params.contains(value)) {
				params.add(value);
			}
		}

		return node;
	}

	public int getTotal() {
		return params.size();
	}

	public void clear() {
		this.params.clear();
	}
}
