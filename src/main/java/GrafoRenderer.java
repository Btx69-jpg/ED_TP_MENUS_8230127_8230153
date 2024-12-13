import Edificio.Sala;
import Graphs.GraphNetwork;
import Item.Item;
import LinkedList.LinearLinkedUnorderedList;
import Missao.Missao;
import Pessoa.Inimigo;

import java.awt.*;
import java.util.Iterator;
import java.util.Random;
import javax.swing.*;

public class GrafoRenderer extends JPanel {

    private static int LARGURA = 1000;
    private static int ALTURA = 500;

    private Missao missao;
    private GraphNetwork<Sala> grafo; // Sua classe de grafo
    private Random random;
    private Point[] coordenadas;
    private LinearLinkedUnorderedList<Sala> vertices;
    private LinearLinkedUnorderedList<Point[]> linhasDesenhadas;

    public GrafoRenderer(Missao missao, boolean organizar) {
        this.missao = missao;
        this.random = new Random();
        this.linhasDesenhadas = new LinearLinkedUnorderedList<>();

        grafo = missao.getEdificio().getSalas();
        if (organizar) {
            organizarSalas();
        } else {
            randomCordinnates();
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        this.setLayout(null);
        LinearLinkedUnorderedList<Integer> valoresVistos = new LinearLinkedUnorderedList<>();

        // Desenhar Arestas
        Iterator<Sala> itArestas = vertices.iterator();
        while (itArestas.hasNext()) {
            Sala origem = itArestas.next();
            int origemIndex = getVertexIndex(origem);

            LinearLinkedUnorderedList<Sala> conexoes = grafo.getConnectedVertices(origem);
            Iterator<Sala> itConexoes = conexoes.iterator();

            while (itConexoes.hasNext()) {
                valoresVistos.addToRear(origemIndex);
                if(valoresVistos.contains(origemIndex)) {
                    continue;
                }

                int squareWidth = 70;
                int squareHeight = 70;
                Sala destino = itConexoes.next();
                int destinoIndex = getVertexIndex(destino);

                Point origemCenter = coordenadas[origemIndex];
                Point destinoCenter = coordenadas[destinoIndex];

                Point origemBorda = getClosestBorderPoint(origemCenter, destinoCenter, squareWidth, squareHeight);
                Point destinoBorda = getClosestBorderPoint(destinoCenter, origemCenter, squareWidth, squareHeight);

                boolean desviarParaCima = isSpaceAbove(origemBorda, destinoBorda, linhasDesenhadas);

                Point pontoIntermediario;
                if (desviarParaCima) {
                    pontoIntermediario = new Point((origemBorda.x + destinoBorda.x) / 2, Math.min(origemBorda.y, destinoBorda.y) - 50);
                } else {
                    pontoIntermediario = new Point((origemBorda.x + destinoBorda.x) / 2, Math.max(origemBorda.y, destinoBorda.y) + 50);
                }

                g2d.drawLine(origemBorda.x, origemBorda.y, pontoIntermediario.x, pontoIntermediario.y);
                g2d.drawLine(pontoIntermediario.x, pontoIntermediario.y, destinoBorda.x, destinoBorda.y);
                g2d.setColor(Color.BLACK);
                linhasDesenhadas.addToRear(new Point[]{origemBorda, destinoBorda});
            }

            // Desenhar Vértices
            Iterator<Sala> itVertices = vertices.iterator();
            int index = 0;
            while (itVertices.hasNext()) {
                Sala sala = itVertices.next();
                Point p = coordenadas[index++];

                int squareSize = 80;

                if(sala.haveToCruz() && !sala.hasInimigos() && !sala.hasItens()){
                    g2d.setColor(Color.BLUE);
                }else if (!sala.haveToCruz() && sala.hasInimigos() && !sala.hasItens()){
                    g2d.setColor(Color.RED);
                }else if (!sala.haveToCruz() && !sala.hasInimigos() && sala.hasItens()){
                    g2d.setColor(Color.GREEN);
                }else if (sala.haveToCruz() && sala.hasInimigos() && !sala.hasItens()){
                    g2d.setColor(Color.MAGENTA);
                }else if (!sala.haveToCruz() && sala.hasInimigos() && sala.hasItens()){
                    g2d.setColor(Color.YELLOW);
                }else if (sala.haveToCruz() && !sala.hasInimigos() && sala.hasItens()){
                    g2d.setColor(Color.GRAY);
                }else if (sala.haveToCruz() && sala.hasInimigos() && sala.hasItens()){
                    g2d.setColor(Color.ORANGE);
                }else {
                    g2d.setColor(Color.BLACK);
                }

                g2d.drawRect(p.x - squareSize / 2, p.y - squareSize / 2, squareSize, squareSize);
                g2d.setColor(Color.BLACK);
                FontMetrics metrics = g2d.getFontMetrics();
                int textX = p.x - metrics.stringWidth(sala.getNome()) / 2;
                int textY = p.y + metrics.getHeight() / 4;
                if (sala.haveAlvo()) {
                    g2d.drawString(sala.getNome() + "(Alvo)", textX, textY);
                } else {
                    g2d.drawString(sala.getNome(), textX, textY);
                }
            }
        }
    }

    private boolean isSpaceAbove(Point origem, Point destino, LinearLinkedUnorderedList<Point[]> linhasExistentes) {
        int midX = (origem.x + destino.x) / 2;
        int aboveY = origem.y - 20;
        int belowY = origem.y + 20;

        int linhasAcima = 0;
        int linhasAbaixo = 0;

        // Adicionar a linha atual como uma linha simulada para a verificação
        LinearLinkedUnorderedList<Point[]> linhasTestadas = new LinearLinkedUnorderedList<>();
        if (linhasExistentes != null) {
            for (Point[] linha : linhasExistentes) {
                linhasTestadas.addToRear(linha);
            }
        }
        linhasTestadas.addToRear(new Point[]{origem, destino});

        // Verificar cada linha existente
        for (Point[] linha : linhasTestadas) {
            Point linhaOrigem = linha[0];
            Point linhaDestino = linha[1];

            // Verificar interseção acima
            if (linhaIntersectsRect(linhaOrigem, linhaDestino, midX, aboveY, 10, 10)) {
                linhasAcima++;
            }

            // Verificar interseção abaixo
            if (linhaIntersectsRect(linhaOrigem, linhaDestino, midX, belowY, 10, 10)) {
                linhasAbaixo++;
            }
        }

        // Retorna true se há mais espaço acima
        return linhasAcima <= linhasAbaixo;
    }


    private boolean linhaIntersectsRect(Point origem, Point destino, int rectX, int rectY, int rectWidth, int rectHeight) {
        int x1 = origem.x, y1 = origem.y;
        int x2 = destino.x, y2 = destino.y;

        // Coordenadas do retângulo
        int rx1 = rectX, ry1 = rectY;
        int rx2 = rectX + rectWidth, ry2 = rectY + rectHeight;

        // Verifica interseção com as bordas do retângulo
        return linhaIntersectsLinha(x1, y1, x2, y2, rx1, ry1, rx2, ry1) || // Topo
                linhaIntersectsLinha(x1, y1, x2, y2, rx1, ry2, rx2, ry2) || // Base
                linhaIntersectsLinha(x1, y1, x2, y2, rx1, ry1, rx1, ry2) || // Esquerda
                linhaIntersectsLinha(x1, y1, x2, y2, rx2, ry1, rx2, ry2);   // Direita
    }

    private boolean linhaIntersectsLinha(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
        // Fórmula para verificar se duas linhas se intersectam
        double denom = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
        if (denom == 0) return false; // Linhas paralelas

        double ua = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / denom;
        double ub = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / denom;

        return ua >= 0 && ua <= 1 && ub >= 0 && ub <= 1;
    }

    private Point getClosestBorderPoint(Point center, Point target, int width, int height) {
        int left = center.x - width / 2;
        int right = center.x + width / 2;
        int top = center.y - height / 2;
        int bottom = center.y + height / 2;

        double dx = target.x - center.x;
        double dy = target.y - center.y;

        // Determinar qual borda está mais próxima
        if (Math.abs(dy) * width > Math.abs(dx) * height) {
            // Vertical: Topo ou fundo
            if (dy < 0) { // Topo
                return new Point(center.x, top);
            } else { // Fundo
                return new Point(center.x, bottom);
            }
        } else {
            // Horizontal: Esquerda ou direita
            if (dx < 0) { // Esquerda
                return new Point(left, center.y);
            } else { // Direita
                return new Point(right, center.y);
            }
        }
    }

    private int getVertexIndex(Sala vertex) {
        Iterator<Sala> iterator = grafo.getVerticesIterator();

        for (int index = 0; iterator.hasNext(); ++index) {
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
        int spacingX = 140;
        int spacingY = 130;

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

    protected Sala detectarSalaClicada(Point clickPoint) {
        Iterator<Sala> itVer = vertices.iterator();
        int i = 0;
        while (itVer.hasNext()) {
            Sala sala = itVer.next();
            Point centro = coordenadas[i++];
            int tamanhoSala = 70;
            Rectangle rect = new Rectangle(centro.x - tamanhoSala / 2, centro.y - tamanhoSala / 2, tamanhoSala, tamanhoSala);
            if (rect.contains(clickPoint)) {
                return sala;
            }
        }
        return null;
    }
    protected void mostrarPainelSala(Sala sala) {
        JPanel painelSala = new JPanel();
        painelSala.setLayout(new BoxLayout(painelSala, BoxLayout.Y_AXIS));

        JLabel nomeSala = new JLabel("Nome: " + sala.getNome());
        painelSala.add(nomeSala);

        if (sala.getInimigos() != null && !sala.getInimigos().isEmpty()) {
            JLabel inimigosLabel = new JLabel("Inimigos: " + sala.getInimigos().size());
            painelSala.add(inimigosLabel);
            for (Inimigo inimigo : sala.getInimigos()) {
                painelSala.add(new JLabel("- " + inimigo));
            }
        }

        if (sala.getItens() != null && !sala.getItens().isEmpty()) {
            JLabel itensLabel = new JLabel("Itens:");
            painelSala.add(itensLabel);
            for (Item item : sala.getItens()) {
                painelSala.add(new JLabel("- " + item.getTipo()));
            }
        }

        JLabel ligacoesLabel = new JLabel("Ligações:");
        painelSala.add(ligacoesLabel);
        for (Sala ligacao : grafo.getConnectedVertices(sala)) {
            painelSala.add(new JLabel("- " + ligacao.getNome()));
        }
        if (sala.haveAlvo()){
            JLabel alvoLabel = new JLabel("Alvo:");
            painelSala.add(alvoLabel);
            painelSala.add(new JLabel(missao.getAlvo().toString()));
        }

        JFrame frameSala = new JFrame("Informações da Sala");
        frameSala.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameSala.setLocationRelativeTo(null);
        frameSala.setSize(500, 500);
        frameSala.add(painelSala);
        frameSala.setVisible(true);
    }

}
