package Data;

import Missao.Missao;
public class TestImport {
    public static void main(String[] args) {
        Missao missao = new Missao();
         DataTreating.ReadMissao("C:\\Faculdade\\2ano\\PrimeiroSemestre\\ED\\Trabalho pratico\\ED_TP_MENUS\\ED_TP_MENUS_8230127_8230153\\src\\main\\resources\\teste.json");
        missao = DataTreating.getMissaoByVersion(1);
        System.out.println(missao);
    }
}
