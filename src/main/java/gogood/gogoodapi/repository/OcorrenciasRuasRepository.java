package gogood.gogoodapi.repository;

import gogood.gogoodapi.domain.models.OcorrenciaRua;
import jakarta.persistence.OrderBy;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OcorrenciasRuasRepository extends MongoRepository<OcorrenciaRua, String> {

    @Query(value = "{ '_id': { $in: ?0 } }", fields = "{ 'total': { $sum: '$count' } }")
    Long findTotalCountByRuas(List<String> ruas);


}
