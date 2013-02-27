package com.etherprod.worldshaper.objects;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.util.Log;

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
 
    public Player(final float pX, final float pY, final TiledTextureRegion pTiledTextureRegion, 
    			  final VertexBufferObjectManager pVertexBufferObjectManager)
    {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }
 
    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
 
    @Override
    public void move()
    {
    	for (Tile Platform : Map.getTilesList()) 
    	{
    	    if (this.collidesWith(Platform)) 
    	    {
    	        Log.v("objects.Player", "just collided with platform :p");
    	    }
    	}
    }

	public void left() 
	{
		// TODO Auto-generated method stub
		
	}
 
    // ===========================================================
    // Methods
    // ===========================================================
}