import Data.DataTreating;
import GameEngine.GamesMode;

/**
 * Main para testar se o modo de jogo automatico funciona
 * */
public class TestAutoGame {
    public static void main(String[] args) {
        GamesMode gameMode = new GamesMode();
        DataTreating.loadGameData();

        gameMode.run(DataTreating.getMissaoByVersion(1), 2);
    }
}
