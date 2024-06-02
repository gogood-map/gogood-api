package gogood.gogoodapi.repository;

import gogood.gogoodapi.domain.models.historicoRota.HistoricoRotas;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HistoricoRotaRepository extends CrudRepository<HistoricoRotas, Integer> {

    Optional<List<HistoricoRotas>> findByIdUsuario(Integer idUsuario);

    @Transactional
    void deleteAllByIdUsuario(Integer idUsuario);

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE historico_rotas AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
}
