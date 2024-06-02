package gogood.gogoodapi.repository;

import gogood.gogoodapi.domain.models.endereco.Enderecos;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnderecoRepository extends CrudRepository<Enderecos, Integer> {
    @Query("SELECT e FROM Enderecos e JOIN e.enderecosUsuarios eu WHERE eu.idUsuario = :idUsuario")
    Optional<List<Enderecos>> findEnderecosByIdUsuario(@Param("idUsuario") int idUsuario);


    @Modifying
    @Transactional
    @Query("DELETE FROM Enderecos e WHERE e.id = :idEndereco")
    void deleteById(Integer idEndereco);

    @Modifying
    @Transactional
    @Query(value = "DELETE e FROM enderecos e LEFT JOIN enderecos_usuarios eu ON e.id = eu.id_endereco WHERE eu.id_endereco IS NULL", nativeQuery = true)
    void deleteOrphanedEnderecos();
}
