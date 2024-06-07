package gogood.gogoodapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
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
public class ConsultaGemini {

    private WeaviateClient client;
    private final Dotenv dotenv = Dotenv.load(); 
    private final String apiKey = dotenv.get("GOOGLE_API_KEY");
    public ConsultaGemini() {
        Map<String, String> headers = new HashMap<String, String>() {{
            put("X-Palm-Api-Key", apiKey);
            put("X-Google-Vertex-Api-Key", apiKey);
            put("X-Google-Studio-Api-Key", apiKey);
        }};

        Config config = new Config("http", "gogood.brazilsouth.cloudapp.azure.com:8080", headers);
        this.client = new WeaviateClient(config);
    }

    public Object consultarGemini(String prompt) {
        ConsultaGemini consulta = new ConsultaGemini();

        int limit = 250;
        int endPage = 5;

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
        String combinedData = String.join(" ", summarizedData);

        String finalSummary = consulta.generateFinalSummaryGemini(combinedData, apiKey);
        return (Objects.requireNonNullElse(finalSummary, "Não foi possível gerar um resumo final."));
    }

    public Result<GraphQLResponse> performQuery(String promptInput, int limit, int offset) {
        String promptAddition = "Com base APENAS nos dados, desconsidere qualquer outra coisa que você saiba sobre o mundo. " +
                "Caso seja necessário fazer suposições, faça-as de forma consistente com os dados. " +
                "Além do mais, caso peça alguma coisa relacionada a calcular algo, faça isso de forma aproximada e com base nos dados.";

        String prompt = promptInput + " " + promptAddition;

        NearTextArgument nearText = NearTextArgument.builder()
                .concepts(new String[]{prompt})
                .build();

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
                    .withGenerativeSearch(GenerativeSearchBuilder.builder()
                            .groupedResultTask(prompt)
                            .build())
                    .withNearText(nearText)
                    .withLimit(limit)
                    .withOffset(offset)
                    .run();

            return result;
        } catch (Exception e) {
            System.err.println("Erro durante a execução da consulta: " + e.getMessage());
            return null;
        }
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

    public String generateFinalSummaryGemini(String combinedData, String apiKey) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=" + apiKey;
        Map<String, Object> requestBody = new HashMap<>();
        Map<String, Object> contents = new HashMap<>();
        Map<String, Object> parts = new HashMap<>();
        parts.put("text", "Una estas informações a seguir sem usar nenhum conhecimento do mundo, não ofusque os dados em números, caso tenha, não altere nada relevante, a menos que o dado seja repetitivo: \"" + combinedData + "\"");
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
}