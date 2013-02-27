package com.etherprod.worldshaper.objects;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Player extends GameObject
{
 
    // ===========================================================
    // Constants
    // ===========================================================
 
    // ===========================================================
    // Fields
    // ===========================================================
 
    // ===========================================================
    // Constructors
    // ===========================================================
	
	public Player(float centerX, float centerY,
			TiledTextureRegion mPlayerTextureRegion,
			VertexBufferObjectManager vertexBufferObjectManager)
	{
        super(centerX, centerY, mPlayerTextureRegion, vertexBufferObjectManager);
	}
 
    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

	@Override
    public void move()
    {
    	/*for (Tile Platform : Map.getTilesList()) 
    	{
    	    if (this.collidesWith(Platform)) 
    	    {
    	        Log.v("objects.Player", "just collided with platform :p");
    	    }
    	}*/
    }

    // ===========================================================
    // Methods
    // ===========================================================
 }