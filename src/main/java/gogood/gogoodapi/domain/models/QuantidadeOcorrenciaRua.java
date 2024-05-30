package gogood.gogoodapi.domain.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@Setter
@Document(value = "viewOcorrenciaRua")

public class QuantidadeOcorrenciaRua {
    @MongoId
    private InfoLogradouro _id;
    private Integer count;
    private Integer count2023;
    private Integer count2024;

}
