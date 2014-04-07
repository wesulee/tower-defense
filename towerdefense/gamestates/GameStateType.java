package towerdefense.gamestates;

public enum GameStateType
{
	Default, RunningGame, PausedGame, MapSelector, LoadingScreen;
	
	public static GameStateType getEnum(String str)
	{
		GameStateType gst;
		try {
			gst = GameStateType.valueOf(GameStateType.class, str);
		}
		catch (Exception e) {
			gst = Default;
		}
		return gst;
	}
}
