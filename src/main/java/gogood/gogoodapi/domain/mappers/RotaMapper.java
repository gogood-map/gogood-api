package gogood.gogoodapi.domain.mappers;

import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import gogood.gogoodapi.domain.models.Rota;
import gogood.gogoodapi.repository.CustomQuantidadeOcorrenciaRuaRepository;
import gogood.gogoodapi.repository.QuantidadeOcorrenciaRuaRepository;
import gogood.gogoodapi.service.GeocodingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

@Service
public class RotaMapper {
    @Autowired
    GeocodingService geocodingService;
    @Autowired
    QuantidadeOcorrenciaRuaRepository repository;
    @Autowired
    private CustomQuantidadeOcorrenciaRuaRepository ocorrenciaRuaRepository;



    public RotaMapper(GeocodingService geocodingService, QuantidadeOcorrenciaRuaRepository repository) {
        this.geocodingService = geocodingService;
        this.repository = repository;
    }

    public List<Rota> toRota(DirectionsResult result) {


        List<Rota> rotas = new ArrayList<>();

        for (int i = 0; i < result.routes.length && i < 3; i++) {
            var resultadoGoogle = result.routes[i];
            var resultadoRotaGoogleRota = resultadoGoogle.legs[0];

            rotas.add(transformarRota(resultadoRotaGoogleRota));

            Rota rotaAtual = rotas.get(i);
            rotaAtual.setPolyline(resultadoGoogle.overviewPolyline.getEncodedPath());


            definirLogradouros(rotaAtual);
        }

        return rotas;
    }

    private Rota transformarRota(DirectionsLeg directionsLeg) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        Date horaAtual = new Date();

        Calendar calendario = Calendar.getInstance();
        calendario.add(Calendar.SECOND, (int) directionsLeg.duration.inSeconds);
        Date horaChegada = calendario.getTime();


        Rota rota = new Rota();
        rota.setDestino(directionsLeg.endAddress);
        rota.setOrigem(directionsLeg.startAddress);
        rota.setEtapas(EtapaMapper.toEtapa(directionsLeg.steps));
        rota.setDuracao(directionsLeg.duration.humanReadable);


        calendario.setTime(horaChegada);

        Duration diferenca = Duration.between(
                horaAtual.toInstant(), horaChegada.toInstant()
        );

        rota.setDuracaoSegundos(diferenca.getSeconds());

        rota.setHorarioSaida(format.format(horaAtual));

        rota.setHorarioChegada(format.format(horaChegada));

        rota.setDistancia((double) directionsLeg.distance.inMeters / 1000);

        return rota;
    }

    private void definirLogradouros(Rota rota) {
        List<String> logradouros = geocodingService.buscarLogradouros(rota.getEtapas());

        rota.setLogradouros(logradouros);

        logradouros.replaceAll(s -> s.replace("\"", ""));
        Integer qtdOcorrenciasTotais = ocorrenciaRuaRepository.getTotalOccurrencesByStreets(logradouros);
        rota.setQtdOcorrenciasTotais(qtdOcorrenciasTotais);
    }



}