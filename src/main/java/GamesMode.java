import Edificio.Edificio;
import GameEngine.GameMode;
import Pessoa.ToCruz;
import Missao.Missao;

public class GamesMode implements GameMode {
    Missao missao;
    private ToCruz toCruz;

    @Override
    public void automatic() {
        //todas as decisões são tomadas automaticamente
        //iterator the shortestpath
    }

    @Override
    public void manual() {
        while (toCruz.getVida() > 0 && toCruz.getEnergia() > 0) {
            //aparecer um menu para deixar o utilizador escolher o que fazer
        }
        //aparecer um menu para deixar o utilizador escolher o que fazer
    }

    @Override
    public void run() {

    }
}
