package com.lucid.lucidengine.rendering.lighting;

import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.*;
import static org.lwjgl.opengl.GL40.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import com.lucid.lucidengine.main.EngineResource;
import com.lucid.lucidengine.main.EngineWindow;
import com.lucid.lucidengine.mathematics.Matrix4f;
import com.lucid.lucidengine.mathematics.Transform;
import com.lucid.lucidengine.mathematics.Util;
import com.lucid.lucidengine.mathematics.Vector3f;
import com.lucid.lucidengine.object.lights.DirectionalLight;
import com.lucid.lucidengine.object.lights.Light;
import com.lucid.lucidengine.object.lights.PointLight;
import com.lucid.lucidengine.object.lights.SpotLight;
import com.lucid.lucidengine.rendering.Material;
import com.lucid.lucidengine.rendering.RenderingEngine;
import com.lucid.lucidengine.ui.Camera;

/**
 * @author Mark Diedericks
 *
 */

public class Shader {
	
	public String vertexFile;
	public String fragmentFile;
	private HashMap<String, ShaderHandler> shaders = new HashMap<String, ShaderHandler>();
	private ShaderHandler handler;
	public String fileName;
	private static final String UNIFORM = "uniform";
	private static final String STRUCT = "struct";
	private static final String IMPORT = "import";
	private static final String ATTRIBUTE = "attribute";
	private int shader;
	
	public Shader(String vsf, String fsf) {
		String vs = loadShader(vsf);
		String fs = loadShader(fsf);
		
		String fileName = "/res/shaders/" + vsf;
		ShaderHandler shader = shaders.get(fileName);
		this.fileName = fileName;
		
		if(shader != null) {
			this.handler = shader;
			this.handler.addReference();
		}
		else {
			this.handler = new ShaderHandler();
			shaders.put(fileName, handler);
		}
		
		vertexFile = vs;
		fragmentFile = fs;
		
		addVertexShader(vs);
		addFragmentShader(fs);
		addAllAttributes(vs);
		compileShader();
		bind();
		addAllUniforms(vs);
		addAllUniforms(fs);
	}
	
	@Override
	protected void finalize() {
		if(handler.subReference() && !fileName.isEmpty()) {
			shaders.remove(this.fileName);
		}
	}
	
