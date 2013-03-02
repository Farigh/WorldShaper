package com.etherprod.worldshaper.objects;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * @author GARCIN David <david.garcin.pro@gmail.com>
 *
 * This class is a abstract one used to easily create game objects
 * with a basic physics handler without collision support
 */
public abstract class GameObject extends AnimatedSprite 
{
	public PhysicsHandler mPhysicsHandler;
	
    /**
     * This is the game object constructor
     * 
     * @param pX The x position of the object
     * @param pY The y position of the object
     * @param pTiledTextureRegion The texture region
     * @param pVertexBufferObjectManager The vertex buffer manager
     */
    public GameObject(final float pX, final float pY, final TiledTextureRegion pTiledTextureRegion, 
    		          final VertexBufferObjectManager pVertexBufferObjectManager)
    {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
        this.mPhysicsHandler = new PhysicsHandler(this);
        this.registerUpdateHandler(this.mPhysicsHandler);
    }
 
    @Override
    protected void onManagedUpdate(float pSecondsElapsed)
    {
        move();
 
        super.onManagedUpdate(pSecondsElapsed);
    }

    //=====================================
    //         Abstract functions
    //=====================================
	
    public abstract void move();
}