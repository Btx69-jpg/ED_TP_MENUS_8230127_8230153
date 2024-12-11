package Data;

import Missao.Missao;
public class TestImport {
    public static void main(String[] args) {
        Missao missao = new Missao();
        missao = Json.ReadMissao("C:\\Faculdade\\2ano\\PrimeiroSemestre\\ED\\dadosJogo.json");

        System.out.println(missao);
    }
}
