package gogood.gogoodapi.repository;

import gogood.gogoodapi.models.Ocorrencias;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoGoodRepository extends CrudRepository<Ocorrencias, Integer> {
}
