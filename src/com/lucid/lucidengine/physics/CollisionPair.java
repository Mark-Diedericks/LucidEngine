package com.lucid.lucidengine.physics;

public class CollisionPair {
	
	public PhysicsObject object1;
	public PhysicsObject object2;
	
	public CollisionPair(PhysicsObject obj1, PhysicsObject obj2) {
		object1 = obj1;
		object2 = obj2;
	}
	
	@Override
	public String toString() {
		return "Object (" + object1.id + ") collided with Object (" + object2.id + ")";
	}

	public ICollisionData getIntersectionDataOne() {
		return object1.getCollider().Intersect(object2.getCollider());
	}
	
	public ICollisionData getIntersectionDataTwo() {
		return object2.getCollider().Intersect(object1.getCollider());
	}
	
}
