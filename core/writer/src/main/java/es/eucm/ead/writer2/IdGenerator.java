package es.eucm.ead.writer2;

import es.eucm.ead.tools.EAdUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class IdGenerator {

	private static final Logger logger = LoggerFactory.getLogger("IdGenerator");

	private int idsOrdinal;

	private List<String> exclusions;

	public IdGenerator() {
		exclusions = new ArrayList<String>();
	}

    public void addExclusion(String id){
        exclusions.add(id);
    }

	public void clear() {
		// Init aux vars
		idsOrdinal = 0;
		exclusions.clear();
	}

	public String generateNewId(String prefix) {
		boolean takenId = true;
		String id = null;
		while (takenId) {
			id = EAdUtils.generateId(prefix, idsOrdinal++);
			takenId = exclusions.contains(id);
			if (takenId) {
				logger.debug("Id {} is taken. Generating a new id...", id);
			}
		}
		return id;
	}
}
