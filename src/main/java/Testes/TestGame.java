package Testes;

import Data.DataTreating;
import GameEngine.GamesMode;

public class TestGame {
    public static void main(String[] args) {
        GamesMode gameMode = new GamesMode();

        gameMode.run(DataTreating.getMissaoByVersion(1),1);
    }
}
