package GameEngine;

import Data.DataTreating;
import Edificio.Edificio;
import Edificio.Sala;
import Graphs.GraphNetwork;
import LinkedList.LinearLinkedUnorderedList;
import Missao.Alvo;
import Missao.Missao;
import Pessoa.ToCruz;

import java.util.Iterator;
import java.util.Scanner;

public class GamesMode implements GameMode {
    private  Missao missao;
    private Iterator<Sala> caminhoMedKit;
    private Iterator<Sala> caminhoAlvo;
    private Iterator<Sala> caminhoSaida;
    //---------------------------------

    private ToCruz toCruz;
    private Sala salaMedKitAM;
    private Edificio edificioAM;
    private GraphNetwork<Sala> salasGrafoAM;
    private Sala salaToCruzAM;
    private Sala saida;

    private boolean end;

    public  GamesMode() {
        this.missao = null;
        this.end = false;
    }

    @Override
    public void automatic() {
        edificioAM = missao.getEdificio();
        salasGrafoAM = edificioAM.getSalas();
        LinearLinkedUnorderedList<Sala> EntradasSaidas = edificioAM.getEntradas_saidas();
        Iterator<Sala> CaminhoTemp;
        Sala salaAlvo = edificioAM.getSalaAlvo();
        salaToCruzAM = EntradasSaidas.first();
        Iterator<Sala> caminho;
        double tempWeight1;
        double tempWeight2;

        // Setup do SpawnPoint
        System.out.println("Setup do SpawnPoint");
        if (EntradasSaidas.size() > 1) {
            for (Sala EntExit : EntradasSaidas) {
                tempWeight1 = salasGrafoAM.shortestWeightWeight(EntExit, salaAlvo);
                if (salasGrafoAM.shortestWeightWeight(salaToCruzAM, edificioAM.getSalaAlvo()) > tempWeight1) {
                    salaToCruzAM = EntExit;
                }
            }
        }

        System.out.println("SpawnPoint: " + salaToCruzAM.getNome());


        missao.changeSala(salaToCruzAM, salaToCruzAM.setHaveToCruz(true));
        AtualizeAM();
        caminho = salasGrafoAM.iteratorShortestWeight(salaToCruzAM, salaAlvo);

        // Início do jogo
        System.out.println("Começou!");
        if (missao.getEdificio().getSalaToCruz().hasInimigos()){
            Cenarios.Confronto(missao, true, true);
        }
        while ( !end) {
            while ( toCruz.getVida() > 0 && caminho.hasNext()) {
                try {
                    Sala proximaSala;

                    //caso tenha mais que 40% da vida maxima
                    if (toCruz.getVida() > toCruz.getMaxLife() * 0.40) {
                        //continuação do caminho caso ele não tenha o alvo
                        if (!toCruz.getGotAlvo()) {
                            CaminhoTemp = getCaminhoAM(salaToCruzAM, salaAlvo, salasGrafoAM);
                            if (CaminhoTemp != null) {
                                caminho = CaminhoTemp;
                            }
                        }
                        //------------------------------
                        //continuação do caminho caso ele tenha o alvo
                        else {
                            CaminhoTemp = getCaminhoAM(salaToCruzAM, saida, salasGrafoAM);
                            if (CaminhoTemp != null) {
                                caminho = CaminhoTemp;
                            }
                        }
                        //---------------------------------
                        caminho.next();
                        proximaSala = caminho.next();
                        System.out.println("To andou para: " + proximaSala.getNome());
                        Rounds.moveToCruz(missao, proximaSala, true);
                        AtualizeAM();
                    }
                    //---------------------------------------------
                    //caso tenha 40% ou menos da vida maxima, e tenha a mochila vazia, vai buscar o medkit dependendo se tem o alvo ou não, caso tenha, anda na direção do que estiver mais proximo(saida ou medKit)
                    else if ((toCruz.getVida() <= toCruz.getMaxLife() * 0.40 && toCruz.getMochila().isEmpty())) {
                        caminhoMedKit = edificioAM.getCaminhoMedkit(true);
                        caminhoMedKit.next();
                        proximaSala = caminhoMedKit.next();
                        tempWeight1 = salasGrafoAM.shortestWeightWeight(edificioAM.getSalaToCruz(), salaMedKitAM);
                        //caso não tenha o alvo nem medKit e tenha a vida a baixo dos40% decide ir para o que esta mais proximo
                        if (!toCruz.getGotAlvo()) {
                            tempWeight2 = salasGrafoAM.shortestWeightWeight(edificioAM.getSalaToCruz(), salaAlvo);

                            if (tempWeight1 < tempWeight2) {

                                CaminhoTemp = getCaminhoAM(edificioAM.getSalaToCruz(), salaMedKitAM, salasGrafoAM);
                                if (CaminhoTemp != null) {
                                    caminho = CaminhoTemp;
                                }
                                System.out.println("To andou para: " + proximaSala.getNome());
                                Rounds.moveToCruz(missao, proximaSala, true);
                                AtualizeAM();
                            } else {

                                CaminhoTemp = getCaminhoAM(edificioAM.getSalaToCruz(), salaAlvo, salasGrafoAM);
                                if (CaminhoTemp != null) {
                                    caminho = CaminhoTemp;
                                }
                                caminho.next();
                                Sala tempSala =  caminho.next();
                                System.out.println("To andou para: " + tempSala.getNome());
                                Rounds.moveToCruz(missao, tempSala, true);
                                AtualizeAM();
                            }
                        }
                        //------------------------------------
                        //caso não tenha medKit mas tem o alvo e tem a vida a baixo dos40% decide ir para o que esta mais proximo (saida ou medkit)
                        if (toCruz.getGotAlvo()) {
                            tempWeight2 = salasGrafoAM.shortestWeightWeight(edificioAM.getSalaToCruz(), edificioAM.getClosestExit(true));

                            if (tempWeight1 < tempWeight2) {

                                CaminhoTemp = getCaminhoAM(edificioAM.getSalaToCruz(), salaMedKitAM, salasGrafoAM);
                                if (CaminhoTemp != null) {
                                    caminho = CaminhoTemp;
                                }
                                System.out.println("To andou para: " + proximaSala.getNome());
                                Rounds.moveToCruz(missao, proximaSala, true);
                                AtualizeAM();
                            } else {
                                CaminhoTemp = getCaminhoAM(edificioAM.getSalaToCruz(), saida, salasGrafoAM);
                                if (CaminhoTemp != null) {
                                    caminho = CaminhoTemp;
                                }
                                caminho.next();
                                proximaSala = caminho.next();
                                System.out.println("To andou para: " + proximaSala.getNome());
                                Rounds.moveToCruz(missao, proximaSala, true);
                                AtualizeAM();
                            }
                        }
                        //----------------------------------------

                    }
                    //---------------------------------------------
                    //caso tenha 40% ou menos da vida maxima, não tenha o alvo e tenha um medkit, vai usar
                    else if ((toCruz.getVida() <= toCruz.getMaxLife() * 0.40 && !toCruz.getMochila().isEmpty())) {
                        Rounds.useMedkit(missao, true, false);
                        AtualizeAM();
                    }
                    if (missao.getToCruz().getVida() <= 0) {
                        end = true;
                        break;
                    }
                } catch (IllegalArgumentException e){
                    System.out.println(e.getMessage());
                }
                AtualizeAM();

                if (toCruz.getGotAlvo() && salaToCruzAM.isEntradaSaida()){
                    missao.setSucess(true);
                    end = true;
                    break;
                }
                else  if (salaToCruzAM.equals(salaMedKitAM) && !toCruz.getGotAlvo()) {
                    CaminhoTemp = getCaminhoAM(salaToCruzAM, salaAlvo, salasGrafoAM);
                    if (CaminhoTemp != null) {
                        caminho = CaminhoTemp;
                    }
                } else if (salaToCruzAM.equals(salaMedKitAM) && toCruz.getGotAlvo()){
                    CaminhoTemp = getCaminhoAM(salaToCruzAM, saida, salasGrafoAM);
                    if (CaminhoTemp != null) {
                        caminho = CaminhoTemp;
                    }
                }
                if (toCruz.getGotAlvo()){
                    CaminhoTemp = getCaminhoAM(salaToCruzAM, saida, salasGrafoAM);
                    if (CaminhoTemp != null) {
                        caminho = CaminhoTemp;
                    }
                }

            }

        }
        if (toCruz.getVida() > 0 ){
            System.out.println("To concluiu a missao com sucesso, restando lhe " + toCruz.getVida() + "  pontos de vida");
        }

    }

