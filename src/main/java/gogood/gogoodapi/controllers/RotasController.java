package gogood.gogoodapi.controllers;

import gogood.gogoodapi.models.Rota;
import gogood.gogoodapi.services.Navegacao;
import gogood.gogoodapi.strategy.rotaStrategy.APeStrategy;
import gogood.gogoodapi.strategy.rotaStrategy.BicicletaStrategy;
import gogood.gogoodapi.strategy.rotaStrategy.TransportePublicoStrategy;
import gogood.gogoodapi.strategy.rotaStrategy.VeiculoStrategy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rotas")
public class RotasController {
    @GetMapping("/transporte-publico")
    public ResponseEntity<List<Rota>> obterRotaTransportePublico(@RequestParam String destino, @RequestParam String origem){
        Navegacao navegacao = new Navegacao(new TransportePublicoStrategy());
        List<Rota> rota = navegacao.montarRotas(origem, destino);
        return ResponseEntity.status(200).body(rota);
    }
    @GetMapping("/bike")
    public ResponseEntity<List<Rota>> obterRotaBike(@RequestParam String destino, @RequestParam String origem){
        Navegacao navegacao = new Navegacao(new BicicletaStrategy());
        List<Rota> rota = navegacao.montarRotas(origem, destino);
        return ResponseEntity.status(200).body(rota);
    }
    @GetMapping("/veiculo")
    public ResponseEntity<List<Rota>> obterRotaVeiculo(@RequestParam String destino, @RequestParam String origem){
        Navegacao navegacao = new Navegacao(new VeiculoStrategy());
        List<Rota> rota = navegacao.montarRotas(origem, destino);
        return ResponseEntity.status(200).body(rota);
    }
    @GetMapping("/a-pe")
    public ResponseEntity<List<Rota>> obterRotaAPe(@RequestParam String destino, @RequestParam String origem){
        Navegacao navegacao = new Navegacao(new APeStrategy());
        List<Rota> rota = navegacao.montarRotas(origem, destino);
        return ResponseEntity.status(200).body(rota);
    }
}
