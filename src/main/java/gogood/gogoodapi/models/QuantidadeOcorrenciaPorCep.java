package gogood.gogoodapi.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@Setter
@Document(value = "viewQtdOcorrenciasPorCep")

public class QuantidadeOcorrenciaPorCep {
    @MongoId
    private String id;
    private Double count;

    public QuantidadeOcorrenciaPorCep(String id, Double count) {
        this.id = id;
        this.count = count;
    }

    public QuantidadeOcorrenciaPorCep() {
    }

}
