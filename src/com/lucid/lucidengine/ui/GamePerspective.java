package com.lucid.lucidengine.ui;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.lucid.lucidengine.main.EngineCore;
import com.lucid.lucidengine.main.EngineSettings;
import com.lucid.lucidengine.main.EngineWindow;
import com.lucid.lucidengine.mathematics.Quaternion;
import com.lucid.lucidengine.mathematics.Vector3f;
import com.lucid.lucidengine.physics.AdvancedIntersectionData;
import com.lucid.lucidengine.physics.CollisionPair;
import com.lucid.lucidengine.physics.ICollisionData;
import com.lucid.lucidengine.physics.ICollisionHandler;
import com.lucid.lucidengine.physics.IntersectionData;
import com.lucid.lucidengine.physics.PhysicsObject;
import com.lucid.lucidengine.physics.Plane;

/**
 * @author Mark Diedericks
 *
 */

public class GamePerspective extends Perspective implements ICollisionHandler {
	
	public final Vector3f yAxis = new Vector3f(0, 1, 0);
	private boolean isGamepad = false;
	private Controller gamepad;
	
	private float verticalSpeed = 0;
	private Player player;
	private boolean superSprint = false;
	
	private CollisionPair collisionPair;
	
	private Vector3f velocity;
	private Vector3f destination;
	
	private float maxStep = 0.25f;
	private ICollisionData data;
	private int dataTime = 0;
	
	public GamePerspective(Player player) {
		this.player = player;
		this.velocity = new Vector3f(0, 0, 0);
		for(Controller cont : ControllerEnvironment.getDefaultEnvironment().getControllers()) {
			if(cont.getType() == Controller.Type.GAMEPAD) {
				gamepad = cont;
			}
		}
		if(gamepad != null) isGamepad = true;
	}
	
	@Override
	public void update(float delta) {
		this.collisionPair = null;
		
		verticalSpeed += this.player.getGravity() * delta;
		if(verticalSpeed < this.player.getGravity() * delta) verticalSpeed = this.player.getGravity() * delta;
		
		this.velocity = new Vector3f(0, verticalSpeed, 0);
		this.destination = getParent().getTransform().getPos();
		
		updateAllways(delta);
		if(EngineCore.ingame) {
			updateKeyboard(delta);
			updateMouse(delta);
			
			if(isGamepad == false && gamepad == null) {
				//no gamepad
			} else {
				updateGamepad(delta);
			}
		}

		this.velocity.setY(verticalSpeed);
	}
	
	
	
	@Override
	public void postUpdate(float delta) {
//		if(data != null && dataTime > 0) {
//			dataTime--;
//			checkCollision(data, delta);
//		}
		
		this.destination.addEq(velocity);
			
		getParent().getTransform().getPos().set(this.destination);
			
		this.player.setVelocity(this.velocity);
	}
	
	public void updateAllways(float delta) {
		if(Keyboard.isKeyDown(Keyboard.KEY_Q)) superSprint = true;
		else superSprint = false;
		if(isGamepad == true && gamepad != null) {
			gamepad.poll();
			for(Component comp : gamepad.getComponents()) {
				if(comp.getName().equals(EngineSettings.START)) {
					if(comp.getPollData() > 0.1f) player.pause(delta);
				}
			}
		}
	}
	
	public void setVerticalSpeed(float verticalSpeed) {
		this.verticalSpeed = verticalSpeed;
	}
	
	public void setMaxStep(float maxStep) {
		this.maxStep = maxStep;
	}
	
