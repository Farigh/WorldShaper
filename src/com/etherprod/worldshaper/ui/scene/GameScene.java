package com.etherprod.worldshaper.ui.scene;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.entity.scene.background.Background;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.util.GLState;
import org.andengine.util.math.MathUtils;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.etherprod.worldshaper.ResourcesManager;
import com.etherprod.worldshaper.SceneManager;
import com.etherprod.worldshaper.SceneManager.SceneType;
import com.etherprod.worldshaper.objects.Map;
import com.etherprod.worldshaper.objects.Player;
import com.etherprod.worldshaper.objects.factories.TileFactory.TileType;
import com.etherprod.worldshaper.util.data.EntityData;
import com.etherprod.worldshaper.util.data.EntityData.EntityType;
import com.etherprod.worldshaper.util.loader.AsyncTaskRunner;
import com.etherprod.worldshaper.util.loader.IAsyncTask;

/**
 * @author GARCIN David <david.garcin.pro@gmail.com>
 * @brief
 * This is the game scene
 */
public class GameScene extends HUDScene
{
	private Player		 	player;
	private PhysicsWorld	physicsWorld;
	private int				lastX;
	private int				lastY;

	@Override
	public void createScene()
	{
		super.createScene();

		// blue background
		this.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		//adding gravity physics
		physicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);
		this.registerUpdateHandler(physicsWorld);
	}

	@Override
	public void onBackKeyPressed()
	{
		// go back to main menu
		SceneManager.getInstance().loadMenuScene(activity.getEngine());
	}

	@Override
	public SceneType getSceneType()
	{
		return SceneType.SCENE_GAME;
	}

	@Override
	public void disposeScene()
	{
		super.disposeScene();

		// don't follow player anymore
		activity.getCamera().setChaseEntity(null);

		// no more bounds
		activity.getCamera().setBoundsEnabled(false);

		// reset camera position
		activity.getCamera().setCenter(activity.getCAMERA_WIDTH() / 2,
				activity.getCAMERA_HEIGHT() / 2);
	}

	@Override
	protected void onLeftControlChange(
			BaseOnScreenControl pBaseOnScreenControl, float pValueX, 
			float pValueY) 
	{
		Player player = GameScene.this.player;

		if (player == null)
			return;

		if (pValueX != 0 && pValueY != 0)
			player.walk();
		else
			player.stop();

		player.getBody().setLinearVelocity(5 * pValueX, player.getBody().getLinearVelocity().y);
	}

	@Override
	protected void onLeftControlClick(
			AnalogOnScreenControl pAnalogOnScreenControl)
	{
		/* Nothing. */
	}

	@Override
	protected void onRightControlChange(
			BaseOnScreenControl pBaseOnScreenControl, float pValueX, 
			float pValueY) 
	{
		Player player = GameScene.this.player;

		if (player == null)
			return;

		if (pValueX == 0 && pValueY == 0)
		{
			player.setRotation(0);
		}
		else
		{
			player.setRotation(MathUtils.radToDeg((float) 
					Math.atan2(pValueX, -pValueY)));
		}
	}

	@Override
	protected void onJumpButtonClick()
	{
		player.jump();
	}

	@Override
	protected void onRightControlClick(
			AnalogOnScreenControl pAnalogOnScreenControl)
	{
		/* Nothing. */
	}

	public Player getPlayer()
	{
		return player;
	}

	@Override
	public void loadResouces()
	{
		super.loadResouces();
		
		IAsyncTask callback = new IAsyncTask(this)
		{
			@Override
			public void execute()
			{
				// create map
				player = Map.mapCreate(scene, activity, physicsWorld);
				lastX = (int) (player.getX() / Map.TILE_SIZE);
				lastY = (int) (player.getY() / Map.TILE_SIZE);
			}

			@Override
			public void onTaskCompleted()
			{
				SceneManager.getInstance().setScene(SceneManager.SceneType.SCENE_GAME);
				activity.getCamera().setHUD(gameHUD);

				// play music if option is activated
				if (activity.isMusicActive())
					ResourcesManager.getInstance().getMainMusic().play();
			}
		};

		new AsyncTaskRunner().execute(callback);
	}
	
	@Override
	protected void onManagedDraw(final GLState pGLState, final Camera pCamera)
	{
		// update onscreen elements
		EntityData[][] map = Map.mapData.getMap();
		int newX = (int) (player.getX() / Map.TILE_SIZE);
		int newY = (int) (player.getY() / Map.TILE_SIZE);

		if (newX != lastX)
		{
			// add new elements horizontally
			int populateX = 0;
			if (newX > lastX)
				populateX = newX + ((activity.getCAMERA_WIDTH() / Map.TILE_SIZE) / 2) + 2;
			else
				populateX = newX - ((activity.getCAMERA_WIDTH() / Map.TILE_SIZE) / 2) - 1;

			int halfHeight = ((activity.getCAMERA_HEIGHT() / Map.TILE_SIZE) / 2);
			int newMaxY = newY + halfHeight + 3;
			for (int j = newY - halfHeight - 2; j < newMaxY; j++)
			{
				EntityData data = map[populateX][j];
				if ((data != null) && (data.getType() == EntityType.TILE))
				{
					Map.addTile(this, activity.getVertexBufferObjectManager(),
							physicsWorld, populateX * Map.TILE_SIZE, j * Map.TILE_SIZE,
							TileType.valueOf(data.getTileType()));
				}
			}
			
			lastX = newX;
		}

		if (newY != lastY)
		{
			// add new elements horizontally
			int populateY = 0;
			if (newY > lastY)
				populateY = newY + ((activity.getCAMERA_HEIGHT() / Map.TILE_SIZE) / 2) + 2;
			else
				populateY = newY - ((activity.getCAMERA_HEIGHT() / Map.TILE_SIZE) / 2) - 1;	

			int halfWidth = ((activity.getCAMERA_WIDTH() / Map.TILE_SIZE) / 2);
			int newMaxX = newX + halfWidth + 3;
			for (int i = newX - halfWidth - 2; i < newMaxX; i++)
			{
				EntityData data = map[i][populateY];
				if ((data != null) && (data.getType() == EntityType.TILE))
				{
					Map.addTile(this, activity.getVertexBufferObjectManager(),
							physicsWorld, i * Map.TILE_SIZE, populateY * Map.TILE_SIZE,
							TileType.valueOf(data.getTileType()));
				}
			}
			
			lastY = newY;
		}

		super.onManagedDraw(pGLState, pCamera);
	}
}
