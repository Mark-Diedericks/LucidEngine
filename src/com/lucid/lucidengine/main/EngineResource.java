package com.lucid.lucidengine.main;


import java.io.InputStream;

/**
 * @author Mark Diedericks
 *
 */

public class EngineResource {
	
	public static InputStream getResource(String file) {
		return EngineResource.class.getResourceAsStream(file);
	}
	
}
