package gogood.gogoodapi.controllers;

import gogood.gogoodapi.domain.DTOS.OcorrenciaRuaSimples;
import gogood.gogoodapi.domain.mappers.OcorrenciaRuaMapper;
import gogood.gogoodapi.domain.models.Fila;
import gogood.gogoodapi.domain.models.OcorrenciaRua;
import gogood.gogoodapi.exceptions.ListaVaziaException;
import gogood.gogoodapi.repository.OcorrenciasRuasRepository;
import gogood.gogoodapi.service.OcorrenciaService;
import gogood.gogoodapi.utils.PesquisaBinaria;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @Operation(summary = "Buscar quantidade de ocorrências por ruas", description = "Retorna a quantidade de ocorrências por ruas")
    @PostMapping("/batch")
    public ResponseEntity<List<OcorrenciaRuaSimples>> buscarQuantidadeOcorrenciasBatch(@RequestBody String[] logradouros){
        var lista = ocorrenciaService.obterQuantidadeOcorrenciasFila(new Fila<>(logradouros));
        if (lista.isEmpty()) throw new ListaVaziaException();
        return ResponseEntity.status(200).body(lista);
    }
}
