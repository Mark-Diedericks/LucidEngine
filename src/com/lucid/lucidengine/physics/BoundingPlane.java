package com.lucid.lucidengine.physics;

import com.lucid.lucidengine.mathematics.Vector3f;

/**
 * @author Mark Diedericks
 *
 */

public class BoundingPlane extends Collider {
	
	private Vector3f normal;
	private float distance;
	
	public BoundingPlane(Vector3f normal, float distance) {
		super(Collider.Type.PLANE);
		this.normal = normal;
		this.distance = distance;
	}
	
	public ICollisionData IntersectSphere(BoundingSphere other) {
		float distanceF = ((float)Math.abs(normal.dot(other.getCenter()) + distance));
		float distanceFS = distanceF - other.getRadius();
		return new IntersectionData(distanceFS < 0, normal.mul(distanceFS), normal, other.getCenter().add(normal.mul(distanceFS)));
	}
	
	public ICollisionData IntersectEllipsiod(BoundingEllipsiod other) {
		float distanceF = ((float)Math.abs(normal.dot(other.toEllipsiodSpace(other.getCenter())) + distance));
		float distanceFS = distanceF - 1.0f;
		return new IntersectionData(distanceFS <= 0, normal.mul(other.getRadius().mul(distanceFS)), normal, other.getCenter().add(normal.mul(other.getRadius().normalize().mul(distanceFS))));
	}
	
	public BoundingPlane normalize() {
		float length = normal.getLength();
		return new BoundingPlane(normal.div(length), distance / length);
	}
	
	public Vector3f getNormal() {
		return normal;
	}

	public float getDistance() {
		return distance;
	}
}
