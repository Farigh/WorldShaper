package com.etherprod.worldshaper.util.map;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.etherprod.worldshaper.MainActivity;
import com.etherprod.worldshaper.objects.Map;
import com.etherprod.worldshaper.objects.factories.TileFactory.TileType;
import com.etherprod.worldshaper.util.data.DataManager;
import com.etherprod.worldshaper.util.data.EntityData;
import com.etherprod.worldshaper.util.data.MapData;
import com.etherprod.worldshaper.util.data.EntityData.EntityType;

public class MapXMLParser extends DefaultHandler
{
	//private String	tmpVal;
	private MapData		mapData;
	private MapEntity	tmpEntity;

	private Scene scene;
	private VertexBufferObjectManager vertexBufferObjectManager;
	private PhysicsWorld physicsWorld;

	public MapXMLParser(Scene scene, VertexBufferObjectManager vertexBufferObjectManager,
			PhysicsWorld physicsWorld)
	{
		super();
		this.scene = scene;
		this.vertexBufferObjectManager = vertexBufferObjectManager;
		this.physicsWorld = physicsWorld;
	}

	public MapData createMapFromFile(MainActivity activity, String file)
	{
		mapData = new MapData();
		//get a factory
		SAXParserFactory factory = SAXParserFactory.newInstance();

		try
		{
			SAXParser parser = factory.newSAXParser();
			InputStream stream = activity.getAssets().open(file);
			parser.parse(stream, this);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		DataManager.saveMap(activity, mapData);

		return mapData;
	}

	//Event Handlers
	public void startElement(String uri, String localName, String qName,
		Attributes attributes) throws SAXException 
	{
		//reset
		//tmpVal = "";
		if (qName.equalsIgnoreCase("entity"))
		{
			//create a new instance of employee
			tmpEntity = new MapEntity(Integer.parseInt(attributes.getValue("x")),
					Integer.parseInt(attributes.getValue("y")),
					attributes.getValue("type"));
		}

		if (qName.equalsIgnoreCase("level"))
		{
			mapData.setMapSize(Integer.parseInt(attributes.getValue("height")),
					Integer.parseInt(attributes.getValue("width")));
		}

		if (qName.equalsIgnoreCase("spawn"))
		{
			mapData.setMapSpawn(Integer.parseInt(attributes.getValue("x")),
					Integer.parseInt(attributes.getValue("y")));
		}
	}

	public void characters(char[] ch, int start, int length) throws SAXException
	{
		//tmpVal = new String(ch, start, length);
	}

	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		if(qName.equalsIgnoreCase("entity"))
		{
			Map.addTile(scene, vertexBufferObjectManager, physicsWorld, tmpEntity.getX(),
					tmpEntity.getY(), TileType.valueOf(tmpEntity.getType()));
			EntityData entity = mapData.addEntity(EntityType.TILE,
					tmpEntity.getX(), tmpEntity.getY());
			entity.setTileType(tmpEntity.getType());
		}
	}
}
