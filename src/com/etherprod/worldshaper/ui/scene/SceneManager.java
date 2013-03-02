package com.etherprod.worldshaper.ui.scene;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;

import com.etherprod.worldshaper.ResourcesManager;

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
    private MyScene splashScene;
    private MainMenuScene menuScene;
    private MyScene loadingScene;
    private MyScene gameScene;

    private MyScene currentScene;

    private SceneType currentSceneType = SceneType.SCENE_GAME;
    private Engine engine = ResourcesManager.getInstance().getActivity().getEngine();

    //=====================================
    //               Types
    //=====================================

    public enum SceneType
    {
    	SCENE_SPLASH,
    	SCENE_MENU,
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
        setScene(menuScene);
        destroySplashScene();
    }

    public void createGameScene()
    {
        gameScene = new GameScene();
        currentScene = gameScene;
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
        mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() 
        {
            public void onTimePassed(final TimerHandler pTimerHandler) 
            {
                mEngine.unregisterUpdateHandler(pTimerHandler);
                ResourcesManager.getInstance().loadGameResources();
                gameScene = new GameScene();
                setScene(gameScene);
            }
        }));
    }

    public void loadMenuScene(final Engine mEngine)
    {
        setScene(loadingScene);
        gameScene.disposeScene();
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
}
