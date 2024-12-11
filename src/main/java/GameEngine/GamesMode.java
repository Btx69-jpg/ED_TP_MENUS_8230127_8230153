package GameEngine;

import Data.Json;
import Edificio.Edificio;
import Graphs.GraphNetwork;
import LinkedList.LinearLinkedUnorderedList;
import Pessoa.ToCruz;
import Missao.Missao;
import Edificio.Sala;
import Missao.Alvo;

import java.util.Iterator;
import java.util.Scanner;

public class GamesMode implements GameMode {
    private Missao missao;
    private Iterator<Sala> caminhoMedKit;
    private Iterator<Sala> caminhoAlvo;
    private double currentWeigthAM;

    private boolean end;

    public  GamesMode() {
        this.missao = null;
        this.end = false;
        currentWeigthAM = 0;
    }

    @Override
    public void automatic() {
        Edificio edificio = missao.getEdificio();
        ToCruz toCruz = missao.getToCruz();
        LinearLinkedUnorderedList<Sala> EntradasSaidas = edificio.getEntradas_saidas();
        GraphNetwork<Sala> salasGrafo = edificio.getSalas();
        Iterator<Sala> CaminhoTemp;
        Sala salaAlvo = edificio.getSalaAlvo();
        Sala salaToCruz = EntradasSaidas.first();
        Sala saida = null;
        Iterator<Sala> caminho ;
        double tempWeight1;
        double tempWeight2;

        //SetUp Do spawnPoint
        System.out.println("SetUp do SpawnPoint");
         if (EntradasSaidas.size() > 1) {
            for (Sala EntExit : EntradasSaidas){
                 currentWeigthAM =  salasGrafo.shortestWeightWeight(EntExit, salaAlvo);
                if (salasGrafo.shortestWeightWeight(salaToCruz, edificio.getSalaAlvo()) > currentWeigthAM){
                    salaToCruz = EntExit;
                }
            }

        }
        System.out.println("SpawnPoint: " + salaToCruz.getNome());

        caminho = salasGrafo.iteratorShortestWeight(salaToCruz, salaAlvo);
         currentWeigthAM = salasGrafo.shortestWeightWeight(salaToCruz, edificio.getSalaAlvo());
        missao.changeSala(salaToCruz, salaToCruz.setHaveToCruz(true));
        //---------------------------------------------------------------------
        //Inicio do jogo
        System.out.println("Comecou!");
        while ( !end) {
            while ( toCruz.getVida() > 0 && caminho.hasNext()) {
                toCruz = missao.getToCruz();
                Sala proximaSala;
                Sala salaMedKit = edificio.getMedKitProx(true);
                edificio = missao.getEdificio();
                salasGrafo = edificio.getSalas();
                salaToCruz = edificio.getSalaToCruz();
                if (toCruz.getGotAlvo()){
                    saida = edificio.getClosestExitAM();
                }

                //caso tenha mais que 40% da vida maxima e não tenha o alvo, constinua a andar em direção ao alvo
                if (toCruz.getVida() > toCruz.getMaxLife() * 0.40 ){

                    //continuação do caminho caso ele não tenha o alvo
                    if (!toCruz.getGotAlvo()){
                        CaminhoTemp = getCaminhoAM(salaToCruz, salaAlvo, salasGrafo);
                        if (CaminhoTemp != null){
                            caminho = CaminhoTemp;
                        }
                    }
                    //------------------------------
                    //continuação do caminho caso ele tenha o alvo
                    else {
                        CaminhoTemp = getCaminhoAM(salaAlvo, saida, salasGrafo);
                        if (CaminhoTemp != null){
                            caminho = CaminhoTemp;
                        }
                    }
                    //---------------------------------
                    caminho.next();
                    proximaSala = caminho.next();
                    System.out.println("To andou para: " + proximaSala.getNome());
                    Rounds.moveToCruz(missao, proximaSala, true);
                    salasGrafo = edificio.getSalas();
                }
                //---------------------------------------------
                //caso tenha 40% ou menos da vida maxima, e tenha a mochila vazia, vai buscar o medkit dependendo se tem o alvo ou não, caso tenha, anda na direção do que estiver mais proximo(saida ou medKit)
                 if ((toCruz.getVida() <= toCruz.getMaxLife() * 0.40 && toCruz.getMochila().isEmpty())){
                    caminhoMedKit = edificio.getCaminhoMedkit(true);
                    caminhoMedKit.next();
                    proximaSala = caminhoMedKit.next();
                    tempWeight1 = salasGrafo.shortestWeightWeight( edificio.getSalaToCruz(), salaMedKit);
                    //caso não tenha o alvo nem medKit e tenha a vida a baixo dos40% decide ir para o que esta mais proximo
                    if ( !toCruz.getGotAlvo()){
                        tempWeight2 = salasGrafo.shortestWeightWeight( edificio.getSalaToCruz(), salaAlvo);

                        if (tempWeight1 < tempWeight2){

                            CaminhoTemp = getCaminhoAM(salaToCruz, salaMedKit, salasGrafo);
                            if (CaminhoTemp != null){
                                caminho = CaminhoTemp;
                            }
                            System.out.println("To andou para: " + proximaSala.getNome());
                            Rounds.moveToCruz(missao, proximaSala, true);
                            salasGrafo = edificio.getSalas();
                        }
                        else {

                            CaminhoTemp = getCaminhoAM(proximaSala, salaAlvo, salasGrafo);
                            if (CaminhoTemp != null){
                                caminho = CaminhoTemp;
                            }
                            caminho.next();
                            System.out.println("To andou para: " + proximaSala.getNome());
                            Rounds.moveToCruz(missao, caminho.next(), true);
                            salasGrafo = edificio.getSalas();
                        }
                    }
                    //------------------------------------
                    //caso não tenha medKit mas tem o alvo e tem a vida a baixo dos40% decide ir para o que esta mais proximo (saida ou medkit)
                     if  (toCruz.getGotAlvo()){
                        tempWeight2 = salasGrafo.shortestWeightWeight( edificio.getSalaToCruz(), edificio.getClosestExitAM());

                        if (tempWeight1 < tempWeight2){

                            CaminhoTemp = getCaminhoAM(salaToCruz, salaMedKit, salasGrafo);
                            if (CaminhoTemp != null){
                                caminho = CaminhoTemp;
                            }
                            System.out.println("To andou para: " + proximaSala.getNome());
                            Rounds.moveToCruz(missao, proximaSala, true);
                            salasGrafo = edificio.getSalas();
                        }

                        else {
                            CaminhoTemp = getCaminhoAM(salaToCruz, saida, salasGrafo);
                            if (CaminhoTemp != null){
                                caminho = CaminhoTemp;
                            }
                            System.out.println("To andou para: " + proximaSala.getNome());
                            Rounds.moveToCruz(missao, edificio.getClosestExitAM(), true);
                            salasGrafo = edificio.getSalas();
                        }
                    }
                    //----------------------------------------

                     salasGrafo = edificio.getSalas();
                    double newWeight = salasGrafo.shortestWeightWeight(edificio.getSalaToCruz(), edificio.getSalaAlvo());

                    if (currentWeigthAM > newWeight){
                        caminho = salasGrafo.iteratorShortestWeight(edificio.getSalaToCruz(), edificio.getSalaAlvo());
                        currentWeigthAM = newWeight;
                    }
                }
                //---------------------------------------------
                //caso tenha 40% ou menos da vida maxima, não tenha o alvo e tenha um medkit, vai usar
                 if ((toCruz.getVida() <= toCruz.getMaxLife() * 0.40 && !toCruz.getMochila().isEmpty())){
                    Rounds.useMedkit(missao, true, false);
                }
                if (missao.getToCruz().getVida() <= 0){
                    end = true;
                    break;
                }
            }

        }
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
            int localCnt = 1;
            System.out.println("Escolha o spawn point:");
            Iterator<Sala> entradasSaidasIterator = missao.getEdificio().getEntradas_saidas().iterator();
            for (localCnt = 1 ; localCnt <= entradasSaidas; localCnt++) {
                System.out.println(localCnt + " - " + entradasSaidasIterator.next());
            }
            System.out.println((localCnt) +" - Sair");
            System.out.print("\nEscolha: ");
            op = sc.nextInt();
            if (op < 1 || op > entradasSaidas + 1){
                System.out.println("Opção inválida");
            }else if (op == (entradasSaidas + 1)) {
                end = true;
                break;
            }
            else {
                entradasSaidasIterator = missao.getEdificio().getEntradas_saidas().iterator();
                for (int i = 1; i < entradasSaidas + 1; i++) {
                    if (i == op) {
                        Sala sala = entradasSaidasIterator.next();

                        missao.changeSala(sala, sala.setHaveToCruz(true));
                        break;
                    }
                    entradasSaidasIterator.next();
                }
            }

        }
        //-------------------------------------- fim do setup do spawn point-------------------------------------------

