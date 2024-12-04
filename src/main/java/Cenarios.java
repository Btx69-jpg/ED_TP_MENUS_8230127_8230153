import Exceptions.EmptyCollectionException;
import GameEngine.Cenario;
import Item.Item;
import LinkedList.LinearLinkedUnorderedList;
import Pessoa.*;

import java.util.Iterator;
import java.util.Scanner;

public class Cenarios implements Cenario {

    /**
     * @return true se o jogador venceu o cenário, false caso contrário
     */
    public static boolean Confronto(ToCruz toCruz, LinearLinkedUnorderedList<Inimigo> p2, boolean TocruzStart, boolean autoMode) {
        Iterator<Inimigo> inimigosIterator = p2.iterator();
        Inimigo inimigo = inimigosIterator.next();
        System.out.print("Confronto entre " + toCruz.getNome() + " e " + inimigo);
        while (inimigosIterator.hasNext()) {
            inimigo = inimigosIterator.next();
            System.out.print( ", " + inimigo );
        }
        System.out.println();

        System.out.println("Início do confronto");
        while (toCruz.getVida() > 0 && !p2.isEmpty()) {
            if (TocruzStart && !autoMode) {
                Scanner sc = new Scanner(System.in);
                int op = 0;
                System.out.println("Tocruz Tem Prioridade de ataque. ");
                while ( op < 1 || op > 2){
                    System.out.println("Ação: \n1 - Atacar\n2 - Usar MedKit");
                    op = sc.nextInt();
                    if (op == 1) {
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


                    } else if (op == 2) {
                        toCruz.usarMedKit();
                    } else {
                        System.out.println("Opção inválida");
                    }
                }

            }
            toCruz.setVida(toCruz.getVida() - p2.getPoder());
            p2.setVida(p2.getVida() - toCruz.getPoder());
            System.out.println(toCruz.getNome() + " atacou " + p2.getNome() + " com poder " + toCruz.getPoder());
            System.out.println(p2.getNome() + " atacou " + toCruz.getNome() + " com poder " + p2.getPoder());
            System.out.println("Vida de " + toCruz.getNome() + ": " + toCruz.getVida());
            System.out.println("Vida de " + p2.getNome() + ": " + p2.getVida());
        }
        if (toCruz.getVida() <= 0) {
            System.out.println(toCruz.getNome() + " foi derrotado");
        } else {
            System.out.println(p2.getNome() + " foi derrotado");
        }
    }
}
