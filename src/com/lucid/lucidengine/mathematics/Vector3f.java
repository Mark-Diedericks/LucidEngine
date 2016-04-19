package com.lucid.lucidengine.mathematics;

/**
 * @author Mark Diedericks
 *
 */

public class Vector3f {
	
	private float x;
	private float y;
	private float z;
	
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	
	public float getLength() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}
	
	public float dot(Vector3f vec3f) {
		return this.x * vec3f.getX() + this.y * vec3f.getY() + this.z * vec3f.getZ();
	}
	
	public float max() {
		return Math.max(x, Math.max(y, z));
	}
	
	public Vector3f cross(Vector3f vec3f) {
		float x_ = y * vec3f.getZ() - z * vec3f.getY();
		float y_ = z * vec3f.getX() - x * vec3f.getZ();
		float z_ = x * vec3f.getY() - y * vec3f.getX();
		
		return new Vector3f(x_, y_, z_);
	}
	
	public Vector3f lerp(Vector3f dest, float factor) {
		return dest.sub(this).mul(factor).add(this);
	}
	
	public Vector3f normalize() {
		float length = this.getLength();
		
		x /= length;
		y /= length;
		z /= length;
		
		return this;
	}
	
	public Vector3f rotate(Vector3f axis, float angle)
	{
		float sinAngle = (float)Math.sin(-angle);
		float cosAngle = (float)Math.cos(-angle);

		return this.cross(axis.mul(sinAngle)).add((this.mul(cosAngle)).add(axis.mul(this.dot(axis.mul(1 - cosAngle)))));
	}

	public Vector3f rotate(Quaternion rotation)
	{
		Quaternion conjugate = rotation.conjugate();

		Quaternion w = rotation.mul(this).mul(conjugate);

		return new Vector3f(w.getX(), w.getY(), w.getZ());
	}
	
	public Vector3f abs() {
		return new Vector3f((float)Math.abs(x), (float)Math.abs(y), (float)Math.abs(z));
	}
	
	public void addEq(Vector3f vec3f) {
		this.x += vec3f.getX(); this.y += vec3f.getY(); this.z += vec3f.getZ();
	}
	
	public void addEq(float vec3f) {
		this.x += vec3f; this.y += vec3f; this.z += vec3f;
	}
	
	public void subEq(Vector3f vec3f) {
		this.x -= vec3f.getX(); this.y -= vec3f.getY(); this.z -= vec3f.getZ();
	}
	
	public void subEq(float vec3f) {
		this.x -= vec3f; this.y -= vec3f; this.z -= vec3f;
	}
	
	public void mulEq(Vector3f vec3f) {
		this.x *= vec3f.getX(); this.y *= vec3f.getY(); this.z *= vec3f.getZ();
	}
	
	public void mulEq(float vec3f) {
		this.x *= vec3f; this.y *= vec3f; this.z *= vec3f;
	}
	
	public void divEq(Vector3f vec3f) {
		this.x /= vec3f.getX(); this.y /= vec3f.getY(); this.z /= vec3f.getZ();
	}
	
	public void divEq(float vec3f) {
		this.x /= vec3f; this.y /= vec3f; this.z /= vec3f;
	}
	
	public Vector3f add(Vector3f vec3f) {
		return new Vector3f(this.x + vec3f.getX(), this.y + vec3f.getY(), this.z + vec3f.getZ());
	}
	
	public Vector3f add(float vec3f) {
		return new Vector3f(this.x + vec3f, this.y + vec3f, this.z + vec3f);
	}
	
	public Vector3f sub(Vector3f vec3f) {
		return new Vector3f(this.x - vec3f.getX(), this.y - vec3f.getY(), this.z - vec3f.getZ());
	}
	
	public Vector3f sub(float vec3f) {
		return new Vector3f(this.x - vec3f, this.y - vec3f, this.z - vec3f);
	}
	
	public Vector3f mul(Vector3f vec3f) {
		return new Vector3f(this.x * vec3f.getX(), this.y * vec3f.getY(), this.z * vec3f.getZ());
	}
	
	public Vector3f mul(float vec3f) {
		return new Vector3f(this.x * vec3f, this.y * vec3f, this.z * vec3f);
	}
	
	public Vector3f div(Vector3f vec3f) {
		return new Vector3f(this.x / vec3f.getX(), this.y / vec3f.getY(), this.z / vec3f.getZ());
	}
	
	public Vector3f div(float vec3f) {
		return new Vector3f(this.x / vec3f, this.y / vec3f, this.z / vec3f);
	}
	
	public String toString() {
		return "(" + x + " " + y + " " + z + ")";
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
	
	public Vector3f set(Vector3f vec3f) {
		return this.setVector(vec3f.getX(), vec3f.getY(), vec3f.getZ());
	}
	
	public Vector3f setVector(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	
	public Vector2f getXY() {
		return new Vector2f(x,y);
	}
	
	public Vector2f getYZ() {
		return new Vector2f(y,z);
	}
	
	public Vector2f getZX() {
		return new Vector2f(z,x);
	}
	
	public Vector2f getYX() {
		return new Vector2f(y,x);
	}
	
	public Vector2f getZY() {
		return new Vector2f(z,y);
	}
	
	public Vector2f getXZ() {
		return new Vector2f(x,z);
	}
	
	public float get(int index) {
		switch(index) {
		case 0:
			return this.x;
		case 1:
			return this.y;
		case 2:
			return this.z;
		default:
			return 0;
		}
	}
	
	public boolean equals(Vector3f vec3f) {
		return x == vec3f.getX() && y == vec3f.getY() && z == vec3f.getZ();
	}

	public Vector3f max(Vector3f vec3f) {
		float x = Math.max(this.x, vec3f.getX());
		float y = Math.max(this.y, vec3f.getY());
		float z = Math.max(this.z, vec3f.getZ());
		return new Vector3f(x, y, z);
	}


	public Vector3f reflect(Vector3f vec3f) {
		return this.sub(vec3f.mul(this.dot(vec3f) * 2.0f));
	}


	public boolean isNaN() {
		return Float.isNaN(x) || Float.isInfinite(x) || Float.isNaN(y) || Float.isInfinite(y) || Float.isNaN(z) || Float.isInfinite(z);
	}


	public Vector3f constrainLength(float length) {
		this.mulEq(length / this.getLength());
		return this;
	}


	public void set(float f, int index) {
		switch(index) {
		case 0:
			this.x = f;
		case 1:
			this.y = f;
		case 2:
			this.z = f;
		}
	}
}
