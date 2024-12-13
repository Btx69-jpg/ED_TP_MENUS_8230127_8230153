import Data.DataTreating;

public class TestImport {
    public static void main(String[] args) {

       DataTreating.loadGameData();
//
//
//
//


        DataTreating.saveGameData();

        DataTreating.loadGameData();

        System.out.println(DataTreating.getMissaoByVersion(2));


    }
}
