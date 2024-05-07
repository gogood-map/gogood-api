package gogood.gogoodapi.domain.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor

public class Coordenada {
   final private Double lat;
   final private Double lng;

    @Override
    public String toString() {
        return "Coordenada:{%s,%s}".formatted(lat.toString(), lng.toString());
    }
}
