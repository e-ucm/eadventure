package ead.writer.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.common.model.assets.AssetDescriptor;
import ead.common.model.elements.EAdElement;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.extra.EAdMap;
import ead.common.model.params.EAdParam;
import ead.reader.DOMTags;
import ead.tools.reflection.ReflectionProvider;
import ead.tools.xml.XMLDocument;
import ead.tools.xml.XMLNode;
import ead.tools.xml.XMLParser;
import ead.writer.model.writers.ListWriter;
import ead.writer.model.writers.MapWriter;
import ead.writer.model.writers.ObjectWriter;
import ead.writer.model.writers.ParamWriter;

public class ModelVisitor {

	private static final Logger logger = LoggerFactory
			.getLogger("ModelVisitor");

	private ReflectionProvider reflectionProvider;

	private XMLParser xmlParser;

	private boolean addedToRoot;

	private XMLNode classes;

	private ParamWriter paramWriter;

	private ListWriter listWriter;

	private MapWriter mapWriter;

	private ObjectWriter objectWriter;

	private XMLDocument currentDocument;

	private List<WriterStep> stepsQueue;

	private Map<Class<?>, String> translations;

	private XMLNode root;

	public ModelVisitor(ReflectionProvider reflectionProvider, XMLParser parser) {
		this.reflectionProvider = reflectionProvider;
		this.reflectionProvider = reflectionProvider;
		this.xmlParser = parser;

		this.stepsQueue = new ArrayList<WriterStep>();
		this.translations = new LinkedHashMap<Class<?>, String>();
		// writers
		paramWriter = new ParamWriter(this);
		listWriter = new ListWriter(this);
		mapWriter = new MapWriter(this);
		objectWriter = new ObjectWriter(this);
	}

	public void writeElement(Object object, VisitorListener listener) {
		stepsQueue.add(new WriterStep(object, listener));
	}

	@SuppressWarnings("rawtypes")
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
			node = newNode(DOMTags.PARAM_TAG);
		} else if (reflectionProvider.isAssignableFrom(EAdParam.class, clazz)) {
			node = paramWriter.write(o);
		} else if (reflectionProvider.isAssignableFrom(AssetDescriptor.class,
				clazz)) {
			objectWriter.setAsset(true);
			node = objectWriter.write((AssetDescriptor) o);
		} else if (reflectionProvider.isAssignableFrom(EAdElement.class, clazz)) {
			objectWriter.setAsset(false);
			node = objectWriter.write((EAdElement) o);
		} else if (reflectionProvider.isAssignableFrom(EAdList.class, clazz)) {
			node = listWriter.write((EAdList) o);
		} else if (reflectionProvider.isAssignableFrom(EAdMap.class, clazz)) {
			node = mapWriter.write((EAdMap) o);
		} else {
			node = paramWriter.write(o);
		}

		if (node == null) {
			logger
					.debug("object ignored beacause it was null or an empty list/map");
		} else {
			listener.load(node, o);
			if (!addedToRoot) {
				root.append(node);
				addedToRoot = true;
			}
		}
		return stepsQueue.isEmpty();
	}

	public void clear() {
		// Create a new document
		this.currentDocument = xmlParser.createDocument();
		root = currentDocument.newNode(DOMTags.ROOT_TAG);
		currentDocument.appendChild(root);
		classes = currentDocument.newNode(DOMTags.CLASSES_TAG);
		root.append(classes);
		addedToRoot = false;
		objectWriter.clear();
	}

	public String translateClass(Class<?> clazz) {
		String value = translations.get(clazz);
		if (value == null) {
			value = Integer.toHexString(translations.size());
			translations.put(clazz, value);
			XMLNode entry = currentDocument.newNode(DOMTags.ENTRY_TAG);
			entry.setAttribute(DOMTags.KEY_AT, value);
			entry.setAttribute(DOMTags.VALUE_AT, clazz.getName());
			classes.append(entry);
		}
		return value;
	}

	public XMLNode newNode(String tag) {
		return currentDocument.newNode(tag);
	}

	public XMLDocument getDocument() {
		return currentDocument;
	}

	public static class WriterStep {
		private VisitorListener listener;
		private Object object;

		public WriterStep(Object object, VisitorListener listener) {
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

	public static interface VisitorListener {

		void load(XMLNode node, Object object);

	}

}
