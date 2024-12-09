import Edificio.Sala;
import Graphs.PropriaAutoria.GraphNetworkEM;
import LinkedList.LinearLinkedUnorderedList;
import Missao.Missao;

import java.awt.*;
import java.util.Iterator;
import java.util.Random;
import javax.swing.JPanel;

public class GrafoRenderer extends JPanel {

    private Missao missao;
    private GraphNetworkEM<Sala> grafo; // Sua classe de grafo
    private Random random;

    public GrafoRenderer(Missao missao) {
        this.missao = missao;
        this.random = new Random();
        grafo = missao.getEdificio().getSalas();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int width = getWidth();
        int height = getHeight();

        LinearLinkedUnorderedList<Sala> vertices = new LinearLinkedUnorderedList<>();
        Iterator<Sala> itvertices = grafo.getVerticesIterator();
        while (itvertices.hasNext()) {
            vertices.addToRear(itvertices.next());
        }

        Point[] coordenadas = new Point[vertices.size()];
        Iterator<Sala> itVertices = vertices.iterator();
        int index = 0;

        // Atribuir coordenadas aleatórias aos vértices
        while (itVertices.hasNext()) {
            Sala sala = itVertices.next();
            int x = random.nextInt(width - 100) + 50;
            int y = random.nextInt(height - 100) + 50;
            coordenadas[index++] = new Point(x, y);
        }

        // Desenhar Arestas
        Iterator<Sala> itArestas = vertices.iterator();
        while (itArestas.hasNext()) {
            Sala origem = itArestas.next();
            int origemIndex = getVertexIndex(origem);

            // Obter vértices conectados a partir da origem
            LinearLinkedUnorderedList<Sala> conexoes = grafo.getConnectedVertices(origem);
            Iterator<Sala> itConexoes = conexoes.iterator();

            while (itConexoes.hasNext()) {
                Sala destino = itConexoes.next();
                int destinoIndex = getVertexIndex(destino);

                Point p1 = coordenadas[origemIndex];
                Point p2 = coordenadas[destinoIndex];

                // Desenhar linha entre os vértices
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
            }

            // Desenhar Vértices
            itVertices = vertices.iterator();
            index = 0;
            while (itVertices.hasNext()) {
                Sala sala = itVertices.next();
                Point p = coordenadas[index++];
                g2d.fillRect(p.x - 10, p.y - 10, 100, 100);
                g2d.drawString(sala.getNome(), p.x - 15, p.y - 15);
            }
        }
    }
    private int getVertexIndex(Sala vertex) {
        Iterator<Sala> iterator = grafo.getVerticesIterator();

        for(int index = 0; iterator.hasNext(); ++index) {
            if (iterator.next().equals(vertex)) {
                return index;
            }
        }

        return -1;
    }
}
