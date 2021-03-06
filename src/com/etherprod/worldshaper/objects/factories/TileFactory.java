package com.etherprod.worldshaper.objects.factories;

import java.util.ArrayList;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.etherprod.worldshaper.MainActivity;
import com.etherprod.worldshaper.ResourcesManager;
import com.etherprod.worldshaper.objects.Map;
import com.etherprod.worldshaper.ui.SelfDetachableSpriteWithBoby;

/**
 * @author GARCIN David <david.garcin.pro@gmail.com>
 *
 * This class is a factory pattern based one in charge of creating
 * the maps tiles and its related resources
 */
public class TileFactory
{
	// Game Texture
	public static BuildableBitmapTextureAtlas	gameTextureAtlas;
	private static int last_tile_index = 0;

	// Game Texture Regions
	public static ArrayList<ITextureRegion>	textureRegionList = new ArrayList<ITextureRegion>();

	public static final FixtureDef TILE_FIX = PhysicsFactory.createFixtureDef(0.0f, 0.01f, 0.5f);;
	
	public enum TileType
	{
		DIRT("dirt.png"),
		COPPER("copper.png"),
		ROCK("rock.png"),
		COIN("coin.png"),
		EMPTY(""),
		BOUNDS("");

		private final int value;
		private final String textureFile;

		private TileType(String textureFile)
		{			
			this.value = TileFactory.last_tile_index;
			this.textureFile = textureFile;
			TileFactory.last_tile_index++;
		}

		public int toInt()
		{
			return this.value;
		}

		public String getTextureFile()
		{
			return textureFile;
		}
	}
	
	/**
	 * This function MUST be used at the beginning of game loading.
	 * It will be called by the @ResourcesManager.
	 * 
	 * @param mainActivity The main activity of the game
	 */
	public static void createResources(MainActivity activity)
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/tiles/");
		gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024,
				1024, TextureOptions.BILINEAR);

		for (TileType tileType : TileType.values())
		{
			if (tileType.getTextureFile() != "")
				textureRegionList.add(BitmapTextureAtlasTextureRegionFactory
						.createFromAsset(gameTextureAtlas, activity, tileType.getTextureFile()));
		}

		try 
		{
			gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource,
					BitmapTextureAtlas>(0, 1, 0));
			gameTextureAtlas.load();
		}
		catch (final TextureAtlasBuilderException e)
		{
			Debug.e(e);
		}
	}
	
	/**
	 * Creates a new tile
	 * 
	 * @param scene The game's scene
	 * @param mainActivity The main activity
	 * @param physicsWorld The physics handler
	 * @param x The x position of the tile
	 * @param y The y position of the tile
	 * 
	 * @return Sprite created
	 */
	public static Sprite addNewTile(Scene scene, VertexBufferObjectManager vertexBufferObjectManager,
			PhysicsWorld physicsWorld, int x, int y, TileType tileType)
	{
		int posX = (int) (x / Map.TILE_SIZE);
		int posY = (int) (y / Map.TILE_SIZE);
		Map.mapData.getMap()[posX][posY].created = true;
		
		SelfDetachableSpriteWithBoby tile = new SelfDetachableSpriteWithBoby(x, y, textureRegionList.get(tileType.toInt()), 
				vertexBufferObjectManager)
		{
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) 
			{
				super.onManagedUpdate(pSecondsElapsed);
				Camera camera = ResourcesManager.getInstance().getActivity().getCamera();

				// remove self when out of the screen
				int posX = (int) (this.getX() / Map.TILE_SIZE);
				int posY = (int) (this.getY() / Map.TILE_SIZE);
				int camHalfHeight = (int) ((camera.getHeight() / 2) / Map.TILE_SIZE);
				int camHalfWidth = (int) ((camera.getWidth() / 2) / Map.TILE_SIZE);
				int camX = (int) (camera.getCenterX() / Map.TILE_SIZE);
				int camY = (int) (camera.getCenterY() / Map.TILE_SIZE);

				if ((posX < (camX - camHalfWidth - 2))
					|| (posX > (camX + camHalfWidth + 2))
					|| (posY < (camY - camHalfHeight - 1))
					|| (posY > (camY + camHalfHeight + 2))
					)
				{
					this.safeDetachSelfAndChildren(ResourcesManager.getInstance().getActivity());
					Map.mapData.getMap()[posX][posY].created = false;
				}
			}
		};

		// set physics to not affect them
		tile.setBody(physicsWorld, BodyType.StaticBody, TILE_FIX);

		scene.attachChild(tile);

		return tile;
	}
}
