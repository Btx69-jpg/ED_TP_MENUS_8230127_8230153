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
            while (iterator.hasNext()){
                sala = iterator.next();
                if(sala.haveToCruz()){
                    sala.setHaveToCruz(false);
                    return;
                }
            }
            //confirmar que não envio uma copia da sala mas sim ela propriamente dita
            to.setHaveToCruz(true);
        }
        //Movimentação de inimigos feita.
        else{
            edificio.removeInimigo((Inimigo) pessoa);
            edificio.addInimigo((Inimigo) pessoa, sala);
        }
    }

    public static void useMedKit(Pessoa pessoa){
        if (pessoa instanceof ToCruz){
            ((ToCruz) pessoa).usarMedKit();
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

}
