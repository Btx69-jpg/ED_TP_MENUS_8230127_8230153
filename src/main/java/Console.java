import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Console {

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
    private JLabel labelComImagem;

    public Console() {

        // Ação do botão Iniciar
        fácilButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Altera para o painel de nível de dificuldade
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(NivelDificuldadePanel);
                frame.setContentPane(DificuldadeFacilPanel); // Troca para o painel de nível de dificuldade
                frame.revalidate(); // Atualiza o frame para refletir a mudança
                frame.repaint();
            }
        });



        // Ação do botão Iniciar
        IniciarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Altera para o painel de nível de dificuldade
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(EcraInicial);
                frame.setContentPane(NivelDificuldadePanel); // Troca para o painel de nível de dificuldade
                frame.revalidate(); // Atualiza o frame para refletir a mudança
                frame.repaint();
            }
        });

        // Ação do botão fechar
        fecharButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Fecha a aplicação
            }
        });
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Console");
        frame.setContentPane(new Console().EcraInicial);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
