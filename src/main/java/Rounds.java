import Edificio.Edificio;
import Edificio.Sala;
import Exceptions.EmptyCollectionException;
import GameEngine.Round;
import Pessoa.Pessoa;
import Pessoa.Inimigo;
import Pessoa.ToCruz;

import java.util.Iterator;

public class Rounds implements Round {
    public static void move(Pessoa pessoa, Sala to, Edificio edificio) {
        Iterator<Sala> iterator = edificio.getSalas().iteratorBFS(to);
        Sala sala = iterator.next();
        //Movimentação do to cruz feita.
        if(pessoa instanceof ToCruz){
            sala.setHaveToCruz(true);
            while (iterator.hasNext()){
                sala = iterator.next();
                if(sala.haveToCruz()){
                    sala.setHaveToCruz(false);
                    return;
                }
            }
        }
        //Movimentação de inimigos feita.
        else{
            edificio.removeInimigo((Inimigo) pessoa);
            edificio.addInimigo((Inimigo) pessoa, sala);

        }
    }

    public static void attack(Pessoa atacante, Pessoa atacado) {
        if(atacante.getVida() <= 0 && atacado.getVida() <= 0){
            throw new IllegalArgumentException("Ambos estão mortos, não podem atacar");
        }
        if (atacante.getVida() <= 0){
            throw new IllegalArgumentException("O atacante não pode atacar, pois está morto");
        }
        if (atacado.getVida() <= 0){
            throw new IllegalArgumentException("O atacado não pode ser atacado, pois está morto");
        }

        atacado.setVida(atacado.getVida() - atacante.getPoder());
    }

    /*
    @Override
    public void attack(Sala sala) throws EmptyCollectionException {
        if (sala.getInimigos().lenght() == 0){
            throw new EmptyCollectionException("A sala não tem inimigos para atacar");
        }
        ToCruz toCRUZ = (ToCruz) pessoa;//está errado, pois pessoa pode ser inimigo
        Inimigo[] inimigos = new Inimigo[sala.getInimigos().lenght()];
        for (i = 0; i < sala.getInimigos().lenght(); i++) {
            inimigos[i] = sala.getInimigos()[i];
        }
        if(toCruz){
            for(Inimigo inimigo : inimigos){
                inimigo.getVida() -= toCRUZ.getPoder();
            }
        }else{
            for(Inimigo inimigo : inimigos){
                toCRUZ.getVida() -= inimigo.getPoder();
            }
        }
    }

    @Override
    public void useItem() throws IllegalArgumentException{
        if(!toCruz){
            throw new IllegalArgumentException("Inimigo não pode usar item");
        }
        ToCruz toCRUZ = (ToCruz) pessoa;
        toCRUZ.usarMedkit();
    }

     */
}
