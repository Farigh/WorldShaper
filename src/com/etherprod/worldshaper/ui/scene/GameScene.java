package com.etherprod.worldshaper.ui.scene;

import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.entity.scene.background.Background;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.util.math.MathUtils;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.etherprod.worldshaper.SceneManager;
import com.etherprod.worldshaper.SceneManager.SceneType;
import com.etherprod.worldshaper.objects.Map;
import com.etherprod.worldshaper.objects.Player;

/**
 * @author GARCIN David <david.garcin.pro@gmail.com>
 *
 * This is the game scene
 */
public class GameScene extends HUDScene
{
	private static Player 	player;
	private PhysicsWorld	physicsWorld;
	
    @Override
    public void createScene()
    {
    	super.createScene();
    	
		// blue background
		this.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
		
		//adding gravity physics
		physicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);
		this.registerUpdateHandler(physicsWorld); 

		// create map
		player = Map.mapCreate(this, activity, physicsWorld);
    }

    @Override
    public void onBackKeyPressed()
    {
    	// go back to main menu
    	SceneManager.getInstance().loadMenuScene(activity.getEngine());
    }

    @Override
    public SceneType getSceneType()
    {
        return SceneType.SCENE_GAME;
    }

    @Override
    public void disposeScene()
    {
    	super.disposeScene();
        
        // don't follow player anymore
    	activity.getCamera().setChaseEntity(null);

    	// no more bounds
		activity.getCamera().setBoundsEnabled(false);

    	// reset camera position
        activity.getCamera().setCenter(activity.getCAMERA_WIDTH() / 2,
        		activity.getCAMERA_HEIGHT() / 2);
    }

	@Override
	protected void onLeftControlChange(
			BaseOnScreenControl pBaseOnScreenControl, float pValueX, 
			float pValueY) 
	{
		Player player = GameScene.getPlayer();
		
		if (pValueX != 0 && pValueY != 0)
			player.walk();
		else
			player.stop();
		
		player.getBody().setLinearVelocity(5 * pValueX, player.getBody().getLinearVelocity().y);
	}

	@Override
	protected void onLeftControlClick(
			AnalogOnScreenControl pAnalogOnScreenControl)
	{
		/* Nothing. */
	}

	@Override
	protected void onRightControlChange(
			BaseOnScreenControl pBaseOnScreenControl, float pValueX, 
			float pValueY) 
	{
		Player player = GameScene.getPlayer();
		if (pValueX == 0 && pValueY == 0)
		{
			player.setRotation(0);
		}
		else
		{
			player.setRotation(MathUtils.radToDeg((float) 
					Math.atan2(pValueX, -pValueY)));
		}
	}

	@Override
	protected void onRightControlClick(
			AnalogOnScreenControl pAnalogOnScreenControl)
	{
		/* Nothing. */
	}
    
    public static Player getPlayer()
    {
    	return player;
    }
}
