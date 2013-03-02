package com.etherprod.worldshaper;

import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.options.EngineOptions;
import org.andengine.util.math.MathUtils;

import com.badlogic.gdx.math.Vector2;
import com.etherprod.worldshaper.objects.Player;
import com.etherprod.worldshaper.ui.ControlsActivity;
import com.etherprod.worldshaper.ui.scene.GameScene;

/**
 * @author GARCIN David <david.garcin.pro@gmail.com>
 *
 * This class is a the main activity of the game
 */
public class MainActivity extends ControlsActivity 
{
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
	}

	@Override
	protected void onLeftControlChange(
			BaseOnScreenControl pBaseOnScreenControl, float pValueX, 
			float pValueY) 
	{
		Player player = GameScene.getPlayer();
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
}
