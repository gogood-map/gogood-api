package gogood.gogoodapi.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
@Data
@Document(value = "ocorrencias-detalhadas")
public class Ocorrencia {
    @Id
    @JsonIgnore
    private String id;

    @Field("num_bo")
    private String numBo;

    private String crime;

    @Field("tipo_local")
    private String tipoLocal;

    private String rua;

    private String bairro;

    private String delegacia;

    private String cidade;

    @Field("data_ocorrencia")
    private String dataOcorrencia;

    @Field("data_abertura_bo")
    private String dataAberturaBo;

    private Point localizacao;

}
