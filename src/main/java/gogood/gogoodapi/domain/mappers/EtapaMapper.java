package gogood.gogoodapi.domain.mappers;

import com.google.maps.model.DirectionsStep;
import gogood.gogoodapi.domain.models.Coordenada;
import gogood.gogoodapi.domain.models.Etapa;

import java.util.ArrayList;
import java.util.List;
public class EtapaMapper {
    public static List<Etapa> toEtapa(DirectionsStep[] steps){
            List<Etapa> etapas = new ArrayList<>();

            for (DirectionsStep etapaFor:steps){
                Etapa etapa = new Etapa();
                etapa.setInstrucao(etapaFor.htmlInstructions);
                etapa.setDistancia(etapaFor.distance.inMeters/1000.);
                etapa.setCoordenadaInicial(new Coordenada(etapaFor.startLocation.lat,etapaFor.startLocation.lng));
                etapa.setCoordenadaFinal(new Coordenada(etapaFor.endLocation.lat,etapaFor.endLocation.lng));
                DirectionsStep[] subEtapas = etapaFor.steps;

                if(subEtapas != null){
                    List<Etapa> subEtapasConvertidas = new ArrayList<>();
                    for (int i = 0; i < subEtapas.length; i++) {
                        subEtapasConvertidas = toEtapa(subEtapas);
                    }

                    etapa.setSubEtapas(subEtapasConvertidas);
                }

                etapas.add(etapa);
            }
        return etapas;
    }
}
