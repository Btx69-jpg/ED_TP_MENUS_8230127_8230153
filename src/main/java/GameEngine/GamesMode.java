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
    private Iterator<Sala> caminhoMedKit;
    private Iterator<Sala> caminhoAlvo;

    private boolean end;

    public  GamesMode(Missao missao) {
        this.missao = missao;
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
        ToCruz toCruz = missao.getToCruz();

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
        toCruz = missao.getToCruz();
        this.caminhoMedKit = missao.getEdificio().getCaminhoMedkit();
        this.caminhoAlvo = missao.getEdificio().getCaminhoAlvo();
        Sala salaToCruz = missao.getEdificio().getSalaToCruz();

        int cntOpc = printOptions(salaToCruz);
            while (op < 1 || op > cntOpc + 1) {

                op = sc.nextInt();
                if (op < 1 || op > cntOpc + 1){
                    System.out.println("Opção inválida!");
                    printOptions(salaToCruz);
                }else if (op == (cntOpc + 1)) {
                    end = true;
                    break;
                }
                else {
                    int opcoesValidas = 1;
                    if (cntOpc == opcoesValidas++){
                        moverMenu(op,sc);
                    }
                    else if (cntOpc == opcoesValidas++){
                        Rounds.useMedkit(toCruz, missao, false, false);
                    }
                    else if (salaToCruz.hasItens()){
                        opcoesValidas++;
                        if ( cntOpc == opcoesValidas) {
                            toCruz.apanhaItem(salaToCruz.getItens());
                        }
                    }
                    else if (salaToCruz.haveAlvo()) {
                        opcoesValidas++;
                        if (cntOpc == opcoesValidas) {
                            toCruz.setGotAlvo(true);
                        }
                    }
                    else if (salaToCruz.isEntradaSaida()) {
                        opcoesValidas++;
                        if ( cntOpc == opcoesValidas) {
                            end = true;
                            break;
                        }
                    }
                    else if (cntOpc == opcoesValidas++){
                        System.out.println("Vida atual: " + toCruz.getVida() + " Vida maxima: " + toCruz.getMaxLife());
                    }
                    else if (cntOpc == opcoesValidas++){
                        System.out.println("Mochila: " + toCruz.getMochila());
                    }
                    else if (cntOpc == opcoesValidas++){
                        System.out.println(missao.getAlvo());
                    }

                }

            }
        }
    }

    private void moverMenu(int op, Scanner sc){
        LinearLinkedUnorderedList<Sala> salas =missao.getEdificio().getSalas().getConnectedVertices(missao.getEdificio().getSalaToCruz());
        Sala caminhoMedkitSala = caminhoMedKit.next();
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
                            caminhoMedKit = missao.getEdificio().getCaminhoMedkit();
                        }
                        if (caminhoAlvoSala != sala){
                            caminhoAlvo = missao.getEdificio().getCaminhoAlvo();
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

    public int printOptions(Sala salaToCruz){
        int cnt = 1;
        System.out.println("Escolha uma opção:");
        System.out.println(cnt++ + " - Mover");
        System.out.println(cnt++ +" - Usar MedKit");

        if (salaToCruz.hasItens()) {
            System.out.println(cnt++ + " - Apanhar Item");
        }
        if (salaToCruz.haveAlvo()) {
            System.out.println(cnt++ + " - Recuperar o alvo");
        }
        if (salaToCruz.isEntradaSaida()) {
            System.out.println(cnt++ + " - Sair do edificio (Terminar missão)");
        }
        System.out.println(cnt++ +" - Verificar Vida");
        System.out.println(cnt++ +" - Verificar Mochila");
        System.out.println(cnt++ +" - Verificar Alvo");
        System.out.println(cnt++ +" - Sair");

        return cnt;
    }
}
