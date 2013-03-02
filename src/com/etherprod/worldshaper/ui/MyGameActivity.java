package com.etherprod.worldshaper.ui;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.ui.activity.BaseGameActivity;

import android.util.DisplayMetrics;

public abstract class MyGameActivity extends BaseGameActivity
{
	protected static int	CAMERA_WIDTH	= 480;
	protected static int	CAMERA_HEIGHT	= 320;
	
	protected Camera		camera;
	protected Scene 		scene;

	protected abstract void onCreateResources();
	protected abstract void onCreateScene();

	@Override
	public EngineOptions onCreateEngineOptions() 
	{
		// getting current screen dimensions
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		CAMERA_HEIGHT = metrics.heightPixels;
		CAMERA_WIDTH = metrics.widthPixels;

		// Sets the camera
		this.camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		// Sets the engine options to landscape
		final EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_SENSOR, new RatioResolutionPolicy(
						CAMERA_WIDTH, CAMERA_HEIGHT), this.camera);
		
		// multi-touch needed
		engineOptions.getTouchOptions().setNeedsMultiTouch(true);

		return engineOptions;
	}
	
	@Override
	public final void onCreateResources(final OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception
	{
		this.onCreateResources();

		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public final void onCreateScene(final OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception
	{
		//TODO: remove the FPDLogger
		this.mEngine.registerUpdateHandler(new FPSLogger());

		scene = new Scene();
		this.onCreateScene();

		pOnCreateSceneCallback.onCreateSceneFinished(scene);
	}

	@Override
	public final void onPopulateScene(final Scene pScene, 
				final OnPopulateSceneCallback pOnPopulateSceneCallback)
			throws Exception
	{
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
	
	public int getCAMERA_WIDTH()
	{
		return CAMERA_WIDTH;
	}

	public int getCAMERA_HEIGHT()
	{
		return CAMERA_HEIGHT;
	}
}
