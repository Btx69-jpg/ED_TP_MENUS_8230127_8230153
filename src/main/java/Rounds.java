import Exceptions.EmptyCollectionException;
import GameEngine.Round;
import Pessoa.Pessoa;
import Pessoa.Inimigo;
import Pessoa.ToCruz;

public class Rounds implements Round {

    private Pessoa pessoa;
    private boolean toCruz;

    public Rounds(Pessoa pessoa) {
        this.pessoa = pessoa;
        if(pessoa instanceof ToCruz){
            toCruz = true;
        }else {
            toCruz = false;
        }

    }

    @Override
    public void move() {
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
    public void attack() {

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
