package gogood.gogoodapi.domain.mappers;

import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import gogood.gogoodapi.domain.models.MapData;
import gogood.gogoodapi.domain.models.Ocorrencia;
import gogood.gogoodapi.domain.models.rotas.Rota;
import gogood.gogoodapi.repository.CustomQuantidadeOcorrenciaRuaRepository;
import gogood.gogoodapi.repository.QuantidadeOcorrenciaRuaRepository;
import gogood.gogoodapi.service.GeocodingService;
import gogood.gogoodapi.service.MapService;
import gogood.gogoodapi.utils.DecoderPolyline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RotaMapper {
    @Autowired
    GeocodingService geocodingService;
    @Autowired
    QuantidadeOcorrenciaRuaRepository repository;
    @Autowired
    private CustomQuantidadeOcorrenciaRuaRepository ocorrenciaRuaRepository;
    @Autowired
    private MapService mapService;
    private DecoderPolyline decoderPolyline;

    public RotaMapper(GeocodingService geocodingService, QuantidadeOcorrenciaRuaRepository repository) {
        this.geocodingService = geocodingService;
        this.repository = repository;
    }

    public List<Rota> toRota(DirectionsResult result) {
        return Arrays.stream(result.routes).limit(3).map(directionsRoute -> {
                    var resultadoRotaGoogleRota = directionsRoute.legs[0];
                    Rota rotaAtual = transformarRota(resultadoRotaGoogleRota);
                    rotaAtual.setPolyline(directionsRoute.overviewPolyline.getEncodedPath());
                    rotaAtual.setOcorrencias(definirPontosDaRota(rotaAtual));

                    definirLogradouros(rotaAtual);

                    return rotaAtual;
                })
                .collect(Collectors.toList());
    }

    private List<Ocorrencia> definirPontosDaRota(Rota rotaAtual) {
        List<double[]> listLoc = DecoderPolyline.decodePolyline(rotaAtual.getPolyline());
        List<MapData> localizacoes = new ArrayList<>();
        for (double[] loc : listLoc) {
            MapData mapData = new MapData();
            mapData.setLatitude(loc[0]);
            mapData.setLongitude(loc[1]);
            localizacoes.add(mapData);
        }

        List<Ocorrencia> todasOcorrencias = new ArrayList<>();
        for (MapData mapData : localizacoes) {
            List<Ocorrencia> ocorrencias = mapService.getOcorrenciasAcrossRoute(mapData.getLatitude(), mapData.getLongitude());
            ocorrencias.forEach(ocorrencia -> {
                if (!todasOcorrencias.contains(ocorrencia)) {
                    todasOcorrencias.add(ocorrencia);
                }
            });
        }
        return todasOcorrencias;
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


        Ocorrencia ocorrencia = new Ocorrencia();
//        ocorrencia.setLocalizacao();


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