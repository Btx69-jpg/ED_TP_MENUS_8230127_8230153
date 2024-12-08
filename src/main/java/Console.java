import Data.Json;
import Edificio.Sala;
import Missao.Missao;
import Pessoa.Inimigo;
import Pessoa.ToCruz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import java.util.Iterator;

public class Console {

    private static int PODER_MODE_EASY = 25;
    private static int PODER_MODE_MEDIO = 20;
    private static int PODER_MODE_HARD = 15;

    private JTextArea bemVindoAoMissionTextArea;
    private JButton IniciarButton;
    private JButton fecharButton;
    private JPanel EcraInicial;
    private JPanel Jogo;
    private JPanel NivelDificuldadePanel;
    private JButton fácilButton;
    private JButton dificilButton;
    private JButton médioButton;
    private JTextArea Dificuldadetxt;
    private JPanel DificuldadeFacilPanel;
    private JLabel labelImagemFacil;
    private JList ToCruzStatus;
    private JButton PlayEasyModeButton;
    private JPanel SpamSelecter;
    private JPanel LoadingScreen;
    private JPanel DificuldadeMediaPanel;
    private JButton PlayMedioModeButton;
    private JLabel labelImagemMedio;
    private JPanel LoadingGame;
    private JPanel DificuldadeDificilPanel;
    private JLabel labelImagemDificil;
    private JButton PlayHardModeButton;
    private JList<String> SpawnList;
    private JLabel SelectSpawnPoint;
    private JButton AvancarJogo;
    private JPanel JogoMapa;
    private JList<String> TurnoUtilizador;
    private JButton TurnoUtillizadorButton;
    private JButton sairJogo;

    private Missao missao;
    private ToCruz toCruz;

    public Console() {

        AvancarJogo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedSpawnPoint = SpawnList.getSelectedValue();
                if(selectedSpawnPoint == null){
                    JOptionPane.showMessageDialog(SpamSelecter, "Por favor, selecione um ponto de spawn!");
                }
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(SpamSelecter);
                frame.setContentPane(JogoMapa);
                opcoesTurnoUtilizador();
                frame.revalidate();
                frame.repaint();
            }
        });

        TurnoUtillizadorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                while (!missao.isSucess() || toCruz.getVida() != 0){
                    escolhaTurnoUtilizador();
                }
            }
        });

        PlayEasyModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(DificuldadeFacilPanel);
                frame.setContentPane(SpamSelecter);
                runGame();
                atualizarSpamList();
                frame.revalidate();
                frame.repaint();
            }
        });

        PlayMedioModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(DificuldadeMediaPanel);
                frame.setContentPane(SpamSelecter);
                runGame();
                atualizarSpamList();
                frame.revalidate();
                frame.repaint();
            }
        });

        PlayHardModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(DificuldadeDificilPanel);
                frame.setContentPane(SpamSelecter);
                runGame();
                atualizarSpamList();
                frame.revalidate();
                frame.repaint();
            }
        });

        fácilButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(NivelDificuldadePanel);
                frame.setContentPane(DificuldadeFacilPanel);
                frame.revalidate();
                labelImagemFacil.setPreferredSize(new Dimension(540, 540));
                toCruz = new ToCruz("ToCruz", PODER_MODE_EASY);
                frame.repaint();
            }
        });

        médioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(NivelDificuldadePanel);
                frame.setContentPane(DificuldadeMediaPanel);
                frame.revalidate();
                labelImagemMedio.setPreferredSize(new Dimension(540, 540));
                toCruz = new ToCruz("ToCruz", PODER_MODE_MEDIO);
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
                toCruz = new ToCruz("ToCruz", PODER_MODE_HARD);
                frame.repaint();
            }
        });

        IniciarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(EcraInicial);
                frame.setContentPane(NivelDificuldadePanel);
                frame.revalidate();
                frame.repaint();
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

    /*protected void paintComponent() {
        missao.getEdificio();
        super.paintComponent(g);

        // Desenhar as arestas
        g.setColor(Color.GRAY);
        for (Grafo.Edge edge : grafo.getEdges()) {
            g.drawLine(edge.from.x, edge.from.y, edge.to.x, edge.to.y);
        }

        // Desenhar os nós
        g.setColor(Color.BLUE);
        for (Grafo.Node node : grafo.getNodes()) {
            g.fillOval(node.x - 10, node.y - 10, 20, 20);
            g.setColor(Color.BLACK);
            g.drawString(node.id, node.x - 15, node.y - 15);
            g.setColor(Color.BLUE);
        }
    }*/

    public void atualizarSpamList() {
        DefaultListModel<String> model = new DefaultListModel<>();
        int entradasSaidas = missao.getEdificio().getNumeroEntradas_saidas();
        Iterator<Sala> entradasSaidasIterator = missao.getEdificio().getEntradas_saidas().iterator();

        for (int i = 0; i < entradasSaidas; i++) {
            model.addElement(i + " - " + entradasSaidasIterator.next().toString());
        }
        SpawnList.setModel(model);
        SpawnList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public void escolhaTurnoUtilizador(){
        String selectedAction = TurnoUtilizador.getSelectedValue();
        Rounds rounds = new Rounds();
        if (selectedAction == null) {
            JOptionPane.showMessageDialog(TurnoUtilizador, "Por favor, selecione uma ação!");
        }
        switch (selectedAction){
            case "1 - Mover":
                rounds.move(toCruz);
                break;
            case "2 - Usar MedKit":
                rounds.useMedKit(toCruz);
                break;
            case "3 - Atacar":
                Iterator<Sala> itSalas = missao.getEdificio().getSalas().iteratorBFS(missao.getEdificio().getSalas().getVertex(0));
                Sala salaToCruz;
                while (itSalas.hasNext()){
                    salaToCruz = itSalas.next();
                    if (salaToCruz.haveToCruz()){
                        if (salaToCruz.hasInimigos()){
                            for (Inimigo inimigo : salaToCruz.getInimigos()){
                                rounds.attack(toCruz, inimigo);
                            }
                            break;
                        }else{
                            JOptionPane.showMessageDialog(TurnoUtilizador, "Sala não inimigos para atacar!");
                            break;
                        }
                    }
                }
            case "4 - Verificar Vida":
                toCruz.getVida();
                break;
            case "5 - Verificar Mochila":
                toCruz.
                break;
            case "6 - Verificar Alvo":
                missao.getAlvo();
                break;
            case "7 - Verificar Edificio":
                missao.getEdificio();
                break;
        }
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(JogoMapa);
        frame.revalidate();
        frame.repaint();
    }

    public void opcoesTurnoUtilizador(){
        DefaultListModel<String> model = new DefaultListModel<>();
        model.addElement("1 - Mover");
        model.addElement("2 - Usar MedKit");
        model.addElement("3 - Atacar");
        model.addElement("4 - Verificar Vida");
        model.addElement("5 - Verificar Mochila");
        model.addElement("6 - Verificar Alvo");
        model.addElement("7 - Verificar Edificio");
        TurnoUtilizador.setModel(model);
        TurnoUtilizador.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public void runGame() {
        missao = Json.ReadJson("C:\\Users\\Gonçalo\\Documents\\GitHub\\ED_TP_8230127_8230153\\ED_TP_MENUS_8230127_8230153\\src\\main\\resources\\teste.json");
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Console");
        frame.setContentPane(new Console().EcraInicial);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
