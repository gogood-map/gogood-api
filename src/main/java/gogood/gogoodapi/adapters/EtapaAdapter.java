package gogood.gogoodapi.adapters;

import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsStep;
import gogood.gogoodapi.models.Coordenada;
import gogood.gogoodapi.models.Etapa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EtapaAdapter {

    public static List<Etapa> tranformarEtapas(DirectionsStep[] steps){
            List<Etapa> etapas = new ArrayList<>();

            for (DirectionsStep etapaFor:steps){
                Etapa etapa = new Etapa();
                etapa.setInstrucao(etapaFor.htmlInstructions);
                etapa.setDistancia(etapaFor.distance.inMeters/1000.);
                etapa.setCoordenadaFinal(new Coordenada(etapaFor.endLocation.lat,etapaFor.endLocation.lng));
                etapa.setCoordenadaInicial(new Coordenada(etapaFor.startLocation.lat,etapaFor.startLocation.lng));

                DirectionsStep[] subEtapas = etapaFor.steps;
                if(subEtapas != null){
                    List<Etapa> subEtapasConvertidas = new ArrayList<>();
                    for (DirectionsStep subEtapa: subEtapas){
                        subEtapasConvertidas = tranformarEtapas(subEtapas);
                    }

                    etapa.setSubEtapas(subEtapasConvertidas);
                }

                etapas.add(etapa);
            }


        return etapas;
    }
}
