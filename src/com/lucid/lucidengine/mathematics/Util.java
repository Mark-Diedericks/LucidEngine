package com.lucid.lucidengine.mathematics;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

import com.lucid.lucidengine.main.EngineResource;

/**
 * @author Mark Diedericks
 *
 */

public class Util {
	
	public static FloatBuffer createFloatBuffer(int size) {
		return BufferUtils.createFloatBuffer(size);
	}
	
	public static IntBuffer createIntBuffer(int size) {
		return BufferUtils.createIntBuffer(size);
	}
	
	public static ByteBuffer createByteBuffer(int size) {
		return BufferUtils.createByteBuffer(size);
	}
	
	public static IntBuffer createFlippedBuffer(int... values) {
		IntBuffer buffer = createIntBuffer(values.length);
		buffer.put(values);
		buffer.flip();
		return buffer;
	}
	
	public static FloatBuffer createFlippedBuffer(Vertex[] vertices) {
		FloatBuffer buffer = createFloatBuffer(vertices.length * Vertex.SIZE);
		
		for(int i = 0; i < vertices.length; i++) {
			buffer.put(vertices[i].getPos().getX());
			buffer.put(vertices[i].getPos().getY());
			buffer.put(vertices[i].getPos().getZ());
			buffer.put(vertices[i].getTextureUV().getX());
			buffer.put(vertices[i].getTextureUV().getY());
			buffer.put(vertices[i].getNormal().getX());
			buffer.put(vertices[i].getNormal().getY());
			buffer.put(vertices[i].getNormal().getZ());
			buffer.put(vertices[i].getTangent().getX());
			buffer.put(vertices[i].getTangent().getY());
			buffer.put(vertices[i].getTangent().getZ());
		}
		
		buffer.flip();
		
		return buffer;
	}
	
	public static FloatBuffer createFlippedBuffer(Matrix4f mat4f) {
		FloatBuffer buffer = createFloatBuffer(4*4);
		
		for(int x = 0; x < 4; x++) {
			for(int y = 0; y < 4; y++) {
				buffer.put(mat4f.get(x, y));
			}
		}
		
		buffer.flip();
		
		return buffer;
	}

	public static String[] removeNullStrings(String[] meshData) {
		
		ArrayList<String> product = new ArrayList<String>();
		
		for(int i = 0; i < meshData.length; i++) {
			if(!meshData[i].equals("")) {
				product.add(meshData[i]);
			}
		}
		
		String[] finalProduct = new String[product.size()];
		product.toArray(finalProduct);
		
		return finalProduct;
	}

	public static int[] toIntArray(Integer[] indicesData) {
		
		int[] indices = new int[indicesData.length];
		
		for(int i = 0; i < indicesData.length; i++) {
			indices[i] = indicesData[i].intValue();
		}
		
		return indices;
	}
	
	public static ByteBuffer loadBufferedImage(BufferedImage texture) {
		int[] pixels = texture.getRGB(0, 0, texture.getWidth(), texture.getHeight(), null, 0, texture.getWidth());
		ByteBuffer buffer = createByteBuffer(texture.getWidth()*texture.getHeight()*4);
		boolean alpha = texture.getColorModel().hasAlpha();
		for(int y = 0; y < texture.getHeight(); y++) {
			for(int x = 0; x < texture.getWidth(); x++) {
				  int rgba = pixels[y*texture.getWidth()+x];
				  buffer.put((byte)((rgba >> 16) & 0xFF));
				  buffer.put((byte)((rgba >> 8) & 0xFF));
				  buffer.put((byte)(rgba & 0xFF));
				  if(alpha) buffer.put((byte)((rgba >> 24) & 0xFF));
				  else buffer.put((byte)(0xFF));
			}
		}
		buffer.flip();
		return buffer;
	}
	
	public static ByteBuffer[] getIcon(String iconName1, String iconName2) {
		try {
			BufferedImage icon1 = ImageIO.read(EngineResource.getResource("/res/icons/" + iconName1));
			BufferedImage icon2 = ImageIO.read(EngineResource.getResource("/res/icons/" + iconName2));
			return new ByteBuffer[] {loadBufferedImage(icon1), loadBufferedImage(icon2)};
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}
}
