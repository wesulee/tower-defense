package towerdefense.maps;

import towerdefense.Utility;

public enum MapType
{
	TestMap("test_map.png"), TestMap2("map2.png");
	
	MapType(String name)
	{
		this.name = name;
	}
	
	private final String name;
	
	public String getPath() {return "Maps/"+name;}
	public String getDataPath()
	{
		return "Maps/" + Utility.excludeExt(name) + ".txt";
	}
}
