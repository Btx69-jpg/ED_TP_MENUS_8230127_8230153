import Data.Json;
import Edificio.Edificio;
import GameEngine.GameMode;
import Pessoa.ToCruz;
import Missao.Missao;
import Edificio.Sala;

import java.util.Iterator;
import java.util.Scanner;

public class GamesMode implements GameMode {
    Missao missao;
    private ToCruz toCruz;
    private boolean end;
    @Override
    public void automatic() {
        //todas as decisões são tomadas automaticamente
        //iterator the shortestpath
    }

    @Override
    public void manual() {
        int op = 0;
        Scanner sc = new Scanner(System.in);

        //setUp do spawn point
        int entradasSaidas = missao.getEdificio().getNumeroEntradas_saidas();
        while (op < 1 || op > entradasSaidas + 1) {
            System.out.println("Escolha o spawn point:");
            Iterator<Sala> entradasSaidasIterator = missao.getEdificio().getEntradas_saidas().iterator();
            for (int i = 0 ; i < entradasSaidas; i++) {
                System.out.println(i + " - " + entradasSaidasIterator.next());
            }
            System.out.println((entradasSaidas + 1) +" - Sair");
            op = sc.nextInt();
            if (op < 1 || op > entradasSaidas + 1){
                System.out.println("Opção inválida");
            }else if (op == (entradasSaidas + 1)) {
                end = true;
                break;
            }
            else {
                entradasSaidasIterator = missao.getEdificio().getSalas().getVerticesIterator();
                for (int i = 0; i < entradasSaidas; i++) {
                    if (i == op) {
                        Sala sala = entradasSaidasIterator.next();
                        sala.setHaveToCruz(true);
                        break;
                    }
                    entradasSaidasIterator.next();
                }
            }

        }
        //-------------------------------------- fim do setup do spawn point-------------------------------------------

        while (toCruz.getVida() > 0 && !end) {


            while (op < 1 || op > 8) {
                System.out.println("Escolha uma opção:");
                System.out.println("1 - Mover");
                System.out.println("2 - Usar MedKit");
                System.out.println("3 - Atacar");
                System.out.println("4 - Verificar Vida");
                System.out.println("5 - Verificar Mochila");
                System.out.println("6 - Verificar Alvo");
                System.out.println("7 - Verificar Edificio");
                System.out.println("8 - Sair");
                op = sc.nextInt();
                switch (op) {
                    case 1:
                        //mover
                        break;
                    case 2:
                        //usar medkit
                        break;
                    case 3:
                        //atacar
                        break;
                    case 4:
                        //verificar vida
                        break;
                    case 5:
                        //verificar mochila
                        break;
                    case 6:
                        //verificar alvo
                        break;
                    case 7:
                        //verificar edificio
                        break;
                    case 8:
                        //sair
                        break;
                    default:
                        System.out.println("Opção inválida");
                }
            }
        }
        //aparecer um menu para deixar o utilizador escolher o que fazer
    }

    public void moveEnimies() {

    }

    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Insira o Cami:");
        this.missao = Json.ReadJson("C:\\Users\\Gonçalo\\Documents\\GitHub\\ED_TP_8230127_8230153\\ED_TP_MENUS_8230127_8230153\\src\\main\\resources\\teste.json");
        manual();
    }
}
