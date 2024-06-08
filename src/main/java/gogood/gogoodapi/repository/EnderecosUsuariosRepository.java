package gogood.gogoodapi.repository;

import gogood.gogoodapi.domain.models.endereco.Enderecos;
import gogood.gogoodapi.domain.models.endereco.EnderecosUsuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnderecosUsuariosRepository extends JpaRepository<EnderecosUsuarios, Integer> {
    @Query("SELECT eu FROM EnderecosUsuarios eu JOIN FETCH eu.enderecos WHERE eu.usuarios.ID = ?1")
    List<EnderecosUsuarios> findByUsuarioId(Integer usuarioId);;

    @Query("SELECT eu FROM EnderecosUsuarios eu WHERE eu.usuarios.ID = ?1 AND eu.enderecos.id = ?2")
    Optional<EnderecosUsuarios> findByUsuarioIdAndEnderecosId(Integer usuarioId, Integer enderecoId);

    @Query("SELECT eu FROM EnderecosUsuarios eu WHERE eu.enderecos.id = ?1")
    List<EnderecosUsuarios> findByEnderecosId(Integer enderecoId);
}