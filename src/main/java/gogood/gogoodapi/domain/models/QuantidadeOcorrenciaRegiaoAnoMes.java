package gogood.gogoodapi.domain.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
@Data
@Document(value = "viewQtdOcorrenciaRegiao")
public class QuantidadeOcorrenciaRegiaoAnoMes {
    @MongoId
    private InfoRegiaoAnoMes infoRegiaoAnoMes;
    private int count;
}