	private boolean sprinting = false;
	private boolean sneaking = false;
	public void updateGamepad(float delta) {
		gamepad.poll();
		for(Component comp : gamepad.getComponents()) {
			float data = comp.getPollData();
			if(comp.getName().equals(EngineSettings.X)) {
				
			} else if(comp.getName().equals(EngineSettings.Y)) {
				
			} else if(comp.getName().equals(EngineSettings.B)) {
				
			} else if(comp.getName().equals(EngineSettings.A)) {
				if(data > 0.1f && !this.player.isInAir()) { this.setVerticalSpeed(9.8f * 4.0f * delta); this.player.setInAir(true); }
			} else if(comp.getName().equals(EngineSettings.LEFT_SHOULDER)) {
				if(data > 0.1f) move(getParent().getTransform().getRotation().getRotateUp().normalize(), -4.5f * delta, delta);
			} else if(comp.getName().equals(EngineSettings.RIGHT_SHOULDER)) {
				if(data > 0.1f) move(getParent().getTransform().getRotation().getRotateUp().normalize(), 4.5f * delta, delta);
			} else if(comp.getName().equals(EngineSettings.LEFT_TRIGGER) && data > 0.0f) {
				sneaking = data >= 0.1f ? true : false;
			} else if(comp.getName().equals(EngineSettings.RIGHT_TRIGGER) && data < 0.0f) {
				sprinting = data <= -0.1f ? true : false;
			} else if(comp.getName().equals(EngineSettings.LEFT_STICK)) {
				
			} else if(comp.getName().equals(EngineSettings.LEFT_X)) {
				float move = 4.5f;
				if(sprinting) move += 1.5f;
				if(sneaking) move -= 0.75f;
				
				if(data < -0.1f) move(getParent().getTransform().getRotation().getRotateRight().normalize(), -move * delta, delta);
				else if(data > 0.1f) move(getParent().getTransform().getRotation().getRotateRight().normalize(), move * delta, delta);
			} else if(comp.getName().equals(EngineSettings.LEFT_Y)) {
				float move = 6.0f;
				if(sprinting) move += 2.25f;
				if(sneaking) move -= 1.5f;
				
				if(data < -0.1f) move(getParent().getTransform().getRotation().getXZRotateForward().normalize(), move * delta, delta);
				else if(data > 0.1f) move(getParent().getTransform().getRotation().getXZRotateForward().normalize(), -move * delta, delta);
			} else if(comp.getName().equals(EngineSettings.RIGHT_STICK)) {
				
			} else if(comp.getName().equals(EngineSettings.RIGHT_X)) {
				if(data < -0.1f)getParent().getTransform().setRotation(getParent().getTransform().getRotation().mul(new Quaternion(yAxis, (float) Math.toRadians(-(data * 7.5f) * EngineSettings.sensitivity * delta)).normalize()));
				else if(data > 0.1f)getParent().getTransform().setRotation(getParent().getTransform().getRotation().mul(new Quaternion(yAxis, (float) Math.toRadians(-(data * 7.5f) * EngineSettings.sensitivity * delta)).normalize()));
			} else if(comp.getName().equals(EngineSettings.RIGHT_Y)) {
				if(data < -0.1f)getParent().getTransform().setRotation(getParent().getTransform().getRotation().mul(new Quaternion(getParent().getTransform().getRotation().getRotateRight().normalize(), (float) Math.toRadians((-data * 225f) * EngineSettings.sensitivity * delta)).normalize()));
				else if(data > 0.1f)getParent().getTransform().setRotation(getParent().getTransform().getRotation().mul(new Quaternion(getParent().getTransform().getRotation().getRotateRight().normalize(), (float) Math.toRadians(-(data * 225f) * EngineSettings.sensitivity * delta)).normalize()));
			} else if(comp.getName().equals(EngineSettings.UP)) {
				
			} else if(comp.getName().equals(EngineSettings.DOWN)) {
				
			} else if(comp.getName().equals(EngineSettings.LEFT)) {
				
			} else if(comp.getName().equals(EngineSettings.RIGHT)) {
				
			}
		}
	}
	
	public void updateKeyboard(float delta) {
		float speed = 0.0f;
		if(superSprint) speed = 30.0f;
		
		if(Keyboard.isKeyDown(EngineSettings.forward)) { if(Keyboard.isKeyDown(EngineSettings.sprint)) { move(getParent().getTransform().getRotation().getXZRotateForward().normalize(), (12.5f+speed) * delta, delta); } else if(Keyboard.isKeyDown(EngineSettings.sneak)) { move(getParent().getTransform().getRotation().getXZRotateForward().normalize(), 4.5f * delta, delta); } else { move(getParent().getTransform().getRotation().getXZRotateForward().normalize(), (7.5f+speed) * delta, delta); } }
		if(Keyboard.isKeyDown(EngineSettings.backward)) { if(Keyboard.isKeyDown(EngineSettings.sprint)) { move(getParent().getTransform().getRotation().getXZRotateForward().normalize(), (-11.0f-speed) * delta, delta); } else if(Keyboard.isKeyDown(EngineSettings.sneak)) { move(getParent().getTransform().getRotation().getXZRotateForward().normalize(), -3.0f * delta, delta); } else { move(getParent().getTransform().getRotation().getXZRotateForward().normalize(), (-6.0f-speed) * delta, delta); } }
		
		if(Keyboard.isKeyDown(EngineSettings.left)) move(getParent().getTransform().getRotation().getRotateRight().normalize(), -4.5f * delta, delta);
		if(Keyboard.isKeyDown(EngineSettings.right)) move(getParent().getTransform().getRotation().getRotateRight().normalize(), 4.5f * delta, delta);
		
		if(Keyboard.isKeyDown(EngineSettings.jump) && !this.player.isInAir()) { this.setVerticalSpeed(9.8f * 4.0f * delta); this.player.setInAir(true); }
	}

