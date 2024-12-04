import Edificio.Edificio;
import GameEngine.GameMode;
import Pessoa.ToCruz;
import Missao.Missao;

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
        while (toCruz.getVida() > 0 && !end) {
            while (op < 1 || op > 8) {
                System.out.println("Escolha o spawn point:");
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

    @Override
    public void run() {

    }
}
