package gogood.gogoodapi.repository;

import gogood.gogoodapi.domain.models.historicoRota.HistoricoRotas;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HistoricoRotaRepository extends CrudRepository<HistoricoRotas, Integer> {

    Optional<List<HistoricoRotas>> findByIdUsuario(Integer idUsuario);
}
