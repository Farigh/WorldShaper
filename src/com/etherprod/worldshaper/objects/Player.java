package com.etherprod.worldshaper.objects;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Player extends GameObject
{	
	public Player(float centerX, float centerY,
			TiledTextureRegion mPlayerTextureRegion,
			VertexBufferObjectManager vertexBufferObjectManager)
	{
        super(centerX, centerY, mPlayerTextureRegion, vertexBufferObjectManager);
	}

	@Override
    public void move()
    {
    	for (Sprite Platform : Map.getTilesList()) 
    	{
    	    if (this.collidesWith(Platform)) 
    	    {
    	    }
    	}
    }
 }