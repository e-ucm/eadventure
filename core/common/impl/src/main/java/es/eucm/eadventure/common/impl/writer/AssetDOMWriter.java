package es.eucm.eadventure.common.impl.writer;

import java.lang.reflect.Field;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;

import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.resources.assets.AssetDescriptor;

public class AssetDOMWriter extends DOMWriter<AssetDescriptor>{

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Element buildNode(AssetDescriptor assetDescriptor) {
		try {
			this.initilizeDOMWriter();
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		}
		node = doc.createElement("asset");
		int index = mappedAsset.indexOf(assetDescriptor);
		if (index != -1) {
			node.setTextContent("" + index);
			return node;
		}

		node.setAttribute("uniqueId", mappedAsset.size() + "");
		mappedAsset.add(assetDescriptor);
		node.setAttribute("class", assetDescriptor.getClass()
				.getName());

		Class<?> clazz = assetDescriptor.getClass();
		try {
			while (clazz != null) {
				Field[] fields = clazz.getDeclaredFields();
				for (Field field : fields) {
					Param param = field.getAnnotation(Param.class);
					if (param != null) {
						boolean accessible = field.isAccessible();
						field.setAccessible(true);
						Object o;

						o = field.get(assetDescriptor);

						if (o != null) {
							DOMWriter writer = super.getDOMWriter(o);
							Element newNode = writer.buildNode(o);
							newNode.setAttribute("param", param.value());
							doc.adoptNode(newNode);
							node.appendChild(newNode);

						}

						field.setAccessible(accessible);
					}

				}
				clazz = clazz.getSuperclass();
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return node;
	}

}
