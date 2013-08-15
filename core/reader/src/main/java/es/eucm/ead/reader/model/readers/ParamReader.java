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

package es.eucm.ead.reader.model.readers;

import es.eucm.ead.model.params.fills.ColorFill;
import es.eucm.ead.model.params.fills.LinearGradientFill;
import es.eucm.ead.model.params.fills.Paint;
import es.eucm.ead.model.params.guievents.DragGEv;
import es.eucm.ead.model.params.guievents.KeyGEv;
import es.eucm.ead.model.params.guievents.MouseGEv;
import es.eucm.ead.model.params.variables.VarDef;
import es.eucm.ead.reader.model.ObjectsFactory;
import es.eucm.ead.reader.model.XMLVisitor;
import es.eucm.ead.tools.xml.XMLNode;

/**
 * Parameters can be any type of object. Nodes with parameters have no children.
 * 
 */
public class ParamReader extends AbstractReader<Object> {

	public ParamReader(ObjectsFactory elementsFactory, XMLVisitor xmlVisitor) {
		super(elementsFactory, xmlVisitor);
	}

	@Override
	public Object read(XMLNode node) {
		Class<?> clazz = getNodeClass(node);
		String value = node.getNodeText();
		if (clazz.isEnum() || clazz == VarDef.class || clazz == ColorFill.class
				|| clazz == Paint.class || clazz == LinearGradientFill.class
				|| clazz == MouseGEv.class || clazz == KeyGEv.class
				|| clazz == DragGEv.class) {
			value = translateParam(value);
		}
		return elementsFactory.getParam(value, clazz);
	}

}
