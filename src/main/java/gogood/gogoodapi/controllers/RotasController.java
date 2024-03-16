package gogood.gogoodapi.controllers;

import gogood.gogoodapi.models.Rota;
import gogood.gogoodapi.services.Navegacao;
import gogood.gogoodapi.strategys.rotaStrategy.APeStrategy;
import gogood.gogoodapi.strategys.rotaStrategy.BicicletaStrategy;
import gogood.gogoodapi.strategys.rotaStrategy.TransportePublicoStrategy;
import gogood.gogoodapi.strategys.rotaStrategy.VeiculoStrategy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rotas")
public class RotasController {
    @GetMapping("/transporte-publico")
    public ResponseEntity<Rota> obterRotaTransportePublico(@RequestParam String destino, @RequestParam String origem){
        Navegacao navegacao = new Navegacao(new TransportePublicoStrategy());
        Rota rota = navegacao.montarRota(origem, destino);
        return ResponseEntity.status(200).body(rota);
    }
    @GetMapping("/bike")
    public ResponseEntity<Rota> obterRotaBike(@RequestParam String destino, @RequestParam String origem){
        Navegacao navegacao = new Navegacao(new BicicletaStrategy());
        Rota rota = navegacao.montarRota(origem, destino);
        return ResponseEntity.status(200).body(rota);
    }
    @GetMapping("/veiculo")
    public ResponseEntity<Rota> obterRotaVeiculo(@RequestParam String destino, @RequestParam String origem){
        Navegacao navegacao = new Navegacao(new VeiculoStrategy());
        Rota rota = navegacao.montarRota(origem, destino);
        return ResponseEntity.status(200).body(rota);
    }
    @GetMapping("/a-pe")
    public ResponseEntity<Rota> obterRotaAPe(@RequestParam String destino, @RequestParam String origem){
        Navegacao navegacao = new Navegacao(new APeStrategy());
        Rota rota = navegacao.montarRota(origem, destino);
        return ResponseEntity.status(200).body(rota);
    }
}
