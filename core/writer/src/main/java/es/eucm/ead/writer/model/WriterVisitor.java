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

package es.eucm.ead.writer.model;

import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.elements.extra.EAdMap;
import es.eucm.ead.model.interfaces.features.Identified;
import es.eucm.ead.model.params.EAdParam;
import es.eucm.ead.reader.DOMTags;
import es.eucm.ead.tools.reflection.ReflectionProvider;
import es.eucm.ead.tools.xml.XMLNode;
import es.eucm.ead.writer.model.writers.WriterContext;
import es.eucm.ead.writer.model.writers.ListWriter;
import es.eucm.ead.writer.model.writers.MapWriter;
import es.eucm.ead.writer.model.writers.ObjectWriter;
import es.eucm.ead.writer.model.writers.ParamWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class WriterVisitor {

	static private Logger logger = LoggerFactory.getLogger(WriterVisitor.class);

	private ReflectionProvider reflectionProvider;

	private ParamWriter paramWriter;

	private ListWriter listWriter;

	private MapWriter mapWriter;

	private ObjectWriter objectWriter;

	private List<WriterStep> stepsQueue;

	private WriterContext writerContext;

	public WriterVisitor(ReflectionProvider reflectionProvider,
			WriterContext writerContext) {
		this.reflectionProvider = reflectionProvider;
		this.writerContext = writerContext;
		stepsQueue = new ArrayList<WriterStep>();
		// writers
		paramWriter = new ParamWriter();
		listWriter = new ListWriter(this);
		mapWriter = new MapWriter(this);
		objectWriter = new ObjectWriter(this);
	}

	public void writeElement(Object object, Object parent,
			VisitorListener listener) {
		stepsQueue.add(new WriterStep(object, parent, listener));
	}

	public boolean step() {
		if (stepsQueue.isEmpty()) {
			return true;
		}

		WriterStep step = stepsQueue.remove(0);
		Object o = step.getObject();
		Class<?> clazz = o == null ? null : o.getClass();
		VisitorListener listener = step.getListener();

		XMLNode node;
		if (o == null) {
			// If the object is null, we don't care what tag to use. We just
			// create an empty param. While reading, a null will be retrieved
			node = new XMLNode(DOMTags.PARAM_TAG);
		} else if (reflectionProvider.isAssignableFrom(EAdParam.class, clazz)) {
			node = paramWriter.write(o, writerContext);
		} else if (reflectionProvider.isAssignableFrom(EAdList.class, clazz)) {
			node = listWriter.write((EAdList) o, writerContext);
		} else if (reflectionProvider.isAssignableFrom(EAdMap.class, clazz)) {
			node = mapWriter.write((EAdMap) o, writerContext);
		} else if (reflectionProvider.isAssignableFrom(Identified.class, clazz)) {
			node = objectWriter.write((Identified) o, writerContext);
		} else {
			node = paramWriter.write(o, writerContext);
		}

		if (node != null) {
			listener.load(node, o);
		}

		return stepsQueue.isEmpty();
	}

	public void finish() {
		while (!step())
			;
	}

	public void clear() {
		stepsQueue.clear();
	}

	public static class WriterStep {
		private VisitorListener listener;
		private Object object;
		private Object parent;

		public WriterStep(Object object, Object parent, VisitorListener listener) {
			super();
			this.listener = listener;
			this.object = object;
			this.parent = parent;
		}

		public VisitorListener getListener() {
			return listener;
		}

		public Object getObject() {
			return object;
		}

		public Object getParent() {
			return parent;
		}

	}

	public static interface VisitorListener {

		void load(XMLNode node, Object object);

	}

}
