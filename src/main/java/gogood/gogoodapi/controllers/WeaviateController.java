package gogood.gogoodapi.controllers;

import gogood.gogoodapi.domain.models.WeaviatePersist;
import gogood.gogoodapi.service.ConsultaGemini;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weaviate")
public class WeaviateController {
    @Autowired
    private ConsultaGemini consultaGemini;

    @PostMapping
    public ResponseEntity<Object> consultarGemini(@RequestBody @Valid WeaviatePersist persist) {
        Object response = consultaGemini.consultarGemini(persist.getPrompt(), persist.getLimit(), persist.getPage());
        return ResponseEntity.ok(response);
    }
}
