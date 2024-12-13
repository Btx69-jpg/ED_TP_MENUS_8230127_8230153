import BinaryTree.AVLPriorityTree;
import Data.DataTreating;
import Edificio.Edificio;
import Edificio.Sala;
import Exceptions.ElementNotFoundException;
import Exceptions.EmptyCollectionException;
import GameEngine.Cenarios;
import Enum.ItemType;
import Item.Item;
import LinkedList.LinearLinkedOrderedList;
import LinkedList.LinearLinkedUnorderedList;
import Missao.Missao;
import Pessoa.Inimigo;
import Pessoa.Pessoa;
import Pessoa.ToCruz;
import Missao.Alvo;
import Missao.Relatorio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;
import java.util.Random;

public class Console {

    private static int PODER_MODE_EASY = 25;
    private static int PODER_MODE_MEDIO = 20;
    private static int PODER_MODE_HARD = 15;
    private static int SIZE_MOCHILA_FACIL = 15;
    private static int SIZE_MOCHILA_MEDIO = 10;
    private static int SIZE_MOCHILA_DIFICIL = 5;

    private JTextArea bemVindoAoMissionTextArea;
    private JButton IniciarButton;
    private JButton fecharButton;
    private JPanel EcraInicial;
    private JPanel Jogo;
    private JPanel NivelDificuldadePanel;
    private JButton facilButton;
    private JButton dificilButton;
    private JButton medioButton;
    private JTextArea Dificuldadetxt;
    private JPanel DificuldadeFacilPanel;
    private JLabel labelImagemFacil;
    private JList ToCruzStatus;
    private JButton PlayEasyModeButton;
    private JPanel SpawnSelecter;
    private JPanel LoadingScreen;
    private JPanel DificuldadeMediaPanel;
    private JButton PlayMedioModeButton;
    private JLabel labelImagemMedio;
    private JPanel LoadingGame;
    private JPanel DificuldadeDificilPanel;
    private JLabel labelImagemDificil;
    private JButton PlayHardModeButton;
    private JList<String> SpawnList;
    private JButton AvancarJogo;
    private JList<String> TurnoUtilizador;
    private JButton TurnoUtillizadorButton;
    private JButton sairJogo;
    private JPanel JogoMapa;
    private JLabel RoundCnt;
    private JLabel SelectSpawnPoint;
    private JButton ModoManual;
    private JButton ModoAutomatico;
    private JPanel ModoDeJogo;
    private JPanel Grafo;
    private JTextArea legenda;
    private JButton inserirMissaoButton;
    private JButton relatoriosButton;
    private JPanel VerRelatorios;
    private JButton voltarButton;
    private JList relatorios;


    private Missao missao;
    private int roundsCount = 1;
    private GrafoRenderer grafoRenderer;

