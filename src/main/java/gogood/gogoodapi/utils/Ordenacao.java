package gogood.gogoodapi.utils;

import gogood.gogoodapi.domain.DTOS.OcorrenciaRuaSimples;
import gogood.gogoodapi.domain.models.estrutura.ListaObj;
import gogood.gogoodapi.domain.models.Rota;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ordenacao {
    public static List<OcorrenciaRuaSimples> ordenarPorQuantidadeOcorrencia(ListaObj<OcorrenciaRuaSimples> ocorrenciasRuaSimplesLista ){
        for (int i = 0; i < ocorrenciasRuaSimplesLista.getTamanho(); i++) {
            int indiceMenor = i;

            for (int j = i+1; j < ocorrenciasRuaSimplesLista.getTamanho(); j++) {

                if(ocorrenciasRuaSimplesLista.getElemento(indiceMenor).getQuantidadeOcorrencias() > ocorrenciasRuaSimplesLista.getElemento(j).getQuantidadeOcorrencias()){
                    indiceMenor = j;
                }
            }

            OcorrenciaRuaSimples aux = ocorrenciasRuaSimplesLista.getElemento(i);
            ocorrenciasRuaSimplesLista.setElemento(i, ocorrenciasRuaSimplesLista.getElemento(indiceMenor));
            ocorrenciasRuaSimplesLista.setElemento(indiceMenor, aux);
        }
        List<OcorrenciaRuaSimples> ocorrenciasRuaSimplesOrdenadas = new ArrayList<>();
        for (int i = 0; i < ocorrenciasRuaSimplesLista.getTamanho(); i++) {
            ocorrenciasRuaSimplesOrdenadas.add(ocorrenciasRuaSimplesLista.getElemento(i));
        }
        return ocorrenciasRuaSimplesOrdenadas;
    }

    public static List<Rota> ordenarRotaPorDuracao(Rota[] rotas){
        ListaObj<Rota> rotasListObj = new ListaObj<>(rotas.length);

        Arrays.stream(rotas).toList().forEach(
                rotasListObj::adiciona
        );

        for (int i = 0; i < rotasListObj.getTamanho(); i++) {
            int indiceMenor = i;

            for (int j = i+1; j < rotasListObj.getTamanho(); j++) {

                if(rotasListObj.getElemento(indiceMenor).getDuracaoSegundos() > rotasListObj.getElemento(j).getDuracaoSegundos()){
                    indiceMenor = j;
                }
            }

            Rota aux = rotasListObj.getElemento(i);
            rotasListObj.setElemento(i, rotasListObj.getElemento(indiceMenor));
            rotasListObj.setElemento(indiceMenor, aux);
        }
        List<Rota> rotasOrdenadas = new ArrayList<>();
        for (int i = 0; i < rotasListObj.getTamanho(); i++) {
            rotasOrdenadas.add(rotasListObj.getElemento(i));
        }

        return rotasOrdenadas;
    }
}
