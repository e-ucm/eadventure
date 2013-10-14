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

package es.eucm.ead.writer2.model.writers;

import es.eucm.ead.model.params.EAdParam;
import es.eucm.ead.model.params.guievents.EAdGUIEvent;
import es.eucm.ead.model.params.paint.EAdPaint;
import es.eucm.ead.model.params.variables.VarDef;
import es.eucm.ead.reader.DOMTags;
import es.eucm.ead.tools.xml.XMLNode;
import es.eucm.ead.writer2.model.WriterContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParamWriter implements Writer<Object> {

	static private Logger logger = LoggerFactory.getLogger(ParamWriter.class);

	@Override
	public XMLNode write(Object o, WriterContext context) {
		if (o == null) {
			return null;
		}

		XMLNode node = new XMLNode(DOMTags.PARAM_TAG);
		String translatedClass = context.translateClass(o.getClass());
		node.setAttribute(DOMTags.CLASS_AT, translatedClass);
		String value;
		if (o instanceof VarDef || o instanceof EAdPaint || o instanceof Enum
				|| o instanceof EAdGUIEvent) {
			value = context.translateParam(o.toString());
		} else if (o instanceof EAdParam) {
			value = ((EAdParam) o).toStringData();
		} else if (o instanceof Class) {
			value = ((Class<?>) o).getName();
		} else if (o instanceof Boolean) {
			value = ((Boolean) o).booleanValue() ? "t" : "f";
		} else if (o instanceof Number) {
			value = o.toString();
			if (value.endsWith(".0")) {
				value = value.substring(0, value.length() - 2);
			}
		} else if (o instanceof String) {
			value = o.toString();
		} else {
			logger.warn(
					"No writer for class {}. Using its string representation",
					o.getClass());
			value = o.toString();
		}
		node.setText(value);

		return node;
	}

}
