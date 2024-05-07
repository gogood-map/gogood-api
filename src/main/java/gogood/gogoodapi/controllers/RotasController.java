package gogood.gogoodapi.controllers;

import gogood.gogoodapi.domain.mappers.RotaMapper;
import gogood.gogoodapi.domain.models.Rota;
import gogood.gogoodapi.service.Navegacao;
import gogood.gogoodapi.domain.strategy.rotaStrategy.APeStrategy;
import gogood.gogoodapi.domain.strategy.rotaStrategy.BicicletaStrategy;
import gogood.gogoodapi.domain.strategy.rotaStrategy.TransportePublicoStrategy;
import gogood.gogoodapi.domain.strategy.rotaStrategy.VeiculoStrategy;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rotas")
@Tag(name = "Rotas", description = "Consultar rotas de transporte")
public class RotasController {

    @Autowired
    RotaMapper rotaMapper;

    public RotasController(RotaMapper rotaMapper) {
        this.rotaMapper = rotaMapper;
    }

    @Operation(summary = "Obter rota de transporte público", description = "Obtém a rota de transporte público entre dois pontos")
    @GetMapping("/transporte-publico")
    public ResponseEntity<List<Rota>> obterRotaTransportePublico(@RequestParam String destino, @RequestParam String origem){
        Navegacao navegacao = new Navegacao(new TransportePublicoStrategy(rotaMapper));
        List<Rota> rota = navegacao.montarRotas(origem, destino);
        return ResponseEntity.status(200).body(rota);
    }

    @Operation(summary = "Obter rota de bicicleta", description = "Obtém a rota de bicicleta entre dois pontos")
    @GetMapping("/bike")
    public ResponseEntity<List<Rota>> obterRotaBike(@RequestParam String destino, @RequestParam String origem){
        Navegacao navegacao = new Navegacao(new BicicletaStrategy(rotaMapper));
        List<Rota> rota = navegacao.montarRotas(origem, destino);
        return ResponseEntity.status(200).body(rota);
    }

    @Operation(summary = "Obter rota de veículo", description = "Obtém a rota de veículo entre dois pontos")
    @GetMapping("/veiculo")
    public ResponseEntity<List<Rota>> obterRotaVeiculo(@RequestParam String destino, @RequestParam String origem){
        Navegacao navegacao = new Navegacao(new VeiculoStrategy(rotaMapper));
        List<Rota> rota = navegacao.montarRotas(origem, destino);
        return ResponseEntity.status(200).body(rota);
    }

    @Operation(summary = "Obter rota a pé", description = "Obtém a rota a pé entre dois pontos")
    @GetMapping("/a-pe")
    public ResponseEntity<List<Rota>> obterRotaAPe(@RequestParam String destino, @RequestParam String origem){
        Navegacao navegacao = new Navegacao(new APeStrategy(rotaMapper));
        List<Rota> rota = navegacao.montarRotas(origem, destino);
        return ResponseEntity.status(200).body(rota);
    }
}
