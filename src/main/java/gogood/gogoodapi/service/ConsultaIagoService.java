package gogood.gogoodapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import gogood.gogoodapi.domain.models.iago.IagoParams;
import gogood.gogoodapi.domain.models.iago.IagoPersist;
import io.github.cdimascio.dotenv.Dotenv;
import io.weaviate.client.Config;
import io.weaviate.client.WeaviateClient;
import io.weaviate.client.base.Result;
import io.weaviate.client.v1.graphql.model.GraphQLResponse;
import io.weaviate.client.v1.graphql.query.argument.NearTextArgument;
import io.weaviate.client.v1.graphql.query.fields.Field;
import io.weaviate.client.v1.graphql.query.fields.GenerativeSearchBuilder;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Service
public class ConsultaIagoService {

    private WeaviateClient client;
    private final Dotenv dotenv = Dotenv.load();
    private final String apiKey = dotenv.get("GEMINI_KEY");
    private Integer limit = 100;
    private Integer endPage = 3;
    private String prePrompt = """
            Você deve unir todas as respostas fornecidas, mesclar dados duplicados somando suas ocorrências.
            Forneça uma única resposta de tudo com o que faz mas sentido com esse prompt:
            """;
    private String posPrompt = """
            Você é um analista de dados responsável por consolidar e analisar dados de ocorrências criminais de diferentes bairros. Você possui várias respostas separadas que precisam ser unificadas, eliminando duplicidades e extraindo insights significativos.
            """;


    public ConsultaIagoService() {
        Map<String, String> headers = new HashMap<String, String>() {{
            put("X-Palm-Api-Key", apiKey);
            put("X-Google-Vertex-Api-Key", apiKey);
            put("X-Google-Studio-Api-Key", apiKey);
        }};

        Config config = new Config("http", "gogood.brazilsouth.cloudapp.azure.com:8080", headers);
        this.client = new WeaviateClient(config);
    }

    public Result<GraphQLResponse> performQuery(String promptInput, int limit, int offset) {
        String promptQuery = prePrompt + " " + promptInput;

        NearTextArgument nearText = NearTextArgument.builder()
                .concepts(getNearTexts(promptInput)).build();;


        try {
            Result<GraphQLResponse> result = client.graphQL().get()
                    .withClassName("OcorrenciaDetalhada")
                    .withFields(
                            Field.builder().name("crime").build(),
                            Field.builder().name("bairro").build(),
                            Field.builder().name("cidade").build(),
                            Field.builder().name("data_ocorrencia").build(),
                            Field.builder().name("delegacia").build(),
                            Field.builder().name("lat").build(),
                            Field.builder().name("lng").build(),
                            Field.builder().name("periodo").build(),
                            Field.builder().name("rua").build(),
                            Field.builder().name("ano").build()
                    )
                    .withNearText(nearText)
                    .withLimit(limit)
                    .withGenerativeSearch(GenerativeSearchBuilder.builder()
                            .groupedResultTask(promptQuery)
                            .groupedResultProperties(new String[]{"crime", "bairro", "cidade", "data_ocorrencia", "delegacia", "lat", "lng", "periodo", "rua", "ano"})
                            .build())
                    .withOffset(offset)
                    .run();

            return result;
        } catch (Exception e) {
            System.err.println("Erro durante a execução da consulta: " + e.getMessage());
            return null;
        }
    }

