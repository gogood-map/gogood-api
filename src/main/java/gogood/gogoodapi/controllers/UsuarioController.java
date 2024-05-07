package gogood.gogoodapi.controllers;

import gogood.gogoodapi.domain.DTOS.AtualizarUsuarioPut;
import gogood.gogoodapi.domain.DTOS.CriarUsuario;
import gogood.gogoodapi.domain.models.Usuarios;
import gogood.gogoodapi.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "Consultar dados de usuários")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Obter todos os usuários", description = "Retorna uma lista com todos os usuários cadastrados")
    @GetMapping
    public ResponseEntity<Iterable<Usuarios>> get() {
        Iterable<Usuarios> usuarios = usuarioService.get();
        return ResponseEntity.status(HttpStatus.OK).body(usuarios);
    }


    @Operation(summary = "Obter usuário por ID", description = "Retorna um usuário específico")
    @PostMapping
    public ResponseEntity<Usuarios> post(@RequestBody @Valid CriarUsuario novoUsuario) {
        Usuarios usuario = usuarioService.save(novoUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @Operation(summary = "Remover usuário por ID", description = "Remove um usuário específico")
    @DeleteMapping("/{id}")
    public ResponseEntity<Usuarios> delete(@PathVariable Integer id) {
        Usuarios usuarios = usuarioService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Atualizar usuário por ID", description = "Atualiza um usuário específico")
    @PutMapping("/{id}")
    public ResponseEntity<Usuarios> put(@PathVariable String id, @Valid @RequestBody AtualizarUsuarioPut dadosAtualizacao) {
        Usuarios usuario = usuarioService.update(id, dadosAtualizacao);
        return ResponseEntity.status(HttpStatus.OK).body(usuario);
    }
}
