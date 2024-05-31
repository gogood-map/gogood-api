package gogood.gogoodapi.controllers;

import gogood.gogoodapi.domain.DTOS.OcorrenciaRuaSimples;
import gogood.gogoodapi.domain.models.QuantidadeOcorrenciaRegiaoAnoMes;
import gogood.gogoodapi.domain.models.QuantidadeOcorrenciaRegiaoAnoMesSimples;
import gogood.gogoodapi.domain.models.estrutura.Fila;
import gogood.gogoodapi.exceptions.ListaVaziaException;
import gogood.gogoodapi.service.OcorrenciaService;
import gogood.gogoodapi.utils.StringHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ocorrencias")
@Tag(name = "Ocorrências", description = "Consultar dados de ocorrências")
public class OcorrenciasController {
   @Autowired
   private final OcorrenciaService ocorrenciaService;

    public OcorrenciasController(OcorrenciaService ocorrenciaService) {
        this.ocorrenciaService = ocorrenciaService;
    }

    @Operation(summary = "Buscar quantidade de ocorrências por rua", description = "Retorna a quantidade de ocorrências por rua")
    @GetMapping("/filtro")
    public ResponseEntity<OcorrenciaRuaSimples> buscarQuantidadeOcorrencias(@RequestParam String rua){
        return ResponseEntity.status(200).body(ocorrenciaService.obterQuantidadeOcorrenciasPorRua(rua));
    }

    @Operation(summary = "Buscar quantidade de ocorrências por região", description = "Retorna a quantidade de ocorrências por região, agrupadas por ano/mês")
    @GetMapping("/regiao")
    public ResponseEntity<List<QuantidadeOcorrenciaRegiaoAnoMesSimples>> buscarQuantidadeOcorrenciasRegiao(@RequestParam String bairro, @RequestParam String cidade){
        return ResponseEntity.status(200).body(ocorrenciaService.obterQuantidadeOcorrenciasPorRegiao(
                StringHelper.normalizar(cidade),
                StringHelper.normalizar(bairro)
        ));
    }

    @Operation(summary = "Buscar quantidade de ocorrências por ruas", description = "Retorna a quantidade de ocorrências por ruas")
    @PostMapping("/batch")
    public ResponseEntity<List<OcorrenciaRuaSimples>> buscarQuantidadeOcorrenciasBatch(@RequestBody String[] logradouros){
        var lista = ocorrenciaService.obterQuantidadeOcorrenciasFila(new Fila<>(logradouros));
        if (lista.isEmpty()) throw new ListaVaziaException();
        return ResponseEntity.status(200).body(lista);
    }
}
