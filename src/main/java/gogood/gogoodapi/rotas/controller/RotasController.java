package gogood.gogoodapi.rotas.controller;

import gogood.gogoodapi.rotas.mapper.RotaMapper;
import gogood.gogoodapi.rotas.models.Rota;
import gogood.gogoodapi.rotas.services.Navegacao;
import gogood.gogoodapi.rotas.strategy.rotaStrategy.APeStrategy;
import gogood.gogoodapi.rotas.strategy.rotaStrategy.BicicletaStrategy;
import gogood.gogoodapi.rotas.strategy.rotaStrategy.TransportePublicoStrategy;
import gogood.gogoodapi.rotas.strategy.rotaStrategy.VeiculoStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rotas")
public class RotasController {

    @Autowired
    RotaMapper rotaMapper;

    public RotasController(RotaMapper rotaMapper) {
        this.rotaMapper = rotaMapper;
    }

    @GetMapping("/transporte-publico")
    public ResponseEntity<List<Rota>> obterRotaTransportePublico(@RequestParam String destino, @RequestParam String origem){
        Navegacao navegacao = new Navegacao(new TransportePublicoStrategy(rotaMapper));
        List<Rota> rota = navegacao.montarRotas(origem, destino);
        return ResponseEntity.status(200).body(rota);
    }
    @GetMapping("/bike")
    public ResponseEntity<List<Rota>> obterRotaBike(@RequestParam String destino, @RequestParam String origem){
        Navegacao navegacao = new Navegacao(new BicicletaStrategy(rotaMapper));
        List<Rota> rota = navegacao.montarRotas(origem, destino);
        return ResponseEntity.status(200).body(rota);
    }
    @GetMapping("/veiculo")
    public ResponseEntity<List<Rota>> obterRotaVeiculo(@RequestParam String destino, @RequestParam String origem){
        Navegacao navegacao = new Navegacao(new VeiculoStrategy(rotaMapper));
        List<Rota> rota = navegacao.montarRotas(origem, destino);
        return ResponseEntity.status(200).body(rota);
    }
    @GetMapping("/a-pe")
    public ResponseEntity<List<Rota>> obterRotaAPe(@RequestParam String destino, @RequestParam String origem){
        Navegacao navegacao = new Navegacao(new APeStrategy(rotaMapper));
        List<Rota> rota = navegacao.montarRotas(origem, destino);
        return ResponseEntity.status(200).body(rota);
    }
}
