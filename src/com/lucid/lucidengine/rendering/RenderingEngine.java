package com.lucid.lucidengine.rendering;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;

import java.util.ArrayList;

import org.lwjgl.opengl.GL30;

import com.lucid.lucidengine.gui.TextRenderer;
import com.lucid.lucidengine.main.EngineCore;
import com.lucid.lucidengine.main.EngineProfiling;
import com.lucid.lucidengine.main.EngineSettings;
import com.lucid.lucidengine.main.EngineWindow;
import com.lucid.lucidengine.mathematics.Matrix4f;
import com.lucid.lucidengine.mathematics.Quaternion;
import com.lucid.lucidengine.mathematics.Transform;
import com.lucid.lucidengine.mathematics.Vector3f;
import com.lucid.lucidengine.object.Map;
import com.lucid.lucidengine.object.lights.Light;
import com.lucid.lucidengine.rendering.lighting.*;
import com.lucid.lucidengine.rendering.mesh.StaticMesh;
import com.lucid.lucidengine.ui.Camera;

/**
 * @author Mark Diedericks
 *
 */

public class RenderingEngine extends RenderingVariables {
	
	
	private EngineCore engineCore;
	private EngineProfiling profiler;
	private Camera camera;
	private Camera specialCamera;
	private ArrayList<Light> lights;
	private Light activeLight;
	private Matrix4f lightingMatrix;
//	private boolean lightCycle = false;
	
	private final StaticMesh filterMesh;
	private Material filterMaterial;
	private final Transform filterTransform;
	
	private static final int NUM_SHADOW_MAPS = 14;
	private Texture[] shadowMaps;
	private Texture[] shadowMapsTemp;
	
	private Shader forwardAmbient;
	private Shader shadowShader;
	private Shader godrays;
	private Shader occluded;
	private Shader FXAA_FILTER;
	private Shader GAUS_FILTER;
	private Shader NULL_FILTER;
	
	public static final int FILTER_NONE = 0;
	public static final int FILTER_FXAA = 1;
	
	public RenderingEngine(EngineCore engineCore) {
		super();
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glFrontFace(GL_CW);
		glCullFace(GL_BACK);
		glEnable(GL_CULL_FACE);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_DEPTH_CLAMP);
		glEnable(GL_TEXTURE_2D);
		
		this.engineCore = engineCore;
		this.profiler = new EngineProfiling("RenderingEngine");
		lights = new ArrayList<Light>();
		
		addTexture("R_displayTexture", new Texture("displayTexture", new int[] {GL_LINEAR_MIPMAP_LINEAR}, EngineWindow.width, EngineWindow.height, null, GL_LINEAR, GL_RGBA, GL_RGBA, true, new int[] {GL30.GL_COLOR_ATTACHMENT0}));
		addTexture("R_displayTextureFinal", new Texture("displayTexture2", new int[] {GL_LINEAR_MIPMAP_LINEAR}, EngineWindow.width, EngineWindow.height, null, GL_LINEAR, GL_RGBA, GL_RGBA, true, new int[] {GL30.GL_COLOR_ATTACHMENT0}));
		addTexture("R_displayOccluded", new Texture("occluded", new int[] {GL_LINEAR_MIPMAP_LINEAR}, EngineWindow.width, EngineWindow.height, null, GL_LINEAR, GL_RGBA, GL_RGBA, true, new int[] {GL30.GL_COLOR_ATTACHMENT0}));
		addTexture("R_displayGodrays", new Texture("godrays", new int[] {GL_LINEAR_MIPMAP_LINEAR}, EngineWindow.width, EngineWindow.height, null, GL_LINEAR, GL_RGBA, GL_RGBA, true, new int[] {GL30.GL_COLOR_ATTACHMENT0}));
		
		specialCamera = new Camera((float)Math.toRadians(EngineSettings.fov), ((float)EngineCore.getWidth())/((float)EngineCore.getHeight()), 0.1f, 1000f);
		
