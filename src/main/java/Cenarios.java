import Exceptions.EmptyCollectionException;
import GameEngine.Cenario;
import Item.Item;
import LinkedList.LinearLinkedUnorderedList;
import Pessoa.Pessoa;

public class Cenarios implements Cenario {

    LinearLinkedUnorderedList<Pessoa> pessoas;
    LinearLinkedUnorderedList<Item> itens;

    @Override
    public Pessoa addPessoa(Pessoa pessoa) throws NullPointerException {
        if (pessoa == null) {
            throw new NullPointerException("Pessoa is null");
        }
        pessoas.addToRear(pessoa);
        return pessoa;
    }

    @Override
    public Pessoa removePessoa(Pessoa pessoa) throws EmptyCollectionException {
        if(pessoas.isEmpty()) {
            throw new EmptyCollectionException("Não existem pessoas");
        }
        pessoas.remove(pessoa);
        return pessoa;
    }

    @Override
    public Item addItem(Item item) throws NullPointerException {
        if (item == null) {
            throw new NullPointerException("Item is null");
        }
        itens.addToRear(item);
        return item;
    }

    @Override
    public Item removeItem(Item item) throws EmptyCollectionException {
        if(itens.isEmpty()) {
            throw new EmptyCollectionException("Não existem itens");
        }
        itens.remove(item);
        return item;
    }

    @Override
    public void run() {
        Rounds round_ToCruz = new Rounds(pessoas.);
        Rounds round_Inimigo = new Rounds(pessoas.);

    }
}
