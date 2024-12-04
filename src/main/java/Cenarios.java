import Exceptions.EmptyCollectionException;
import GameEngine.Cenario;
import Item.Item;
import LinkedList.LinearLinkedUnorderedList;
import Pessoa.*;

public class Cenarios implements Cenario {

    public static void Confronto(ToCruz p1, Inimigo[] p2) {
        System.out.print("Confronto entre " + p1.getNome() + " e ");
        for (int i = 0; i < p2.length; i++) {
            System.out.print( p2[i].getNome());
        }
        System.out.println("Confronto entre " + p1.getNome() + " e " + p2.getNome());
        System.out.println("Vida inicial de " + p1.getNome() + ": " + p1.getVida());
        System.out.println("Vida inicial de " + p2.getNome() + ": " + p2.getVida());
        System.out.println("Poder de " + p1.getNome() + ": " + p1.getPoder());
        System.out.println("Poder de " + p2.getNome() + ": " + p2.getPoder());
        System.out.println("Início do confronto");
        while (p1.getVida() > 0 && p2.getVida() > 0) {
            p1.setVida(p1.getVida() - p2.getPoder());
            p2.setVida(p2.getVida() - p1.getPoder());
            System.out.println(p1.getNome() + " atacou " + p2.getNome() + " com poder " + p1.getPoder());
            System.out.println(p2.getNome() + " atacou " + p1.getNome() + " com poder " + p2.getPoder());
            System.out.println("Vida de " + p1.getNome() + ": " + p1.getVida());
            System.out.println("Vida de " + p2.getNome() + ": " + p2.getVida());
        }
        if (p1.getVida() <= 0) {
            System.out.println(p1.getNome() + " foi derrotado");
        } else {
            System.out.println(p2.getNome() + " foi derrotado");
        }
    }
}
