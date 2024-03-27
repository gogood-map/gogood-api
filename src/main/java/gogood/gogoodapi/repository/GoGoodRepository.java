package gogood.gogoodapi.repository;

import gogood.gogoodapi.models.Ocorrencia;
import org.springframework.data.repository.CrudRepository;

public interface GoGoodRepository extends CrudRepository<Ocorrencia, String> {
}
