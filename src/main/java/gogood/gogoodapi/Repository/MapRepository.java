package gogood.gogoodapi.Repository;

import gogood.gogoodapi.Models.MapList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MapRepository extends CrudRepository<MapList, String> {
}
