package gogood.gogoodapi.services;

import org.json.JSONObject;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

public class AzureMapsService {


    public static List<String> buscarLogradouros(String batchRequest){
        RestClient clienteRest = RestClient.create();
        var requisicao = clienteRest.post().uri(
                "https://atlas.microsoft.com/search/address/reverse/batch/sync/json?api-version=1.0&subscription-key=Tgehvd0GXs7mbEi3hb7ovpoNrfEtTkvlQMA8ANL-1zs"
        ).contentType(APPLICATION_JSON).body(
                """
                {
                    "batchItems": [
                        %s
                    ]
                }
                """.formatted(batchRequest)
        ).retrieve().toEntity(String.class).getBody();

        JSONObject jo = new JSONObject(requisicao);

        List<String> logradouros = new ArrayList<>();


        var itensRetorno = jo.getJSONArray("batchItems");
        int qtdItens = itensRetorno.length();

        for (int i = 0; i < qtdItens; i++) {

            int qtdRuasAproximadas = itensRetorno.getJSONObject(i).getJSONObject("response")
                    .getJSONArray("addresses").length();
            int indiceRuasAproximadas = 0;

            while (indiceRuasAproximadas < qtdRuasAproximadas){
                try {
                    String rua = itensRetorno.getJSONObject(i).getJSONObject("response")
                            .getJSONArray("addresses").getJSONObject(indiceRuasAproximadas).getJSONObject("address").getString("streetName");
                    logradouros.add(rua);
                }catch (Exception e){
                    indiceRuasAproximadas++;
                }
                indiceRuasAproximadas = qtdRuasAproximadas;
            }


        }

        return logradouros.stream().distinct().toList();
    }
}