        while (toCruz.getVida() > 0 && !end) {
            op = 0;
            System.out.println();
            toCruz = missao.getToCruz();
            this.caminhoMedKit = missao.getEdificio().getCaminhoMedkit(false);
            this.caminhoAlvo = missao.getEdificio().getCaminhoAlvo(false);
            Sala salaToCruz = missao.getEdificio().getSalaToCruz();

            int cntOpc = printOptions(salaToCruz);
            while (op < 1 || op > cntOpc ) {

                op = sc.nextInt();
                if (op < 1 || op > cntOpc){
                    System.out.println("Opção inválida!");
                    printOptions(salaToCruz);
                }else if (op == (cntOpc)) {
                    end = true;
                    break;
                }
                else {
                    int opcoesValidas = 1;
                    if (op == opcoesValidas++){
                        moverMenu(op,sc);
                    }
                     if (op == opcoesValidas){
                        Rounds.useMedkit(missao, false, false);
                    }
                     if (salaToCruz.hasItens()){
                        opcoesValidas++;
                        if ( op == opcoesValidas) {
                            toCruz.apanhaItem(salaToCruz.getItens());
                            missao.changeToCruz(toCruz);
                        }

                    }
                     if (salaToCruz.haveAlvo()) {
                        opcoesValidas++;
                        if (op == opcoesValidas) {
                            toCruz.setGotAlvo(true);
                            salaToCruz.setAlvo(false);
                            missao.changeToCruz(toCruz);
                            missao.changeSala(salaToCruz, salaToCruz.setAlvo(false));
                            missao.changeAlvo(new Alvo(new Sala("ToCruz", true, false), missao.getAlvo().getTipo()));
                        }

                    }
                     if (salaToCruz.isEntradaSaida()) {
                        opcoesValidas++;
                        if ( op == opcoesValidas) {
                            end = true;
                            break;
                        }

                    }
                     if (op == ++opcoesValidas){
                        System.out.println("Vida atual: " + toCruz.getVida() + " Vida maxima: " + toCruz.getMaxLife());
                    }
                     if (op == ++opcoesValidas){
                        System.out.println("Mochila: " + toCruz.getMochila());
                    }
                     if (op == ++opcoesValidas){
                        System.out.println(missao.getAlvo());
                    }

                }

            }
        }
        //criar o relatorio com os dados da missao
    }

    private void moverMenu(int op, Scanner sc){
        LinearLinkedUnorderedList<Sala> salas =missao.getEdificio().getSalas().getConnectedVertices(missao.getEdificio().getSalaToCruz());
        Sala caminhoMedkitSala = caminhoMedKit.next();
        Sala caminhoAlvoSala = caminhoAlvo.next();
        op = 0;
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
            System.out.print("\nEscolha: ");
            op = sc.nextInt();
            if (op < 1 || op > salas.size() + 3){
                System.out.println("Opção inválida");
            }else if (op == (salas.size() + 3)) {
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
                        if (caminhoMedkitSala != sala){
                            caminhoMedKit = missao.getEdificio().getCaminhoMedkit(true);
                        }
                        if (caminhoAlvoSala != sala){
                            caminhoAlvo = missao.getEdificio().getCaminhoAlvo(false);
                        }
                        Rounds.moveToCruz(missao, sala, false);
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
    public void run(boolean autoMode) {
        ToCruz toTeste = new ToCruz("teste", 30);
        this.missao = Json.ReadMissao("C:\\Faculdade\\2ano\\PrimeiroSemestre\\ED\\dadosJogo.json");
        missao.setToCruz(toTeste);
        if (!autoMode) {
            manual();
        }
        automatic();
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
        System.out.println(cnt +" - Sair");

        System.out.print("\nEscolha: ");

        return cnt;
    }

    private Iterator<Sala> getCaminhoAM(Sala from, Sala to, GraphNetwork<Sala> salasGraph){
        double newWeight = salasGraph.shortestWeightWeight(from, to);

        if (currentWeigthAM > newWeight){
            currentWeigthAM = newWeight;
            return salasGraph.iteratorShortestWeight(from, to);
        }
        return null;
    }
}
