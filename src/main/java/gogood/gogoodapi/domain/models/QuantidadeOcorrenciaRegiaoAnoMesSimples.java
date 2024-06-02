package gogood.gogoodapi.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@AllArgsConstructor
@Getter
@Setter
public class QuantidadeOcorrenciaRegiaoAnoMesSimples {
    private String anoMes;
    private int count;
}
