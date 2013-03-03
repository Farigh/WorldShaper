package com.etherprod.worldshaper.ui.scene;

import org.andengine.entity.scene.Scene;

import com.etherprod.worldshaper.MainActivity;
import com.etherprod.worldshaper.ResourcesManager;
import com.etherprod.worldshaper.SceneManager.SceneType;

/**
 * @author GARCIN David <david.garcin.pro@gmail.com>
 *
 * This class is a abstract class extending scene.
 */
public abstract class MyScene extends Scene
{
	protected MainActivity		activity;
	protected ResourcesManager	resourcesManager;

	/**
	 * Creates the scene
	 */
    public MyScene()
    {
        this.resourcesManager = ResourcesManager.getInstance();
        this.activity = resourcesManager.getActivity();
        createScene();
    }

    //=====================================
    //         Abstract functions
    //=====================================

    public abstract void createScene();
    public abstract void onBackKeyPressed();
    public abstract SceneType getSceneType();
    public abstract void disposeScene();
}
