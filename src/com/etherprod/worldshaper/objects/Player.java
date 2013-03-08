package com.etherprod.worldshaper.objects;

import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.etherprod.worldshaper.MainActivity;

/**
 * @author GARCIN David <david.garcin.pro@gmail.com>
 *
 * This class is designed to create the player and
 * handle his actions
 */
public class Player extends GameObject
{	
	private Body body;
	private boolean walking = false;
	private MainActivity activity;

	/**
	 * Gets the player's body. This is the element attached to the spite
	 * which handles the physics
	 * 
	 * @return The palyer's body
	 */
	public Body getBody() 
	{
		return body;
	}

	/**
	 * Creates the player object
	 * 
	 * @param centerX The x position of the player
	 * @param centerY The y position of the player
	 * @param mPlayerTextureRegion The player's texture region
	 * @param activity.getVertexBufferObjectManager() The vertex buffer manager
	 * @param physicsWorld The physics object
	 */
	public Player(float centerX, float centerY,
			TiledTextureRegion mPlayerTextureRegion,
			final MainActivity activity, PhysicsWorld physicsWorld)
	{
        super(centerX, centerY, mPlayerTextureRegion, activity.getVertexBufferObjectManager());
		this.activity = activity;
        final FixtureDef PLAYER_FIX = PhysicsFactory.createFixtureDef(10.0f, 0.0f, 0.0f);
        body = PhysicsFactory.createCircleBody(physicsWorld, this,
        		BodyType.DynamicBody, PLAYER_FIX);

        physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false));

        // camera will follow the player
        activity.getCamera().setChaseEntity(this);
	}

	@Override
    public void move()
    {
        activity.getCamera().onUpdate(0.1f);

        if (getY() <= 0)
        {                    
            onDie();
        }
    }

	public void walk()
	{
	    final long[] PLAYER_ANIMATE = new long[] { 100, 100, 100 };

	    if (!walking)
	    {
	    	animate(PLAYER_ANIMATE, 0, 2, true);
	    	walking = true;
	    }
	}

	public void stop()
	{
		if (walking)
		{
			stopAnimation();
			walking = false;
		}
	}

	public void jump()
	{
	    body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, -8)); 
	}

	public void onDie()
	{
		
	}
 }