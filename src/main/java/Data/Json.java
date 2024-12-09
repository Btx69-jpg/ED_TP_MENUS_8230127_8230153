package Data;

import Edificio.Edificio;
import Edificio.Sala;
import Enum.ItemType;
import Graphs.GraphNetwork;
import Graphs.PropriaAutoria.GraphNetworkEM;
import Item.Item;
import Missao.Alvo;
import Missao.Missao;
import Missao.Relatorio;
import Pessoa.Inimigo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;


public class Json {

    private static Sala salaIterator;

    public static Missao ReadJson(String filePath) {
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(filePath)) {
            // Lê o JSON como objeto
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

            // Lê o código da missão e a versão
            String codMissao = (String) jsonObject.get("cod-missao");
            long versao = (long) jsonObject.get("versao");

            // Lê o alvo
            JSONObject alvoJson = (JSONObject) jsonObject.get("alvo");
            String alvoDivisao = (String) alvoJson.get("divisao");
            String alvoTipo = (String) alvoJson.get("tipo");


            // Lê o edifício e cria os vértices do grafo
            JSONArray edificioArray = (JSONArray) jsonObject.get("edificio");
            GraphNetworkEM<Sala> salas = new GraphNetworkEM<>();

            // Adiciona entradas e saídas
            JSONArray entradasSaidasArray = (JSONArray) jsonObject.get("entradas-saidas");


            for (Object nomeSalaObj : edificioArray) {
                String nomeSala = (String) nomeSalaObj;
                boolean isAlvo = nomeSala.equals(alvoDivisao);
                boolean isEntradaSaida = false;
                for (Object entradaSaidaObj : entradasSaidasArray) {
                    String nomeEntradaSaid = (String) entradaSaidaObj;
                    if (nomeSala.equals(nomeEntradaSaid)) {
                        isEntradaSaida = true;
                        break;
                    }
                }
                Sala sala = new Sala(nomeSala, isAlvo, isEntradaSaida);
                salaIterator = sala;

                salas.addVertex(sala);
            }

            // Adiciona os inimigos às salas
            JSONArray inimigosArray = (JSONArray) jsonObject.get("inimigos");
            // Adiciona as ligações (arestas) entre as salas
            JSONArray ligacoesArray = (JSONArray) jsonObject.get("ligacoes");
            for (Object ligacaoObj : ligacoesArray) {
                JSONArray ligacao = (JSONArray) ligacaoObj;
                String sala1 = (String) ligacao.get(0);
                String sala2 = (String) ligacao.get(1);

                Sala salaObj1 = findSala(salas, sala1);
                Sala salaObj2 = findSala(salas, sala2);

                if (salaObj1 != null && salaObj2 != null) {
                    salas.addEdge(salaObj1, salaObj2);
                }
            }





            // Adiciona os itens às salas
            JSONArray itensArray = (JSONArray) jsonObject.get("itens");
            for (Object itemObj : itensArray) {
                JSONObject itemJson = (JSONObject) itemObj;
                String divisaoItem = (String) itemJson.get("divisao");
                long pontosItem = (long) itemJson.get("pontos");
                String tipoItem = (String) itemJson.get("tipo");

                Sala sala = findSala(salas, divisaoItem);
                if (sala != null) {
                    ItemType itemType = ItemType.valueOf(tipoItem.toUpperCase().replace(" ", "_"));
                    Item item = new Item(itemType, (int) pontosItem);
                    sala.addItem(item);
                }
            }

            Edificio edificio = new Edificio(salas);

            for (Object inimigoObj : inimigosArray) {
                JSONObject inimigoJson = (JSONObject) inimigoObj;
                String nomeInimigo = (String) inimigoJson.get("nome");
                long poderInimigo = (long) inimigoJson.get("poder");
                String divisaoInimigo = (String) inimigoJson.get("divisao");

                Sala sala = findSala(salas, divisaoInimigo);
                if (sala != null) {
                    Inimigo inimigo = new Inimigo(nomeInimigo, (int) poderInimigo);
                    edificio.addInimigo(inimigo,sala);
                }
            }

            // Adiciona o alvo
            Sala salaAlvo = findSala(salas, alvoDivisao);
            return new Missao(codMissao, (int) versao, edificio, new Alvo(salaAlvo, alvoTipo));


        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo JSON: " + e.getMessage());
        } catch (ParseException e) {
            System.err.println("Erro ao parsear o JSON: " + e.getMessage());
        }
        return null;
    }

    public static void WriteJson() {
        Relatorio relatorio = new Relatorio();
        try (FileWriter arquivoJson = new FileWriter("mapa.json")) {
            arquivoJson.write(relatorio.toJsonString());
        } catch (IOException e) {
            System.err.println("Erro ao escrever o JSON: " + e.getMessage());
        }
    }

    /**
     * Procura por uma sala no grafo de salas com base no nome.
     *
     * @param salas    o grafo de salas.
     * @param nomeSala o nome da sala a ser procurada.
     * @return a sala encontrada, ou null se não for encontrada.
     */
    private static Sala findSala(GraphNetwork<Sala> salas, String nomeSala) {
        for (Iterator<Sala> it = salas.iteratorBFS(salaIterator); it.hasNext(); ) {
            Sala sala = it.next();
            if (sala.getNome().equals(nomeSala)) {
                return sala;
            }
        }
        return null;
    }
}
