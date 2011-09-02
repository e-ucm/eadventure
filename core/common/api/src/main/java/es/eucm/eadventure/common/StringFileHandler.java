package es.eucm.eadventure.common;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import es.eucm.eadventure.common.resources.EAdString;

public interface StringFileHandler {
	
	Map<EAdString, String> read( InputStream inputStream );
	
	boolean write( OutputStream outputStream, Map<EAdString, String> strings);

}
