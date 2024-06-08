package gogood.gogoodapi.domain.models.endereco;


import com.fasterxml.jackson.annotation.JsonBackReference;
import gogood.gogoodapi.domain.models.Usuarios;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class EnderecosUsuarios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    @JsonBackReference
    private Usuarios usuarios;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_endereco")
    @JsonBackReference
    private Enderecos enderecos;

    private String tipoEndereco;
}
