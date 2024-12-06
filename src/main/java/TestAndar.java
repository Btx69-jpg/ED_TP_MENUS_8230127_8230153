import Data.Json;
import Missao.Missao;

public class TestAndar {
    public static void main(String[] args) {
        Missao missao;

        missao = Json.ReadJson("C:\\Faculdade\\2ano\\PrimeiroSemestre\\ED\\dadosJogo.json");

        System.out.println(missao);
    }
}
