package com.lucid.lucidengine.gui;

import static org.lwjgl.opengl.GL11.GL_CULL_FACE;

import java.util.HashMap;

import org.lwjgl.opengl.GL11;

import com.lucid.lucidengine.mathematics.Quaternion;
import com.lucid.lucidengine.mathematics.Transform;
import com.lucid.lucidengine.mathematics.Vector2f;
import com.lucid.lucidengine.mathematics.Vector3f;
import com.lucid.lucidengine.mathematics.Vertex;
import com.lucid.lucidengine.rendering.Material;
import com.lucid.lucidengine.rendering.RenderingEngine;
import com.lucid.lucidengine.rendering.Texture;
import com.lucid.lucidengine.rendering.mesh.MeshRenderer;
import com.lucid.lucidengine.rendering.mesh.StaticMesh;

/**
 * @author Mark Diedericks
 *
 */

public class TextRenderer {
	
	private final MeshRenderer character;
	private Vertex[] vertices;
	private int[] indices;
	
	private HashMap<Integer, Integer> widths;
	
	private GuiEngine guiEngine;
	private int charPerRow = 16;
	
	public TextRenderer(GuiEngine guiEngine) {
		this.guiEngine = guiEngine;
		vertices = new Vertex[] {new Vertex(new Vector3f(64f, 64f, 1f), new Vector2f(1f, 1f)),
				new Vertex(new Vector3f(64f, 0f, 1f), new Vector2f(1f, 0)),
				new Vertex(new Vector3f(0f, 64f, 1f), new Vector2f(0, 1f)),
				new Vertex(new Vector3f(0f, 0f, 1f), new Vector2f(0, 0))};
		indices = new int[] {0, 1, 2, 3, 2, 1};
		
		StaticMesh mesh  = new StaticMesh(vertices, indices, false);
		Material material = new Material(new Texture("/res/textures/FontWhite.png"), new Vector3f(1f, 1f, 1f), 1f, 1f);
		this.character = new MeshRenderer(mesh, material);
		
		this.widths = new HashMap<Integer, Integer>();
		
		this.widths.put(0, 24); 
		this.widths.put(1, 24); 
		this.widths.put(2, 24); 
		this.widths.put(3, 24); 
		this.widths.put(4, 24); 
		this.widths.put(5, 24); 
		this.widths.put(6, 24); 
		this.widths.put(7, 24); 
		this.widths.put(8, 24); 
		this.widths.put(9, 24); 
		this.widths.put(10, 24); 
		this.widths.put(11, 24); 
		this.widths.put(12, 24); 
		this.widths.put(13, 24); 
		this.widths.put(14, 24); 
		this.widths.put(15, 24); 
		this.widths.put(16, 24); 
		this.widths.put(17, 24); 
		this.widths.put(18, 24); 
		this.widths.put(19, 24); 
		this.widths.put(20, 24); 
		this.widths.put(21, 24); 
		this.widths.put(22, 24); 
		this.widths.put(23, 24); 
		this.widths.put(24, 24); 
		this.widths.put(25, 24); 
		this.widths.put(26, 24); 
		this.widths.put(27, 24); 
		this.widths.put(28, 24); 
		this.widths.put(29, 24); 
		this.widths.put(30, 24); 
		this.widths.put(31, 24); 
		this.widths.put(32, 7); 
		this.widths.put(33, 8); 
		this.widths.put(34, 10); 
		this.widths.put(35, 15); 
		this.widths.put(36, 15); 
		this.widths.put(37, 24); 
		this.widths.put(38, 18); 
		this.widths.put(39, 5); 
		this.widths.put(40, 9); 
		this.widths.put(41, 9); 
		this.widths.put(42, 11); 
		this.widths.put(43, 16); 
		this.widths.put(44, 8); 
		this.widths.put(45, 9); 
		this.widths.put(46, 8); 
		this.widths.put(47, 8); 
		this.widths.put(48, 15); 
		this.widths.put(49, 15); 
		this.widths.put(50, 15); 
		this.widths.put(51, 15); 
		this.widths.put(52, 15); 
		this.widths.put(53, 15); 
		this.widths.put(54, 15); 
		this.widths.put(55, 15); 
		this.widths.put(56, 15); 
		this.widths.put(57, 15); 
		this.widths.put(58, 15); 
		this.widths.put(59, 8); 
		this.widths.put(60, 16); 
		this.widths.put(61, 16); 
		this.widths.put(62, 16); 
		this.widths.put(63, 15); 
		this.widths.put(64, 27); 
		this.widths.put(65, 20); //CAPS START
		this.widths.put(66, 21); 
		this.widths.put(67, 19); 
		this.widths.put(68, 19); 
		this.widths.put(69, 19); 
		this.widths.put(70, 17); 
		this.widths.put(71, 19); 
		this.widths.put(72, 20); 
		this.widths.put(73, 6); 
		this.widths.put(74, 13); 
		this.widths.put(75, 18); 
		this.widths.put(76, 17); 
		this.widths.put(77, 22); 
		this.widths.put(78, 18); 
		this.widths.put(79, 21); 
		this.widths.put(80, 18); 
		this.widths.put(81, 21); 
		this.widths.put(82, 20); 
		this.widths.put(83, 19); 
		this.widths.put(84, 16); 
		this.widths.put(85, 18); 
		this.widths.put(86, 18); 
		this.widths.put(87, 26); 
		this.widths.put(88, 18); 
		this.widths.put(89, 18); 
		this.widths.put(90, 17); //CAPS END
		this.widths.put(91, 8); 
		this.widths.put(92, 8); 
		this.widths.put(93, 8); 
		this.widths.put(94, 12); 
		this.widths.put(95, 15); 
		this.widths.put(96, 9); 
		this.widths.put(97, 15); //LOWCASE START
		this.widths.put(98, 16); 
		this.widths.put(99, 14); 
		this.widths.put(100, 15); 
		this.widths.put(101, 15); 
		this.widths.put(102, 10); 
		this.widths.put(103, 15); 
		this.widths.put(104, 15); 
		this.widths.put(105, 6); 
		this.widths.put(106, 6); 
		this.widths.put(107, 15); 
		this.widths.put(108, 6); 
		this.widths.put(109, 23); 
		this.widths.put(110, 15); 
		this.widths.put(111, 16); 
		this.widths.put(112, 16); 
		this.widths.put(113, 15); 
		this.widths.put(114, 11); 
		this.widths.put(115, 14); 
		this.widths.put(116, 9); 
		this.widths.put(117, 15); 
		this.widths.put(118, 15); 
		this.widths.put(119, 22); 
		this.widths.put(120, 15); 
		this.widths.put(121, 15); 
		this.widths.put(122, 14); //LOWCASE END
		this.widths.put(123, 6); 
		this.widths.put(124, 9); 
		this.widths.put(125, 16); 
		this.widths.put(126, 20); 
		this.widths.put(127, 15); 
		this.widths.put(128, 15); 
		this.widths.put(129, 20); 
		this.widths.put(130, 6); 
		this.widths.put(131, 15); 
		this.widths.put(132, 9); 
		this.widths.put(133, 27); 
		this.widths.put(134, 15); 
		this.widths.put(135, 15); 
		this.widths.put(136, 9); 
		this.widths.put(137, 29); 
		this.widths.put(138, 18); 
		this.widths.put(139, 9); 
		this.widths.put(140, 27); 
		this.widths.put(141, 20); 
		this.widths.put(142, 17); 
		this.widths.put(143, 20); 
		this.widths.put(144, 20); 
		this.widths.put(145, 6); 
		this.widths.put(146, 6); 
		this.widths.put(147, 9); 
		this.widths.put(148, 9); 
		this.widths.put(149, 9); 
		this.widths.put(150, 15); 
		this.widths.put(151, 27); 
		this.widths.put(152, 8); 
		this.widths.put(153, 27); 
		this.widths.put(154, 14); 
		this.widths.put(155, 9); 
		this.widths.put(156, 25); 
		this.widths.put(157, 20); 
		this.widths.put(158, 13); 
		this.widths.put(159, 18); 
		this.widths.put(160, 8); 
		this.widths.put(161, 8); 
		this.widths.put(162, 15); 
		this.widths.put(163, 15); 
		this.widths.put(164, 15); 
		this.widths.put(165, 15); 
		this.widths.put(166, 6); 
		this.widths.put(167, 15); 
		this.widths.put(168, 9); 
		this.widths.put(169, 20); 
		this.widths.put(170, 10); 
		this.widths.put(171, 15); 
		this.widths.put(172, 16); 
		this.widths.put(173, 9); 
		this.widths.put(174, 20); 
		this.widths.put(175, 15); 
		this.widths.put(176, 11); 
		this.widths.put(177, 15); 
		this.widths.put(178, 9); 
		this.widths.put(179, 9); 
		this.widths.put(180, 9); 
		this.widths.put(181, 16); 
		this.widths.put(182, 15); 
		this.widths.put(183, 9); 
		this.widths.put(184, 9); 
		this.widths.put(185, 9); 
		this.widths.put(186, 10); 
		this.widths.put(187, 15); 
		this.widths.put(188, 23); 
		this.widths.put(189, 23); 
		this.widths.put(190, 23); 
		this.widths.put(191, 17); 
		this.widths.put(192, 18); 
		this.widths.put(193, 18); 
		this.widths.put(194, 18); 
		this.widths.put(195, 18); 
		this.widths.put(196, 18); 
		this.widths.put(197, 18); 
		this.widths.put(198, 27); 
		this.widths.put(199, 20); 
		this.widths.put(200, 18); 
		this.widths.put(201, 18); 
		this.widths.put(202, 18); 
		this.widths.put(203, 18); 
		this.widths.put(204, 8); 
		this.widths.put(205, 8); 
		this.widths.put(206, 8); 
		this.widths.put(207, 8); 
		this.widths.put(208, 20); 
		this.widths.put(209, 19); 
		this.widths.put(210, 21); 
		this.widths.put(211, 21); 
		this.widths.put(212, 21); 
		this.widths.put(213, 21); 
		this.widths.put(214, 21); 
		this.widths.put(215, 16); 
		this.widths.put(216, 21); 
		this.widths.put(217, 19); 
		this.widths.put(218, 19); 
		this.widths.put(219, 19); 
		this.widths.put(220, 19); 
		this.widths.put(221, 18); 
		this.widths.put(222, 18); 
		this.widths.put(223, 17); 
		this.widths.put(224, 15); 
		this.widths.put(225, 15); 
		this.widths.put(226, 15); 
		this.widths.put(227, 15); 
		this.widths.put(228, 15); 
		this.widths.put(229, 15); 
		this.widths.put(230, 24); 
		this.widths.put(231, 14); 
		this.widths.put(232, 15); 
		this.widths.put(233, 15); 
		this.widths.put(234, 15); 
		this.widths.put(235, 15); 
		this.widths.put(236, 6); 
		this.widths.put(237, 6); 
		this.widths.put(238, 6); 
		this.widths.put(239, 6); 
		this.widths.put(240, 15); 
		this.widths.put(241, 15); 
		this.widths.put(242, 15); 
		this.widths.put(243, 15); 
		this.widths.put(244, 15); 
		this.widths.put(245, 15); 
		this.widths.put(246, 15); 
		this.widths.put(247, 15); 
		this.widths.put(248, 17); 
		this.widths.put(249, 15); 
		this.widths.put(250, 15); 
		this.widths.put(251, 15); 
		this.widths.put(252, 15); 
		this.widths.put(253, 14); 
		this.widths.put(254, 15); 
		this.widths.put(255, 14); 
	}
	
