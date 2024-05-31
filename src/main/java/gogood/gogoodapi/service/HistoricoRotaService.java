package gogood.gogoodapi.service;

import gogood.gogoodapi.domain.models.historicoRota.HistoricoRotas;
import gogood.gogoodapi.domain.models.historicoRota.HistoricoRotaPersist;
import gogood.gogoodapi.repository.HistoricoRotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class HistoricoRotaService {
    @Autowired
    HistoricoRotaRepository repository;
    public HistoricoRotas save(HistoricoRotaPersist historicoRotaPersist) {
        HistoricoRotas historicoRotas = new HistoricoRotas();
        historicoRotas.setIdUsuario(historicoRotaPersist.getId_usuario());
        historicoRotas.setOrigem(historicoRotaPersist.getOrigem());
        historicoRotas.setDestino(historicoRotaPersist.getDestino());
        historicoRotas.setMeio_locomocao(historicoRotaPersist.getMeio_locomocao());
        historicoRotas.setCreated_at(Date.from(new Date().toInstant()));
        return repository.save(historicoRotas);
    }

    public List<HistoricoRotas> findByIdUser(Integer id) {
        Optional<List<HistoricoRotas>> historicoRotas = repository.findByIdUsuario(id);
        return historicoRotas.orElse(null);
    }

    public void deleteAll(Integer id) {
        repository.deleteAllByIdUsuario(id);
    }
}
