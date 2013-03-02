package com.etherprod.worldshaper.ui.scene;

import org.andengine.entity.scene.background.Background;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.etherprod.worldshaper.MainActivity;
import com.etherprod.worldshaper.ResourcesManager;
import com.etherprod.worldshaper.objects.Map;
import com.etherprod.worldshaper.objects.Player;
import com.etherprod.worldshaper.objects.factories.PlayerFactory;
import com.etherprod.worldshaper.ui.scene.SceneManager.SceneType;

/**
 * @author GARCIN David <david.garcin.pro@gmail.com>
 *
 * This is the game scene
 */
public class GameScene extends MyScene
{
	private MainActivity	activity;

	private static Player 			player;
	private PhysicsWorld	physicsWorld;
	
    @Override
    public void createScene()
    {
    	activity = ResourcesManager.getInstance().getActivity();
    	
		// blue background
		this.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
		
		//adding gravity physics
		physicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);
		this.registerUpdateHandler(physicsWorld); 

		// create map
		Map.mapCreate(this, activity.getVertexBufferObjectManager(), physicsWorld);

		// add player
		player = PlayerFactory.getNewPlayer(this, activity, physicsWorld);
    }

    @Override
    public void onBackKeyPressed()
    {

    }

    @Override
    public SceneType getSceneType()
    {
        return SceneType.SCENE_GAME;
    }

    @Override
    public void disposeScene()
    {
    }
    
    public static Player getPlayer()
    {
    	return player;
    }
}
