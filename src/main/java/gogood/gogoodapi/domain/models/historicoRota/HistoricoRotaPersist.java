package gogood.gogoodapi.domain.models.historicoRota;

import lombok.Data;

@Data
public class HistoricoRotaPersist {
    private Integer id_usuario;
    private String origem;
    private String destino;
    private String meio_locomocao;
}