    public String[] getNearTexts(String promptUser){
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro-latest:generateContent?key=" + apiKey;
        Map<String, Object> requestBody = new HashMap<>();
        Map<String, Object> contents = new HashMap<>();
        Map<String, Object> parts = new HashMap<>();

        String prompt = "Com base nesse prompt, crie conceitos para uma busca proximal (nearText), me retorne apenas a string com os conceitos (ignores conceitos que não tem relação com criminalidade, relatorio, ocorrencia policial, São Paulo):" + "\n\n" + promptUser;
        parts.put("text", prompt);
        contents.put("parts", Collections.singletonList(parts));
        requestBody.put("contents", Collections.singletonList(contents));



        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(requestBody)))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Map<String, Object> responseData = new ObjectMapper().readValue(response.body(), Map.class);
                List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseData.get("candidates");
                for (Map<String, Object> candidate : candidates) {
                    Map<String, Object> content = (Map<String, Object>) candidate.get("content");
                    List<Map<String, Object>> contentParts = (List<Map<String, Object>>) content.get("parts");
                    for (Map<String, Object> part : contentParts) {
                        if (part.containsKey("text")) {
                            return new String[]{
                                    (String) part.get("text")
                            };
                        }
                    }
                }
            } else {
                System.err.println("Erro ao gerar resumo com Gemini: " + response.statusCode() + " - " + response.body());
            }
        } catch (Exception e) {
            System.err.println("Erro ao enviar a solicitação: " + e.getMessage());
        }
        return null;
    }

    public Object consultarGemini(String prompt) {
        ConsultaIagoService consulta = new ConsultaIagoService();

        List<GraphQLResponse> allResponses = new ArrayList<>();
        int offset = 0;

        for (int page = 1; page <= endPage; page++) {
            Result<GraphQLResponse> result = consulta.performQuery(prompt, limit, offset);
            if (result != null && result.getResult() != null) {
                allResponses.add(result.getResult());
            } else {
                break;
            }
            offset += limit;
        }

        List<String> summarizedData = consulta.summarizeResults(allResponses);
        List<Map<String, Object>> structuredData = consulta.extractStructuredData(allResponses);
        String combinedData = String.join(" ", summarizedData);

        String finalSummary = consulta.generateFinalSummaryGemini(prompt, combinedData, structuredData, apiKey);
        return (Objects.requireNonNullElse(finalSummary, "Não foi possível gerar um resumo final."));
    }


    public List<String> summarizeResults(List<GraphQLResponse> responses) {
        List<String> summarizedData = new ArrayList<>();
        for (GraphQLResponse response : responses) {
            if (response.getErrors() != null) {
                System.out.println("Erro na consulta: " + Arrays.toString(response.getErrors()));
                return Collections.emptyList();
            }

            Map<String, Object> data = (Map<String, Object>) response.getData();
            if (data != null && data.containsKey("Get")) {
                Map<String, Object> get = (Map<String, Object>) data.get("Get");
                if (get.containsKey("OcorrenciaDetalhada")) {
                    List<Map<String, Object>> ocorrencias = (List<Map<String, Object>>) get.get("OcorrenciaDetalhada");
                    for (Map<String, Object> ocorrencia : ocorrencias) {
                        Map<String, Object> additional = (Map<String, Object>) ocorrencia.get("_additional");
                        if (additional != null && additional.containsKey("generate")) {
                            Map<String, Object> generate = (Map<String, Object>) additional.get("generate");
                            String groupedResult = (String) generate.get("groupedResult");
                            if (groupedResult != null) {
                                summarizedData.add(groupedResult);
                            }
                        }
                    }
                }
            }
        }
        return summarizedData;
    }

    public String generateFinalSummaryGemini(String promptUser, String combinedData, List<Map<String, Object>> structuredData, String apiKey) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro-latest:generateContent?key=" + apiKey;
        Map<String, Object> requestBody = new HashMap<>();
        Map<String, Object> contents = new HashMap<>();
        Map<String, Object> parts = new HashMap<>();

        String context = "Eu extrai os dados da SSP e coloquei aqui para você.";
        String prompt = "Você deve unir todas as respostas fornecidas, mesclar dados duplicados somando suas ocorrências (ignore um dado caso ele tenha informado não conseguir informações). Forneça uma única resposta de tudo com o que faz mas sentido"
                + " Dados combinados: " + combinedData + " Dados estruturados: " + structuredData;

        parts.put("text", context + " " + prompt+ "\"");
        contents.put("parts", Collections.singletonList(parts));
        requestBody.put("contents", Collections.singletonList(contents));

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(requestBody)))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Map<String, Object> responseData = new ObjectMapper().readValue(response.body(), Map.class);
                List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseData.get("candidates");
                for (Map<String, Object> candidate : candidates) {
                    Map<String, Object> content = (Map<String, Object>) candidate.get("content");
                    List<Map<String, Object>> contentParts = (List<Map<String, Object>>) content.get("parts");
                    for (Map<String, Object> part : contentParts) {
                        if (part.containsKey("text")) {
                            return (String) part.get("text");
                        }
                    }
                }
            } else {
                System.err.println("Erro ao gerar resumo com Gemini: " + response.statusCode() + " - " + response.body());
            }
        } catch (Exception e) {
            System.err.println("Erro ao enviar a solicitação: " + e.getMessage());
        }
        return null;
    }

    public List<Map<String, Object>> extractStructuredData(List<GraphQLResponse> responses) {
        List<Map<String, Object>> structuredData = new ArrayList<>();
        for (GraphQLResponse response : responses) {
            if (response.getErrors() != null) {
                System.out.println("Erro na consulta: " + Arrays.toString(response.getErrors()));
                continue;
            }

            Map<String, Object> data = (Map<String, Object>) response.getData();
            if (data != null && data.containsKey("Get")) {
                Map<String, Object> get = (Map<String, Object>) data.get("Get");
                if (get.containsKey("OcorrenciaDetalhada")) {
                    List<Map<String, Object>> ocorrencias = (List<Map<String, Object>>) get.get("OcorrenciaDetalhada");
                    for (Map<String, Object> ocorrencia : ocorrencias) {
                        Map<String, Object> structuredEntry = new HashMap<>();
                        structuredEntry.put("crime", ocorrencia.get("crime"));
                        structuredEntry.put("bairro", ocorrencia.get("bairro"));
                        structuredEntry.put("cidade", ocorrencia.get("cidade"));
                        structuredEntry.put("data_ocorrencia", ocorrencia.get("data_ocorrencia"));
                        structuredEntry.put("delegacia", ocorrencia.get("delegacia"));
                        structuredEntry.put("lat", ocorrencia.get("lat"));
                        structuredEntry.put("lng", ocorrencia.get("lng"));
                        structuredEntry.put("periodo", ocorrencia.get("periodo"));
                        structuredEntry.put("rua", ocorrencia.get("rua"));
                        structuredEntry.put("ano", ocorrencia.get("ano"));

                        structuredData.add(structuredEntry);
                    }
                }
            }
        }
        return structuredData;
    }


    public Object mudarParametros(Integer limit, Integer page) {
        this.limit = limit;
        this.endPage = page;
        return new IagoParams(limit, page);
    }

    public Object consultarParametros() {
        return new IagoParams(limit, endPage);
    }

    public Object mudarPrompt(String prompt) {
        this.prePrompt = prompt;
        return new IagoPersist(prompt);
    }

    public Object consultarPrompt() {
        return new IagoPersist(prePrompt);
    }

    public Object mudarPosPrompt(String prompt) {
        this.posPrompt = prompt;
        return new IagoPersist(prompt);
    }

    public Object consultarPosPrompt() {
        return new IagoPersist(posPrompt);
    }

}