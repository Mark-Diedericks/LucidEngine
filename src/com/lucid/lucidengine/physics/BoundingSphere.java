package com.lucid.lucidengine.physics;

import com.lucid.lucidengine.mathematics.Vector3f;

/**
 * @author Mark Diedericks
 *
 */

public class BoundingSphere extends Collider {
	
	private Vector3f center;
	private float radius;
	
	public BoundingSphere(Vector3f center, float radius) {
		super(Collider.Type.SPHERE);
		this.center = center;
		this.radius = radius;
	}
	
	@Override
	public void Transform(Vector3f translation) {
		center.addEq(translation);
	}
	
	public ICollisionData IntersectBoundingSphere(BoundingSphere other) {
		
		float radiusDistance = this.radius + other.getRadius();
		Vector3f direction = (other.getCenter().sub(this.center));
		float centerDistance = direction.getLength();
		direction.divEq(centerDistance);
		
		float distance = centerDistance - radiusDistance;
		
		return new IntersectionData(distance < 0, direction, direction.normalize());
	}
	
	@Override
	public Vector3f getCenter() {
		return center.add(position);
	}

	public float getRadius() {
		return radius;
	}
}
