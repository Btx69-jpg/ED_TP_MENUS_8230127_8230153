package GameEngine;

import Data.Json;
import LinkedList.LinearLinkedUnorderedList;
import Pessoa.ToCruz;
import Missao.Missao;
import Edificio.Sala;

import java.util.Iterator;
import java.util.Scanner;

public class GamesMode implements GameMode {
    private Missao missao;
    private ToCruz toCruz;
    private boolean end;

    public  GamesMode(Missao missao) {
        this.missao = missao;
        //LEMBRAR DE N\AO FAZER DANO FIXO E IMPLEMENTAR DIFICULDADES
        this.toCruz = new ToCruz("ToCruz", 40);
        this.end = false;
    }

    @Override
    public void automatic() {
        //todas as decisões são tomadas automaticamente
        //iterator the shortestpath
    }

    @Override
    public void manual() {
        int op = 0;
        Scanner sc = new Scanner(System.in);

        //setUp do spawn point
        int entradasSaidas = missao.getEdificio().getNumeroEntradas_saidas();
        while (op < 1 || op > entradasSaidas + 1) {
            System.out.println("Escolha o spawn point:");
            Iterator<Sala> entradasSaidasIterator = missao.getEdificio().getEntradas_saidas().iterator();
            for (int i = 0 ; i < entradasSaidas; i++) {
                System.out.println(i + " - " + entradasSaidasIterator.next());
            }
            System.out.println((entradasSaidas + 1) +" - Sair");
            op = sc.nextInt();
            if (op < 1 || op > entradasSaidas + 1){
                System.out.println("Opção inválida");
            }else if (op == (entradasSaidas + 1)) {
                end = true;
                break;
            }
            else {
                entradasSaidasIterator = missao.getEdificio().getSalas().getVerticesIterator();
                for (int i = 0; i < entradasSaidas; i++) {
                    if (i == op) {
                        Sala sala = entradasSaidasIterator.next();
                        sala.setHaveToCruz(true);
                        break;
                    }
                    entradasSaidasIterator.next();
                }
            }

        }
        //-------------------------------------- fim do setup do spawn point-------------------------------------------

        while (toCruz.getVida() > 0 && !end) {
        Iterator<Sala> caminhoMedkit = missao.getEdificio().getCaminhoMedkit();
        Iterator<Sala> caminhoAlvo = missao.getEdificio().getCaminhoAlvo();
            while (op < 1 || op > 8) {
                System.out.println("Escolha uma opção:");
                System.out.println("1 - Mover");
                System.out.println("2 - Usar MedKit");
                System.out.println("3 - Atacar");
                System.out.println("4 - Verificar Vida");
                System.out.println("5 - Verificar Mochila");
                System.out.println("6 - Verificar Alvo");
                System.out.println("7 - Verificar Edificio");
                System.out.println("8 - Sair");
                System.out.println("Caminho para o medKit mais proximo: " + PrintCaminho(caminhoMedkit));
                System.out.println("Caminho mais curto para o alvo: " + PrintCaminho(caminhoMedkit));
                op = sc.nextInt();
                switch (op) {
                    case 1:
                        moverMenu(op,sc, caminhoMedkit, caminhoAlvo);
                        break;
                    case 2:
                        //usar medkit
                        break;
                    case 3:
                        //atacar
                        break;
                    case 4:
                        //verificar vida
                        break;
                    case 5:
                        //verificar mochila
                        break;
                    case 6:
                        //verificar alvo
                        break;
                    case 7:
                        //verificar edificio
                        break;
                    case 8:
                        //sair
                        break;
                    default:
                        System.out.println("Opção inválida");
                }
            }
        }
        //aparecer um menu para deixar o utilizador escolher o que fazer
    }

    private void moverMenu(int op, Scanner sc, Iterator<Sala> caminhoMedkit, Iterator<Sala> caminhoAlvo){
        LinearLinkedUnorderedList<Sala> salas =missao.getEdificio().getSalas().getConnectedVertices(missao.getEdificio().getSalaToCruz());
        Sala caminhoMedkitSala = caminhoMedkit.next();
        Sala caminhoAlvoSala = caminhoAlvo.next();
        while (op < 1 || op > salas.size()) {
            int cnt = 3;
            System.out.println("Escolha uma opção:");
            System.out.println( "1 - Mover para MedKit mais proximo. ");
            System.out.println("2 - Mover para sala mais proxima do alvo ");
            for (Sala sala : salas){
                System.out.println(cnt + " - Mover para: " + sala.getNome());
                cnt++;
            }
            System.out.println((cnt + 1) + " - Sair do jogo ");

            op = sc.nextInt();
            if (op < 1 || op > salas.size() + 1){
                System.out.println("Opção inválida");
            }else if (op == (salas.size() + 1)) {
                end = true;
                break;
            }
            else {
                if (op == 1){
                    Rounds.moveToCruz(missao, caminhoMedkitSala,false);
                }
                if (op == 2){
                    Rounds.moveToCruz(missao, caminhoAlvoSala, false);
                }
                Iterator<Sala> salasIt = salas.iterator();
                for (int i = 0; i < salas.size() ; i++) {

                    if (i + 3 == op) {
                        Sala sala = salasIt.next();
                        Rounds.moveToCruz(missao, sala, false);
                        if (caminhoMedkitSala != sala){
                            caminhoMedkit = missao.getEdificio().getCaminhoMedkit();
                        }
                        if (caminhoAlvoSala != sala){
                            caminhoMedkit = missao.getEdificio().getCaminhoAlvo();
                        }
                        break;
                    }
                    salasIt.next();
                }
            }
        }
    }

    public String PrintCaminho(Iterator<Sala> caminho){
        StringBuilder caminhoStr = new StringBuilder();
        caminhoStr.append("[  ").append(caminho.next().getNome());
        while (caminho.hasNext()) {
            Sala sala = caminho.next();
            caminhoStr.append(", ").append(sala.getNome());
        }
        caminhoStr.append("  ]");

        return caminhoStr.toString();
    }
    @Override
    public void run() {

        new Missao();
        this.missao = Json.ReadJson("/teste.json");
        manual();
    }
}
