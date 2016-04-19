package com.lucid.lucidengine.mathematics;

/**
 * @author Mark Diedericks
 *
 */

public class Vector4f {
	
	private float x;
	private float y;
	private float z;
	private float w;
	
	public Vector4f(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	
	public float getLength() {
		return (float) Math.sqrt(x * x + y * y + z * z + w * w);
	}
	
	public float dot(Vector4f vec4f) {
		return this.x * vec4f.getX() + this.y * vec4f.getY() + this.z * vec4f.getZ() + this.w * vec4f.getZ();
	}
	
	public float max() {
		return Math.max(x, Math.max(y, Math.max(z, w)));
	}
	
	public Vector4f cross(Vector4f vec4f) {
		float x_ = y * vec4f.getZ() - z * vec4f.getY();
		float y_ = z * vec4f.getX() - x * vec4f.getZ();
		float z_ = x * vec4f.getY() - y * vec4f.getX();
		
		return new Vector4f(x_, y_, z_, w);
	}
	
	public Vector4f lerp(Vector4f dest, float factor) {
		return dest.sub(this).mul(factor).add(this);
	}
	
	public Vector4f normalize() {
		float length = this.getLength();
		
		x /= length;
		y /= length;
		z /= length;
		w /= length;
		
		return this;
	}
	
	public Vector4f abs() {
		return new Vector4f((float)Math.abs(x), (float)Math.abs(y), (float)Math.abs(z), (float)Math.abs(w));
	}
	
	public void addEq(Vector4f vec4f) {
		this.x += vec4f.getX(); this.y += vec4f.getY(); this.z += vec4f.getZ(); this.w += vec4f.getW();
	}
	
	public void addEq(float vec4f) {
		this.x += vec4f; this.y += vec4f; this.z += vec4f; this.w += vec4f;
	}
	
	public void subEq(Vector4f vec4f) {
		this.x -= vec4f.getX(); this.y -= vec4f.getY(); this.z -= vec4f.getZ(); this.w -= vec4f.getW();
	}
	
	public void subEq(float vec4f) {
		this.x -= vec4f; this.y -= vec4f; this.z -= vec4f; this.w /= vec4f; this.w -= vec4f;
	}
	
	public void mulEq(Vector4f vec4f) {
		this.x *= vec4f.getX(); this.y *= vec4f.getY(); this.z *= vec4f.getZ(); this.w *= vec4f.getW();
	}
	
	public void mulEq(float vec4f) {
		this.x *= vec4f; this.y *= vec4f; this.z *= vec4f; this.w *= vec4f;
	}
	
	public void divEq(Vector4f vec4f) {
		this.x /= vec4f.getX(); this.y /= vec4f.getY(); this.z /= vec4f.getZ(); this.w /= vec4f.getW();
	}
	
	public void divEq(float vec4f) {
		this.x /= vec4f; this.y /= vec4f; this.z /= vec4f; this.w /= vec4f;
	}
	
	public Vector4f add(Vector4f vec4f) {
		return new Vector4f(this.x + vec4f.getX(), this.y + vec4f.getY(), this.z + vec4f.getZ(), this.w + vec4f.getW());
	}
	
	public Vector4f add(float vec4f) {
		return new Vector4f(this.x + vec4f, this.y + vec4f, this.z + vec4f, this.w + vec4f);
	}
	
	public Vector4f sub(Vector4f vec4f) {
		return new Vector4f(this.x - vec4f.getX(), this.y - vec4f.getY(), this.z - vec4f.getZ(), this.w - vec4f.getW());
	}
	
	public Vector4f sub(float vec4f) {
		return new Vector4f(this.x - vec4f, this.y - vec4f, this.z - vec4f, this.w - vec4f);
	}
	
	public Vector4f mul(Vector4f vec4f) {
		return new Vector4f(this.x * vec4f.getX(), this.y * vec4f.getY(), this.z * vec4f.getZ(), this.w * vec4f.getW());
	}
	
	public Vector4f mul(float vec4f) {
		return new Vector4f(this.x * vec4f, this.y * vec4f, this.z * vec4f, this.w * vec4f);
	}
	
	public Vector4f div(Vector4f vec4f) {
		return new Vector4f(this.x / vec4f.getX(), this.y / vec4f.getY(), this.z / vec4f.getZ(), this.w / vec4f.getW());
	}
	
	public Vector4f div(float vec4f) {
		return new Vector4f(this.x / vec4f, this.y / vec4f, this.z / vec4f, this.w / vec4f);
	}
	
	public String toString() {
		return "(" + x + " " + y + z + ")";
	}
	
	public float getX() {
		return this.x;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public float getY() {
		return this.y;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public float getZ() {
		return this.z;
	}
	
	public void setZ(float z) {
		this.z = z;
	}
	
	public float getW() {
		return w;
	}
	
	public void setW(float w) {
		this.w = w;
	}
	
	public Vector4f set(Vector4f vec4f) {
		return this.setVector(vec4f.getX(), vec4f.getY(), vec4f.getZ(), vec4f.getW());
	}
	
	public Vector4f setVector(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		return this;
	}
	
	public Vector2f getXY() {
		return new Vector2f(x,y);
	}
	
	public Vector2f getXZ() {
		return new Vector2f(x,z);
	}
	
	public Vector2f getXW() {
		return new Vector2f(x,w);
	}
	
	public Vector2f getYX() {
		return new Vector2f(y,x);
	}
	
	public Vector2f getYZ() {
		return new Vector2f(y,z);
	}
	
	public Vector2f getYW() {
		return new Vector2f(y,w);
	}
	
	public Vector2f getZX() {
		return new Vector2f(z,x);
	}
	
	public Vector2f getZY() {
		return new Vector2f(z,y);
	}
	
	public Vector2f getZW() {
		return new Vector2f(z,w);
	}
	
	public Vector2f getWX() {
		return new Vector2f(w,x);
	}
	
	public Vector2f getWY() {
		return new Vector2f(w,y);
	}
	
	public Vector2f getWZ() {
		return new Vector2f(w,z);
	}
	
	
	
	public Vector3f getXYZ() {
		return new Vector3f(x,y,z);
	}
	
	public Vector3f getXYW() {
		return new Vector3f(x,y,w);
	}
	
	public Vector3f getXZY() {
		return new Vector3f(x,z,y);
	}
	
	public Vector3f getXZW() {
		return new Vector3f(x,w,x);
	}
	
	public Vector3f getXWY() {
		return new Vector3f(x,w,y);
	}
	
	public Vector3f getXWZ() {
		return new Vector3f(x,w,z);
	}
	
	
	public Vector3f getYXZ() {
		return new Vector3f(y,x,z);
	}
	
	public Vector3f getYXW() {
		return new Vector3f(y,x,w);
	}
	
	public Vector3f getYZX() {
		return new Vector3f(y,z,x);
	}
	
	public Vector3f getYZW() {
		return new Vector3f(y,z,w);
	}
	
	public Vector3f getYWX() {
		return new Vector3f(y,w,x);
	}
	
	public Vector3f getYWZ() {
		return new Vector3f(y,w,z);
	}
	
	
	public Vector3f getZXY() {
		return new Vector3f(z,x,y);
	}
	
	public Vector3f getZXW() {
		return new Vector3f(z,x,w);
	}
	
	public Vector3f getZYX() {
		return new Vector3f(z,y,x);
	}
	
	public Vector3f getZYW() {
		return new Vector3f(z,y,w);
	}
	
	public Vector3f getZWX() {
		return new Vector3f(z,w,x);
	}
	
	public Vector3f getZWY() {
		return new Vector3f(z,w,y);
	}
	
	//TODO 
	public Vector3f getWXY() {
		return new Vector3f(w,x,y);
	}
	
	public Vector3f getWXZ() {
		return new Vector3f(w,x,z);
	}
	
	public Vector3f getWYX() {
		return new Vector3f(w,y,x);
	}
	
	public Vector3f getWYZ() {
		return new Vector3f(w,y,z);
	}
	
	public Vector3f getWZX() {
		return new Vector3f(w,z,x);
	}
	
	public Vector3f getWZY() {
		return new Vector3f(w,z,y);
	}
	
	public boolean equals(Vector4f vec4f) {
		return x == vec4f.getX() && y == vec4f.getY() && z == vec4f.getZ()&& w == vec4f.getW();
	}

	public Vector4f max(Vector4f vec4f) {
		float x = Math.max(this.x, vec4f.getX());
		float y = Math.max(this.y, vec4f.getY());
		float z = Math.max(this.z, vec4f.getZ());
		float w = Math.max(this.w, vec4f.getW());
		return new Vector4f(x, y, z, w);
	}


	public Vector4f reflect(Vector4f vec4f) {
		return this.sub(vec4f.mul(this.dot(vec4f) * 2.0f));
	}
}
