package com.etherprod.worldshaper.objects;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Tile extends GameObject
{
	public Tile(int pX, int pY, TiledTextureRegion pTextureRegion,
			    VertexBufferObjectManager pVertexBufferObjectManager)
	{
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
	}

	@Override
	public void move() {
		// TODO Auto-generated method stub

	}
}
