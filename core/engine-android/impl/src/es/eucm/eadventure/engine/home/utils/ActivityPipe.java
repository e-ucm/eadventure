package es.eucm.eadventure.engine.home.utils;

import java.util.HashMap;

public class ActivityPipe {

	private static HashMap<String, Object> pipe = new HashMap<String, Object>();

	public static String add(Object e) {

		synchronized (pipe) {

			String key = String.valueOf(System.currentTimeMillis());

			pipe.put(key, e);

			return key;

		}

	}

	
	public static void add(Object e,String key) {

		synchronized (pipe) {

			pipe.put(key, e);

		}

	}
	
	
	public static Object remove(String key) {

		synchronized (pipe) {

			return pipe.remove(key);

		}
	}
	
	public static boolean exists(String key) {

		synchronized (pipe) {

			return pipe.containsKey(key);

		}
	}
}
