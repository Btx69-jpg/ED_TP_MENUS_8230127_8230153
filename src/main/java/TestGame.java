import Data.DataTreating;
import GameEngine.GamesMode;

public class TestGame {
    public static void main(String[] args) {
        GamesMode gameMode = new GamesMode();
        DataTreating.loadGameData();

        gameMode.run(DataTreating.getMissaoByVersion(1), 2);
    }
}