package gogood.gogoodapi.controllers;

import gogood.gogoodapi.domain.models.historicoRota.HistoricoRotas;
import gogood.gogoodapi.domain.models.historicoRota.HistoricoRotaPersist;
import gogood.gogoodapi.service.HistoricoRotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/historico-rota")
@Tag(name = "Histórico de Rotas", description = "API para gerenciamento de histórico de rotas dos usuários")
public class HistoricoRotaController {
    @Autowired
    private HistoricoRotaService historicoRotaService;

    @Operation(summary = "Salvar Histórico de Rota", description = "Salva um novo histórico de rota no banco de dados")
    @PostMapping
    public ResponseEntity<HistoricoRotas> salvarHistoricoRota(@RequestBody HistoricoRotaPersist historicoRotaPersist) {
        HistoricoRotas historicoRotas = historicoRotaService.save(historicoRotaPersist);
        return ResponseEntity.status(201).body(historicoRotas);
    }

    @Operation(summary = "Buscar Histórico de Rota por ID de Usuário", description = "Busca todos os históricos de rotas associados a um ID de usuário específico")
    @GetMapping("/{id}")
    public ResponseEntity<List<HistoricoRotas>> buscarHistoricoRota(@PathVariable Integer id) {
        List<HistoricoRotas> historicoRotas = historicoRotaService.findByIdUser(id);
        return ResponseEntity.status(200).body(historicoRotas);
    }

    @Operation(summary = "Deletar Histórico de Rota", description = "Deleta todos os históricos de rotas associados a um ID de usuário")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarHistoricoRota(@PathVariable Integer id) {
        historicoRotaService.deleteAll(id);
        return ResponseEntity.status(204).build();
    }
}
