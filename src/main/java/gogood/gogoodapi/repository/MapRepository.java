package gogood.gogoodapi.repository;

import gogood.gogoodapi.domain.models.Ocorrencia;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MapRepository extends MongoRepository<Ocorrencia, String> {
    List<Ocorrencia> findByLocalizacaoNear(Point location, Distance distance);
}
