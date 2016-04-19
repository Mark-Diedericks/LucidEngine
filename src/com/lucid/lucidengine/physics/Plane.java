package com.lucid.lucidengine.physics;

import com.lucid.lucidengine.mathematics.Vector3f;

public class Plane {
	
	private float[] equation = new float[4];
	
	private Vector3f origin;
	private Vector3f normal;
	
	private Vector3f forward;
	private Vector3f left;
	
	public Plane(Vector3f origin, Vector3f normal) {
		this.normal = normal;
		this.origin = origin;
		
		equation[0] = normal.getX();
		equation[1] = normal.getY();
		equation[2] = normal.getZ();
		equation[3] = -(normal.getX() * origin.getX() + normal.getY() * origin.getY() + normal.getZ() * origin.getZ());
	}
	
	public Plane(Vector3f p1, Vector3f p2, Vector3f p3) {
		this.forward = p3.sub(p1);
		this.left = p2.sub(p1);
		this.normal = forward.cross(left).normalize();
		this.origin = p1;
		
		equation[0] = normal.getX();
		equation[1] = normal.getY();
		equation[2] = normal.getZ();
		equation[3] = -(normal.getX() * origin.getX() + normal.getY() * origin.getY() + normal.getZ() * origin.getZ());
	}
	
	public boolean isFrontfacingTo(Vector3f direction) {
		float dot = normal.dot(direction);
		return dot <= 0.0f;
	}
	
	public float signedDistanceTo(Vector3f point) {
		return (point.dot(normal)) + equation[3];
	}
	
	public Vector3f getNormal() {
		return normal;
	}
	
	public Vector3f getOrigin() {
		return origin;
	}
	
	public float getConstant() {
		return equation[3];
	}
	
	public Vector3f intersectLine(Vector3f a, Vector3f b) {
		Vector3f ba = b.sub(a);
		float nDotA = normal.dot(a);
		float nDotBA = normal.dot(ba);
		
		float d = normal.dot(origin);
		
		return a.add(ba.mul((d - nDotA) / nDotBA));
	}
	
	public Vector3f intesectPlane(Plane p, int largestAxis, Vector3f d, float d1, float d2) {
		float x, y, z;
		
		switch(largestAxis) {
		case 0:
			x = 0;
			y = ((d2 * normal.getZ()) - (d1 * p.normal.getZ())) / d.getX();
			z = ((d1 * p.normal.getY()) - (d2 * normal.getY())) / d.getX();
			return new Vector3f(x, y, z);
		case 1:
			x = ((d1 * p.normal.getZ()) - (d2 * normal.getZ())) / d.getY();
			y = 0;
			z = ((d2 * normal.getX()) - (d1 * p.normal.getX())) / d.getY();
			return new Vector3f(x, y, z);
		case 2:
			x = ((d2 * normal.getY()) - (d1 * p.normal.getY())) / d.getZ();
			y = ((d1 * p.normal.getX()) - (d2 * normal.getX())) / d.getZ();
			z = 0;
			return new Vector3f(x, y, z);
		}
		
		return new Vector3f(0, 0, 0);
	}
}
