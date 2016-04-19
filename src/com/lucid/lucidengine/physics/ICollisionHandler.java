package com.lucid.lucidengine.physics;

public interface ICollisionHandler {
	
	public abstract void handleCollision(ICollisionData intersect, float delta, PhysicsObject me, PhysicsObject other);
	
}
