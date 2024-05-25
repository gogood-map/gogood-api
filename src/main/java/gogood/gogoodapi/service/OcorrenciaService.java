package gogood.gogoodapi.service;

import gogood.gogoodapi.domain.DTOS.OcorrenciaRuaSimples;
import gogood.gogoodapi.domain.mappers.OcorrenciaRuaMapper;
import gogood.gogoodapi.domain.models.Fila;
import gogood.gogoodapi.domain.models.ListaObj;
import gogood.gogoodapi.domain.models.OcorrenciaRua;
import gogood.gogoodapi.repository.OcorrenciasRuasRepository;
import gogood.gogoodapi.utils.Ordenacao;
import gogood.gogoodapi.utils.PesquisaBinaria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class OcorrenciaService {

    @Autowired
    OcorrenciasRuasRepository ocorrenciasRuasRepository;

    public OcorrenciaService(OcorrenciasRuasRepository ocorrenciasRuasRepository) {
        this.ocorrenciasRuasRepository = ocorrenciasRuasRepository;
    }

    public OcorrenciaRuaSimples obterQuantidadeOcorrenciasPorRua(String rua){
        var consulta = ocorrenciasRuasRepository.findAll(Sort.by(Sort.Direction.ASC, "_id"));
        OcorrenciaRua[] arrayOcorrenciasRua = new OcorrenciaRua[consulta.size()];
        arrayOcorrenciasRua = consulta.toArray(arrayOcorrenciasRua);
        int indice = PesquisaBinaria.pesquisarQuantidadeOcorrenciasPorRua(
                arrayOcorrenciasRua, rua
        );
        if (indice == -1) throw new ResponseStatusException(HttpStatusCode.valueOf(404), "Rua não encontrada");
        var dto = OcorrenciaRuaMapper.toDTO(arrayOcorrenciasRua[indice]);

        return dto;
    }

    public List<OcorrenciaRuaSimples> obterQuantidadeOcorrenciasFila(Fila<String> ruas){
        List<OcorrenciaRuaSimples> listaQuantidadeOcorrenciaPorRuas = new ArrayList<>();
        while (!ruas.isEmpty()){
            try {
                listaQuantidadeOcorrenciaPorRuas.add(
                    obterQuantidadeOcorrenciasPorRua(ruas.poll())
                );

            }catch (ResponseStatusException ignored){}

        }

        return   Ordenacao.ordenarPorQuantidadeOcorrencia(new ListaObj<>(
                listaQuantidadeOcorrenciaPorRuas.toArray(new OcorrenciaRuaSimples[listaQuantidadeOcorrenciaPorRuas.size()])
        ));
    }
}
