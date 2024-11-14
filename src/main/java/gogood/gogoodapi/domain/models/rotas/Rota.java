package gogood.gogoodapi.domain.models.rotas;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gogood.gogoodapi.domain.models.Etapa;
import gogood.gogoodapi.domain.models.Ocorrencia;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Rota {
    @JsonIgnore
    private List<String> logradouros;
    private String origem;
    private String destino;
    private Double distancia;
    private String duracao;
    private String horarioSaida;
    private String horarioChegada;
    private Integer qtdOcorrenciasTotais;
    private String polyline;

    private List<Etapa> etapas;
    @JsonIgnore
    private Long duracaoSegundos;
    @JsonIgnore
    private List<Ocorrencia> ocorrencias;



    public Rota(){
        etapas = new ArrayList<>();
    }
}
