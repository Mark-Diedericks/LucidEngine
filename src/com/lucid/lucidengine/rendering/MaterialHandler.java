package com.lucid.lucidengine.rendering;

import java.util.HashMap;

/**
 * @author Mark Diedericks
 *
 */

public class MaterialHandler {
	
	private HashMap<String, Material> materials;
	
	public MaterialHandler() {
		materials = new HashMap<String, Material>();
	}
	
	public void addMaterial(String name, Material mat) {
		materials.put(name, mat);
	}
	
	public Material getMaterial(String name) {
		return materials.get(name);
	}
	
}
