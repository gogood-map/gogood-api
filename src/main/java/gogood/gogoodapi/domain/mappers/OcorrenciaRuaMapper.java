package gogood.gogoodapi.domain.mappers;
import gogood.gogoodapi.domain.DTOS.OcorrenciaRuaSimples;
import gogood.gogoodapi.domain.models.QuantidadeOcorrenciaRua;

import java.util.List;

public class OcorrenciaRuaMapper {

    public static OcorrenciaRuaSimples toDTO(QuantidadeOcorrenciaRua quantidadeOcorrenciaRua){
        return new OcorrenciaRuaSimples(quantidadeOcorrenciaRua.get_id().getRua(), quantidadeOcorrenciaRua.getCount());
    }
    public static List<OcorrenciaRuaSimples> toDTO(List<QuantidadeOcorrenciaRua> quantidadeOcorrenciaRua){
        return quantidadeOcorrenciaRua.stream().map(
                oc->new OcorrenciaRuaSimples(oc.get_id().getRua(), oc.getCount())
        ).toList();
    }

}
