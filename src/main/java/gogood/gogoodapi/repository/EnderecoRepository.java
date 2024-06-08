package gogood.gogoodapi.repository;

import gogood.gogoodapi.domain.models.endereco.Enderecos;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnderecoRepository extends CrudRepository<Enderecos, Integer> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Enderecos e WHERE e.id = :idEndereco")
    void deleteById(Integer idEndereco);

}
