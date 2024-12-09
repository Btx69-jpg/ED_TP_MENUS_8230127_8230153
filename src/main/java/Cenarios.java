import Edificio.Edificio;
import Exceptions.EmptyCollectionException;
import GameEngine.Cenario;
import Heaps.PriorityHeap;
import Item.Item;
import LinkedList.LinearLinkedUnorderedList;
import Pessoa.*;
import Edificio.Sala;

import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

public abstract class Cenarios implements Cenario {

    /**
     * @return true se o jogador venceu o cenário, false caso contrário
     */
    //LEMBRAR DE REMOVER Os INIMIGOs DO EDIFICIO NO FINAL DO CONFRONTO CASO SEJA BEM SUCEDIDO ou adicionar verificaçõoes para ver se o inimigo é valido antes de iniciar o confronto(se tem vida)
    //falta adicionar o metodo para todos os outros andarem caso estes não morram
    public static boolean Confronto(ToCruz toCruz, LinearLinkedUnorderedList<Inimigo> p2, boolean TocruzStart, boolean autoMode, Edificio edificio) {
//      PRINT DOS ENVOLVIDOS NO CONFRONTO
        toCruz.setInConfronto(true);
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
                            try {
                                Rounds.attack(toCruz, inimigo);
                            } catch (IllegalArgumentException e) {
                                System.out.println(e.getMessage());
                                return false;
                            }
                            if (inimigo.getVida() <= 0) {
                                System.out.println(inimigo.getNome() + " foi derrotado");
                                p2.remove(inimigo);
                            }
                            System.out.println("Vida do  atual inimigo " + inimigo.getNome() + ": " + inimigo.getVida());
                        }

                    } else if (op == 2) {
                        try {
                            Item medkit = toCruz.usarMedKit();
                            System.out.println("Curou " + medkit.getQuantidade() + "pontos de vida");
                        } catch (EmptyCollectionException e) {
                            System.out.println("Não há medkits disponíveis");
                        } catch (IllegalArgumentException e) {
                            System.out.println("To Cruz não pode usar medkit, pois tem a vida cheia");
                        }

                    } else {
                        System.out.println("Opção inválida");
                    }
                }
                TocruzStart = false;

            }

            if (TocruzStart) {

                System.out.println("Tocruz ataca. ");

                //
                if (toCruz.getVida() > toCruz.getVida() * 0.35) {
                    //ataca todos os inimigos na sala       LEMBRAR DE REMOVER Os INIMIGOs DO EDIFICIO NO FINAL DO CONFRONTO CASO SEJA BEM SUCEDIDO
                    inimigosIterator = p2.iterator();
                    while (inimigosIterator.hasNext()) {
                        inimigo = inimigosIterator.next();
                        Rounds.attack(toCruz, inimigo);
                        if (inimigo.getVida() <= 0) {
                            System.out.println(inimigo.getNome() + " foi derrotado");
                            p2.remove(inimigo);
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




            //ataque do(s) inimigo(s)       LEMBRAR DE FAZER TODOS OS OUTROS INIMIGOS ANDAREM CASO ESSES NÃO MORRAM
            if (!p2.isEmpty()) {
                System.out.println("Inimigos atacam. ");
                inimigosIterator = p2.iterator();
                while (inimigosIterator.hasNext()) {
                    inimigo = inimigosIterator.next();
                    Rounds.attack(inimigo, toCruz);
                    if (toCruz.getVida() <= 0) {
                        System.out.println("To Cruz foi derrotado foi derrotado");
                        return false;
                    }
                    System.out.println("Vida do  atual To Cruz " + ": " + toCruz.getVida());
                }
                TocruzStart = true;
                walkEnimies(edificio);
            }

        }
        System.out.println("Fim do confronto");
        toCruz.setInConfronto(false);
        return true;
    }


    //CONTINUAR DAQUI VER A ALEATORIEDADE DA POSIÇÃO DOS INIMIGOS
    public static void walkEnimies(Edificio edificio) throws EmptyCollectionException {
        LinearLinkedUnorderedList<Inimigo> inimigos = edificio.getAllInimigos();
        LinearLinkedUnorderedList<Sala> salasComInimigos = edificio.getSalaComInimigos();
        LinearLinkedUnorderedList<Sala> salasConnectadas;
        PriorityHeap<Sala> PossiveisSalas = new PriorityHeap<>();

        Random random = new Random();
        int cnt = 1;
        if (inimigos.isEmpty()){
            throw new EmptyCollectionException("Não há inimigos no edifício");
        }
        for( Sala sala : salasComInimigos){
            inimigos = sala.getInimigos();
            salasConnectadas = edificio.getSalas().getConnectedVertices(sala);
            PossiveisSalas.addElement(sala, cnt);
            for (Sala salaConnectada : salasConnectadas){
                cnt++;
                PossiveisSalas.addElement(salaConnectada, cnt);

            }
            for (Inimigo inimigo : inimigos) {
                sala = PossiveisSalas.FindELPriority(random.nextInt(cnt)+ 1);
                Rounds.move(inimigo,  sala, edificio);
            }
        }
    }


}
