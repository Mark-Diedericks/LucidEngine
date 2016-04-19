package com.lucid.lucidengine.physics;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.lucid.lucidengine.mathematics.Matrix4f;
import com.lucid.lucidengine.mathematics.Quaternion;
import com.lucid.lucidengine.mathematics.Transform;
import com.lucid.lucidengine.mathematics.Vector2f;
import com.lucid.lucidengine.mathematics.Vector3f;
import com.lucid.lucidengine.object.Terrain;
import com.lucid.lucidengine.ui.Camera;

public class Raycast {
	
	private final boolean mousePick;
	
	private Vector3f currentRay;
	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix;
	private Camera camera;
	private final int maxRecursions = 200;
	
	private Vector3f position;
	
	public Raycast(Camera camera) {
		this.camera = camera;
		this.viewMatrix = this.camera.getView().clone();
		this.projectionMatrix = this.camera.getProjection().clone();
		this.mousePick = true;
	}
	
	public Raycast(Vector3f position, Vector3f direction) {
		this.position = position;
		this.currentRay = direction;
		this.mousePick = false;
	}
	
	public Vector3f getDirection() {
		return currentRay;
	}
	
	public Vector3f getPosition() {
		return this.position;
	}
	
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	public void update() {
		this.viewMatrix = this.camera.getView().clone();
		this.projectionMatrix = camera.getProjection().clone();
		if(mousePick) this.currentRay = calcMouseRay();
		else this.currentRay = calcRay();
	}
	
	private Vector3f calcRay() {
		return null;
	}
	
	private Vector3f calcMouseRay() {
		float mx = Mouse.getX();
		float my = Mouse.getY();
		Vector2f normDeviceCoords = getNormalizedDeviceCoords(mx, my);
		Vector3f clipCoords = new Vector3f(normDeviceCoords.getX(), normDeviceCoords.getY(), -1f);
		Vector3f eyeCoords = toEyeSpace(clipCoords);
		Vector3f worldSpace = toWorldSpace(eyeCoords);
		return worldSpace;
	}
	
	private Vector2f getNormalizedDeviceCoords(float mx, float my) {
		float x = (2f * mx) / Display.getWidth() - 1f;
		float y = (2f * my) / Display.getHeight() - 1f;
		return new Vector2f(x, y);
	}
	
	private Vector3f toEyeSpace(Vector3f clipCoords) {
		Matrix4f inverseProjection = this.projectionMatrix.inverse();
		Vector3f eyeCoords = inverseProjection.transform(clipCoords);
		return new Vector3f(eyeCoords.getX(), eyeCoords.getY(), -1f);
	}
	
	private Vector3f toWorldSpace(Vector3f eyeCoods) {
		Matrix4f inverseView = this.viewMatrix.inverse();
		Vector3f worldCoords = inverseView.transform(eyeCoods);
		worldCoords.normalize();
		return worldCoords;
	}
	
	private boolean pointAbove(Vector3f point, Terrain terrain) {
		return (point.getY() > terrain.getHeightAt(point.getX(), point.getY()));
	}
	
	private boolean pointBellow(Vector3f point, Terrain terrain) {
		return (point.getY() < terrain.getHeightAt(point.getX(), point.getZ()));
	}
	
	private int recursions = 0;
	public Vector3f getIntersectionPoint(Terrain terrain, int maxChecksPerSeg, float maxLength) {
		if(pointBellow(this.getPosition(), terrain)) return this.getPosition();
		recursions = 0;
		return recursion(terrain, maxChecksPerSeg, maxLength, this.getPosition());
	}
	
	private Vector3f recursion(Terrain terrain, int CPS, float rayLength, Vector3f startPos) {
		recursions++;
		
		Transform transform = new Transform();
		transform.getPos().set(startPos);
		Vector3f up = new Vector3f(this.getDirection().getY(), this.getDirection().getX(), this.getDirection().getZ());
		if(this.getDirection().getX() > up.getX()) up = this.getDirection().cross(up);
		else up = up.cross(this.getDirection());
		up.normalize();
		
		//System.out.println("UP: " + up + "    FORWARD: " + getDirection());
		
		transform.setRotation(new Quaternion(this.getDirection(), up));
		
		Vector3f bellow = new Vector3f(0f, -99999999999999999999999999999999999999f, 0f);
		Vector3f above = getPosition();
		
		float addDist = rayLength / (float)CPS;
		for(int i = 0; i < CPS; i++) {
			transform.move(getDirection(), addDist);
			if(pointAbove(transform.getPos(), terrain)) {
				if(transform.getPos().getY() < above.getY()) above = transform.getPos();
			} else if(pointBellow(transform.getPos(), terrain)) {
				if(transform.getPos().getY() > bellow.getY()) bellow = transform.getPos();
			} else {
				System.out.println("Collision, Apperently");
				return transform.getPos();
			}
		}
		
		if(recursions < maxRecursions) {
			float dist = rayLength;
			if(bellow.getY() != -99999999999999999999999999999999999999f)
				dist = above.sub(bellow).getLength();
			
			return recursion(terrain, CPS, dist, above);
		} else {
			Vector3f pos;
			
			if(bellow.getY() == -99999999999999999999999999999999999999f) {
				pos = above;
			} else {
				Vector3f diff = above.sub(bellow);
				diff.divEq(2.0f);
				pos = above.add(diff);
			}
			
			//System.out.println("Max");
			
			recursions = 0;
			return pos;
		}
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}
}
