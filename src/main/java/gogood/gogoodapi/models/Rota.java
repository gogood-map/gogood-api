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
public class Rota {
    private String origem;
    private String destino;
    private Double distancia;
    private String duracao;
    private String horarioSaida;
    private String horarioChegada;
    private List<Etapa> etapas;
    private Double duracaoNumerica;

    public Rota(){
        etapas = new ArrayList<>();
    }
}
