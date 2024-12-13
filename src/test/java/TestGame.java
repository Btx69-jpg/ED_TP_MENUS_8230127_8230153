import Data.DataTreating;
import GameEngine.GamesMode;

/**
 * Main para testar o funcionamento do modo de jogo manual
 * */
public class TestGame {
    public static void main(String[] args) {
        GamesMode gameMode = new GamesMode();
        DataTreating.loadGameData();

        gameMode.run(DataTreating.getMissaoByVersion(1), 2);
    }
}
