package Data;

import LinkedList.LinearLinkedOrderedList;
import Missao.Missao;

import java.io.File;

public class TestImport {
    public static void main(String[] args) {

//        DataTreating.loadGameData();
//
//
//
//
//        System.out.println(DataTreating.getMissaoByVersion(2));

        File diretorio = new File("oscar");
        File diretorio2 = new File("GameData\\Relatorios");
        File diretorio3 = new File("GameData\\Missoes");
        if (!diretorio.exists()) {
            diretorio.mkdir();
        }
        if (!diretorio2.exists()) {
            diretorio2.mkdir();
        }
        if (!diretorio3.exists()) {
            diretorio3.mkdir();
        }
    }
}
