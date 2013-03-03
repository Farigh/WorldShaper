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

import android.content.res.AssetManager;

import com.etherprod.worldshaper.objects.Map;
import com.etherprod.worldshaper.objects.factories.TileFactory.TileType;
import com.etherprod.worldshaper.util.IntVector2;

public class MapXMLParser extends DefaultHandler
{
	//private String		tmpVal;
	private MapXMLData	tmpData;
	private IntVector2 tmpBound;
	
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

	public IntVector2 createMapFromFile(AssetManager assetManager, String file)
	{
		//get a factory
		SAXParserFactory factory = SAXParserFactory.newInstance();
		
		try
		{
			SAXParser parser = factory.newSAXParser();
			InputStream stream = assetManager.open(file);
			parser.parse(stream, this);

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return tmpBound;
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
			tmpData = new MapXMLData();
			tmpData.type = attributes.getValue("type");
			tmpData.x = Integer.parseInt(attributes.getValue("x"));
			tmpData.y = Integer.parseInt(attributes.getValue("y"));
		}
		
		if (qName.equalsIgnoreCase("level"))
		{
			tmpBound = new IntVector2(Integer.parseInt(attributes.getValue("height")),
					Integer.parseInt(attributes.getValue("width")));
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
			Map.addTile(scene, vertexBufferObjectManager, physicsWorld, tmpData.x, tmpData.y,
					TileType.valueOf(tmpData.type));
		}
	}
}
