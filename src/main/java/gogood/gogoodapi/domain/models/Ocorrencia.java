package gogood.gogoodapi.domain.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDate;
@Data
@Document(value = "ocorrencias-detalhadas")
public class Ocorrencia {
    @MongoId
    private String _id;

    private Double lat;

    private Double lng;

    private String crime;

    private Integer ano;

    private String rua;

    private String bairro;

    private String delegacia;

    private String cidade;

    private LocalDate data_ocorrencia;

    private String periodo;

}
