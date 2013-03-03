package com.etherprod.worldshaper.ui.scene;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.util.color.Color;

import com.etherprod.worldshaper.ui.scene.SceneManager.SceneType;

public class LoadingScene extends MyScene
{
	@Override
	public void createScene()
	{
		Font font = resourcesManager.getFont();
	    setBackground(new Background(Color.WHITE));
	    attachChild(new Text(40, 100, font, "Loading...", activity.getVertexBufferObjectManager()));
	}

	@Override
	public void onBackKeyPressed()
	{
		// Can't leave while loading
	}

	@Override
	public SceneType getSceneType()
	{
		return null;
	}

	@Override
	public void disposeScene()
	{

	}
}