	public int renderString(String str, float x, float y, float size) {
		float charSize = (1.0f / (float)charPerRow);
		float scale = size / 64f;
		character.getTransform().getScale().setVector(scale, scale, scale);
		for(int i = 0; i < str.length(); i++) {
			int ascii = (int)str.charAt(i);
			float coordX = ((int) ascii % charPerRow) * charSize;
			float coordY = ((int) ascii / charPerRow) * charSize;
			
			vertices = character.getStaticMesh().getVertices();
			vertices[0].getTextureUV().setVector(coordX+charSize, coordY+charSize);
			vertices[1].getTextureUV().setVector(coordX+charSize, coordY);
			vertices[2].getTextureUV().setVector(coordX, coordY+charSize);
			vertices[3].getTextureUV().setVector(coordX, coordY);
			character.getStaticMesh().addVertices(vertices, indices, false);
			
			character.getTransform().getPos().setVector(x, y, 0.1f);
			character.getTransform().setRotation(new Quaternion(0f, 0f, 0f, 0f));
			guiEngine.drawComponent(character.getMaterial().getTexture("diffuse"), character.getStaticMesh(), character.getTransform());
			x += ((this.widths.get(ascii) / 32.0f) * size) + scale; 
		}
		return (int)x;
	}
	
	public void renderString(String str, Transform transform, float yOffset, float size, RenderingEngine renderingEngine, boolean centered) {
		GL11.glDisable(GL_CULL_FACE);
		float charSize = (1.0f / (float)charPerRow);
		float scale = size / 64f;
		float width = getWidth(str, size);
		character.setTransform(transform);
		if(centered) character.getTransform().move(transform.getRotation().getRotateRight(), -(width / 2.0f));
		character.getTransform().getPos().setY(character.getTransform().getPos().getY() + yOffset);
		character.getTransform().getScale().setVector(scale, scale, scale);
		
		float startX = transform.getPos().getX();
		float startZ = transform.getPos().getZ();
		double direction = Math.atan2((renderingEngine.getCamera().getTransform().getPos().getX()) - startX, (renderingEngine.getCamera().getTransform().getPos().getZ()) - startZ);
		character.getTransform().setRotation((new Quaternion(new Vector3f(0.0f, 0.0f, 1.0f), (float)Math.toRadians(180.0f)).mul(new Quaternion(new Vector3f(0.0f, 1.0f, 0.0f), (float)direction).conjugate())).normalize());
		for(int i = 0; i < str.length(); i++) {
			int ascii = (int)str.charAt(i);
			float coordX = ((int) ascii % charPerRow) * charSize;
			float coordY = ((int) ascii / charPerRow) * charSize;
			vertices = character.getStaticMesh().getVertices();
			vertices[0].getTextureUV().setVector(coordX+charSize, coordY+charSize);
			vertices[1].getTextureUV().setVector(coordX+charSize, coordY);
			vertices[2].getTextureUV().setVector(coordX, coordY+charSize);
			vertices[3].getTextureUV().setVector(coordX, coordY);
			character.getStaticMesh().addVertices(vertices, indices, false);
			
			character.render(renderingEngine.getFilter(RenderingEngine.FILTER_NONE), renderingEngine, renderingEngine.getCamera());
			character.getTransform().move(character.getTransform().getRotation().getRotateRight(), ((this.widths.get(ascii) / 32.0f) * size) + scale); 
			//TODO sean.computerphile@gmail.com 
		}
		GL11.glEnable(GL_CULL_FACE);
	}
	
