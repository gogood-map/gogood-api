package gogood.gogoodapi.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Etapa {
    private Coordenada coordenadaInicial;
    private Coordenada coordenadaFinal;
    private Double distancia;
    private String instrucao;
    private List<Etapa> subEtapas;

    public Etapa(){
        subEtapas = new ArrayList<>();
    }
}
