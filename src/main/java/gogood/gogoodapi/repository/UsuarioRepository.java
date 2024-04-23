package gogood.gogoodapi.repository;

import gogood.gogoodapi.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, String> {
}
