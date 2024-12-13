import Data.DataTreating;
import GameEngine.Cenarios;
import Missao.Missao;
import Pessoa.ToCruz;

public class TestAndar {
    public static void main(String[] args) {
        Missao missao;

        DataTreating.ReadMissao("C:\\Faculdade\\2ano\\PrimeiroSemestre\\ED\\dadosJogo.json");
        missao = DataTreating.getMissaoByVersion(1);
                System.out.println("Antes de se moverem: " + missao.getEdificio().getSalaComInimigos());

        missao.setToCruz(new ToCruz("to", 45, 10));

        Cenarios.walkEnimies(missao , false, false);

        System.out.println("\n\nDepois de se moverem: " + missao.getEdificio().getSalaComInimigos());

    }
}
