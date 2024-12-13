package Data;

import LinkedList.LinearLinkedOrderedList;
import Missao.Missao;
public class TestImport {
    public static void main(String[] args) {

        DataTreating.loadGameData();

        System.out.println(DataTreating.getMissaoByVersion(2));

    }
}
