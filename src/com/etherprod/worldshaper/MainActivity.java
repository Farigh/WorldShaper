package com.etherprod.worldshaper;

import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.scene.background.Background;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.util.math.MathUtils;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.etherprod.worldshaper.objects.Map;
import com.etherprod.worldshaper.objects.Player;
import com.etherprod.worldshaper.objects.factories.PlayerFactory;
import com.etherprod.worldshaper.ui.ControlsActivity;

public class MainActivity extends ControlsActivity 
{
	private Player 				player;
	private PhysicsWorld 		physicsWorld;
	private ResourcesManager	resourcesManager;

	@Override
	public EngineOptions onCreateEngineOptions()
	{
		// TODO Auto-generated method stub
		return super.onCreateEngineOptions();
	}

	@Override
	public void onCreateResources()
	{
		super.onCreateResources();

		ResourcesManager.prepareManager(this);
	    resourcesManager = ResourcesManager.getInstance();
	    resourcesManager.loadGameResources();
	}

	@Override
	public void onCreateScene()
	{
		super.onCreateScene();
		// blue background
		scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
		
		//adding gravity physics
		physicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);
		scene.registerUpdateHandler(physicsWorld); 

		// create map
		Map.mapCreate(scene, this.getVertexBufferObjectManager(), physicsWorld);

		// add player
		player = PlayerFactory.getNewPlayer(scene, this, physicsWorld);
	}

	@Override
	protected void onLeftControlChange(
			BaseOnScreenControl pBaseOnScreenControl, float pValueX, 
			float pValueY) 
	{
		player.getBody().setLinearVelocity(new Vector2(pValueX * 50, pValueY * 50));		
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
}
