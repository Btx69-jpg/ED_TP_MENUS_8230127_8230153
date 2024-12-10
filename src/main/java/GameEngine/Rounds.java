package GameEngine;

import Edificio.Edificio;
import Edificio.Sala;
import Pessoa.*;
import Missao.Missao;
import Item.Item;

import java.util.Iterator;

public class Rounds implements Round {
    public static void moveToCruz(Missao missao, Sala to, boolean autoMode) {
        Edificio edificio = missao.getEdificio();
        Iterator<Sala> iterator = edificio.getSalas().iteratorBFS(to);
        Sala sala = iterator.next();
        //Movimentação do to cruz feita.
        while (iterator.hasNext()){
            sala = iterator.next();
            if(sala.haveToCruz()){
                sala.setHaveToCruz(false);
                return;
            }
        }
        //confirmar que não envio uma copia da sala mas sim ela propriamente dita
        to.setHaveToCruz(true);
        if (to.hasInimigos()){
            Cenarios.Confronto(missao, to.getInimigos(), true, autoMode);
        } else {
            Cenarios.walkEnimies(missao, autoMode,false );
        }



    }

    public static void attack(Pessoa atacante, Pessoa atacado) throws IllegalArgumentException{
        if(atacante.getVida() <= 0 && atacado.getVida() <= 0){
            throw new IllegalArgumentException("Ambos estão mortos, não podem atacar");
        }
        if (atacante.getVida() <= 0){
            throw new IllegalArgumentException("O atacante não pode atacar, pois está morto");
        }
        if (atacado.getVida() <= 0){
            throw new IllegalArgumentException("O alvo não pode ser atacado, pois está morto");
        }

        atacado.setVida(atacado.getVida() - atacante.getPoder());
    }

    public static void useMedkit(ToCruz toCruz, Missao missao, boolean autoMode, boolean wasInConfronto) {
        Item kit = toCruz.usarMedKit();
        System.out.println("ToCruz usou um medkit, Curou: " + kit.getQuantidade());
        Cenarios.walkEnimies(missao, autoMode, wasInConfronto);
    }

}
