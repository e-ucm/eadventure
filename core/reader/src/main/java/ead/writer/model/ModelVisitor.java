package ead.writer.model;

import java.util.List;

import ead.tools.reflection.ReflectionProvider;
import ead.tools.xml.XMLNode;

public class ModelVisitor {

	private ReflectionProvider reflectionProvider;

	private List<WriterStep> stepsQueue;

	public ModelVisitor(ReflectionProvider reflectionProvider) {
		this.reflectionProvider = reflectionProvider;
	}

	public void writeElement(Object object, VisitorListener listener) {

	}

	public boolean step() {
		if (stepsQueue.isEmpty()) {
			return true;
		}

		WriterStep step = stepsQueue.get(0);
		Object o = step.getObject();
		VisitorListener listener = step.getListener();

		return false;
	}

	public static interface VisitorListener {

		void load(XMLNode node, Object object);

	}

	public static class WriterStep {
		private VisitorListener listener;
		private Object object;

		public WriterStep(VisitorListener listener, Object object) {
			super();
			this.listener = listener;
			this.object = object;
		}

		public VisitorListener getListener() {
			return listener;
		}

		public Object getObject() {
			return object;
		}

	}

}
