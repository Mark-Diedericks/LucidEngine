package com.lucid.lucidengine.ui;

import com.lucid.lucidengine.main.EngineWindow;
import com.lucid.lucidengine.mathematics.Matrix4f;
import com.lucid.lucidengine.mathematics.Quaternion;
import com.lucid.lucidengine.mathematics.Vector3f;
import com.lucid.lucidengine.object.MapComponent;

/**
 * @author Mark Diedericks
 *
 */

public class Camera extends MapComponent {
	
	private Matrix4f projection;
	private Matrix4f perspective;
	public boolean isPerspective = true;
	public final float ar;
	public final float zNear;
	public final float zFar;
	
	public Camera(float fov, float aspectRatio, float zNear, float zFar) {
		super();
		this.ar = aspectRatio;
		this.zNear = zNear;
		this.zFar = zFar;
		this.perspective = new Matrix4f().initPerspective(fov, aspectRatio, zNear, zFar);
		this.projection = this.perspective;
	}
	
	public Matrix4f getViewProjection() {
		Matrix4f cameraRot = getTransform().getRotation().toRotationMatrix();
		Vector3f cameraPos = getTransform().getPos().mul(-1);
		Matrix4f cameraTrans = new Matrix4f().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
		
		return projection.mul(cameraRot.mul(cameraTrans));
	}
	
	public Matrix4f getProjection() {
		return projection;
	}
	
	public Matrix4f getView() {
		Matrix4f cameraRot = getTransform().getRotation().toRotationMatrix();
		Vector3f cameraPos = getTransform().getPos().mul(-1);
		Matrix4f cameraTrans = new Matrix4f().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
		
		return cameraRot.mul(cameraTrans);
	}
	
	public void setProjection(Vector3f pos, Quaternion rot) {
		this.getTransform().getPos().set(pos);
		this.getTransform().setRotation(rot);
	}
	
	public void setProjection(Matrix4f projection) {
		this.projection = projection;
	}
	
	public void ortho() {
		isPerspective = false;
		this.projection = new Matrix4f().initOthographic(0, EngineWindow.width, EngineWindow.height, 0, -1f, 1f);
	}
	
	public void perspective() {
		isPerspective = true;
		this.projection = this.perspective;
	}
}
