package com.etherprod.worldshaper;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;

import com.etherprod.worldshaper.ui.scene.GameScene;
import com.etherprod.worldshaper.ui.scene.LoadingScene;
import com.etherprod.worldshaper.ui.scene.MainMenuScene;
import com.etherprod.worldshaper.ui.scene.MyScene;
import com.etherprod.worldshaper.ui.scene.SettingsScene;
import com.etherprod.worldshaper.ui.scene.SplashScene;

/**
 * @author GARCIN David <david.garcin.pro@gmail.com>
 *
 * This class is a singleton pattern based one in charge of creating and
 * managing all scenes used within our game
 */
public class SceneManager
{
	// instance
	private static final SceneManager INSTANCE = new SceneManager();

	// Game's scenes
	private MyScene			splashScene;
	private MainMenuScene	menuScene;
	private SettingsScene	settingsScene;
	private LoadingScene	loadingScene;
	private GameScene		gameScene;

	private MyScene			currentScene;

	private SceneType currentSceneType = SceneType.SCENE_GAME;
	private Engine engine = ResourcesManager.getInstance().getActivity().getEngine();

	//=====================================
	//               Types
	//=====================================

	public enum SceneType
	{
		SCENE_SPLASH,
		SCENE_MENU,
		SCENE_SETTINGS,
		SCENE_GAME,
	}

	/**
	 * Sets the selected scene to display
	 * 
	 * @param scene The scene to display
	 */
	public void setScene(MyScene scene)
	{
		engine.setScene(scene);
		currentScene = scene;
		currentSceneType = scene.getSceneType();
	}

	/**
	 * Sets the selected type scene to display
	 *
	 * @param sceneType The scene type to display
	 */
	public void setScene(SceneType sceneType)
	{
		switch (sceneType)
		{
			case SCENE_GAME:
				setScene(gameScene);
				currentScene = gameScene;
				break;
			case SCENE_MENU:
				setScene(menuScene);
				currentScene = menuScene;
				break;
			default:
				break;
		}
	}

	//=====================================
	//      Scene creation functions
	//=====================================

	public void createSplashScene()
	{
		splashScene = new SplashScene();
		currentScene = splashScene;
	}

	public void createMenuScene()
	{
		MainMenuScene.onCreateRessources();
		menuScene = new MainMenuScene();
		loadingScene = new LoadingScene();
		SettingsScene.onCreateRessources();
		settingsScene = new SettingsScene();

		setScene(menuScene);
		destroySplashScene();
	}

	public void createGameScene()
	{
		ResourcesManager.getInstance().getActivity().runOnUiThread(new Runnable()
		{
			public void run()
			{
				gameScene = new GameScene();
				gameScene.loadResouces();
			}
		});
	}

	//=====================================
	//     Scene destruction functions
	//=====================================

	private void destroySplashScene()
	{
		splashScene.disposeScene();
		splashScene = null;
	}

	//=====================================
	//       Scene loading functions
	//=====================================

	public void loadGameScene(final Engine mEngine)
	{
		setScene(loadingScene);
		menuScene.unloadTextures();

		ResourcesManager.getInstance().loadGameResources();
		createGameScene();
	}

	public void loadMenuScene(final Engine mEngine)
	{
		setScene(loadingScene);

		if (ResourcesManager.getInstance().getActivity().isMusicActive())
			ResourcesManager.getInstance().getMainMusic().pause();

		gameScene.disposeScene();
		gameScene = null;
		ResourcesManager.getInstance().unloadGameResources();
		mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() 
		{
			public void onTimePassed(final TimerHandler pTimerHandler) 
			{
				mEngine.unregisterUpdateHandler(pTimerHandler);
				menuScene.loadTextures();
				setScene(menuScene);
			}
		}));
	}

	public void loadSettingsScene(final Engine mEngine)
	{
		mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() 
		{
			public void onTimePassed(final TimerHandler pTimerHandler) 
			{
				mEngine.unregisterUpdateHandler(pTimerHandler);
				settingsScene.loadTextures();
				setScene(settingsScene);
			}
		}));
	}

	//=====================================
	//      Getters/setters functions
	//=====================================

	public static SceneManager getInstance()
	{
		return INSTANCE;
	}

	public SceneType getCurrentSceneType()
	{
		return currentSceneType;
	}

	public MyScene getCurrentScene()
	{
		return currentScene;
	}

	public void setLife(int life, int maxlife)
	{
		gameScene.setLife((life < 0) ? 0 : life, maxlife);
	}
	
	public void setProgress(int progress, String message)
	{
		loadingScene.setProgress(progress, message);
	}
}
