package com.etherprod.worldshaper.ui.scene;

import org.andengine.engine.Engine;

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
    private MyScene gameScene;

    private MyScene currentScene;

    private SceneType currentSceneType = SceneType.SCENE_GAME;
    private Engine engine = ResourcesManager.getInstance().getActivity().getEngine();

    //=====================================
    //               Types
    //=====================================

    public enum SceneType
    {
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
    
    public void createGameScene()
    {
        gameScene = new GameScene();
        currentScene = gameScene;
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