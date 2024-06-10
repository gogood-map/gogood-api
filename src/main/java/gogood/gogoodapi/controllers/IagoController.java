package gogood.gogoodapi.controllers;

import gogood.gogoodapi.domain.models.iago.IagoParams;
import gogood.gogoodapi.domain.models.iago.IagoPersist;
import gogood.gogoodapi.service.ConsultaIagoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/iago")
public class IagoController {
    @Autowired
    private ConsultaIagoService consultaIagoService;

    @PostMapping
    public ResponseEntity<Object> consultarGemini(@RequestBody @Valid IagoPersist persist) {
        Object response = consultaIagoService.consultarGemini(persist.getPrompt());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/parametros")
    public ResponseEntity<Object> consultarGeminiParametros(@RequestBody @Valid IagoParams params) {
        Object response = consultaIagoService.mudarParametros(params.getLimit(), params.getPage());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/parametros")
    public ResponseEntity<Object> consultarGeminiParametros() {
        Object response = consultaIagoService.consultarParametros();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/preprompt")
    public ResponseEntity<Object> mudarPrompt(@RequestBody @Valid IagoPersist persist) {
        Object response = consultaIagoService.mudarPrompt(persist.getPrompt());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/preprompt")
    public ResponseEntity<Object> consultarPrompt() {
        Object response = consultaIagoService.consultarPrompt();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/posprompt")
    public ResponseEntity<Object> mudarPosPrompt(@RequestBody @Valid IagoPersist persist) {
        Object response = consultaIagoService.mudarPosPrompt(persist.getPrompt());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/posprompt")
    public ResponseEntity<Object> consultarPosPrompt() {
        Object response = consultaIagoService.consultarPosPrompt();
        return ResponseEntity.ok(response);
    }


}
