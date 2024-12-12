package Data;

import Missao.Missao;
public class TestImport {
    public static void main(String[] args) {
        Missao missao = new Missao();
        Missao missao2 = new Missao();
      //   DataTreating.ReadMissao("C:\\Faculdade\\2ano\\PrimeiroSemestre\\ED\\Trabalho pratico\\ED_TP_MENUS\\ED_TP_MENUS_8230127_8230153\\src\\main\\resources\\teste.json");
       // DataTreating.ReadMissao("C:\\Faculdade\\2ano\\PrimeiroSemestre\\ED\\dadosJogo.json");
        DataTreating.ReadMissoes("C:\\Faculdade\\2ano\\PrimeiroSemestre\\ED\\Trabalho pratico\\ED_TP_MENUS\\ED_TP_MENUS_8230127_8230153\\src\\main\\resources\\Teste2missoes.json");
        missao = DataTreating.getMissaoByVersion(1);
        System.out.println(missao);
        System.out.println("\n\n\n\n\n\n\n\n");

        missao2 = DataTreating.getMissaoByVersion(2);
        System.out.println(missao2);
    }
}
