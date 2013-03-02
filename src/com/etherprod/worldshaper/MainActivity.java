package com.etherprod.worldshaper;

import org.andengine.engine.options.EngineOptions;

import com.etherprod.worldshaper.ui.MyGameActivity;

/**
 * @author GARCIN David <david.garcin.pro@gmail.com>
 *
 * This class is a the main activity of the game
 */
public class MainActivity extends MyGameActivity 
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
		ResourcesManager.prepareManager(this);
	    resourcesManager = ResourcesManager.getInstance();
	    resourcesManager.loadGameResources();
	}

	@Override
	public void onCreateScene()
	{
	}
}
