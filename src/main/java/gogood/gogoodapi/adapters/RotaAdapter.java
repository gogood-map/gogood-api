package gogood.gogoodapi.adapters;

import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.LatLng;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.utils.PolylineUtils;
import gogood.gogoodapi.models.Coordenada;
import gogood.gogoodapi.models.Etapa;
import gogood.gogoodapi.models.Rota;
import gogood.gogoodapi.services.AzureMapsService;
import gogood.gogoodapi.services.MongoService;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RotaAdapter{
    public Mono<List<Rota>> transformarRotas(DirectionsResult result){


        List<Rota> rotas = new ArrayList<>();

        for (int i = 0; i < result.routes.length && i < 3; i++) {
            var resultadoGoogle = result.routes[i];
            var resultadoRotaGoogleRota = resultadoGoogle.legs[0];

            rotas.add(transformarRota(resultadoRotaGoogleRota));



            Rota rotaAtual = rotas.get(i);
            rotaAtual.setPolyline(resultadoGoogle.overviewPolyline.getEncodedPath());
            definirCeps(rotaAtual);
            definirFlag(rotaAtual);
        }

        return Mono.just(rotas);
    }

    private Rota transformarRota(DirectionsLeg directionsLeg){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        Date horaAtual = new Date();

        Calendar  calendario = Calendar.getInstance();
        calendario.add(Calendar.SECOND,(int)directionsLeg.duration.inSeconds);
        Date horaChegada = calendario.getTime();


        Rota rota = new Rota();
        rota.setDestino(directionsLeg.endAddress);
        rota.setOrigem(directionsLeg.startAddress);
        rota.setEtapas(EtapaAdapter.tranformarEtapas(directionsLeg.steps));
        rota.setDuracao(directionsLeg.duration.humanReadable);


        calendario.setTime(horaChegada);

        Duration diferenca = Duration.between(
                horaAtual.toInstant(), horaChegada.toInstant()
        );

        rota.setDuracaoSegundos(diferenca.getSeconds());

        rota.setHorarioSaida(format.format(horaAtual));

        rota.setHorarioChegada(format.format(horaChegada));

        rota.setDistancia((double) directionsLeg.distance.inMeters /1000);

        return rota;
    }
    private void definirQuantidadeOcorrencias(){

    }

    private void definirCeps(Rota rota){

        String itensRequest = "{\"query\": \"?query=%s,%s\"}";
        StringBuilder corpoRequisicao = new StringBuilder();

        List<Etapa> etapas = rota.getEtapas();
        int qtdTotalCoordenadas = etapas.size();

        List<Point> coordenadas = new ArrayList<>();


        for (int i = 0; i < qtdTotalCoordenadas; i++) {

            Coordenada coordenadaInicialEtapa = etapas.get(i).getCoordenadaInicial();
            Coordenada coordenadaFinalEtapa = etapas.get(i).getCoordenadaFinal();

            Double latInicial = coordenadaInicialEtapa.getLat();
            Double lngInicial = coordenadaInicialEtapa.getLng();

            Double latFinal = coordenadaFinalEtapa.getLat();
            Double lngFinal =  coordenadaFinalEtapa.getLng();


            corpoRequisicao.append(
                    itensRequest.formatted(latFinal, lngFinal)
            );
            if(i<qtdTotalCoordenadas-1) corpoRequisicao.append(",");
        }
        List<String> ceps = new AzureMapsService().buscarCeps(corpoRequisicao.toString()).block();

        rota.setCeps(ceps);


    }

    public static void definirFlag(Rota rota){
        MongoService mongoService = new MongoService();
        int quantidadeTotalOcorrencias = mongoService.obterQuantidadeDeOcorrenciasTotais(rota.getCeps()).block();
        rota.setQtdOcorrenciasTotais(quantidadeTotalOcorrencias);
    }
}
