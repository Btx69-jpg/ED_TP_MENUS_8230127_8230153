import GameEngine.Round;
import Pessoa.Pessoa;
import Pessoa.Inimigo;
import Pessoa.ToCruz;

public class Rounds implements Round {

    private Pessoa pessoa;

    public Rounds(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    @Override
    public void move() {

    }

    @Override
    public void attack() {

    }

    @Override
    public void useItem() throws IllegalArgumentException{
        if(pessoa instanceof Inimigo){
            throw new IllegalArgumentException("Inimigo não pode usar item");
        }
        
    }
}
