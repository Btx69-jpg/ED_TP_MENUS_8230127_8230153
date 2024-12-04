import Exceptions.EmptyCollectionException;
import GameEngine.Cenario;
import Item.Item;
import LinkedList.LinearLinkedUnorderedList;
import Pessoa.*;

import java.util.Iterator;

public class Cenarios implements Cenario {

    public static boolean Confronto(ToCruz toCruz, LinearLinkedUnorderedList<Inimigo> p2) {
        Iterator<Inimigo> inimigosIterator = p2.iterator();
        Inimigo inimigo = inimigosIterator.next();
        System.out.print("Confronto entre " + toCruz.getNome() + " e " + inimigo);
        while (inimigosIterator.hasNext()) {
            inimigo = inimigosIterator.next();
            System.out.print( ", " + inimigo );
        }
        System.out.println();

        System.out.println("Início do confronto");
        while (toCruz.getVida() > 0 && p2. > 0) {
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