	public void updateMouse(float delta) {
		double xangle = (EngineWindow.getCenter().getX() - Mouse.getX());
		double yangle = (EngineWindow.getCenter().getY() - Mouse.getY());
		
		getParent().getTransform().setRotation(getParent().getTransform().getRotation().mul(new Quaternion(yAxis, (float) Math.toRadians(xangle * EngineSettings.sensitivity)).normalize()));
		getParent().getTransform().setRotation(getParent().getTransform().getRotation().mul(new Quaternion(getParent().getTransform().getRotation().getRotateRight(), (float) Math.toRadians(-yangle * EngineSettings.sensitivity)).normalize()));
		
		Mouse.setCursorPosition((int)EngineWindow.getCenter().getX(), (int)EngineWindow.getCenter().getY());
	}

	private void move(Vector3f dir, float amt, float delta) {
		dir = dir.normalize();
		dir.setY(0f);
		this.velocity.addEq(dir.mul(amt));
	}
	
	private float veryCloseDistance = 0.005f;
	private int recursionDepth = 0;
	private int maxRecursion = 5;
	
	private void checkCollision(ICollisionData datas, float delta) {
		if(datas instanceof IntersectionData) {
			IntersectionData data = (IntersectionData)datas;
			if(!data.isIntersect()) return;
			
//			Vector3f dest = this.getParent().getTransform().getPos();
//			dest.subEq(data.getNormal().mul(this.velocity.abs()));
//			
//			if(dest.isNaN()) return;
//			this.velocity = dest.sub(this.getParent().getTransform().getPos());
//			this.velocity.mulEq(delta);
			
			Vector3f intersection = data.getIntersectionPoint();
			Vector3f destinationPoint = this.getParent().getTransform().getPos().add(this.velocity);
			Vector3f newBasePoint = this.getParent().getTransform().getPos().add(this.velocity);
			
			if(data.getDistance() >= veryCloseDistance) {
				Vector3f v = this.velocity;
				v.constrainLength(data.getDistance() - veryCloseDistance);
				
				newBasePoint = this.getParent().getTransform().getPos().add(v);
				
				v.normalize();
				intersection.subEq(v.mul(veryCloseDistance));
			}
			
			Vector3f spo = intersection;
			Vector3f spn = newBasePoint.sub(spo);
			spn.normalize();
			
			Plane sp = new Plane(spo, spn);
			
			Vector3f newDestination = destinationPoint.sub(spn.mul(sp.signedDistanceTo(destinationPoint)));
			Vector3f newVelocity = newDestination.sub(intersection);
			
			if(intersection.isNaN() || newDestination.isNaN() || newVelocity.isNaN() || newDestination.equals(intersection)) return;
			
			this.velocity = newVelocity;
		} else if(datas instanceof AdvancedIntersectionData) {
			if(!((AdvancedIntersectionData)datas).isIntersect()) return;
			IntersectionData data = new IntersectionData(false, new Vector3f(0, 0, 0));
			float distance = 10000.0f;
			for(IntersectionData small : ((AdvancedIntersectionData)datas).getIntersects()) {
				if(small.getDistance() < distance) {
					distance = data.getDistance();
					data = small;
				}
			}
			
			Vector3f intersection = data.getIntersectionPoint();
			Vector3f position = this.getParent().getTransform().getPos();
//			Vector3f newBasePoint = this.getParent().getTransform().getPos().add(this.velocity);
//			
//			if(data.getDistance() >= veryCloseDistance) {
//				Vector3f v = this.velocity;
//				v.constrainLength(data.getDistance() - veryCloseDistance);
//				
//				newBasePoint = this.getParent().getTransform().getPos().add(v);
//				
//				v.normalize();
//				intersection.subEq(v.mul(veryCloseDistance));
//			}
//			
//			Vector3f spo = intersection;
//			Vector3f spn = newBasePoint.sub(spo);
//			spn.normalize();
//			
//			Plane sp = new Plane(spo, spn);
//			
//			Vector3f newDestination = destinationPoint.sub(spn.mul(sp.signedDistanceTo(destinationPoint)));
//			Vector3f newVelocity = newDestination.sub(intersection);
			
			Vector3f newVelocity = velocity.normalize().mul(intersection.sub(position).getLength() * -1.0f);
			
			Vector3f destination = position.add(newVelocity);
			
			if(intersection.isNaN() || destination.isNaN() || newVelocity.isNaN() || destination.equals(intersection)) return;
			
			this.velocity = newVelocity;
			
			System.out.println(intersection + "    " + destination + "    " + newVelocity + "    --- ADVANCED");
		}
		
		this.destination.addEq(velocity);
		
		this.player.setVelocity(this.velocity);
		
		if(velocity.getLength() > veryCloseDistance && recursionDepth < maxRecursion) {
			this.recursionDepth++;
			this.collisionPair.object1.getPhysicsEngine().recur(this.collisionPair);
		}
		
		recursionDepth = 0;
	}
	
	@Override
	public void handleCollision(ICollisionData data, float delta, PhysicsObject me, PhysicsObject other) {
//		this.data = data;
//		this.dataTime = 1;
		this.collisionPair = new CollisionPair(me, other);
		checkCollision(data, delta);
	}
}
