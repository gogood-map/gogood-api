package gogood.gogoodapi.rotas.models;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class CoordenadaRua {
    private String logradouro;
    private Coordenada coordenada;

    public CoordenadaRua(String logradouro, Coordenada coordenada) {
        this.logradouro = logradouro;
        this.coordenada = coordenada;
    }
}
