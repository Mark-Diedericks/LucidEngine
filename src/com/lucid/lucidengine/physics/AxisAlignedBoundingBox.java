package com.lucid.lucidengine.physics;

import com.lucid.lucidengine.mathematics.Vector3f;

/**
 * @author Mark Diedericks
 *
 */

public class AxisAlignedBoundingBox extends Collider {
	
	private Vector3f minExtent;
	private Vector3f maxExtent;
	
	public AxisAlignedBoundingBox(Vector3f minExtent, Vector3f maxExtent) {
		super(Collider.Type.AABB);
		this.minExtent = minExtent;
		this.maxExtent = maxExtent;
	}
	
	@Override
	public void Transform(Vector3f position) {
		this.position = position;
	}
	
	@Override
	public Vector3f getCenter() {
		Vector3f size = maxExtent.sub(minExtent);
		size.divEq(2);
		size.addEq(minExtent);
		return size.add(position);
	}
	
	public ICollisionData IntersectAABB(AxisAlignedBoundingBox other) {
		Vector3f distance1 = other.getPosMinExtent().sub(this.getPosMaxExtent());
		Vector3f distance2 = this.getPosMinExtent().sub(other.getPosMaxExtent());
		Vector3f distance = distance1.max(distance2);
		float maxDistance = distance.max();
		
		return new IntersectionData(maxDistance < 0, distance, getCollidingAxis(this.getCenter(), other.getCenter()));
	}
	
	private Vector3f getCollidingAxis(Vector3f me, Vector3f other) {
		Vector3f result = new Vector3f(0, 0, 0);
		
		if(me.getX() < other.getX())  result.setX(1f);
		else if (me.getX() > other.getX()) result.setX(-1f);
		
		if(me.getY() < other.getY())  result.setY(1f);
		else if (me.getY() > other.getY()) result.setY(-1f);
		
		if(me.getZ() < other.getZ())  result.setZ(1f);
		else if (me.getZ() > other.getZ()) result.setZ(-1f);
		
		return result;
	}
	
	public Vector3f getMinExtent() {
		return minExtent;
	}

	public Vector3f getMaxExtent() {
		return maxExtent;
	}
	
	public Vector3f getPosMinExtent() {
		return minExtent.add(this.position);
	}

	public Vector3f getPosMaxExtent() {
		return maxExtent.add(this.position);
	}
}
