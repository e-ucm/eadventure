package ead.reader.elements.readers;

import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.extra.EAdListImpl;
import ead.reader.elements.ElementsFactory;
import ead.reader.elements.XMLVisitor;
import ead.reader.elements.XMLVisitor.VisitorListener;
import ead.tools.xml.XMLNode;
import ead.tools.xml.XMLNodeList;

@SuppressWarnings("rawtypes")
public class ListReader extends AbstractReader<EAdList> {

	public ListReader(ElementsFactory elementsFactory, XMLVisitor visitor) {
		super(elementsFactory, visitor);
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public EAdList read(XMLNode node) {
		Class<?> clazz = this.getNodeClass(node);
		EAdList list = new EAdListImpl( clazz );
		XMLNodeList children = node.getChildNodes();
		for ( int i = 0; i < children.getLength(); i++){
			xmlVisitor.loadElement(children.item(i), new ListVisitorListener( list ));
		}
		return list;
	}
	
	public static class ListVisitorListener implements VisitorListener {
		private EAdList list;
		
		public ListVisitorListener( EAdList list ){
			this.list = list;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void loaded(Object object) {
			list.add(object);
		}
	
	}

}
