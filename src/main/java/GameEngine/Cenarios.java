package GameEngine;

import BinaryTree.AVLPriorityTree;
import Edificio.Edificio;
import Edificio.Sala;
import Exceptions.ElementNotFoundException;
import Exceptions.EmptyCollectionException;
import Item.Item;
import LinkedList.LinearLinkedUnorderedList;
import Missao.Missao;
import Pessoa.Inimigo;
import Pessoa.ToCruz;

import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

public abstract class Cenarios implements Cenario {

    /**
     * @return true se o jogador venceu o cenário, false caso contrário
     */
    public static void Confronto(Missao missao, boolean TocruzStart, boolean autoMode) throws IllegalArgumentException{
//      PRINT DOS ENVOLVIDOS NO CONFRONTO
        ToCruz toCruz = missao.getToCruz();
        Edificio edificio = missao.getEdificio();
        toCruz.setInConfronto(true);
        missao.changeToCruz(toCruz);
        LinearLinkedUnorderedList<Inimigo> p2 = edificio.getSalaToCruz().getInimigos();
        LinearLinkedUnorderedList<Inimigo> temKilledEn = new LinearLinkedUnorderedList<>();

        if (p2 == null){
            throw new NullPointerException("Não há inimigos na sala");
        }

        Iterator<Inimigo> inimigosIterator = p2.iterator();
        Inimigo inimigo = inimigosIterator.next();

        System.out.print("Confronto entre " + toCruz.getNome() + " e " + inimigo);
        while (inimigosIterator.hasNext()) {
            inimigo.setInConfronto(true);
            inimigo = inimigosIterator.next();
            System.out.print( ", " + inimigo );
        }
        System.out.println();
//-------------------------------------------

        System.out.println("Início do confronto");
        while (toCruz.getVida() > 0 && !p2.isEmpty()) {
            //Confronto manual ------------------------------
            if (TocruzStart && !autoMode) {
                Scanner sc = new Scanner(System.in);
                int op = 0;
                System.out.println("Round do Tó Cruz. ");
                while ( op < 1 || op > 2){
                    System.out.println("Ação: \n1 - Atacar\n2 - Usar MedKit");
                    op = sc.nextInt();
                    if (op == 1) {
                        //ataca todos os inimigos na sala
                        inimigosIterator = p2.iterator();
                        while (inimigosIterator.hasNext()) {
                            inimigo = inimigosIterator.next();
                            if (inimigo != null){
                                try {
                                    Rounds.attack(toCruz, inimigo);
                                } catch (IllegalArgumentException e) {
                                    System.out.println(e.getMessage());
                                }
                                if (inimigo.getVida() <= 0) {
                                    System.out.println(inimigo.getNome() + " foi derrotado");
                                    edificio.removeInimigo(inimigo);
                                    temKilledEn.addToRear(inimigo);
                                }
                                System.out.println("Vida do  atual inimigo " + inimigo.getNome() + ": " + inimigo.getVida());
                            }

                        }

                    } else if (op == 2) {
                        try {
                            Item medkit = toCruz.usarMedKit();
                            System.out.println("Curou " + medkit.getQuantidade() + "pontos de vida");
                        } catch (EmptyCollectionException e) {
                            System.out.println("Não há medkits disponíveis");
                        } catch (IllegalArgumentException e) {
                            System.out.println("To Cruz não pode usar medkit, pois tem a vida cheia");
                        }catch (NullPointerException e) {
                            System.out.println(e.getMessage());
                        }

                    } else {
                        System.out.println("Opção inválida");
                    }
                }
                TocruzStart = false;
            }
//---------------------------------------------------------------------------------
            //confronto automatico
            if (TocruzStart) {

                System.out.println("Tocruz ataca. ");
                inimigosIterator = p2.iterator();
                int totalDmg = 0;
                while (inimigosIterator.hasNext()) {
                    inimigo = inimigosIterator.next();
                    totalDmg += inimigo.getPoder();
                }

                if (toCruz.getMochila().isEmpty() || (toCruz.getVida() > toCruz.getMaxLife() * 0.35 && toCruz.getVida() > totalDmg )) {
                    //ataca todos os inimigos na sala
                    inimigosIterator = p2.iterator();
                    while (inimigosIterator.hasNext()) {
                        inimigo = inimigosIterator.next();
                        Rounds.attack(toCruz, inimigo);
                        if (inimigo.getVida() <= 0) {
                            System.out.println(inimigo.getNome() + " foi derrotado");
                            edificio.removeInimigo(inimigo);
                            temKilledEn.addToRear(inimigo);
                        }
                        System.out.println("Vida do  atual inimigo " + inimigo.getNome() + ": " + inimigo.getVida());
                    }

                } else {
                    try {
                        Item medkit = toCruz.usarMedKit();
                        System.out.println("Curou " + medkit.getQuantidade() + "pontos de vida");
                    } catch (EmptyCollectionException e) {
                        System.out.println("Não há medkits disponíveis");
                    } catch (IllegalArgumentException e) {
                        System.out.println("To Cruz não pode usar medkit, pois tem a vida cheia");
                    }
                }
                TocruzStart = false;
            }
            //Fim do ataque do TO no modo automatico---------------------------------------------------------------------------------

            if (!temKilledEn.isEmpty()){
                for (Inimigo desdEnimy : temKilledEn){
                    p2.remove(desdEnimy);
                }
                temKilledEn = new LinearLinkedUnorderedList<>();
            }

            //ataque do(s) inimigo(s)    -----------------------------------
            if (!p2.isEmpty()) {
                walkEnimies(missao, autoMode, true);
                System.out.println("Inimigos atacam. ");
                inimigosIterator = p2.iterator();
                while (inimigosIterator.hasNext()) {
                    inimigo = inimigosIterator.next();
                    Rounds.attack(inimigo, toCruz);
                    if (toCruz.getVida() <= 0) {
                        System.out.println("To Cruz foi derrotado foi derrotado");
                        missao.changeToCruz(toCruz);
                        missao.setSucess(false);
                    }
                    System.out.println("Vida do  atual To Cruz " + ": " + toCruz.getVida());
                }
                TocruzStart = true;
            }
            //fim do ataque do inimigo -----------------------

        }
        System.out.println("Fim do confronto!");
        toCruz.setInConfronto(false);
        missao.changeToCruz(toCruz);
        missao.changeEdificio(edificio);
        if (toCruz.getVida() <= 0){
            throw new IllegalArgumentException("\nTó Cruz foi derrotado!");
        }
        if (toCruz.getVida() >= 0){
            System.out.println("\nTó Cruz venceu o confronto!");
        }

    }

