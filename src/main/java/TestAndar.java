import Data.Json;
import GameEngine.Cenarios;
import Missao.Missao;
import Pessoa.ToCruz;

public class TestAndar {
    public static void main(String[] args) {
        Missao missao;

        missao = Json.ReadMissao("C:\\Faculdade\\2ano\\PrimeiroSemestre\\ED\\dadosJogo.json");

        System.out.println("Antes de se moverem: " + missao.getEdificio().getSalaComInimigos());

        missao.setToCruz(new ToCruz("to", 45));

        Cenarios.walkEnimies(missao , false, false);

        System.out.println("\n\nDepois de se moverem: " + missao.getEdificio().getSalaComInimigos());

    }
}
