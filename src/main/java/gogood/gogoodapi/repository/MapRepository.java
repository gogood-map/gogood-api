package gogood.gogoodapi.repository;

import gogood.gogoodapi.models.MapList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MapRepository extends CrudRepository<MapList, String> {
}