    public Console() {

        voltarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(VerRelatorios);
                frame.setContentPane(EcraInicial);
                frame.revalidate();
                frame.repaint();
            }
        });

        relatoriosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(EcraInicial);
                frame.setContentPane(VerRelatorios);
                getRelatorios();
                frame.revalidate();
                frame.repaint();
            }
        });

        inserirMissaoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lerJson();
            }
        });

        Grafo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                carregarSalas(e);
            }
        });

        AvancarJogo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedSpawnPoint = SpawnList.getSelectedValue();
                if(selectedSpawnPoint == null){
                    JOptionPane.showMessageDialog(SpawnSelecter, "Por favor, selecione um ponto de spawn!");
                }else {
                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(SpawnSelecter);
                    frame.setContentPane(NivelDificuldadePanel);
                    Iterator<Sala> itSala = missao.getEdificio().getEntradas_saidas().iterator();
                    while (itSala.hasNext()){
                        Sala sala = itSala.next();
                        if (sala.getNome().equals(selectedSpawnPoint)){
                            missao.changeSala(sala, sala.setHaveToCruz(true));
                            break;
                        }
                    }
                    opcoesTurnoUtilizador(missao.getEdificio().getSalaToCruz());
                    frame.revalidate();
                    frame.repaint();
                }
            }
        });

        TurnoUtillizadorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean jogoEmAndamento = true;
                if (!jogoEmAndamento) {
                    JOptionPane.showMessageDialog(JogoMapa, "O jogo já terminou! Reinicie para jogar novamente.");
                    System.exit(0);
                }

                grafoRenderer.repaint();
                escolhaTurnoUtilizador();
                roundsCount++;
                atualizarRound();
                opcoesTurnoUtilizador(missao.getEdificio().getSalaToCruz());

                if (missao.isSucess()) {
                    JOptionPane.showMessageDialog(JogoMapa, "Missão concluída com sucesso!");
                    jogoEmAndamento = false;
                } else if (missao.getToCruz().getVida() <= 0) {
                    JOptionPane.showMessageDialog(JogoMapa, "Game Over! To Cruz foi derrotado.");
                    jogoEmAndamento = false;
                }

                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(JogoMapa);
                frame.revalidate();
                frame.repaint();
            }
        });

        PlayEasyModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(DificuldadeFacilPanel);
                frame.setContentPane(JogoMapa);
                Grafo.setLayout(new BorderLayout());
                Grafo.add(grafoRenderer, BorderLayout.CENTER);
                grafoRenderer.repaint();
                atualizarRound();
                frame.revalidate();
                frame.repaint();
            }
        });

        PlayMedioModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(DificuldadeMediaPanel);
                frame.setContentPane(JogoMapa);
                Grafo.setLayout(new BorderLayout());
                Grafo.add(grafoRenderer, BorderLayout.CENTER);
                grafoRenderer.repaint();
                atualizarRound();
                frame.revalidate();
                frame.repaint();
            }
        });

        legenda.setText("ToCruz: Azul \n" +
                "Inimigo: Vermelho \n" +
                "Itens: Verde \n" +
                "ToCruz e Inimigos: Rosa \n" +
                "Tocruz e Itens: Cinzento \n" +
                "Inimigo e Itens: Amarelo\n" +
                "ToCruz e Inimigos e Itens: Orange");

        PlayHardModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(DificuldadeDificilPanel);
                frame.setContentPane(JogoMapa);
                Grafo.setLayout(new BorderLayout());
                Grafo.add(grafoRenderer, BorderLayout.CENTER);
                grafoRenderer.repaint();
                atualizarRound();
                frame.revalidate();
                frame.repaint();
            }
        });

        facilButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(NivelDificuldadePanel);
                frame.setContentPane(DificuldadeFacilPanel);
                frame.revalidate();
                labelImagemFacil.setPreferredSize(new Dimension(540, 540));
                missao.setToCruz(new ToCruz("ToCruz", PODER_MODE_EASY, SIZE_MOCHILA_FACIL));
                frame.repaint();
            }
        });

        medioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(NivelDificuldadePanel);
                frame.setContentPane(DificuldadeMediaPanel);
                frame.revalidate();
                labelImagemMedio.setPreferredSize(new Dimension(540, 540));
                missao.setToCruz(new ToCruz("ToCruz", PODER_MODE_MEDIO, SIZE_MOCHILA_MEDIO));
                frame.repaint();
            }
        });

        dificilButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(NivelDificuldadePanel);
                frame.setContentPane(DificuldadeDificilPanel);
                frame.revalidate();
                labelImagemDificil.setPreferredSize(new Dimension(540, 540));
                missao.setToCruz(new ToCruz("ToCruz", PODER_MODE_HARD, SIZE_MOCHILA_DIFICIL));
                frame.repaint();
            }
        });

        ModoManual.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(ModoDeJogo);
                frame.setContentPane(SpawnSelecter);
                escolherMissao();
                atualizarSpawnList();
                frame.revalidate();
                frame.repaint();
            }
        });

        IniciarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (DataTreating.getMissoes().isEmpty()){
                    JOptionPane.showMessageDialog(EcraInicial, "Por favor insire uma missão antes de começar");
                }else {
                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(EcraInicial);
                    frame.setContentPane(ModoDeJogo);
                    frame.revalidate();
                    frame.repaint();
                }
            }
        });

        fecharButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Fecha a aplicação
            }
        });

        sairJogo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Fecha a aplicação
            }
        });

    }

    private void atualizarSpawnList() {
        DefaultListModel<String> model = new DefaultListModel<>();
        int entradasSaidas = missao.getEdificio().getNumeroEntradas_saidas();
        Iterator<Sala> entradasSaidasIterator = missao.getEdificio().getEntradas_saidas().iterator();

        for (int i = 0; i < entradasSaidas; i++) {
            model.addElement(entradasSaidasIterator.next().getNome());
        }
        SpawnList.setModel(model);
        SpawnList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void escolhaTurnoUtilizador(){
        String selectedAction = TurnoUtilizador.getSelectedValue();
        if (selectedAction == null) {
            JOptionPane.showMessageDialog(TurnoUtilizador, "Por favor, selecione uma ação!");
        }
        switch (selectedAction){
            case "1 - Mover":
                Sala salaAtual = missao.getEdificio().getSalaToCruz();
                LinearLinkedUnorderedList<Sala> salasConectadas = new LinearLinkedUnorderedList<>();
                Iterator<Sala> it = missao.getEdificio().getSalas().getConnectedVertices(salaAtual).iterator();
                while (it.hasNext()) {
                    salasConectadas.addToRear(it.next());
                }
                String[] nomesSalasConectadas = new String[salasConectadas.size()];
                Iterator<Sala> salaIt = salasConectadas.iterator();
                int index = 0;
                while (salaIt.hasNext()) {
                    nomesSalasConectadas[index++] = salaIt.next().getNome();
                }

                String escolha = (String) JOptionPane.showInputDialog(
                        null,
                        "Escolha a sala para a qual deseja mover:",
                        "Mover para uma Sala",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        nomesSalasConectadas,
                        nomesSalasConectadas[0] // Padrão selecionado
                );

                if (escolha != null) {
                    salaIt = salasConectadas.iterator();
                    Sala salaEscolhida = null;
                    while (salaIt.hasNext()){
                        salaEscolhida = salaIt.next();
                        if (salaEscolhida.getNome().equals(escolha)){
                            break;
                        }
                    }
                    moveToCruz(missao, salaEscolhida, false);
                    confronto(missao, false, false);
                    JOptionPane.showMessageDialog(TurnoUtilizador, "Moveu se para a sala" + salaEscolhida);
                } else {
                    JOptionPane.showMessageDialog(TurnoUtilizador, "O utilizador cancelou ou não fez uma escolha.");
                }
                break;
            case "2 - Usar MedKit":
                try{
                    useMedkit(missao, false, false);
                }catch (NullPointerException | IllegalArgumentException e){
                    JOptionPane.showMessageDialog(TurnoUtilizador, e.getMessage());
                }
                break;
            case "3 - Verificar Vida":
                JOptionPane.showMessageDialog(TurnoUtilizador, missao.getToCruz().getVida());
                break;
            case "4 - Verificar Mochila":
                try{
                    JOptionPane.showMessageDialog(TurnoUtilizador,  missao.getToCruz().getMochila().peek());
                }catch (NullPointerException | IllegalArgumentException e){
                    JOptionPane.showMessageDialog(TurnoUtilizador, e.getMessage());
                }
                break;
            case "5 - Apanhar Item":
                missao.getToCruz().apanhaItem(missao.getEdificio().getSalaToCruz().getItens());
                missao.changeToCruz(missao.getToCruz());
                Sala oldSala = missao.getEdificio().getSalaToCruz();
                Iterator<Item> itItem = missao.getEdificio().getSalaToCruz().getItens().iterator();
                while (itItem.hasNext()){
                    Item item = itItem.next();
                    if (item.getTipo().equals(ItemType.MEDKIT)) {
                        JOptionPane.showMessageDialog(TurnoUtilizador, missao.getToCruz().getMochila().peek());
                    }
                    missao.getEdificio().getSalaToCruz().removeItem(item);
                }
                missao.changeSala(oldSala, missao.getEdificio().getSalaToCruz());
                break;
            case "6 - Recuperar o alvo":
                missao.getToCruz().setGotAlvo(true);
                missao.getEdificio().getSalaToCruz().setAlvo(false);
                missao.changeToCruz(missao.getToCruz());
                missao.changeSala(missao.getEdificio().getSalaToCruz(), missao.getEdificio().getSalaToCruz().setAlvo(false));
                missao.changeAlvo(new Alvo(new Sala("ToCruz", true, false), missao.getAlvo().getTipo()));
                JOptionPane.showMessageDialog(TurnoUtilizador, "To Cruz apanhou o alvo: " + missao.getAlvo());
                break;
            case "7 - Sair do edificio":
                if(missao.getToCruz().getGotAlvo()){
                    JOptionPane.showMessageDialog(TurnoUtilizador, "Missão Concluída");
                }else {
                    JOptionPane.showMessageDialog(TurnoUtilizador, "Missão Falhada");
                }
                DataTreating.addRelatorio(new Relatorio(missao));
                System.exit(0);
                break;
        }
    }

    private void opcoesTurnoUtilizador(Sala salaToCruz){
        DefaultListModel<String> model = new DefaultListModel<>();
        model.addElement("1 - Mover");
        model.addElement("2 - Usar MedKit");
        model.addElement("3 - Verificar Vida");
        model.addElement("4 - Verificar Mochila");

        if (salaToCruz.hasItens()) {
            model.addElement("5 - Apanhar Item");
        }
        if (salaToCruz.haveAlvo()) {
            model.addElement("6 - Recuperar o alvo");
        }
        if (salaToCruz.isEntradaSaida()) {
            model.addElement("7 - Sair do edificio");
        }
        TurnoUtilizador.setModel(model);
        TurnoUtilizador.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void carregarSalas(MouseEvent e){
        Point clickPoint = e.getPoint();
        Sala salaClicada = grafoRenderer.detectarSalaClicada(clickPoint);
        if (salaClicada != null) {
            grafoRenderer.mostrarPainelSala(salaClicada);
        }
    }

    private void runGame(int versao) {
        missao = DataTreating.getMissaoByVersion(versao);
        grafoRenderer = new GrafoRenderer(missao, true);
        grafoRenderer.repaint();
    }

    private void atualizarRound(){
        RoundCnt.setText("Round:" + roundsCount);
    }

    private void getRelatorios(){
        DefaultListModel<String> model = new DefaultListModel<>();
        Iterator<Relatorio> itRelatorio = DataTreating.getRelatorios().getAllRelatorios().iterator();
        while (itRelatorio.hasNext()){
            model.addElement(itRelatorio.next().relatorioMissao());
        }
        relatorios.setModel(model);
    }

    private void escolherMissao(){
        boolean sair = false;
        while (!sair) {
            LinearLinkedOrderedList<Missao> missoes = DataTreating.getMissoes();
            Object[] nomeMissoes = new Object[missoes.size()];
            int[] versoesMissoes = new int[missoes.size()];
            Iterator<Missao> missoesIt = missoes.iterator();
            int index = 0;
            while (missoesIt.hasNext()) {
                Missao missao1 = missoesIt.next();
                nomeMissoes[index] = missao1.getCod_missao() + " (Versão: " + missao1.getVersion() + ")";
                versoesMissoes[index++] = missao1.getVersion();
            }

            int escolha = JOptionPane.showOptionDialog(
                    null,
                    "Escolha a sala para a qual deseja mover:",
                    "Mover para uma Sala",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    nomeMissoes,
                    nomeMissoes[0] // Padrão selecionado
            );
            if (escolha >= 0) {
                int versaoEscolhida = versoesMissoes[escolha];
                runGame(versaoEscolhida);
                sair = true;
            }
        }
    }

    private void lerJson(){
        boolean sair = false;
        while (!sair) {
            Object[] opcoes = {"Selecionar Caminho", "Voltar"};
            int escolha = JOptionPane.showOptionDialog(
                    null,
                    "Escolha uma opção:",
                    "Carregar Missão",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]
            );

            if (escolha == 1) {
                JOptionPane.showMessageDialog(
                        null,
                        "Voltando ao menu anterior.",
                        "Ação Cancelada",
                        JOptionPane.INFORMATION_MESSAGE
                );
                sair = true;
            } else if (escolha == 0) {
                String caminhoJson = JOptionPane.showInputDialog(
                        null,
                        "Insira o caminho do arquivo JSON:",
                        "Carregar Missão",
                        JOptionPane.PLAIN_MESSAGE
                );

                if (caminhoJson != null && !caminhoJson.isEmpty()) {
                    try {
                        caminhoJson = caminhoJson.replaceAll("^\"|\"$", "");
                        DataTreating.ReadMissao(caminhoJson);
                        JOptionPane.showMessageDialog(
                                null,
                                "Missão carregada com sucesso!\n",
                                "Sucesso",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                        sair = true;
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Erro ao carregar o arquivo JSON: " + e.getMessage(),
                                "Erro",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                } else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Nenhum caminho foi inserido.",
                            "Ação Cancelada",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            }else {
                sair = true;
            }
        }
    }

    private void confronto(Missao missao, boolean autoMode, boolean TocruzStart) {
        Edificio edificio = missao.getEdificio();
        Sala salaAtual = edificio.getSalaToCruz();
        ToCruz toCruz = missao.getToCruz();
        toCruz.setInConfronto(true);
        missao.changeToCruz(toCruz);
        LinearLinkedUnorderedList<Inimigo> inimigos = salaAtual.getInimigos();
        LinearLinkedUnorderedList<Inimigo> temKilledEn = new LinearLinkedUnorderedList<>();

        boolean confrontoAtivo = true;

        while (confrontoAtivo && toCruz.getVida() > 0 && !inimigos.isEmpty()) {
            if (!autoMode) {
                String[] opcoes = {"Atacar", "Usar MedKit"};
                int escolha = JOptionPane.showOptionDialog(
                        null,
                        "Escolha uma ação:",
                        "Confronto",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        opcoes,
                        opcoes[0]
                );

                if (escolha == 0) {
                    Iterator<Inimigo> inimigosIterator = inimigos.iterator();
                    while (inimigosIterator.hasNext()) {
                        Inimigo inimigo = inimigosIterator.next();
                        try {
                            attack(toCruz, inimigo);
                            JOptionPane.showMessageDialog(null,
                                    "Você atacou o inimigo " + inimigo.getNome() + "!\nVida restante do inimigo: " + inimigo.getVida(),
                                    "Confronto",
                                    JOptionPane.INFORMATION_MESSAGE);
                            if (inimigo.getVida() <= 0) {
                                JOptionPane.showMessageDialog(null,
                                        inimigo.getNome() + " foi derrotado!",
                                        "Confronto",
                                        JOptionPane.INFORMATION_MESSAGE);
                                edificio.removeInimigo(inimigo);
                                temKilledEn.addToRear(inimigo);
                            }
                        } catch (IllegalArgumentException e) {
                            JOptionPane.showMessageDialog(null, e.getMessage(), "Erro de Ataque", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else if (escolha == 1) {
                    useMedkit(missao,false,true);
                } else {
                    confrontoAtivo = false;
                    JOptionPane.showMessageDialog(null, "Confronto encerrado.", "Confronto", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                if (TocruzStart) {
                    for (Inimigo inimigo : inimigos) {
                        attack(toCruz, inimigo);
                        if (inimigo.getVida() <= 0) {
                            edificio.removeInimigo(inimigo);
                            temKilledEn.addToRear(inimigo);
                        }
                    }
                    TocruzStart = false;
                }

                if (!inimigos.isEmpty()) {
                    for (Inimigo inimigo : inimigos) {
                        attack(inimigo, toCruz);
                    }
                    JOptionPane.showMessageDialog(null, "Vida do To Cruz: " + toCruz.getVida(), "Confronto", JOptionPane.INFORMATION_MESSAGE);
                    TocruzStart = true;
                }
            }

            if (!temKilledEn.isEmpty()) {
                for (Inimigo inimigo : temKilledEn) {
                    inimigos.remove(inimigo);
                }
                temKilledEn = new LinearLinkedUnorderedList<>();
            }

            // verifica se o confronto continua
            confrontoAtivo = !inimigos.isEmpty() && toCruz.getVida() > 0;
        }

        if (toCruz.getVida() > 0) {
            JOptionPane.showMessageDialog(null, "Tó Cruz venceu o confronto!", "Confronto", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Tó Cruz foi derrotado!", "Game Over", JOptionPane.ERROR_MESSAGE);
            missao.setSucess(false);
        }

        toCruz.setInConfronto(false);
        missao.changeToCruz(toCruz);
        missao.changeEdificio(edificio);
    }


    private void walkEnimies(Missao missao, boolean autoMode, boolean wasInConfronto) {
        Edificio edificio = missao.getEdificio();
        LinearLinkedUnorderedList<Inimigo> inimigos = edificio.getAllInimigos();
        LinearLinkedUnorderedList<Sala> salasComInimigos = edificio.getSalaComInimigos();
        LinearLinkedUnorderedList<Sala> salasConnectadas;
        AVLPriorityTree<Sala> PossiveisSalas = new AVLPriorityTree<>();

        Random random = new Random();

        if (inimigos.isEmpty()){
            return;
        }
        for( Sala sala : salasComInimigos){
            int cnt = 1;
            //impedir de movimentar os inimigos que estão em confronto
            if (sala.hasInimigos() && sala.haveToCruz()){
                continue;
            }
            inimigos = sala.getInimigos();
            salasConnectadas= edificio.getSalas().getConnectedVertices(sala);

            PossiveisSalas.addElement(sala, cnt);
            for (Sala salaConnectada : salasConnectadas){
                //para impedir inimigos de entrarem na sala em que esta a haver um confronto
                if (salaConnectada.hasInimigos() && salaConnectada.haveToCruz()){
                    continue;
                }
                cnt++;
                PossiveisSalas.addElement(salaConnectada, cnt);
            }

            for (Inimigo inimigo : inimigos) {
                try {
                    sala = PossiveisSalas.FindELPriority(random.nextInt(cnt) + 1);
                    edificio.addInimigo(inimigo, sala);
                }catch (ElementNotFoundException | EmptyCollectionException _){

                }
            }
        }
        missao.changeEdificio(edificio);
        Sala checkConfronto = edificio.getSalaToCruz();
        if (checkConfronto.haveToCruz() && checkConfronto.hasInimigos() && !wasInConfronto){
            confronto(missao, autoMode,false);
        }
    }
    private void moveToCruz(Missao missao, Sala to, boolean autoMode) {
        Edificio edificio = missao.getEdificio();
        Iterator<Sala> iterator = edificio.getSalas().iteratorBFS(to);
        Sala sala = edificio.getSalaToCruz();

        if (sala != null){
            missao.changeSala(sala, sala.setHaveToCruz(false));
        }

        missao.changeSala(to, to.setHaveToCruz(true));
        missao.addSalaCaminhoTo(to);

        if (to.hasInimigos()){
            confronto(missao, true, autoMode);
        } else {
            walkEnimies(missao, autoMode,false );
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

    private void useMedkit(Missao missao, boolean autoMode, boolean wasInConfronto) {
        ToCruz toCruz = missao.getToCruz();
        Item kit = toCruz.usarMedKit();
        JOptionPane.showMessageDialog(TurnoUtilizador, "ToCruz usou um medkit, Curou: " + kit.getQuantidade());
        missao.changeToCruz(toCruz);
        Cenarios.walkEnimies(missao, autoMode, wasInConfronto);
    }

    private void attack(Pessoa atacante, Pessoa atacado) throws IllegalArgumentException{
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

    public static void main(String[] args) {
        DataTreating.loadGameData();
        JFrame frame = new JFrame("Console");
        frame.setContentPane(new Console().EcraInicial);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
