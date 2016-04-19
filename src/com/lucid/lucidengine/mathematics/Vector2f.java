package com.lucid.lucidengine.mathematics;

/**
 * @author Mark Diedericks
 *
 */

public class Vector2f {
	
	private float x;
	private float y;
	
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2f(float x, double y) {
		this.x = x;
		this.y =(float) y;
	}
	
	public Vector2f(double x, float y) {
		this.x = (float)x;
		this.y = y;
	}
	
	public Vector2f(double x, double y) {
		this.x = (float)x;
		this.y = (float)y;
	}
	
	public float getLength() {
		return (float) Math.sqrt(x * x + y * y);
	}
	
	public float dot(Vector2f vec2f) {
		return this.x * vec2f.getX() + this.y * vec2f.getY();
	}
	
	public Vector2f normalize() {
		float length = this.getLength();
		
		x /= length;
		y /= length;
		
		return this;
	}
	
	public Vector2f rotate(float angle) {
		double rad = Math.toRadians(angle);
		double cos = Math.cos(rad);
		double sin = Math.sin(rad);
		
		return new Vector2f((float)(x * cos - y * sin), (float)(x * sin + y * cos));
	}
	
	public float cross(Vector2f vec2f) {
		return x * vec2f.getY() - y * vec2f.getX();
	}
	
	public Vector2f lerp(Vector2f dest, float factor) {
		return dest.sub(this).mul(factor).add(this);
	}
	
	public Vector2f add(Vector2f vec2f) {
		return new Vector2f(this.x + vec2f.getX(), this.y + vec2f.getY());
	}
	
	public Vector2f add(float vec2f) {
		return new Vector2f(this.x + vec2f, this.y + vec2f);
	}
	
	public Vector2f sub(Vector2f vec2f) {
		return new Vector2f(this.x - vec2f.getX(), this.y - vec2f.getY());
	}
	
	public Vector2f sub(float vec2f) {
		return new Vector2f(this.x - vec2f, this.y - vec2f);
	}
	
	public Vector2f mul(Vector2f vec2f) {
		return new Vector2f(this.x * vec2f.getX(), this.y * vec2f.getY());
	}
	
	public Vector2f mul(float vec2f) {
		return new Vector2f(this.x * vec2f, this.y * vec2f);
	}
	
	public Vector2f div(Vector2f vec2f) {
		return new Vector2f(this.x / vec2f.getX(), this.y / vec2f.getY());
	}
	
	public Vector2f div(float vec2f) {
		return new Vector2f(this.x / vec2f, this.y / vec2f);
	}
	
	public String toString() {
		return "(" + x + " " + y + ")";
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
	
	public Vector2f set(Vector2f vec2f) {
		return this.setVector(vec2f.getX(), vec2f.getY());
	}
	
	public Vector2f setVector(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	public boolean equals(Vector2f vec2f) {
		return x == vec2f.getX() && y == vec2f.getY();
	}

	public void sort() {
		if(this.y > this.x) {
			float temp = this.x;
			this.x = this.y;
			this.y = temp;
		}
	}
	
}
