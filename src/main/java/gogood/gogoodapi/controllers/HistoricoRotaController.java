package gogood.gogoodapi.controllers;

import gogood.gogoodapi.domain.models.historicoRota.HistoricoRotas;
import gogood.gogoodapi.domain.models.historicoRota.HistoricoRotaPersist;
import gogood.gogoodapi.service.HistoricoRotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/historico-rota")
public class HistoricoRotaController {
    @Autowired
    private HistoricoRotaService historicoRotaService;
    @PostMapping
    public ResponseEntity<HistoricoRotas> salvarHistoricoRota(@RequestBody HistoricoRotaPersist historicoRotaPersist) {
        HistoricoRotas historicoRotas = historicoRotaService.save(historicoRotaPersist);
        return ResponseEntity.status(201).body(historicoRotas);
    }
    @GetMapping("/{id}")
    public ResponseEntity<List<HistoricoRotas>> buscarHistoricoRota(@PathVariable Integer id) {
        List<HistoricoRotas> historicoRotas = historicoRotaService.findByIdUser(id);

        return ResponseEntity.status(200).body(historicoRotas);
    }
}