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

package es.eucm.ead.writer2.model;

import es.eucm.ead.model.elements.BasicElement;
import es.eucm.ead.model.interfaces.features.Identified;
import es.eucm.ead.reader.DOMTags;
import es.eucm.ead.tools.xml.XMLNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ReferenceResolver {

	static private Logger logger = LoggerFactory
			.getLogger(ReferenceResolver.class);

	private Map<String, XMLNode> objects;

	private Map<Integer, Map<String, XMLNode>> pendingReferences;

	public ReferenceResolver() {
		objects = new HashMap<String, XMLNode>();
		pendingReferences = new HashMap<Integer, Map<String, XMLNode>>();
	}

	public void check(Identified i, XMLNode node, WriterContext context) {
		// We check if it is a reference
		if (i.getClass() == BasicElement.class) {
			// If the context contains the id, or we already have a pending reference with this id in this context,
			// we don't need to worry, just let the writer to create the reference
			// If not, we need to queue the reference
			Map<String, XMLNode> contextRefs = pendingReferences.get(context
					.getContextId());
			if ((contextRefs == null || !contextRefs.containsKey(i.getId()))
					&& !context.containsId(i.getId())) {
				if (contextRefs == null) {
					contextRefs = new HashMap<String, XMLNode>();
					pendingReferences.put(new Integer(context.getContextId()),
							contextRefs);
				}
				contextRefs.put(i.getId(), node);
			}
		} else {
			if (!objects.containsKey(i.getId())) {
				objects.put(i.getId(), node);
			}
		}

	}

	public void resolveReferences() {
		logger.debug("Resolving references...");
		for (Map.Entry<Integer, Map<String, XMLNode>> e : pendingReferences
				.entrySet()) {
			for (Map.Entry<String, XMLNode> ee : e.getValue().entrySet()) {
				XMLNode o = objects.get(ee.getKey());
				if (o == null) {
					logger.warn("Reference {} unresolved", ee.getKey());
				} else {
					XMLNode n = ee.getValue();
					n.setText(o.getNodeText());
					for (Map.Entry<String, String> en : o.getAttributes()
							.entrySet()) {
						if (!en.getKey().equals(DOMTags.FIELD_AT)) {
							n.setAttribute(en.getKey(), en.getValue());
						}
					}
					n.setChildren(o.getChildren());
				}
			}
		}
		logger.debug("References resolved.");
	}

	public void clear() {
		objects.clear();
		pendingReferences.clear();
	}
}
