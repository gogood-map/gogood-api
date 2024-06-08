package gogood.gogoodapi.controllers;

import gogood.gogoodapi.domain.DTOS.EnderecoUsuarioDTO;
import gogood.gogoodapi.domain.models.endereco.Enderecos;
import gogood.gogoodapi.domain.models.endereco.EnderecosResponse;
import gogood.gogoodapi.domain.models.endereco.EnderecosUsuarios;
import gogood.gogoodapi.service.EnderecoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {
    @Autowired
    private EnderecoService service;
    @PostMapping
    public ResponseEntity<Enderecos> salvarEndereco(@RequestBody EnderecoUsuarioDTO historicoRotaPersist) {
        Enderecos enderecos = service.save(historicoRotaPersist);
        return ResponseEntity.status(201).body(enderecos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Enderecos> atualizarEndereco(@PathVariable Integer id, @RequestBody EnderecoUsuarioDTO enderecoPersist) {
        Enderecos enderecos = service.update(id, enderecoPersist);
        return ResponseEntity.ok(enderecos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<EnderecosResponse>> buscarEnderecoporIdUsuario(@PathVariable Integer id) {
        List<EnderecosResponse> enderecos = service.getEnderecosByUsuarioId(id);
        return ResponseEntity.ok(enderecos);
    }

    @DeleteMapping("/usuario/{usuarioId}/endereco/{enderecoId}")
    public ResponseEntity<Void> deleteEnderecoByUsuarioId(@PathVariable Integer usuarioId, @PathVariable Integer enderecoId) {
        service.deleteEnderecoByUsuarioId(usuarioId, enderecoId);
        return ResponseEntity.status(204).build();
    }

}