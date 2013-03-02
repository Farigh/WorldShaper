package com.etherprod.worldshaper.objects;

import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * @author GARCIN David <david.garcin.pro@gmail.com>
 *
 * This class is designed to create the player and
 * handle his actions
 */
public class Player extends GameObject
{	
	private Body body;

	/**
	 * Gets the player's body. This is the element attached to the spite
	 * which handles the physics
	 * 
	 * @return The palyer's body
	 */
	public Body getBody() {
		return body;
	}

	/**
	 * Creates the player object
	 * 
	 * @param centerX The x position of the player
	 * @param centerY The y position of the player
	 * @param mPlayerTextureRegion The player's texture region
	 * @param vertexBufferObjectManager The vertex buffer manager
	 * @param physicsWorld The physics object
	 */
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
    }
 }