		forwardAmbient = new Shader("forward-ambient.vs", "forward-ambient.fs");
		shadowShader = new Shader("shadow-mapping.vs", "shadow-mapping.fs");
		godrays = new Shader("godray-mapping.vs", "godray-mapping.fs");
		occluded = new Shader("occlude.vs", "occlude.fs");
		FXAA_FILTER = new Shader("filter-fxaa.vs", "filter-fxaa.fs");
		GAUS_FILTER = new Shader("filter-gaus7x1.vs", "filter-gaus7x1.fs");
		NULL_FILTER = new Shader("filter-null.vs", "filter-null.fs");
		filterMesh = new StaticMesh("/res/models/plane.obj", false);
		filterMaterial = new Material(getTexture("R_displayTexture"), new Vector3f(1.0f, 1.0f, 1.0f), 1.0f, 8.0f);
		filterTransform = new Transform();
		
		filterTransform.getScale().setVector(1.0f, 1.0f, 1.0f);
		filterTransform.setRotation((new Quaternion(new Vector3f(0.0f, 1.0f, 0.0f), (float)Math.toRadians(180.0f)).mul(new Quaternion(new Vector3f(1.0f, 0.0f, 0.0f), (float)Math.toRadians(90.0f)))).normalize());
		
		addVector3f("R_ambient", new Vector3f(0.1f, 0.1f, 0.1f));
		addVector3f("C_eyePos", new Vector3f(0.1f, 0.1f, 0.1f));
		addMatrix4f("T_MVP", new Matrix4f().initIdentity());
		addMatrix4f("T_model", new Matrix4f().initIdentity());
		addInteger("diffuse", 0);
		addInteger("normalMap", 1);
		addInteger("R_shadowMap", 2);
		addInteger("R_godrayMap", 3);
		addFloat("specularIntensity", 1.0f);
		addFloat("specularPower", 1.0f);
		addVector3f("R_inverseFilterTextureSize", new Vector3f(0f, 0f, 0f));
		addFloat("R_blurScale", 0f);
		addFloat("R_shadowVarianceMin", 0.00002f);
		addFloat("R_shadowLightBleedReduction", 0.2f);
		addFloat("R_fxaaSpanMax", 0f);
		addFloat("R_fxaaReduceMin", 0f);
		addFloat("R_fxaaReduceMul", 0f);
		addFloat("R_godrayDensity", 0.96f);
		addFloat("R_godrayWeight", 0.4f);
		addFloat("R_godrayExposure", 0.6f);
		addFloat("R_godrayDecay", 0.93f);
		addInteger("R_godraySamples", 20);
		addVector3f("R_lightPos", new Vector3f(0, 0, 0));
		lightingMatrix = new Matrix4f().initScale(0.0f, 0.0f, 0.0f);
		
		shadowMaps = new Texture[NUM_SHADOW_MAPS];
		shadowMapsTemp = new Texture[NUM_SHADOW_MAPS];
		
