package gogood.gogoodapi.controllers;

import gogood.gogoodapi.domain.DTOS.RotaSharePersist;
import gogood.gogoodapi.domain.mappers.RotaMapper;
import gogood.gogoodapi.domain.models.rotas.Rota;
import gogood.gogoodapi.domain.models.rotas.RotaShareResponse;
import gogood.gogoodapi.domain.strategy.RotaStrategy;
import gogood.gogoodapi.service.NavegacaoService;
import gogood.gogoodapi.domain.strategy.rotaStrategy.APeStrategy;
import gogood.gogoodapi.domain.strategy.rotaStrategy.BicicletaStrategy;
import gogood.gogoodapi.domain.strategy.rotaStrategy.TransportePublicoStrategy;
import gogood.gogoodapi.domain.strategy.rotaStrategy.VeiculoStrategy;
import gogood.gogoodapi.service.RotasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rotas")
@Tag(name = "Rotas", description = "Consultar rotas de transporte")
public class RotasController {

    @Autowired
    RotaMapper rotaMapper;

    @Autowired
    private RotasService rotasService;

    @Autowired
    private NavegacaoService navegacaoService;

    @Operation(summary = "Obter rota de transporte público", description = "Obtém a rota de transporte público entre dois pontos")
    @GetMapping("/transporte-publico")
    public ResponseEntity<List<Rota>> obterRotaTransportePublico(@RequestParam String destino, @RequestParam String origem){
        RotaStrategy tipoTransporte = new TransportePublicoStrategy(rotaMapper);
        String id = UUID.randomUUID().toString();
        List<Rota> rota = navegacaoService.escolherStrategy(id, tipoTransporte, origem, destino);
        return ResponseEntity.status(200).body(rota);
    }

    @Operation(summary = "Obter rota de bicicleta", description = "Obtém a rota de bicicleta entre dois pontos")
    @GetMapping("/bike")
    public ResponseEntity<List<Rota>> obterRotaBike(@RequestParam String destino, @RequestParam String origem){
        RotaStrategy tipoTransporte = new BicicletaStrategy(rotaMapper);
        String id = UUID.randomUUID().toString();
        List<Rota> rota = navegacaoService.escolherStrategy(id, tipoTransporte, origem, destino);
        return ResponseEntity.status(200).body(rota);
    }

    @Operation(summary = "Obter rota de veículo", description = "Obtém a rota de veículo entre dois pontos")
    @GetMapping("/veiculo")
    public ResponseEntity<List<Rota>> obterRotaVeiculo(@RequestParam String destino, @RequestParam String origem){
        RotaStrategy tipoTransporte = new VeiculoStrategy(rotaMapper);
        String id = UUID.randomUUID().toString();
        List<Rota> rota = navegacaoService.escolherStrategy(id, tipoTransporte, origem, destino);
        return ResponseEntity.status(200).body(rota);
    }

    @Operation(summary = "Obter rota a pé", description = "Obtém a rota a pé entre dois pontos")
    @GetMapping("/a-pe")
    public ResponseEntity<List<Rota>> obterRotaAPe(@RequestParam String destino, @RequestParam String origem){
        RotaStrategy tipoTransporte = new APeStrategy(rotaMapper);
        String id = UUID.randomUUID().toString();
        List<Rota> rota = navegacaoService.escolherStrategy(id, tipoTransporte, origem, destino);
        return ResponseEntity.status(200).body(rota);
    }

    @PostMapping("/compartilhar")
    public ResponseEntity<RotaShareResponse> compartilharRota(@RequestBody RotaSharePersist rota){
        String id = UUID.randomUUID().toString();
        RotaShareResponse response = rotasService.processarRotaCompartilhada(id, rota, rotaMapper);
        return ResponseEntity.status(200).body(response);
    }
    @GetMapping("/compartilhar/{id}")
    public ResponseEntity<List<Rota>> obterRotaCompartilhada(@PathVariable String id){
        List<Rota> response = rotasService.obterRotaCompartilhada(id);
        return ResponseEntity.status(200).body(response);
    }
}
