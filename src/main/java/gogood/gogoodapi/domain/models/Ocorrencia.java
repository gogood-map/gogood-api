package gogood.gogoodapi.domain.models;

import com.mongodb.client.model.geojson.Point;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDate;
@Data
@Document(value = "ocorrencias-detalhadas")
public class Ocorrencia {
    @Id
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

    private String periodo;

    private Point localizacao;

}
