package com.lucid.lucidengine.rendering.mesh;

/**
 * @author Mark Diedericks
 *
 */

public class OBJIndex {
	public int vertexIndex;
	public int textureIndex;
	public int normalIndex;
	
	@Override
	public boolean equals(Object obj) {
		OBJIndex index = (OBJIndex)obj;
		return vertexIndex == index.vertexIndex && textureIndex == index.textureIndex && normalIndex == index.normalIndex;
	}
	
	@Override
	public int hashCode() {
		final int BASE = 17;
		final int MULTI = 31;
		int result =  BASE;
		
		result = (MULTI * result) + vertexIndex;
		result = (MULTI * result) + textureIndex;
		result = (MULTI * result) + normalIndex;
		
		return result;
	}
}