		for(int i = 0; i < shadowMaps.length; i++) {
			int shadowMapSize = 1 << (i + 1);
			shadowMaps[i] = new Texture("shadowMap", new int[] {GL_LINEAR_MIPMAP_LINEAR}, shadowMapSize, shadowMapSize, null, GL_LINEAR, GL30.GL_RG32F, GL_RGBA, true, new int[] {GL30.GL_COLOR_ATTACHMENT0});
			shadowMapsTemp[i] = new Texture("shadowMap", new int[] {GL_LINEAR_MIPMAP_LINEAR}, shadowMapSize, shadowMapSize, null, GL_LINEAR, GL30.GL_RG32F, GL_RGBA, true, new int[] {GL30.GL_COLOR_ATTACHMENT0});
		}
	}
	
	public double DisplayProfilingInfo(double dividend) {
		return this.profiler.displayAndReset("Render Time: ", dividend);
	}
	public void ResetProfilingInfo() {
		this.profiler.reset();
	}
	
	public void render(Map object) {
		this.profiler.startInvocation();
		if(!camera.isPerspective) camera.perspective();
		
		getTexture("R_displayTexture").bindAsRenderTarget();
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glFrontFace(GL_CW);
		glCullFace(GL_BACK);
		glEnable(GL_CULL_FACE);
		glEnable(GL_DEPTH_TEST);
		
//		if(this.lightCycle) {
//			this.activeLight = this.engineCore.getLightCycle().getLight();
//			object.render(this.engineCore.getLightCycle().getLight().getShader(), this);
//		}
		
		object.render(forwardAmbient, this, camera);
		for(Light light : lights) {
			activeLight = light;
			
			Shadow shadow = activeLight.calcShadowCameraTransform(camera.getTransform().getPos(), camera.getTransform().getRotation());
			
			if(shadow != null) {
				setTexture("R_shadowMap", shadowMaps[shadow.getShadowMapSizePower() - 1]);
				shadowMaps[shadow.getShadowMapSizePower() - 1].bindAsRenderTarget();
				glClearColor(1.0f, 1.0f, 0.0f, 0.0f);
				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
				
				specialCamera.setProjection(shadow.getProjection());
				specialCamera.getTransform().getPos().set(shadow.getPosition(camera));
				specialCamera.getTransform().setRotation(shadow.getRotation());
				
				lightingMatrix = specialCamera.getViewProjection();
				
				setFloat("R_shadowVarianceMin", shadow.getvMin());
				setFloat("R_shadowLightBleedReduction", shadow.getLightBleedReduction());
				setFloat("R_blurScale", shadow.getShadowSoftness() / (float)getTexture("R_shadowMap").getWidth());
				
				boolean flipFaces = shadow.isFlipFaces();
				
				if(flipFaces) glCullFace(GL_FRONT);
				glEnable(GL_DEPTH_CLAMP);
				object.render(shadowShader, this, specialCamera);
				glDisable(GL_DEPTH_CLAMP);
				if(flipFaces) glCullFace(GL_BACK);
				
				if(shadow.getShadowSoftness() > 0.0f) {
					blurShadowMap(shadow.getShadowMapSizePower() - 1, shadow.getShadowSoftness());
				}
				
			} else {
				setTexture("R_shadowMap", shadowMaps[0]);
				shadowMaps[0].bindAsRenderTarget();
				glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			}
			
			glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
			setVector3f("R_inverseFilterTextureSize", new Vector3f(1.0f / EngineWindow.width, 1.0f/ EngineWindow.height, 0.0f));
			setFloat("R_fxaaSpanMax", 8.0f);
			setFloat("R_fxaaReduceMin", 1.0f / 128.0f);
			setFloat("R_fxaaReduceMul", 1.0f / 8.0f);
			
			getTexture("R_displayTexture").bindAsRenderTarget();
			
			glEnable(GL_BLEND);
			glBlendFunc(GL_ONE, GL_ONE);
			glDepthMask(false);
			glDepthFunc(GL_EQUAL);
			
			object.render(light.getShader(), this, camera);
			
			glDepthFunc(GL_LESS);
			glDepthMask(true);
			glDisable(GL_BLEND);
			
		}
		applyFilter(FXAA_FILTER, getTexture("R_displayTexture"), getTexture("R_displayTextureFinal"));
		
//		if(EngineSettings.godrays) {
//			getTexture("R_displayOccluded").bindAsRenderTarget();
//			
//			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
//			camera.getTransform().aimAt(new Vector3f(0f, engineCore.getGame().terrain.getHeightAt(256, 256) + 8f, 8f), camera.getTransform().getRotation().getRotateUp());
//			
//			for(Light light : lights) {
//				setVector3f("R_color", new Vector3f(1, 1, 1));
//				light.render(occluded, this, camera);
//				
//				setVector3f("R_color", new Vector3f(1, 1, 1));
//				object.render(occluded, this, camera);
//				
//				setVector3f("R_lightPos", new Vector3f(0.5f, 1.0f, 0.0f));
//				System.out.println(getVector3f("R_lightPos"));
//				applyFilter(godrays, getTexture("R_displayOccluded"), getTexture("R_displayGodrays"));
//			}
//		}
		
		applyFilter(getFilter(EngineSettings.PostRenderingFilter), getTexture("R_displayTextureFinal"), null);
		
		EngineWindow.bindAsRenderTarget();
		object.renderPost(NULL_FILTER, this, camera);
		engineCore.getGuiEngine().render(engineCore, NULL_FILTER, engineCore.getGame(), engineCore.getPlayer(), EngineCore.ingame, EngineWindow.width, EngineWindow.height, (float)EngineWindow.width / (float)EngineWindow.height);
		
		this.profiler.stopInvocation();
	}
	
	private void blurShadowMap(int shadowMapIndex, float blurAmount) {
		setVector3f("R_blurScale", new Vector3f(blurAmount / shadowMaps[shadowMapIndex].getWidth(), 0.0f, 0.0f));
		applyFilter(GAUS_FILTER, shadowMaps[shadowMapIndex], shadowMapsTemp[shadowMapIndex]);
		
		setVector3f("R_blurScale", new Vector3f(0.0f, blurAmount / shadowMaps[shadowMapIndex].getHeight(), 0.0f));
		applyFilter(GAUS_FILTER, shadowMapsTemp[shadowMapIndex], shadowMaps[shadowMapIndex]);
	}
	
	private void applyFilter(Shader filter, Texture src, Texture dest) {
		if(dest != null) dest.bindAsRenderTarget();
		else EngineWindow.bindAsRenderTarget();
		
		filterMaterial.setTexture("diffuse", src);
		
		specialCamera.setProjection(new Matrix4f().initIdentity());
		specialCamera.getTransform().getScale().setVector(1.0f, 1.0f, 1.0f);
		specialCamera.getTransform().getPos().setVector(0.0f, 0.0f, 0.0f);
		specialCamera.getTransform().setRotation(new Quaternion(new Vector3f(0.0f, 0.0f, 0.0f), (float)Math.toRadians(0.0f)));
		
		glClear(GL_DEPTH_BUFFER_BIT);
		filter.bind();
		filter.updateUniforms(filterTransform, this, filterMaterial, specialCamera);
		filterMesh.draw();
	}
	
	public Shader getFilter(int type) {
		switch(type) {
			case FILTER_NONE:
				return NULL_FILTER;
			case FILTER_FXAA:
				return FXAA_FILTER;
			default:
				return NULL_FILTER;
		}
	}
	
	public Vector3f getScreenCoordinates(Transform transform) {
		Vector3f result = camera.getViewProjection().mul(transform.getTransformation()).transform(transform.getPos());
		
		return new Vector3f((int) Math.round(((1 + result.getX()) / 2.0f) * EngineWindow.width), (int) Math.round(((1 - result.getY()) / 2.0f) * EngineWindow.height), 0);
	}
	
	public Camera getSpecialCamera() {
		return this.specialCamera;
	}
	
	public Light getLight() {
		return activeLight;
	}

	public Shader getForwardAmbient() {
		return forwardAmbient;
	}
	
	public void setLightCycle(boolean lightCycle) {
//		this.lightCycle = lightCycle;
	}
	
	public void addLight(Light light) {
		lights.add(light);
	}
	
	public Matrix4f getLightingMatrix() {
		return this.lightingMatrix;
	}
	
	public static void setTextures(boolean enabled) {
		if(enabled) glEnable(GL_TEXTURE_2D);
		else glDisable(GL_TEXTURE_2D);
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public EngineCore getEngineCore() {
		return this.engineCore;
	}

	public TextRenderer getTextRenderer() {
		return engineCore.getGuiEngine().getTextRenderer();
	}
}
