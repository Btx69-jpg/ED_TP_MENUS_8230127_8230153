import Data.DataTreating;
import GameEngine.GamesMode;
import Missao.Missao;

/**
 * Main para testar o funcionamento do modo de jogo manual
 * */
public class TestGame {
    public static void main(String[] args) {
        GamesMode gameMode = new GamesMode();
        DataTreating.loadGameData();
        Missao missao = DataTreating.getMissaoByVersion(1);

        gameMode.run(missao, 2);
    }
}
