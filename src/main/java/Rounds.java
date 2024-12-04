import Exceptions.EmptyCollectionException;
import GameEngine.Round;
import Pessoa.Pessoa;
import Pessoa.Inimigo;
import Pessoa.ToCruz;

public class Rounds implements Round {

    public Rounds() {
    }

    @Override
    public void move(Pessoa pessoa) {
        if(toCruz){
            //só pode fazer dois movimentos, andar para a frente ou andar para tras
        }else{
            /*
            Tem 5 opções de movimento, duas para tras, uma para tras
            ficar no mesmo sitio, andar uma para a frente, andar duas para a frente
         */
        }
    }

    @Override
    public void attack(Pessoa atacante, Pessoa atacado) {
        if (atacante.getVida() == 0){
            throw new IllegalArgumentException("O atacante não pode atacar, pois está morto");
        }
        if (atacado.getVida() == 0){
            throw new IllegalArgumentException("O atacado não pode ser atacado, pois está morto");
        }
        if(atacante.getVida() == 0 && atacado.getVida() == 0){
            throw new IllegalArgumentException("Ambos estão mortos, não podem atacar");
        }
        if(atacante.getVida() > 0 && atacado.getVida() > 0){
            atacado.setVida() -= atacante.getPoder();
        }

    }

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
}
