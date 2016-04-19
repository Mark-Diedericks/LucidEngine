package com.lucid.lucidengine.mathematics;


/**
 * @author Mark Diedericks
 *
 */

public class Vertex {
	
	public static final int SIZE = 11;
	
	private Vector3f pos;
	private Vector2f textureUV;
	private Vector3f normal;
	public Vector3f tan;
	
	public Vertex(Vector3f pos, Vector2f textureUV, Vector3f norm, Vector3f tan) {
		this.pos = pos;
		this.textureUV = textureUV;
		this.normal = norm;
		this.tan = tan;
	}
	
	public Vertex(Vector3f pos, Vector2f textureUV, Vector3f norm) {
		this(pos,textureUV, norm, new Vector3f(0, 0, 0));
	}
	
	public Vertex(Vector3f pos, Vector2f textureUV) {
		this(pos, textureUV, new Vector3f(0, 0, 0));
	}
	
	public Vertex(Vector3f pos) {
		this(pos, new Vector2f(0, 0));
	}
	
	public Vector3f getPos() {
		return pos;
	}

	public void setPos(Vector3f pos) {
		this.pos = pos;
	}

	public Vector2f getTextureUV() {
		return textureUV;
	}

	public void setTextureUV(Vector2f textureUV) {
		this.textureUV = textureUV;
	}

	public Vector3f getNormal() {
		return normal;
	}

	public void setNormal(Vector3f normal) {
		this.normal = normal;
	}
	
	public Vector3f getTangent() {
		return tan;
	}

	public void setTangent(Vector3f tan) {
		this.tan = tan;
	}
}
