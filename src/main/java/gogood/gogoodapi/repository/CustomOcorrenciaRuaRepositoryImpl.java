package gogood.gogoodapi.repository;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
public class CustomOcorrenciaRuaRepositoryImpl implements CustomOcorrenciaRuaRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Integer getTotalOccurrencesByStreets(List<String> streets) {
        MatchOperation matchStage = match(Criteria.where("_id").in(streets));
        Aggregation aggregation = newAggregation(
            matchStage,
            group().sum("count").as("total")
        );

        AggregationResults<Document> output = mongoTemplate.aggregate(aggregation, "viewOcorrenciaRua", Document.class);
        Document result = output.getUniqueMappedResult();
        return result != null ? result.getInteger("total") : 0;
    }
}
