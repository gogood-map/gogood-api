package gogood.gogoodapi.utils;

import gogood.gogoodapi.domain.models.QuantidadeOcorrenciaRua;

public class PesquisaBinaria {

    public static int pesquisarQuantidadeOcorrenciasPorRua(QuantidadeOcorrenciaRua[] vetor, String id){
        int indiceSuperior = vetor.length-1;
        int indiceInferior = 0;


        while(indiceInferior <= indiceSuperior){
            int meio = (indiceInferior+indiceSuperior)/2;
            String ruaMeio = vetor[meio].get_id().getRua();
            if(ruaMeio.equals(id)){
                return meio;
            }else if(ruaMeio.compareTo(id) > 0){
                indiceSuperior = meio-1;
            }else{
                indiceInferior = meio+1;
            }
        }
        return -1;
    }
}
