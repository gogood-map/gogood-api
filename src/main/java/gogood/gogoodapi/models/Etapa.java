package gogood.gogoodapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Etapa {
    @JsonIgnore
    private Coordenada coordenadaInicial;
    @JsonIgnore
    private Coordenada coordenadaFinal;
    @JsonIgnore
    private Double distancia;
    private String instrucao;
    @JsonIgnore
    private List<Etapa> subEtapas;

    public Etapa(){
        subEtapas = new ArrayList<>();
    }
}
