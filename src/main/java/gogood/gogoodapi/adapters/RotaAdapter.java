package gogood.gogoodapi.adapters;

import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsRoute;
import gogood.gogoodapi.models.Rota;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class RotaAdapter {
    public static Rota transformarRota(DirectionsLeg directionsLeg){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm");

        Date horaAtual = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND,(int)directionsLeg.duration.inSeconds);
        Date horaChegada = calendar.getTime();


        Rota rota = new Rota();
        rota.setDestino(directionsLeg.endAddress);
        rota.setOrigem(directionsLeg.startAddress);
        rota.setEtapas(EtapaAdapter.tranformarEtapas(directionsLeg.steps));
        rota.setDuracao(directionsLeg.duration.humanReadable);

        rota.setHorarioSaida(format.format(horaAtual));


        rota.setHorarioChegada(format.format(horaChegada));



        Long distancia = directionsLeg.distance.inMeters;

        rota.setDistancia(distancia.doubleValue()/1000);
        return rota;
    }
}
