package gogood.gogoodapi.domain.models.endereco;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class EnderecosUsuarios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "id_usuario")
    private int idUsuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_endereco")
    private Enderecos enderecos;

    @Column(name = "tipo_endereco", length = 63)
    private String tipoEndereco;
}
