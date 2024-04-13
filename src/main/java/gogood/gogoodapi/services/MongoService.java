package gogood.gogoodapi.services;

import gogood.gogoodapi.models.QuantidadeOcorrenciaPorCep;
import gogood.gogoodapi.models.config.MongoConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Query.*;


@Service
public class MongoService {



    public MongoService() {
    }

    public Mono<Integer> obterQuantidadeDeOcorrenciasTotais(List<String> ceps){
        MongoConfig mongoConfig = new MongoConfig();
        MongoOperations operations = mongoConfig.mongoTemplate();

        int qtdOcorrenciasTotais = 0;

        for (String cep: ceps){
            var registro  = operations.findOne(query(Criteria.where("_id").is(cep)), QuantidadeOcorrenciaPorCep.class);
            if(registro != null){
                qtdOcorrenciasTotais += registro.getCount().intValue();
            }
        }

       return Mono.just(qtdOcorrenciasTotais);
    }
}
