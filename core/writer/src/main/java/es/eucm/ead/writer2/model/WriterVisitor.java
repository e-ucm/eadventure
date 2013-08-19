package es.eucm.ead.writer2.model;

import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.elements.extra.EAdMap;
import es.eucm.ead.model.interfaces.features.Identified;
import es.eucm.ead.model.params.EAdParam;
import es.eucm.ead.reader.DOMTags;
import es.eucm.ead.tools.reflection.ReflectionProvider;
import es.eucm.ead.tools.xml.XMLNode;
import es.eucm.ead.writer2.model.writers.ListWriter;
import es.eucm.ead.writer2.model.writers.MapWriter;
import es.eucm.ead.writer2.model.writers.ObjectWriter;
import es.eucm.ead.writer2.model.writers.ParamWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class WriterVisitor {

	private static final Logger logger = LoggerFactory
			.getLogger("WriterVisitor");

	private ReflectionProvider reflectionProvider;

	private ParamWriter paramWriter;

	private ListWriter listWriter;

	private MapWriter mapWriter;

	private ObjectWriter objectWriter;

	private List<WriterStep> stepsQueue;

	private List<FinalStep> finalStepsQueue;

	private WriterContext writerContext;

	public WriterVisitor(ReflectionProvider reflectionProvider,
			WriterContext writerContext) {
		this.reflectionProvider = reflectionProvider;
		this.writerContext = writerContext;
		stepsQueue = new ArrayList<WriterStep>();
		finalStepsQueue = new ArrayList<FinalStep>();

		// writers
		paramWriter = new ParamWriter(this);
		listWriter = new ListWriter(this);
		mapWriter = new MapWriter(this);
		objectWriter = new ObjectWriter(this);
	}

	public void addFinalStep(FinalStep step) {
		finalStepsQueue.add(step);
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

		XMLNode node = null;
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

    public void clear(){
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

	public static interface FinalStep {
		void execute();
	}

	public static interface VisitorListener {

		void load(XMLNode node, Object object);

	}

}
