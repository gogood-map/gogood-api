package gogood.gogoodapi.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@Setter
@Document(value = "viewQtdOcorrenciasPorCep")

public class QuantidadeOcorrenciaPorCep {
    @MongoId
    private String _id;
    private Integer count;

}
