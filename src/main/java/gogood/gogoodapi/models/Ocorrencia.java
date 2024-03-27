package gogood.gogoodapi.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Ocorrencia {

    @Id
    private String id;
    private Double latitude;
    private Double longitude;
}