	/*
	 * This method takes in a bitmap, the bitmap has to be an exponent of 2.
	 */
	public void setFont(Texture bitmap, int charPerRow) {
		this.charPerRow = charPerRow;
		this.character.getMaterial().setTexture("diffuse", bitmap);
	}
	
	public float getFontHeight(float size) {
		float scale = size / 64f;
		return 48*scale;
	}
	
	public float getWidth(String str, float size) {
		float width = 0;
		for(int i = 0; i < str.length(); i++)
			width += ((this.widths.get((int)str.charAt(i)) / 32.0f) * size) + (size/64f);
		return width;
	}
	
	public int getStringWidth(String str, float size) {
		float scale = size / 64f;
		return (int)(str.length()*48f*scale);
	}
	
	public int charsInWidth(float size, int width) {
		int chars = 0;
		int length = 0;
		int charWidth = (int)(48f*(size / 64f));
		while(length < width) {
			chars++;
			length += charWidth;
		}
		
		return chars;
	}
	
	public String trimToWidth(String str, int width, float size, int cursorPosition) {
		int strWidth = getStringWidth(str, size);
		if(strWidth > width && str.length() > 0) {
			float scale = size / 64f;
			int end = (int)-(((48*(size/64)) - width)/(48*scale));
			return str.substring(cursorPosition, end);
		} else {
			return str;
		}
	}

	public float calcScaleFactor(String str, int width) {
		int strWidth = getStringWidth(str, 64);
		return strWidth/width/2;
	}

	public int getCenteredStart(String string, int width, float size) {
		return (int)((width - getStringWidth(string, size)) / 2f);
	}
}
