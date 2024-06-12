package gogood.gogoodapi.service;

import gogood.gogoodapi.domain.models.historicoRota.HistoricoRotas;
import gogood.gogoodapi.domain.models.historicoRota.HistoricoRotaPersist;
import gogood.gogoodapi.repository.HistoricoRotaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HistoricoRotaServiceTest {

    @Mock
    private HistoricoRotaRepository repository;

    @InjectMocks
    private HistoricoRotaService service;

    private HistoricoRotaPersist historicoRotaPersist;
    private HistoricoRotas historicoRotas;

    @BeforeEach
    void setUp() {
        historicoRotaPersist = new HistoricoRotaPersist();
        historicoRotaPersist.setId_usuario(1);
        historicoRotaPersist.setOrigem("A");
        historicoRotaPersist.setDestino("B");
        historicoRotaPersist.setMeio_locomocao("Car");

        historicoRotas = new HistoricoRotas();
        historicoRotas.setId(1);
        historicoRotas.setIdUsuario(1);
        historicoRotas.setOrigem("A");
        historicoRotas.setDestino("B");
        historicoRotas.setMeio_locomocao("Car");
        historicoRotas.setCreated_at(new Date());
    }

    @Test
    void testSave() {
        when(repository.save(any(HistoricoRotas.class))).thenReturn(historicoRotas);

        HistoricoRotas saved = service.save(historicoRotaPersist);

        assertNotNull(saved);
        assertEquals(historicoRotas.getId(), saved.getId());
        verify(repository, times(1)).save(any(HistoricoRotas.class));
    }

    @Test
    void testFindByIdUser() {
        when(repository.findByIdUsuario(1)).thenReturn(Optional.of(Arrays.asList(historicoRotas)));

        List<HistoricoRotas> found = service.findByIdUser(1);

        assertNotNull(found);
        assertEquals(1, found.size());
        verify(repository, times(1)).findByIdUsuario(1);
    }

    @Test
    void testFindByIdUserNotFound() {
        when(repository.findByIdUsuario(1)).thenReturn(Optional.empty());

        List<HistoricoRotas> found = service.findByIdUser(1);

        assertNull(found);
        verify(repository, times(1)).findByIdUsuario(1);
    }

    @Test
    void testDeleteAll() {
        doNothing().when(repository).deleteAllByIdUsuario(1);
        doNothing().when(repository).resetAutoIncrement();

        service.deleteAll(1);

        verify(repository, times(1)).deleteAllByIdUsuario(1);
        verify(repository, times(1)).resetAutoIncrement();
    }
}
