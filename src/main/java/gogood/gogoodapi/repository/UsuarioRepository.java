package gogood.gogoodapi.repository;


import gogood.gogoodapi.domain.DTOS.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface UsuarioRepository extends JpaRepository<Usuarios, Integer> {
    Optional<Usuarios> findByEmail(String email);
    @Query("SELECT u FROM Usuarios u where u.google_id like ?1")
    Optional<Usuarios> buscarPeloGoogleId(String google_id);

    @Query("SELECT u.google_id FROM Usuarios u where u.google_id like ?1")
    Optional<String> buscarGoogleId(String google_id);
}
