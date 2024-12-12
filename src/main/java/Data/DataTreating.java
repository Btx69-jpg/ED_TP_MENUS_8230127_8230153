package Data;

import Edificio.Edificio;
import Edificio.Sala;
import Enum.ItemType;
import Graphs.GraphNetwork;
import Item.Item;
import LinkedList.LinearLinkedOrderedList;
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


public class DataTreating {
    private static LinearLinkedOrderedList<Missao> missoes = new LinearLinkedOrderedList<>();
    public static Relatorios relatorios = new Relatorios();

    public static Missao getMissaoByVersion(int Version) {
        for (Missao missao : missoes) {
            if (missao.getVersion() == Version) {
                return missao.clone();
            }
        }
        return null;
    }

    public static void removeMissaoByVersion(int Version) {
        for (Missao missao : missoes) {
            if (missao.getVersion() == Version) {
                missoes.remove(missao);
            }
        }
    }

    public static void removeMissao(Missao missaoremove) {
        for (Missao missao : missoes) {
            if (missao.equals(missaoremove)) {
                missoes.remove(missaoremove);
            }
        }
    }

    public static LinearLinkedOrderedList<Missao> getMissoes() {
        LinearLinkedOrderedList<Missao> missoesclone = new LinearLinkedOrderedList<>();
        for (Missao missao : missoes) {
                missoesclone.add(missao.clone());
        }
        return missoesclone;
    }

    public static void addRelatorio(Relatorio relatorio){
        relatorios.addRelatorio(relatorio);
    }

    public static void RemoveRelatorio(Relatorio relatorio){
        relatorios.removeRelatorio(relatorio);
    }

    public static void RemoveRelatoriosByVersion(int Version){
        relatorios.removeRelatorios(Version);
    }

    public static void GetRelatoriosByVersion(int Version) {
        relatorios.getRelatorios(Version);
    }

    public static void GetAllRelatorios() {
        relatorios.getAllRelatorios();
    }

    public static Relatorios getRelatorios() {
        return relatorios;
    }
    public static void ReadMissao(String filePath) {
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(filePath)) {
            // Lê o JSON como objeto
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

            // Lê o código da missão e a versão
            long versao = (long) jsonObject.get("versao");
            if (getMissaoByVersion((int) versao) == null) {
                String codMissao = (String) jsonObject.get("cod-missao");

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
                    salasArray[index] = sala;
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
                        edificio.addInimigo(inimigo, sala);
                    }
                }

                // Adiciona o alvo
                Sala salaAlvo = findSala(salasArray, alvoDivisao);
                missoes.add(new Missao(codMissao, (int) versao, edificio, new Alvo(salaAlvo, alvoTipo)));

            }else {
                System.out.println("Missão com esta versão já existe");
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo JSON: " + e.getMessage());
        } catch (ParseException e) {
            System.err.println("Erro ao parsear o JSON: " + e.getMessage());
        }

    }

    public static void ReadMissoes(String filePath) {
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(filePath)) {
            JSONArray missoesArray = (JSONArray) jsonParser.parse(reader);

            for (Object missaoObj : missoesArray) {

                // Lê o JSON como objeto
                JSONObject jsonObject = (JSONObject) missaoObj;

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
                    salasArray[index] = sala;
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
                        edificio.addInimigo(inimigo, sala);
                    }
                }

                // Adiciona o alvo
                Sala salaAlvo = findSala(salasArray, alvoDivisao);
                missoes.add(new Missao(codMissao, (int) versao, edificio, new Alvo(salaAlvo, alvoTipo)));

            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo JSON: " + e.getMessage());
        } catch (ParseException e) {
            System.err.println("Erro ao parsear o JSON: " + e.getMessage());
        }

    }

    public static void SaveMissoes() {
        JSONArray missoesArray = new JSONArray();

        for (Missao missao : missoes) {
            JSONObject missaoJson = new JSONObject();
            missaoJson.put("cod-missao", missao.getCod_missao());
            missaoJson.put("versao", missao.getVersion());

            JSONObject alvoJson = new JSONObject();
            alvoJson.put("divisao", missao.getAlvo().getLocalizacao().getNome());
            alvoJson.put("tipo", missao.getAlvo().getTipo());
            missaoJson.put("alvo", alvoJson);

            JSONArray edificioArray = new JSONArray();
            Iterator<Sala> itSalas = missao.getEdificio().getSalas().getVerticesIterator();
            while (itSalas.hasNext()){
                Sala sala = itSalas.next();
                edificioArray.add(sala.getNome());
            }
            missaoJson.put("edificio", edificioArray);

            JSONArray entradasSaidasArray = new JSONArray();
            for (Sala sala : missao.getEdificio().getEntradas_saidas()) {
                entradasSaidasArray.add(sala.getNome());
            }
            missaoJson.put("entradas-saidas", entradasSaidasArray);

            JSONArray ligacoesArray = new JSONArray();
            itSalas = missao.getEdificio().getSalas().getVerticesIterator();
            while (itSalas.hasNext()) {
                Sala sala = itSalas.next();
                for (Sala adjacente : missao.getEdificio().getSalas().getConnectedVertices(sala)) {
                    JSONArray ligacao = new JSONArray();
                    ligacao.add(sala.getNome());
                    ligacao.add(adjacente.getNome());
                    ligacoesArray.add(ligacao);
                }
            }
            missaoJson.put("ligacoes", ligacoesArray);

            JSONArray itensArray = new JSONArray();
            itSalas = missao.getEdificio().getSalas().getVerticesIterator();
            while (itSalas.hasNext()) {
                Sala sala = itSalas.next();
                for (Item item : sala.getItens()) {
                    JSONObject itemJson = new JSONObject();
                    itemJson.put("divisao", sala.getNome());
                    itemJson.put("pontos", item.getQuantidade());
                    itemJson.put("tipo", item.getTipo().toString());
                    itensArray.add(itemJson);
                }
            }
            missaoJson.put("itens", itensArray);

            JSONArray inimigosArray = new JSONArray();

            for (Sala SalaComInimigo : missao.getEdificio().getSalaComInimigos()) {
                for (Inimigo inimigo : SalaComInimigo.getInimigos()) {
                    JSONObject inimigoJson = new JSONObject();
                    inimigoJson.put("nome", inimigo.getNome());
                    inimigoJson.put("poder", inimigo.getPoder());
                    inimigoJson.put("divisao", SalaComInimigo.getNome());
                    inimigosArray.add(inimigoJson);
                }
            }
            missaoJson.put("inimigos", inimigosArray);

            missoesArray.add(missaoJson);
        }

        try (FileWriter file = new FileWriter(".\\missoes.json")) {
            file.write(missoesArray.toJSONString());
            System.out.println("Successfully Copied JSON Object to File...");
            System.out.println("\nJSON Object: " + missoesArray.toJSONString());
        } catch (IOException e) {
            System.err.println("Erro ao escrever o JSON: " + e.getMessage());
        }
    }

    public static void SaveRelatorios() {

        try (FileWriter arquivoJson = new FileWriter(".\\relatorios.json")) {
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