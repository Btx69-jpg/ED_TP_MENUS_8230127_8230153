package GameEngine;

import Edificio.Edificio;
import Edificio.Sala;
import Exceptions.EmptyCollectionException;
import Item.Item;
import Missao.Alvo;
import Missao.Missao;
import Pessoa.Pessoa;
import Pessoa.ToCruz;

import java.util.Iterator;

public class Rounds implements Round {
    public static void moveToCruz(Missao missao, Sala to, boolean autoMode) {

        Edificio edificio = missao.getEdificio();
        Iterator<Sala> iterator = edificio.getSalas().iteratorBFS(to);
        Sala sala = edificio.getSalaToCruz();

        if (sala != null){
            missao.changeSala(sala, sala.setHaveToCruz(false));
        }

        missao.changeSala(to, to.setHaveToCruz(true));
        missao.addSalaCaminhoTo(to);

        if (to.hasInimigos()){
            Cenarios.Confronto(missao, true, autoMode);
        } else {
            Cenarios.walkEnimies(missao, autoMode,false );
        }
        if (to.haveAlvo() && autoMode){
            ToCruz toCruz = missao.getToCruz();
            toCruz.setGotAlvo(true);
            to.setAlvo(false);
            missao.changeToCruz(toCruz);
            missao.changeSala(to, to.setAlvo(false));
            missao.changeAlvo(new Alvo(new Sala("ToCruz", true, false), missao.getAlvo().getTipo()));

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

    public static void useMedkit(Missao missao, boolean autoMode, boolean wasInConfronto) {
            ToCruz toCruz = missao.getToCruz();
            Item kit = toCruz.usarMedKit();
            System.out.println("ToCruz usou um medkit, Curou: " + kit.getQuantidade());
            missao.changeToCruz(toCruz);
            Cenarios.walkEnimies(missao, autoMode, wasInConfronto);
    }

}
