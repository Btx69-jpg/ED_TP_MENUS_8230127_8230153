package Testes;

import Data.DataTreating;
import GameEngine.GamesMode;
import Missao.Missao;
import Pessoa.ToCruz;

public class TestAutoGame {
    public static void main(String[] args) {
        GamesMode gameMode = new GamesMode();
        DataTreating.loadGameData();
        Missao missao = DataTreating.getMissaoByVersion(1);
        missao.setToCruz(new ToCruz("ToCruz", 100, 5));
        gameMode.run(missao,2);
    }
}
