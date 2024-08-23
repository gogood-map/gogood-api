package gogood.gogoodapi.domain.models.rotas;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class RotaShareResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 8523305899594565689L;
    private String id;
    private String url;

    @Override
    public String toString() {
        return "RotaShareResponse{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
