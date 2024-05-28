package gogood.gogoodapi.repository;

import gogood.gogoodapi.domain.models.endereco.EnderecosUsuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnderecosUsuariosRepository extends JpaRepository<EnderecosUsuarios, Integer> {
}