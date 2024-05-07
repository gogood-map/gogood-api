package gogood.gogoodapi.repository;

import gogood.gogoodapi.domain.models.Usuarios;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuarios, Integer> {
}
