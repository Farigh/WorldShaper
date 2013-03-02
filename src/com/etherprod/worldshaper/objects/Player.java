package com.etherprod.worldshaper.objects;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Player extends GameObject
{	
	private Body body;
	
	public Body getBody() {
		return body;
	}

	public Player(float centerX, float centerY,
			TiledTextureRegion mPlayerTextureRegion,
			VertexBufferObjectManager vertexBufferObjectManager, PhysicsWorld physicsWorld)
	{
        super(centerX, centerY, mPlayerTextureRegion, vertexBufferObjectManager);
        final FixtureDef PLAYER_FIX = PhysicsFactory.createFixtureDef(10.0f, 0.0f, 0.0f);
        body = PhysicsFactory.createCircleBody(physicsWorld, this,
        		BodyType.DynamicBody, PLAYER_FIX);
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false));
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