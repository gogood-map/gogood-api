package gogood.gogoodapi.controllers;

import gogood.gogoodapi.domain.DTOS.EnderecoUsuarioDTO;
import gogood.gogoodapi.domain.models.endereco.Enderecos;
import gogood.gogoodapi.domain.models.endereco.EnderecosResponse;
import gogood.gogoodapi.domain.models.endereco.EnderecosUsuarios;
import gogood.gogoodapi.service.EnderecoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enderecos")
@Tag(name = "Endereços", description = "Gerenciamento de endereços")
public class EnderecoController {
    @Autowired
    private EnderecoService service;

    @PostMapping
    @Operation(summary = "Criar um novo endereço", description = "Cria um novo endereço")
    public ResponseEntity<Enderecos> salvarEndereco(@RequestBody EnderecoUsuarioDTO historicoRotaPersist) {
        Enderecos enderecos = service.save(historicoRotaPersist);
        return ResponseEntity.status(201).body(enderecos);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um endereço", description = "Atualiza um endereço existente")
    public ResponseEntity<Enderecos> atualizarEndereco(@PathVariable Integer id, @RequestBody EnderecoUsuarioDTO enderecoPersist) {
        Enderecos enderecos = service.update(id, enderecoPersist);
        return ResponseEntity.ok(enderecos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter um endereço por ID", description = "Obtém um endereço específico pelo ID")
    public ResponseEntity<List<EnderecosResponse>> buscarEnderecoporIdUsuario(@PathVariable Integer id) {
        List<EnderecosResponse> enderecos = service.getEnderecosByUsuarioId(id);
        return ResponseEntity.ok(enderecos);
    }

    @DeleteMapping("/usuario/{usuarioId}/endereco/{enderecoId}")
    @Operation(summary = "Deletar um endereço", description = "Deleta um endereço pelo ID")
    public ResponseEntity<Void> deleteEnderecoByUsuarioId(@PathVariable Integer usuarioId, @PathVariable Integer enderecoId) {
        service.deleteEnderecoByUsuarioId(usuarioId, enderecoId);
        return ResponseEntity.status(204).build();
    }

}