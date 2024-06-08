package gogood.gogoodapi.controllers;

import gogood.gogoodapi.domain.models.iago.IagoParams;
import gogood.gogoodapi.domain.models.iago.IagoPersist;
import gogood.gogoodapi.service.ConsultaIago;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/iago")
public class IagoController {
    @Autowired
    private ConsultaIago consultaIago;

    @PostMapping
    public ResponseEntity<Object> consultarGemini(@RequestBody @Valid IagoPersist persist) {
        Object response = consultaIago.consultarGemini(persist.getPrompt());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/parametros")
    public ResponseEntity<Object> consultarGeminiParametros(@RequestBody @Valid IagoParams params) {
        Object response = consultaIago.mudarParametros(params.getLimit(), params.getPage());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/parametros")
    public ResponseEntity<Object> consultarGeminiParametros() {
        Object response = consultaIago.consultarParametros();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/preprompt")
    public ResponseEntity<Object> mudarPrompt(@RequestBody @Valid IagoPersist persist) {
        Object response = consultaIago.mudarPrompt(persist.getPrompt());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/preprompt")
    public ResponseEntity<Object> consultarPrompt() {
        Object response = consultaIago.consultarPrompt();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/posprompt")
    public ResponseEntity<Object> mudarPosPrompt(@RequestBody @Valid IagoPersist persist) {
        Object response = consultaIago.mudarPosPrompt(persist.getPrompt());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/posprompt")
    public ResponseEntity<Object> consultarPosPrompt() {
        Object response = consultaIago.consultarPosPrompt();
        return ResponseEntity.ok(response);
    }


}
