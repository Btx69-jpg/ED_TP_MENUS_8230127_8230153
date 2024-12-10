import Edificio.Sala;
import Graphs.PropriaAutoria.GraphNetworkEM;
import LinkedList.LinearLinkedUnorderedList;
import Missao.Missao;
import Queue.LinkedQueue;

import java.awt.*;
import java.util.Iterator;
import java.util.Random;
import javax.swing.JPanel;

public class GrafoRenderer extends JPanel {

    private static int LARGURA = 1000;
    private static int ALTURA = 500;

    private Missao missao;
    private GraphNetworkEM<Sala> grafo; // Sua classe de grafo
    private Random random;
    private Point[] coordenadas;
    private LinearLinkedUnorderedList<Sala> vertices;

    public GrafoRenderer(Missao missao, boolean organizar) {
        this.missao = missao;
        this.random = new Random();
        grafo = missao.getEdificio().getSalas();
        if(organizar){
            organizarSalas();
        }else {
            randomCordinnates();
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        Iterator<Sala> itVertices = vertices.iterator();
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
            int index = 0;
            while (itVertices.hasNext()) {
                Sala sala = itVertices.next();
                Point p = coordenadas[index++];
                g2d.fillRect(p.x - 10, p.y - 10, 50, 50);
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
    private void randomCordinnates() {
        int width = LARGURA;
        int height = ALTURA;

        vertices = new LinearLinkedUnorderedList<>();
        Iterator<Sala> itvertices = grafo.getVerticesIterator();
        while (itvertices.hasNext()) {
            vertices.addToRear(itvertices.next());
        }

        coordenadas = new Point[vertices.size()];
        Iterator<Sala> itVertices = vertices.iterator();
        int index = 0;

        // Lista de pontos já ocupados
        LinearLinkedUnorderedList<Point> ocupados = new LinearLinkedUnorderedList<>();
        int minDist = 70;

        // Atribuir coordenadas aleatórias sem sobreposição
        while (itVertices.hasNext()) {
            itVertices.next();
            Point novaCoordenada;

            do {
                int x = random.nextInt(width - 100) + 50;
                int y = random.nextInt(height - 100) + 50;
                novaCoordenada = new Point(x, y);
            } while (!posicaoValida(novaCoordenada, ocupados, minDist));

            coordenadas[index++] = novaCoordenada;
            ocupados.addToRear(novaCoordenada);
        }
    }

    // Verificar se a posição é válida (não sobrepõe outra sala)
    private boolean posicaoValida(Point nova, LinearLinkedUnorderedList<Point> ocupados, int minDist) {
        for (Point ocupada : ocupados) {
            if (nova.distance(ocupada) < minDist) {
                return false;
            }
        }
        return true;
    }

    private void organizarSalas() {
        int width = LARGURA;
        int spacingX = 150;
        int spacingY = 100;

        vertices = new LinearLinkedUnorderedList<>();
        Iterator<Sala> itVertices = grafo.getVerticesIterator();
        while (itVertices.hasNext()) {
            vertices.addToRear(itVertices.next());
        }

        coordenadas = new Point[vertices.size()];
        Iterator<Sala> it = vertices.iterator();

        int index = 0;
        int x = 50;
        int y = 50;

        while (it.hasNext()) {
            it.next();
            coordenadas[index++] = new Point(x, y);

            x += spacingX;

            if (x + spacingX > width) {
                x = 50;
                y += spacingY;
            }
        }
    }

}
