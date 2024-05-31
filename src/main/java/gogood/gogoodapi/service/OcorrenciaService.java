package gogood.gogoodapi.service;

import gogood.gogoodapi.domain.DTOS.OcorrenciaRuaSimples;
import gogood.gogoodapi.domain.mappers.OcorrenciaRuaMapper;
import gogood.gogoodapi.domain.models.QuantidadeOcorrenciaRegiaoAnoMes;
import gogood.gogoodapi.domain.models.QuantidadeOcorrenciaRegiaoAnoMesSimples;
import gogood.gogoodapi.domain.models.estrutura.Fila;
import gogood.gogoodapi.domain.models.estrutura.ListaObj;
import gogood.gogoodapi.domain.models.Ocorrencia;
import gogood.gogoodapi.domain.models.QuantidadeOcorrenciaRua;
import gogood.gogoodapi.exceptions.ListaVaziaException;
import gogood.gogoodapi.repository.OcorrenciaRepository;
import gogood.gogoodapi.repository.QuantidadeOcorrenciaRegiaoRepository;
import gogood.gogoodapi.repository.QuantidadeOcorrenciaRuaRepository;
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
    QuantidadeOcorrenciaRuaRepository quantidadeOcorrenciaRuaRepository;
    @Autowired
    OcorrenciaRepository ocorrenciaRepository;
    @Autowired
    QuantidadeOcorrenciaRegiaoRepository quantidadeOcorrenciaRegiaoRepository;


    public OcorrenciaService(QuantidadeOcorrenciaRuaRepository quantidadeOcorrenciaRuaRepository, OcorrenciaRepository ocorrenciaRepository) {
        this.quantidadeOcorrenciaRuaRepository = quantidadeOcorrenciaRuaRepository;
        this.ocorrenciaRepository = ocorrenciaRepository;
    }



    public OcorrenciaRuaSimples obterQuantidadeOcorrenciasPorRua(String rua){
        var consulta = quantidadeOcorrenciaRuaRepository.findAll(Sort.by(Sort.Direction.ASC, "_id"));
        QuantidadeOcorrenciaRua[] arrayOcorrenciasRua = new QuantidadeOcorrenciaRua[consulta.size()];
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
    public List<Ocorrencia> listarTodasOcorrenciasPorBairro(String bairro, String cidade){
        return ocorrenciaRepository.findAllByBairroAndCidade(bairro, cidade);
    }
    public String[][] obterHistoricoQuantidadeOcorrenciasBairro(String bairro, String cidade){
        var lista = quantidadeOcorrenciaRuaRepository.listarQuantidadeOcorrenciaRuaPorBairroECidade(bairro, cidade);
        if(lista.isEmpty()){
            throw new ListaVaziaException();
        }
        String[][] historico = new String[lista.size()][4];

        for (int i = 0; i < historico.length; i++) {
            var quantidadeOcorrenciaAtual = lista.get(i);
            historico[i][0] = "%s".formatted(quantidadeOcorrenciaAtual.get_id().getRua());
            historico[i][1] = "%d".formatted(quantidadeOcorrenciaAtual.getCount2023());
            historico[i][2] = "%d".formatted(quantidadeOcorrenciaAtual.getCount2024());
            historico[i][3] = "%d".formatted(quantidadeOcorrenciaAtual.getCount());
        }
        return historico;
    }

    public List<QuantidadeOcorrenciaRegiaoAnoMesSimples> obterQuantidadeOcorrenciasPorRegiao(String cidade, String bairro) {
        var lista = quantidadeOcorrenciaRegiaoRepository.findFirst12ByInfoRegiaoAnoMes_AnoMesNotContainsAndInfoRegiaoAnoMes_CidadeAndInfoRegiaoAnoMes_BairroOrderByInfoRegiaoAnoMes_AnoMesDesc(
                "nan",cidade,bairro);
        if(lista.isEmpty()){
            throw new ListaVaziaException();
        }
        var listaSimples = lista.stream().map(
                i->new QuantidadeOcorrenciaRegiaoAnoMesSimples(i.getInfoRegiaoAnoMes().getAnoMes(), i.getCount())
        ).toList();
        return listaSimples;
    }
}
