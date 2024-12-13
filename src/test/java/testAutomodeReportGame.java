import Data.DataTreating;
import GameEngine.GamesMode;
import Missao.Missao;
import Pessoa.ToCruz;

/**
 * Main para testar se o modo de jogo automatico que devolve um "relatorio" funciona
 * */
public class testAutomodeReportGame {
    public static void main(String[] args) {
        GamesMode gameMode = new GamesMode();
        DataTreating.loadGameData();
        Missao missao = DataTreating.getMissaoByVersion(1);
        missao.setToCruz(new ToCruz("ToCruz", 100, 5));
      //  System.out.println(gameMode.RunAutomaticSimulation(missao));
    }
}