	protected static String loadShader(String fileName) {
		StringBuilder shaderSource = new StringBuilder();
		BufferedReader shaderReader;
		
		try {
			shaderReader = new BufferedReader(new InputStreamReader(EngineResource.getResource("/res/shaders/" + fileName)));
			String line;
			
			while((line = shaderReader.readLine()) != null) {
				if(line.startsWith(IMPORT)) {
					shaderSource.append(loadShader(line.substring(IMPORT.length()+1, line.length()-1)));
				}
				else
				shaderSource.append(line).append("\n");
			}
			
			shaderReader.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return shaderSource.toString();
	}
	
	public void addVertexShader(String text) {
		addProgram(text, GL_VERTEX_SHADER);
	}
	
	public void addGeometryShader(String text) {
		addProgram(text, GL_GEOMETRY_SHADER);
	}
	
	public void addFragmentShader(String text) {
		addProgram(text, GL_FRAGMENT_SHADER);
	}
	
	public void addTessellationCShader(String text) {
		addProgram(text, GL_TESS_CONTROL_SHADER);
	}
	
	public void addTessellationEShader(String text) {
		addProgram(text, GL_TESS_EVALUATION_SHADER);
	}
	
	public void setAttribLocation(String name, int location) {
		glBindAttribLocation(handler.getProgram(), location, name);
	}
	
	private void compileShader() {
		glLinkProgram(handler.getProgram());
		
		if(glGetProgrami(handler.getProgram(), GL_LINK_STATUS) == 0)
		{
			System.err.println(glGetProgramInfoLog(handler.getProgram(), 1024));
			System.exit(1);
		}

		glValidateProgram(handler.getProgram());

		if(glGetProgrami(handler.getProgram(), GL_VALIDATE_STATUS) == 0)
		{
			System.err.println(glGetProgramInfoLog(handler.getProgram(), 1024));
			System.exit(1);
		}
	}
	
	public void bind() {
		glUseProgram(handler.getProgram());
	}
	
	private void addAllAttributes(String shaderText)
	{
		int attributeStartLocation = shaderText.indexOf(ATTRIBUTE);
		int attribNumber = 0;
		while(attributeStartLocation != -1)
		{
			if(!(attributeStartLocation != 0
				&& (Character.isWhitespace(shaderText.charAt(attributeStartLocation - 1)) || shaderText.charAt(attributeStartLocation - 1) == ';')
				&& Character.isWhitespace(shaderText.charAt(attributeStartLocation + ATTRIBUTE.length())))) {
					attributeStartLocation = shaderText.indexOf(ATTRIBUTE, attributeStartLocation + ATTRIBUTE.length());
					continue;

			}

			int begin = attributeStartLocation + ATTRIBUTE.length() + 1;
			int end = shaderText.indexOf(";", begin);

			String attributeLine = shaderText.substring(begin, end).trim();
			String attributeName = attributeLine.substring(attributeLine.indexOf(' ') + 1, attributeLine.length()).trim();

			setAttribLocation(attributeName, attribNumber);
			attribNumber++;

			attributeStartLocation = shaderText.indexOf(ATTRIBUTE, attributeStartLocation + ATTRIBUTE.length());
		}
	}
	
	public void updateUniforms(Transform transform, RenderingEngine renderingEngine, Material material, Camera camera) {
		Matrix4f worldMat = transform.getTransformation();
		renderingEngine.setMatrix4f("T_model", worldMat);
		Matrix4f projectMat = camera.getViewProjection().mul(worldMat);
		renderingEngine.setMatrix4f("T_MVP", projectMat);
		
		renderingEngine.setFloat("specularIntensity", material.getSpecularIntensity());
		renderingEngine.setFloat("specularPower", material.getSpecularExponent());
		renderingEngine.setVector3f("R_color", material.getColor());
		
		renderingEngine.setInteger("R_width", EngineWindow.width);
		renderingEngine.setInteger("R_height", EngineWindow.height);
		
		for(Uniform uniform : handler.getUniforms()) {
			if(uniform.name.startsWith("R_")) {
				
				if(uniform.type.equals("sampler2D")) {
					int slot = renderingEngine.getInteger(uniform.name);
					assert(slot >= 0 && slot <= 31);
					glActiveTexture(GL_TEXTURE0 + slot);
					renderingEngine.getTexture(uniform.name).bind();
					setUniformi(uniform.name, renderingEngine.getInteger(uniform.name));
				} else if(uniform.type.equals("mat4") && uniform.name.equals("R_lightingMatrix")) {
					setUniform("R_lightingMatrix", renderingEngine.getLightingMatrix().mul(worldMat));
				} else if(uniform.type.equals("vec3") && (uniform.name.equals("R_ambient") || uniform.name.equals("R_inverseFilterTextureSize") || uniform.name.equals("R_blurScale") || uniform.name.equals("R_lightPos") || uniform.name.equals("R_color") || uniform.name.equals("R_fogColor"))) {
					setUniform(uniform.name, (Vector3f)renderingEngine.getVector3f(uniform.name));
				} else if(uniform.type.equals("float") && (uniform.name.equals("R_shadowVarianceMin") || uniform.name.equals("R_shadowLightBleedReduction") || uniform.name.equals("R_blurScale")|| uniform.name.equals("R_fxaaSpanMax")|| uniform.name.equals("R_fxaaReduceMin")|| uniform.name.equals("R_fxaaReduceMul")|| uniform.name.equals("R_godraysDensity")|| uniform.name.equals("R_godraysWeight")|| uniform.name.equals("R_godraysExposure") || uniform.name.equals("R_godrayDecay") || uniform.name.equals("R_fogIntensity"))) {
					setUniformf(uniform.name, renderingEngine.getFloat(uniform.name));
				} else if(uniform.type.equals("int") && (uniform.name.equals("R_renderType") || uniform.name.equals("R_godraySamples") || uniform.name.equals("R_width") || uniform.name.equals("R_height"))) {
					setUniformi(uniform.name, renderingEngine.getInteger(uniform.name));
				} else if(uniform.name.contains(".")) {
					String Lname = uniform.name.substring(0, uniform.name.indexOf('.'));
					
					if(Lname.equals("R_directionalLight")) {
						setUniformDL(Lname, (DirectionalLight)renderingEngine.getLight());
					}
					
					if(Lname.equals("R_pointLight")) {
						setUniformPL(Lname, (PointLight)renderingEngine.getLight());
					}
					
					if(Lname.equals("R_spotLight")) {
						setUniformSL(Lname, (SpotLight)renderingEngine.getLight());
					}
				}
				
			} else if(!uniform.name.contains(".")) {
				if(uniform.type.equals("int")) setUniformi(uniform.name, renderingEngine.getInteger(uniform.name));
				else if(uniform.type.equals("float")) setUniformf(uniform.name, renderingEngine.getFloat(uniform.name));
				else if(uniform.type.equals("vec3")) setUniform(uniform.name, renderingEngine.getVector3f(uniform.name));
				else if(uniform.type.equals("mat4")) setUniform(uniform.name, renderingEngine.getMatrix4f(uniform.name));
				else if(uniform.type.equals("sampler2D")) {
					int slot = renderingEngine.getInteger(uniform.name);
					assert(slot >= 0 && slot <= 31);
					glActiveTexture(GL_TEXTURE0 + slot);
					material.getTexture(uniform.name).bind();
					setUniformi(uniform.name, renderingEngine.getInteger(uniform.name));
				}
			}
		}
	}
	
	public void addAllUniforms(String text) {
		int start = text.indexOf(UNIFORM);
		HashMap<String, ArrayList<Structure>> structs = identifyStructs(text);
		
		while(start != -1) {
			if(!(start != 0
					&& (Character.isWhitespace(text.charAt(start - 1)) || text.charAt(start - 1) == ';')
					&& Character.isWhitespace(text.charAt(start + UNIFORM.length()))))
				continue;
			
			int indexBegin = start + UNIFORM.length() + 1;
			int indexEnd = text.indexOf(";", indexBegin);
			
			String data = text.substring(indexBegin, indexEnd).trim();
			int emptyness = data.indexOf(' ');
			String uniformName = data.substring(emptyness + 1, data.length()).trim();
			String uniformType = data.substring(0, emptyness).trim(); 
			
			addUniformWithStruct(uniformName, uniformType, structs);
			
			start = text.indexOf(UNIFORM, start + UNIFORM.length());
		}
	}
	
	private void addUniformWithStruct(String uniformName, String uniformType, HashMap<String, ArrayList<Structure>> structs) {
		boolean shouldAdd = true;
		ArrayList<Structure> variables = structs.get(uniformType);
		
		if(variables != null) {
			shouldAdd = false;
			
			for(Structure struct : variables) {
				addUniformWithStruct(uniformName + "." + struct.name, struct.type, structs);
			}
		}
		
		if(shouldAdd) addUniform(uniformName, uniformType);
	}
	
	private HashMap<String, ArrayList<Structure>> identifyStructs(String text) {
		int start = text.indexOf(STRUCT);
		HashMap<String, ArrayList<Structure>> res = new HashMap<String, ArrayList<Structure>>();
		while(start > -1) {
			if(!(start != 0
					&& (Character.isWhitespace(text.charAt(start - 1)) || text.charAt(start - 1) == ';')
					&& Character.isWhitespace(text.charAt(start + STRUCT.length()))))
				continue;
			int nameBegin = start + STRUCT.length() + 1;
			int brackets = text.indexOf("{", nameBegin);
			int bracketsEnd = text.indexOf("}", nameBegin);
			
			String structName = text.substring(nameBegin, brackets).trim();
			ArrayList<Structure> variables = new ArrayList<Structure>();
			
			int variableEnd = text.indexOf(";", brackets);
			while(variableEnd != -1 && variableEnd < bracketsEnd) {
				int nameStart = variableEnd;
				while(!Character.isWhitespace(text.charAt(nameStart-1)))
					nameStart--;
				
				int variableTypeEnd = nameStart -1;
				int variableTypeStart = variableTypeEnd;
				
				while(!Character.isWhitespace(text.charAt(variableTypeStart -1)))
					variableTypeStart--;
				
				Structure struct = new Structure();
				struct.name = text.substring(nameStart, variableEnd);
				struct.type = text.substring(variableTypeStart, variableTypeEnd);
				
				variables.add(struct);
				
				variableEnd = text.indexOf(";", variableEnd + 1);
			}
			
			res.put(structName, variables);
			start = text.indexOf(STRUCT, start + STRUCT.length());
		}
		return res;
	}
	
	public void addUniform(String uniformName, String uniformType) {
		int uniformLocation = glGetUniformLocation(handler.getProgram(), uniformName.trim());
		
		if(uniformLocation == -1) {
			System.err.println("Error: could not find uniform, " + uniformName + " in shader; " + this.fileName);
			new Exception().printStackTrace();
			System.exit(1);
		}
		
		Uniform uniform = new Uniform();
		uniform.name = uniformName;
		uniform.type = uniformType;
		uniform.location = uniformLocation;
		
		handler.addUniform(uniform);
	}

	public void setUniformf(String uniform, float value) {
		glUniform1f(handler.getLocations().get(uniform), value);
	}
	
	public void setUniformi(String uniform, int value) {
		glUniform1i(handler.getLocations().get(uniform), value);
	}
	
	public void setUniform(String uniform, Vector3f value) {
		glUniform3f(handler.getLocations().get(uniform), value.getX(), value.getY(), value.getZ());
	}
	
	public void setUniform(String uniform, Matrix4f mat4f) {
		glUniformMatrix4(handler.getLocations().get(uniform), true, Util.createFlippedBuffer(mat4f));
	}
	
	
	public void setUniformL(String uniform, Light light) {
		setUniform(uniform + ".color", light.getColor());
		setUniformf(uniform + ".intensity", light.getIntensity());
	}
	
	public void setUniformDL(String uniform, DirectionalLight light) {
		setUniformL(uniform + ".base", (Light)light);
		setUniform(uniform + ".direction", light.getDirection());
	}
	
	private void setUniformPL(String uniform, PointLight pl) {
		setUniformL(uniform + ".base", (Light)pl);
		setUniformf(uniform + ".atten.constant", pl.getConstant());
		setUniformf(uniform + ".atten.linear", pl.getLinear());
		setUniformf(uniform + ".atten.exponent", pl.getExp());
		setUniform(uniform + ".position", pl.getTransform().getPos());
		setUniformf(uniform + ".range", pl.getRange());
	}
	
	private void setUniformSL(String uniform, SpotLight pl) {
		setUniformPL(uniform + ".pointLight", (PointLight)pl);
		setUniform(uniform + ".direction", pl.getDirection());
		setUniformf(uniform + ".cutoff", pl.getCutoff());
	}
	
	private void addProgram(String text, int type) {
		shader = glCreateShader(type);
		
		if(shader == 0) {
			System.err.println("Shader creation failed, could not find valid memory location whilst adding shader!");
			System.exit(1);
		}
		
		glShaderSource(shader, text);
		glCompileShader(shader);
		
		if(glGetShaderi(shader, GL_COMPILE_STATUS) == 0)
		{
			System.err.println(glGetShaderInfoLog(shader, 2048));
			System.exit(1);
		}
		glAttachShader(handler.getProgram(), shader);
	}

	public void dispose() {
		glDetachShader(handler.getProgram(), shader);
		glDeleteShader(shader);
		glDeleteProgram(handler.getProgram());
	}
	
}