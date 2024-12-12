package Data;

import Missao.Missao;
public class TestImport {
    public static void main(String[] args) {
        Missao missao = new Missao();
         DataTreating.ReadMissao("C:\\Faculdade\\2ano\\PrimeiroSemestre\\ED\\dadosJogo.json");
        missao = DataTreating.getMissaoByVersion(1);
        System.out.println(missao);
    }
}
