package towerdefense.maps;

public enum MapType
{
	TestMap("test_map.png"), TestMap2("map2.png");
	
	MapType(String name)
	{
		this.name = name;
	}
	
	private final String name;
	
	public String getFileName() {return name;}
	public String getPath() {return "Maps/"+name;}
}