    public static void walkEnimies(Missao missao, boolean autoMode, boolean wasInConfronto) {
        Edificio edificio = missao.getEdificio();
        LinearLinkedUnorderedList<Inimigo> inimigos = edificio.getAllInimigos();
        LinearLinkedUnorderedList<Sala> salasComInimigos = edificio.getSalaComInimigos();
        LinearLinkedUnorderedList<Sala> salasConnectadas;
        AVLPriorityTree<Sala> PossiveisSalas = new AVLPriorityTree<>();

        Random random = new Random();

        if (inimigos.isEmpty()){
            return;
        }
        for( Sala sala : salasComInimigos){
            int cnt = 1;

            //impedir de movimentar os inimigos que estão em confronto
            if (sala.hasInimigos() && sala.haveToCruz()){
                continue;
            }
            inimigos = sala.getInimigos();
            salasConnectadas= edificio.getSalas().getConnectedVertices(sala);

            PossiveisSalas.addElement(sala, cnt);
            for (Sala salaConnectada : salasConnectadas){
                //para impedir inimigos de entrarem na sala em que esta a haver um confronto
                if (salaConnectada.hasInimigos() && salaConnectada.haveToCruz()){
                    continue;
                }
                cnt++;
                PossiveisSalas.addElement(salaConnectada, cnt);
            }

            for (Inimigo inimigo : inimigos) {
                try {
                    sala = PossiveisSalas.FindELPriority(random.nextInt(cnt) + 1);
                    edificio.addInimigo(inimigo, sala);
                }catch (ElementNotFoundException | EmptyCollectionException _){

                }
            }
        }
        missao.changeEdificio(edificio);
        Sala checkConfronto = edificio.getSalaToCruz();
        if (checkConfronto.haveToCruz() && checkConfronto.hasInimigos() && !wasInConfronto){
            Cenarios.Confronto(missao, false, autoMode);
        }
    }

}