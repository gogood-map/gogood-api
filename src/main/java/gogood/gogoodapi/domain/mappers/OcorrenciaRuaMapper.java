package gogood.gogoodapi.domain.mappers;
import gogood.gogoodapi.domain.dtos.OcorrenciaRuaSimples;
import gogood.gogoodapi.domain.models.OcorrenciaRua;

import java.util.List;

public class OcorrenciaRuaMapper {

    public static OcorrenciaRuaSimples toDTO(OcorrenciaRua ocorrenciaRua){
        return new OcorrenciaRuaSimples(ocorrenciaRua.get_id(), ocorrenciaRua.getCount());
    }
    public static List<OcorrenciaRuaSimples> toDTO(List<OcorrenciaRua> ocorrenciaRua){
        return ocorrenciaRua.stream().map(
                oc->new OcorrenciaRuaSimples(oc.get_id(), oc.getCount())
        ).toList();
    }

}
