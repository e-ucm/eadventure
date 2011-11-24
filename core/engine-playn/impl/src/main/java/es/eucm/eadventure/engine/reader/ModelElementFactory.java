package es.eucm.eadventure.engine.reader;

import java.util.logging.Logger;

import com.gwtent.reflection.client.ClassType;
import com.gwtent.reflection.client.TypeOracle;

import es.eucm.eadventure.common.model.EAdElement;

public class ModelElementFactory {

	private final static Logger logger = Logger.getLogger("ModelElementFactory");
	
	private static ModelElementFactory instance;
	
	private ModelElementFactory() {
		
		
	}
	
	private EAdElement get(String name, String id) {
		ClassType<?> classType = TypeOracle.Instance.getClassType(name);
		if (classType.findConstructor() != null) {
			EAdElement element =  (EAdElement) classType.findConstructor().newInstance();
			element.setId(id);
			return element;
		}
		
		logger.severe("Cant get instace " + name);
		return null;
	}
	
	public static EAdElement getInstance(String name, String id) {
		if (instance == null)
			instance = new ModelElementFactory();
		return instance.get(name, id);
		
		
	}
}
