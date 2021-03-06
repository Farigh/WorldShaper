package com.etherprod.worldshaper.ui;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.ui.activity.BaseGameActivity;

import com.etherprod.worldshaper.SceneManager;
import com.etherprod.worldshaper.ui.scene.MyScene;

import android.util.DisplayMetrics;
import android.view.KeyEvent;

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
	
	protected BoundCamera		camera;
	private   MyScene			scene;

	private boolean music_active_is		= true;
	private boolean sounds_active_are	= true;
	
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
		this.camera = new BoundCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		// Sets the engine options to landscape
		final EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_SENSOR, new RatioResolutionPolicy(
						CAMERA_WIDTH, CAMERA_HEIGHT), this.camera);
		
		// multi-touch and music needed
		engineOptions.getTouchOptions().setNeedsMultiTouch(true);
		engineOptions.getAudioOptions().setNeedsMusic(true);
		engineOptions.getAudioOptions().setNeedsSound(true);

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
				SceneManager.getInstance().createMenuScene();
			}
		}));
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	/**
	 * Overriding destroy method to make sure the game is fully destroyed when
	 * leaving (some devices keep the activity alive)
	 */
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		System.exit(0);
	}
	
	/**
	 * Overriding key down method to do what we want when Back key is pressed
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			// as the back action depends on the scene, the current scene's
			// method is called
			SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
		}
		return false; 
	}

	public void computeMusic()
	{
		music_active_is = !music_active_is;
	}

	public void computeSounds()
	{
		sounds_active_are = !sounds_active_are;
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

	public BoundCamera getCamera()
	{
		return camera;
	}

	public boolean isMusicActive()
	{
		return music_active_is;
	}

	public boolean isSoundsActive()
	{
		return sounds_active_are;
	}	

	//=====================================
	//         Abstract functions
	//=====================================

	protected abstract void onCreateResources();
	protected abstract void onCreateScene();
}
