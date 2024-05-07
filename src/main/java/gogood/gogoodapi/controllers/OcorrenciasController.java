package gogood.gogoodapi.controllers;

import gogood.gogoodapi.domain.DTOS.OcorrenciaRuaSimples;
import gogood.gogoodapi.domain.mappers.OcorrenciaRuaMapper;
import gogood.gogoodapi.domain.models.OcorrenciaRua;
import gogood.gogoodapi.repository.OcorrenciasRuasRepository;
import gogood.gogoodapi.utils.PesquisaBinaria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/ocorrencias")
public class OcorrenciasController {
    @Autowired
    OcorrenciasRuasRepository ocorrenciasRuasRepository;

    @GetMapping("/filtro")
    public ResponseEntity<OcorrenciaRuaSimples> buscarQuantidadeOcorrenciasPorRua(@RequestParam String rua){
        var consulta = ocorrenciasRuasRepository.findAll(Sort.by(Sort.Direction.ASC, "_id"));
        OcorrenciaRua[] arrayOcorrenciasRua = new OcorrenciaRua[consulta.size()];
        arrayOcorrenciasRua = consulta.toArray(arrayOcorrenciasRua);
        int indice = PesquisaBinaria.pesquisarQuantidadeOcorrenciasPorRua(
                arrayOcorrenciasRua, rua
        );
        if (indice == -1) throw new ResponseStatusException(HttpStatusCode.valueOf(404), "Rua não encontrada");
        var dto = OcorrenciaRuaMapper.toDTO(arrayOcorrenciasRua[indice]);
        return ResponseEntity.status(200).body(dto);
    }
}
