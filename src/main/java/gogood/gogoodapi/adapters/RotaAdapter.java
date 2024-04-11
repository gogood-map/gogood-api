package gogood.gogoodapi.adapters;

import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import gogood.gogoodapi.models.Rota;
import gogood.gogoodapi.services.AzureMapsService;
import gogood.gogoodapi.services.ClientGoogleMaps;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RotaAdapter {
    public static Rota transformarRota(DirectionsLeg directionsLeg){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        SimpleDateFormat formatDuracao = new SimpleDateFormat("hh:mm:ss");

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


        long distancia = directionsLeg.distance.inMeters;

        rota.setDistancia((double) distancia /1000);
        return rota;
    }
    public static List<Rota> transformarRota(DirectionsResult result){
        List<Rota> rotas = new ArrayList<>();

        for (int i = 0; i < result.routes.length && i < 3; i++) {
            rotas.add(transformarRota(result.routes[i].legs[0]));
            rotas.get(i).setPolyline(result.routes[i].overviewPolyline.getEncodedPath());

            Rota rotaAtual = rotas.get(i);
            String itensRequest = "{\"query\": \"?query=%s,%s\"}";
            StringBuilder batchRequest = new StringBuilder();
            rotaAtual.getEtapas().forEach(
                etapa -> batchRequest.append(
                        itensRequest.formatted(
                            etapa.getCoordenadaFinal().getLat().toString(),
                            etapa.getCoordenadaFinal().getLng()
                        )+","
                )
            );

            rotaAtual.setLogradouros(AzureMapsService.buscarLogradouros(batchRequest.substring(0, batchRequest.length()-1)));
        }

        return rotas;
    }
}
