package Data;

import Edificio.Edificio;
import Edificio.Sala;
import Enum.ItemType;
import Graphs.GraphNetwork;
import Interfaces.OrderedListADT;
import Item.Item;
import Missao.*;
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

    private static OrderedListADT<Missao> missoes;
    private static Relatorios relatorios;

    public static Missao ReadMissao(String filePath) {
        JSONParser jsonParser = new JSONParser();
        int bothFind = 0;

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
            Sala[] salasArray = new Sala[edificioArray.size()];
            int index = 0;
            GraphNetwork<Sala> salas = new GraphNetwork<>();

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
                salasArray [index] = sala;
                index++;

                salas.addVertex(sala);
            }

            // Adiciona as ligações (arestas) entre as salas
            JSONArray ligacoesArray = (JSONArray) jsonObject.get("ligacoes");
            for (Object ligacaoObj : ligacoesArray) {
                JSONArray ligacao = (JSONArray) ligacaoObj;
                String sala1 = (String) ligacao.get(0);
                String sala2 = (String) ligacao.get(1);

                int pos1 = -1;
                int pos2 = -1;

                for (int i = 0; i < salasArray.length; i++) {
                    if (salasArray[i].getNome().trim().equalsIgnoreCase(sala1.trim())) {
                        pos1 = i;
                    }
                    if (salasArray[i].getNome().trim().equalsIgnoreCase(sala2.trim())) {
                        pos2 = i;
                    }
                }

                if (pos1 == -1 || pos2 == -1) {
                    System.err.println("Erro ao criar ligação entre: " + sala1 + " e " + sala2);
                    continue;
                }

                salas.addEdge(salasArray[pos1], salasArray[pos2]);
            }


            // Adiciona os itens às salas
            JSONArray itensArray = (JSONArray) jsonObject.get("itens");
            for (Object itemObj : itensArray) {
                JSONObject itemJson = (JSONObject) itemObj;
                String divisaoItem = (String) itemJson.get("divisao");
                long pontosItem = (long) itemJson.get("pontos");
                String tipoItem = (String) itemJson.get("tipo");

                Sala sala = findSala(salasArray, divisaoItem);
                if (sala != null) {
                    ItemType itemType = ItemType.fromString(tipoItem);
                    Item item = new Item(itemType, (int) pontosItem);
                    sala.addItem(item);
                }
            }

            Edificio edificio = new Edificio(salas);

            // Adiciona os inimigos às salas
            JSONArray inimigosArray = (JSONArray) jsonObject.get("inimigos");
            for (Object inimigoObj : inimigosArray) {
                JSONObject inimigoJson = (JSONObject) inimigoObj;
                String nomeInimigo = (String) inimigoJson.get("nome");
                long poderInimigo = (long) inimigoJson.get("poder");
                String divisaoInimigo = (String) inimigoJson.get("divisao");

                Sala sala = findSala(salasArray, divisaoInimigo);
                if (sala != null) {
                    Inimigo inimigo = new Inimigo(nomeInimigo, (int) poderInimigo);
                    edificio.addInimigo(inimigo,sala);
                }
            }

            // Adiciona o alvo
            Sala salaAlvo = findSala(salasArray, alvoDivisao);
            return new Missao(codMissao, (int) versao, edificio, new Alvo(salaAlvo, alvoTipo));


        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo JSON: " + e.getMessage());
        } catch (ParseException e) {
            System.err.println("Erro ao parsear o JSON: " + e.getMessage());
        }
        return null;
    }

    public static void WriteJson() {

        try (FileWriter arquivoJson = new FileWriter("mapa.json")) {
            arquivoJson.write(relatorios.toJsonString());
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
    private static Sala findSala(Sala [] salas, String nomeSala) {
        for (int i = 0; i < salas.length; i++) {

            if (salas[i].getNome().equals(nomeSala)) {
                return salas[i];
            }
        }
        return null;
    }
}
