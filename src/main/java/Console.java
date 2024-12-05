import javax.swing.*;

public class Console {

    private JTextArea bemVindoAoMissionTextArea;
    private JButton IniciarButton;
    private JButton fecharButton;
    private JPanel EcraInicial;
    private JPanel Jogo;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Console");
        frame.setContentPane(new Console().Jogo);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
