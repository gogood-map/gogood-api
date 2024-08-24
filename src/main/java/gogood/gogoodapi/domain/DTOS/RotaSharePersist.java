package gogood.gogoodapi.domain.DTOS;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RotaSharePersist implements Serializable {
    @Serial
    private static final long serialVersionUID = 9015789214740851790L;
    private String origem;
    private String destino;
    private String tipoTransporte;


    @Override
    public String toString() {
        return origem + "-" + destino + "-" + tipoTransporte;
    }
}
