//package Data;
//
//
//
//
//import Graphs.GraphNetwork;
//import Missao.*;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import Edificio.*;
//import Pessoa.ToCruz;
//
//public class TestRelatoriosToJson {
//    public static void main(String[] args) {
//        // Create some Missao instances
//        Sala sala1 = new Sala("porcas", false, false);
//        Sala sala2 = new Sala("estg", false, false);
//        Sala sala3 = new Sala("feup", false, false);
//        Sala sala4 = new Sala("um", false, false);
//        GraphNetwork<Sala> g1 = new GraphNetwork<>();
//        g1.addVertex(sala1);
//        g1.addVertex(sala2);
//        g1.addEdge(sala1, sala2, 1);
//        Edificio edificio1 = new Edificio(g1);
//        GraphNetwork<Sala> g2 = new GraphNetwork<>();
//        g2.addVertex(sala1);
//        g2.addVertex(sala3);
//        g2.addEdge(sala1, sala3, 1);
//        Edificio edificio2 = new Edificio(g2);
//        GraphNetwork<Sala> g3 = new GraphNetwork<>();
//        g2.addVertex(sala2);
//        g2.addVertex(sala4);
//        g2.addEdge(sala2, sala4, 1);
//        Edificio edificio3 = new Edificio(g3);
//
//
//        //Missao missao1 = new Missao("String cod_missao", 1, edificio1, new Alvo(sala1,"Mini")) ;
//        //Missao missao2 = new Missao("y cod_missao", 2, edificio1, new Alvo(sala1,"Sagres"));
//       // Missao missao3 = new Missao("String p", 3, edificio1, new Alvo(sala1,"Leitao assado"));
//         DataTreating.ReadMissao("C:\\Faculdade\\2ano\\PrimeiroSemestre\\ED\\dadosJogo.json");
//        Missao funcionall = DataTreating.getMissaoByVersion(1);
//
//        missao1.setToCruz(new ToCruz("p1", 200));
//        missao2.setToCruz(new ToCruz("p2", 10));
//        missao3.setToCruz(new ToCruz("MrPizza", 20));
//        funcionall.setToCruz(new ToCruz("TODelas", 2));
//
//
//        missao1.addSalaCaminhoTo(sala1);
//        missao1.addSalaCaminhoTo(sala2);
//        missao1.addSalaCaminhoTo(sala1);
//
//        missao2.addSalaCaminhoTo(sala4);
//        missao2.addSalaCaminhoTo(sala2);
//        missao2.addSalaCaminhoTo(sala1);
//
//        missao3.addSalaCaminhoTo(sala4);
//        missao3.addSalaCaminhoTo(sala2);
//        missao3.addSalaCaminhoTo(sala3);
//
//        funcionall.addSalaCaminhoTo(sala4);
//        funcionall.addSalaCaminhoTo(sala1);
//        funcionall.addSalaCaminhoTo(sala3);
//        // Create some Relatorio instances
//        Relatorio relatorio1 = new Relatorio(missao1);
//        Relatorio relatorio2 = new Relatorio(missao2);
//        Relatorio relatorio3 = new Relatorio(missao3);
//        Relatorio relatorio4 = new Relatorio(funcionall);
//
//        // Create a Relatorios instance
//        Relatorios relatorios = new Relatorios();
//
//        // Add Relatorio instances to the Relatorios
//        try {
//            relatorios.addRelatorio(relatorio1);
//            relatorios.addRelatorio(relatorio2);
//            relatorios.addRelatorio(relatorio3);
//            relatorios.addRelatorio(relatorio4);
//        } catch (NullPointerException e) {
//            System.err.println(e.getMessage());
//        }
//
//        try {
//
//
//        } catch (NullPointerException e) {
//            System.err.println(e.getMessage());
//        }
//
//        // Serialize to JSON and write to file
//        String jsonString = relatorios.toJsonString();
//    //    DataTreating.SaveRelatorios();
//        //writeJsonToFile(jsonString, "relatorios.json");
//
//        // Read JSON from file and deserialize
//        //String readJsonString = readJsonFromFile("relatorios.json");
//        //Relatorios deserializedRelatorios = Relatorios.fromJsonString(readJsonString);
//
//        // Print deserialized Relatorios
//        //System.out.println(deserializedRelatorios.toJsonString());
//    }
//
//    private static void writeJsonToFile(String jsonString, String filePath) {
//        try (FileWriter file = new FileWriter(filePath)) {
//            file.write(jsonString);
//            System.out.println("Successfully wrote JSON to the file.");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static String readJsonFromFile(String filePath) {
//        StringBuilder contentBuilder = new StringBuilder();
//        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
//            String currentLine;
//            while ((currentLine = br.readLine()) != null) {
//                contentBuilder.append(currentLine).append("\n");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return contentBuilder.toString();
//    }
//}