    public String automaticSimulation() {
        StringBuilder ResultadoDaSimulacao = new StringBuilder();

        edificioAM = missao.getEdificio();
        salasGrafoAM = edificioAM.getSalas();
        LinearLinkedUnorderedList<Sala> EntradasSaidas = edificioAM.getEntradas_saidas();
        Iterator<Sala> CaminhoIda;
        Iterator<Sala> CaminhoVolta;
        Sala salaAlvo = edificioAM.getSalaAlvo();
        salaToCruzAM = EntradasSaidas.first();
        double tempWeight1;
        double tempWeight2;

        if (EntradasSaidas.size() > 1) {
            for (Sala EntExit : EntradasSaidas) {
                tempWeight1 = salasGrafoAM.shortestWeightWeight(EntExit, salaAlvo);
                if (salasGrafoAM.shortestWeightWeight(salaToCruzAM, edificioAM.getSalaAlvo()) > tempWeight1) {
                    salaToCruzAM = EntExit;
                }
            }
        }

        ResultadoDaSimulacao.append("SpawnPoint: ").append(salaToCruzAM.getNome()).append("\n");

        missao.changeSala(salaToCruzAM, salaToCruzAM.setHaveToCruz(true));

        CaminhoIda = salasGrafoAM.iteratorShortestWeight(salaToCruzAM, salaAlvo);
        tempWeight1 = salasGrafoAM.shortestWeightWeight(salaToCruzAM, salaAlvo);
        missao.changeSala(salaToCruzAM, salaToCruzAM.setHaveToCruz(false));
        missao.changeSala(salaAlvo, salaAlvo.setHaveToCruz(true));
        edificioAM = missao.getEdificio();
        CaminhoVolta = salasGrafoAM.iteratorShortestWeight(salaAlvo,  edificioAM.getClosestExit(true));
        tempWeight2 = salasGrafoAM.shortestWeightWeight(salaAlvo,  edificioAM.getClosestExit(true));

        ResultadoDaSimulacao.append("É Possivel concluir a missão com sucesso: ");
        if (missao.getToCruz().getVida() - tempWeight1 - tempWeight2 > 0){
            ResultadoDaSimulacao.append("Sim\n");
        } else {
            ResultadoDaSimulacao.append("Não\n");
        }

        ResultadoDaSimulacao.append("Vida Final do To Cruz: ").append(missao.getToCruz().getVida() - tempWeight1 - tempWeight2).append("\n");

        ResultadoDaSimulacao.append("Caminho Ida: ").append(PrintCaminho(CaminhoIda));

        ResultadoDaSimulacao.append("Caminho Volta: ").append(PrintCaminho(CaminhoVolta));

        return ResultadoDaSimulacao.toString();
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
                    Sala sala = entradasSaidasIterator.next();
                    if (i == op) {

                        missao.changeSala(sala, sala.setHaveToCruz(true));
                        break;
                    }

                }
            }

        }
        //-------------------------------------- fim do setup do spawn point-------------------------------------------
        if (missao.getEdificio().getSalaToCruz().hasInimigos()){
            Cenarios.Confronto(missao, true, false);
        }
        while (toCruz.getVida() > 0 && !end) {
            op = 0;
            System.out.println();
            toCruz = missao.getToCruz();

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
                        this.caminhoMedKit = missao.getEdificio().getCaminhoMedkit(false);
                        this.caminhoAlvo = missao.getEdificio().getCaminhoAlvo(false);
                        this.caminhoSaida = missao.getEdificio().getCaminhoSaida(false);
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
        Sala caminhoMedkitSala = null;
        Sala caminhoAlvoSala = null;
        Sala caminhoSaidaSala= null;
        if (caminhoMedKit!= null && caminhoMedKit.hasNext()){
            caminhoMedKit.next();
            if (caminhoMedKit.hasNext()) {
                caminhoMedkitSala = caminhoMedKit.next();
            }
        }
        if (caminhoAlvo != null && caminhoAlvo.hasNext()) {
            caminhoAlvo.next();
            if (caminhoAlvo.hasNext()) {
                caminhoAlvoSala = caminhoAlvo.next();
            }
        }
        if (caminhoSaida != null && caminhoSaida.hasNext() && missao.getToCruz().getGotAlvo()) {
            caminhoSaida.next();
            if (caminhoSaida.hasNext()) {
                caminhoSaidaSala = caminhoSaida.next();
            }
        }
        op = 0;
        int cnt = printWalkOptions(caminhoMedkitSala, caminhoAlvoSala, caminhoSaidaSala,salas);
        while (op < 1 || op > cnt) {

            op = sc.nextInt();
            if (op < 1 || op > salas.size() + 3){
                System.out.println("Opção inválida");
            }else if (op == (cnt + 1)) {
                return;
            }
            else {
                int opcoesValidas = 1;
                if (caminhoMedkitSala != null){

                    if ( op == opcoesValidas) {
                        Rounds.moveToCruz(missao, caminhoMedkitSala,false);
                    }
                    opcoesValidas++;
                }
                if (caminhoAlvoSala != null) {

                    if (op == opcoesValidas) {
                        Rounds.moveToCruz(missao, caminhoAlvoSala,false);
                        break;
                    }
                    opcoesValidas++;
                }
                if (caminhoSaidaSala != null) {
                    if ( op == opcoesValidas) {
                        Rounds.moveToCruz(missao, caminhoSaidaSala,false);
                        break;
                    }
                    opcoesValidas++;
                }

                Iterator<Sala> salasIt = salas.iterator();
                for (int i = 0; i < salas.size() ; i++) {
                    Sala sala = salasIt.next();
                    if (i + opcoesValidas == op) {

                        if (caminhoMedkitSala != sala){
                            caminhoMedKit = missao.getEdificio().getCaminhoMedkit(false);
                        }
                        if (caminhoAlvoSala != sala){
                            caminhoAlvo = missao.getEdificio().getCaminhoAlvo(false);
                        }
                        if (caminhoSaidaSala != sala && missao.getToCruz().getGotAlvo()){
                            caminhoAlvo = missao.getEdificio().getCaminhoAlvo(false);
                        }
                        Rounds.moveToCruz(missao, sala, false);
                        System.out.println("To cruz moveu se para: " + sala.getNome());
                        break;
                    }

                }

            }
        }
    }

    public String PrintCaminho(Iterator<Sala> caminho){
        StringBuilder caminhoStr = new StringBuilder();
        caminhoStr.append("[  ").append(caminho.next().getNome());
        while (caminho.hasNext()) {
            Sala sala = caminho.next();
            caminhoStr.append(" -> ").append(sala.getNome());
        }
        caminhoStr.append("  ]");

        return caminhoStr.toString();
    }
    @Override
    public void run(Missao missao, int mode) throws IllegalArgumentException {

        this.missao = missao;

        if (mode < 1 || mode > 2 ){
            throw new IllegalArgumentException("Modo inválido, o valor deve ser entre 1 e 2");
        }
        if ( mode== 1) {
            manual();
        }
        else if (mode == 2){
            automatic();
        }
    }

    public String RunAutomaticSimulation(Missao missao) {
        this.missao = missao;
        return automaticSimulation();
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

    public int printWalkOptions(Sala caminhoMedkitSala, Sala caminhoAlvoSala, Sala caminhoSaida, LinearLinkedUnorderedList<Sala> salas){
        int cnt = 1;
        System.out.println("Escolha uma opção:");
        if (caminhoMedkitSala != null) {
            System.out.println(cnt +" - Mover para MedKit mais proximo. ");
            cnt++;
        }
        if (caminhoAlvoSala != null) {
            System.out.println(cnt + " - Mover para sala mais proxima do alvo ");
            cnt++;
        }
        if (caminhoSaida != null){
            System.out.println(cnt + " - Mover para saida mais proxima ");
            cnt++;
        }
        for (Sala sala : salas){
            System.out.println(cnt + " - Mover para: " + sala.getNome());
            cnt++;
        }
        System.out.println((cnt) + " - Voltar ao menu anterior ");

        System.out.print("\nEscolha: ");
        return cnt;
    }

    private void AtualizeAM() {
        toCruz = missao.getToCruz();
        edificioAM = missao.getEdificio();
        salaMedKitAM = edificioAM.getMedKitProx(true);
        salasGrafoAM = edificioAM.getSalas();
        salaToCruzAM = edificioAM.getSalaToCruz();
        if (toCruz.getGotAlvo()) {
            saida = edificioAM.getClosestExit(true);
        }
    }

    private Iterator<Sala> getCaminhoAM(Sala from, Sala to, GraphNetwork<Sala> salasGraph){
        return salasGraph.iteratorShortestWeight(from, to);
    }
}
