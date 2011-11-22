package es.eucm.eadventure.common.impl.DOMreader;

import java.lang.reflect.Constructor;
import java.util.logging.Logger;

import org.w3c.dom.Node;

import es.eucm.eadventure.common.impl.DOMTags;
import es.eucm.eadventure.common.impl.reader.subparsers.extra.ObjectFactory;
import es.eucm.eadventure.common.model.EAdElement;

public class ElementNodeVisitor extends NodeVisitor<EAdElement> {
	
	public static final String TAG = "element";

	protected static final Logger logger = Logger.getLogger("ElementNodeVisitor");

	@Override
	public EAdElement visit(Node node) {
		String value = node.getNodeValue();
		if (value != null)
			return (EAdElement) ObjectFactory.getObject(value, EAdElement.class);
		
		String uniqueId = node.getAttributes().getNamedItem(DOMTags.UNIQUE_ID_AT).getNodeValue();
		String id = node.getAttributes().getNamedItem(DOMTags.ID_AT).getNodeValue();

		String clazz = node.getAttributes().getNamedItem(loaderType).getNodeValue();
		clazz = translateClass(clazz);
		
		EAdElement element = null;
		Class<?> c = null;
		try {
			c = ClassLoader.getSystemClassLoader().loadClass(clazz);
			Constructor<?> con = c.getConstructor(String.class);
			element = (EAdElement) con.newInstance(new Object[] { id });
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			Constructor<?> con;
			try {
				con = c.getConstructor();
				element = (EAdElement) con.newInstance();
			} catch (NoSuchMethodException e1) {
				logger.info("You must define a constructor without parameters or a constructor only with the id for the class "
						+ c + " in order to make work XML read and write");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception e) {

		}
		if (element != null)
			ObjectFactory.addElement(uniqueId, element);

		readFields(element, node);
		
		return element;
	}
	
	


	@Override
	public String getNodeType() {
		return TAG;
	}

}
