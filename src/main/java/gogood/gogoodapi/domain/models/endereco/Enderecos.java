package gogood.gogoodapi.domain.models.endereco;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
@Entity
public class Enderecos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 9)
    private String cep;

    @Column(length = 255)
    private String rua;

    @Column
    private int numero;

    @Column(length = 255)
    private String bairro;

    @Column(name = "created_at")
    private Date createdAt;

    @JsonIgnore
    @OneToMany(mappedBy = "enderecos", fetch = FetchType.LAZY)
    private Set<EnderecosUsuarios> enderecosUsuarios;

}
