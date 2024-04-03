package gogood.gogoodapi.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Entity
@Data

public class Ocorrencias implements Serializable {

    @Id
    private String id;
    private Double latitude;
    private Double longitude;
}
