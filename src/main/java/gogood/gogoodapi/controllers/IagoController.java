package gogood.gogoodapi.controllers;

import gogood.gogoodapi.domain.models.iago.IagoParams;
import gogood.gogoodapi.domain.models.iago.IagoPersist;
import gogood.gogoodapi.service.ConsultaIagoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/iago")
@Tag(name = "Iago", description = "API para consulta e configuração do sistema Iago")
public class IagoController {
    @Autowired
    private ConsultaIagoService consultaIagoService;

    @Operation(summary = "Consultar Gemini", description = "Consulta o serviço Gemini com um prompt fornecido")
    @PostMapping
    public ResponseEntity<Object> consultarGemini(@RequestBody @Valid IagoPersist persist) {
        Object response = consultaIagoService.consultarGemini(persist.getPrompt());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Consultar Gemini com Parâmetros", description = "Consulta o serviço Gemini com parâmetros de limite e página")
    @PostMapping("/parametros")
    public ResponseEntity<Object> consultarGeminiParametros(@RequestBody @Valid IagoParams params) {
        Object response = consultaIagoService.mudarParametros(params.getLimit(), params.getPage());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Consultar Parâmetros", description = "Consulta os parâmetros atuais do serviço Gemini")
    @GetMapping("/parametros")
    public ResponseEntity<Object> consultarGeminiParametros() {
        Object response = consultaIagoService.consultarParametros();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Mudar Prompt", description = "Muda o prompt utilizado pelo serviço Gemini")
    @PostMapping("/preprompt")
    public ResponseEntity<Object> mudarPrompt(@RequestBody @Valid IagoPersist persist) {
        Object response = consultaIagoService.mudarPrompt(persist.getPrompt());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Consultar Prompt", description = "Consulta o prompt atual utilizado pelo serviço Gemini")
    @GetMapping("/preprompt")
    public ResponseEntity<Object> consultarPrompt() {
        Object response = consultaIagoService.consultarPrompt();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Mudar Pós-Prompt", description = "Muda o pós-prompt utilizado pelo serviço Gemini")
    @PostMapping("/posprompt")
    public ResponseEntity<Object> mudarPosPrompt(@RequestBody @Valid IagoPersist persist) {
        Object response = consultaIagoService.mudarPosPrompt(persist.getPrompt());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Consultar Pós-Prompt", description = "Consulta o pós-prompt atual utilizado pelo serviço Gemini")
    @GetMapping("/posprompt")
    public ResponseEntity<Object> consultarPosPrompt() {
        Object response = consultaIagoService.consultarPosPrompt();
        return ResponseEntity.ok(response);
    }
}
