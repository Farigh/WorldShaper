package com.etherprod.worldshaper;

import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.scene.Scene;
import org.andengine.util.math.MathUtils;

import com.etherprod.worldshaper.objects.Map;
import com.etherprod.worldshaper.objects.Player;
import com.etherprod.worldshaper.objects.factories.PlayerFactory;
import com.etherprod.worldshaper.objects.factories.TileFactory;
import com.etherprod.worldshaper.ui.ControlsActivity;

public class MainActivity extends ControlsActivity 
{
	private Player player;
	
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
		TileFactory.createResources(this);
		PlayerFactory.createResources(this);
	}

	@Override
	public Scene onCreateScene()
	{
		Scene scene = super.onCreateScene();

		Map.mapCreate(scene, this.getVertexBufferObjectManager());
		player = PlayerFactory.getNewPlayer(scene, this);

		return scene;
	}

	@Override
	protected void onLeftControlChange(
			BaseOnScreenControl pBaseOnScreenControl, float pValueX, 
			float pValueY) 
	{
		player.mPhysicsHandler.setVelocity(pValueX * 100, pValueY * 100);		
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
