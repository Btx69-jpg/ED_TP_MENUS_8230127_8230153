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

/**
 * Classe que gerencia os rounds do jogo.
 */
public class Rounds implements Round {

    /**
     * Move o personagem ToCruz para uma sala específica, caso haja inimigos na sala, inicia se o confronto.
     * Apos a movimentação do to cruz movem se todos os inimigos.
     * caso seja o modo automatico e toCruz entre na sala que tem o alvo, To Cruz recolhe o alvo
     *
     * @param missao a missão atual
     * @param to a sala de destino
     * @param autoMode indica se o modo automático está ativado
     */
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

    /**
     * Realiza um ataque entre duas pessoas (To cruz e um inimigo habitualmente).
     *
     * @param atacante a pessoa que ataca
     * @param atacado a pessoa que é atacada
     * @throws IllegalArgumentException se ambos estiverem mortos ou se algum deles estiver morto
     */
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

    /**
     * Usa um kit médico para curar o personagem ToCruz e anda todos os inimigos.
     *
     * @param missao a missão atual
     * @param autoMode indica se o modo automático está ativado
     * @param wasInConfronto indica se estava em confronto
     */
    public static void useMedkit(Missao missao, boolean autoMode, boolean wasInConfronto) {
        try {
            ToCruz toCruz = missao.getToCruz();
            Item kit = toCruz.usarMedKit();
            System.out.println("ToCruz usou um medkit, Curou: " + kit.getQuantidade());
            missao.changeToCruz(toCruz);
            Cenarios.walkEnimies(missao, autoMode, wasInConfronto);
        }catch (EmptyCollectionException | NullPointerException  | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

}
