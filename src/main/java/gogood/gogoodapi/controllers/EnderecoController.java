package gogood.gogoodapi.controllers;

import gogood.gogoodapi.domain.DTOS.EnderecoUsuarioDTO;
import gogood.gogoodapi.domain.models.endereco.Enderecos;
import gogood.gogoodapi.domain.models.endereco.EnderecoPersist;
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
    @GetMapping("/{id}")
    public ResponseEntity<List<Enderecos>> buscarEnderecos(@PathVariable Integer id) {
        List<Enderecos> enderecos = service.findAllAdress(id);

        return ResponseEntity.status(200).body(enderecos);
    }
}