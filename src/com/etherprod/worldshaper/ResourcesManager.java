package com.etherprod.worldshaper;

import com.etherprod.worldshaper.objects.factories.PlayerFactory;
import com.etherprod.worldshaper.objects.factories.TileFactory;

/**
 * @author GARCIN David <david.garcin.pro@gmail.com>
 *
 * This class is a singleton pattern based one in charge of creating and
 * managing all resources used within our game
 */
public class ResourcesManager
{
	private static final ResourcesManager INSTANCE = new ResourcesManager();

    private MainActivity 	activity;

    /**
     * This function MUST be used at the beginning of game loading.
     * It will prepare Resources Manager properly, setting all needed parameters, 
     * so we can latter access them from different classes
     * 
     * @param engine The game engine
     * @param activity The main activity
     * @param camera The scene camera
     * @param vbom The vertex buffer object manager
     */
    public static void prepareManager(MainActivity activity)
    {
        getInstance().activity = activity;
    }
    
    /**
     * This function return the ResourcesManager instance
     * 
     * @return The ResourcesManager instance
     */
    public static ResourcesManager getInstance()
    {
        return INSTANCE;
    }
    
    /**
     * This function is in charge of loading game resources
     */
    public void loadGameResources()
    {
        loadGameGraphics();
    }
    
    /**
     * This function is in charge of unloading game resources
     */
    public void unloadGameResources()
    {
    	//TODO: unload game's texture
    }

    //=====================================
    //      Getters/setters functions
    //=====================================

	public MainActivity getActivity() 
    {
		return activity;
	}

    //=====================================
    //         Private functions
    //=====================================
    
    private void loadGameGraphics()
    {
		TileFactory.createResources(activity);
		PlayerFactory.createResources(activity);
    }
}
