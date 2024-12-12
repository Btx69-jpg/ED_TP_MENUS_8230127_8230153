package Data;




import Graphs.GraphNetwork;
import Missao.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import Edificio.*;
import Pessoa.ToCruz;

public class TestRelatoriosToJson {
    public static void main(String[] args) {
        // Create some Missao instances
        Sala sala1 = new Sala("porcas", false, false);
        Sala sala2 = new Sala("estg", false, false);
        Sala sala3 = new Sala("feup", false, false);
        Sala sala4 = new Sala("um", false, false);
        GraphNetwork<Sala> g1 = new GraphNetwork<>();
        g1.addVertex(sala1);
        g1.addVertex(sala2);
        g1.addEdge(sala1, sala2, 1);
        Edificio edificio1 = new Edificio(g1);
        GraphNetwork<Sala> g2 = new GraphNetwork<>();
        g2.addVertex(sala1);
        g2.addVertex(sala3);
        g2.addEdge(sala1, sala3, 1);
        Edificio edificio2 = new Edificio(g2);
        GraphNetwork<Sala> g3 = new GraphNetwork<>();
        g2.addVertex(sala2);
        g2.addVertex(sala4);
        g2.addEdge(sala2, sala4, 1);
        Edificio edificio3 = new Edificio(g3);


        Missao missao1 = new Missao("String cod_missao", 1, edificio1, new Alvo(sala1,"Mini")) ;
        Missao missao2 = new Missao("y cod_missao", 2, edificio1, new Alvo(sala1,"Sagres"));
        Missao missao3 = new Missao("String p", 3, edificio1, new Alvo(sala1,"Leitao assado"));
        DataTreating.ReadMissao("C:\\Faculdade\\2ano\\PrimeiroSemestre\\ED\\dadosJogo.json");
        Missao funcionall = DataTreating.getMissaoByVersion(1);

        missao1.setToCruz(new ToCruz("p1", 200));
        missao2.setToCruz(new ToCruz("p2", 10));
        missao3.setToCruz(new ToCruz("MrPizza", 20));
        funcionall.setToCruz(new ToCruz("TODelas", 2));


        missao1.addSalaCaminhoTo(sala1);
        missao1.addSalaCaminhoTo(sala2);
        missao1.addSalaCaminhoTo(sala1);

        missao2.addSalaCaminhoTo(sala4);
        missao2.addSalaCaminhoTo(sala2);
        missao2.addSalaCaminhoTo(sala1);

        missao3.addSalaCaminhoTo(sala4);
        missao3.addSalaCaminhoTo(sala2);
        missao3.addSalaCaminhoTo(sala3);

        funcionall.addSalaCaminhoTo(sala4);
        funcionall.addSalaCaminhoTo(sala1);
        funcionall.addSalaCaminhoTo(sala3);
        // Create some Relatorio instances
        Relatorio relatorio1 = new Relatorio(missao1);
        Relatorio relatorio2 = new Relatorio(missao2);
        Relatorio relatorio3 = new Relatorio(missao3);
        Relatorio relatorio4 = new Relatorio(funcionall);


        try {
        DataTreating.addRelatorio(relatorio1);
            DataTreating.addRelatorio(relatorio2);
            DataTreating.addRelatorio(relatorio3);
            DataTreating.addRelatorio(relatorio4);

        } catch (NullPointerException e) {
            System.err.println(e.getMessage());
        }


        DataTreating.SaveRelatorios();

    }


}
