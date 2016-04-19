package com.lucid.lucidengine.physics;

import com.lucid.lucidengine.mathematics.Vector3f;

public class BoundingEllipsiod extends Collider {
	
	private Vector3f radius;
	private Vector3f center;
	
	public BoundingEllipsiod(Vector3f radius, Vector3f center) {
		super(Type.ELLIPSIOD);
		this.radius = radius;
		this.center = center;
	}
	
	public Vector3f toEllipsiodSpace(Vector3f in) {
		return in.div(radius);
	}
	
	public Vector3f toVectorSpace(Vector3f in) {
		return in.mul(radius);
	}
	
	@Override
	public void Transform(Vector3f translation) {
		center.addEq(translation);
	}
	
	@Override
	public Vector3f getCenter() {
		return center.add(position);
	}

	public Vector3f getRadius() {
		return radius;
	}
	
}
