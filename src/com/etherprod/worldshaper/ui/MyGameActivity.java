package com.etherprod.worldshaper.ui;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.ui.activity.BaseGameActivity;

import com.etherprod.worldshaper.ui.scene.MyScene;
import com.etherprod.worldshaper.ui.scene.SceneManager;

import android.util.DisplayMetrics;

/**
 * @author GARCIN David <david.garcin.pro@gmail.com>
 *
 * This class is a abstract one used to easily create game activity
 * which is full-screened, supporting multi_touch and screen orientation.
 * The camera and the scene are already created.
 * It has a limited to 60FPS Engine 
 */
public abstract class MyGameActivity extends BaseGameActivity
{
	protected static int		CAMERA_WIDTH	= 480;
	protected static int		CAMERA_HEIGHT	= 320;
	
	protected Camera			camera;
	private   MyScene			scene;
	
	/**
	 * Replace the default Engine by a 60FPS limited one
	 * This will avoid the game to run too fast in high end devices
	 */
	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) 
	{
	    return new LimitedFPSEngine(pEngineOptions, 60);
	}

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
		
		// multi-touch and music needed
		engineOptions.getTouchOptions().setNeedsMultiTouch(true);
		engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);

		// wake-up phone only on screen at full brightness
		engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);

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
		
		SceneManager.getInstance().createSplashScene();
		scene = SceneManager.getInstance().getCurrentScene();

		this.onCreateScene();

		pOnCreateSceneCallback.onCreateSceneFinished(scene);
	}

	@Override
	public final void onPopulateScene(final Scene pScene, 
				final OnPopulateSceneCallback pOnPopulateSceneCallback)
			throws Exception
	{
		mEngine.registerUpdateHandler(new TimerHandler(2f, new ITimerCallback() 
	    {
	            public void onTimePassed(final TimerHandler pTimerHandler) 
	            {
	                mEngine.unregisterUpdateHandler(pTimerHandler);
	                // TODO: load things ?
	            }
	    }));
	    pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

    //=====================================
    //      Getters/setters functions
    //=====================================
	
	public int getCAMERA_WIDTH()
	{
		return CAMERA_WIDTH;
	}

	public int getCAMERA_HEIGHT()
	{
		return CAMERA_HEIGHT;
	}

	public Camera getCamera()
	{
		return camera;
	}

    //=====================================
    //         Abstract functions
    //=====================================

	protected abstract void onCreateResources();
	protected abstract void onCreateScene();
}
