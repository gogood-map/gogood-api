package gogood.gogoodapi.controllers;

import gogood.gogoodapi.domain.DTOS.AtualizarUsuarioPut;
import gogood.gogoodapi.domain.DTOS.CriarUsuario;
import gogood.gogoodapi.domain.mappers.UsuarioAdapter;
import gogood.gogoodapi.domain.models.Usuario;
import gogood.gogoodapi.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private ArrayList<Usuario> usuarios = new ArrayList<>();
    @Autowired
    UsuarioRepository repository;

    @Operation(summary = "Obter todos os usuários", description = "Retorna uma lista com todos os usuários cadastrados")
    @GetMapping
    public ResponseEntity<List<Usuario>> getAll(){
        if(usuarios.isEmpty()){
           return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(200).body(usuarios);
    }


    @Operation(summary = "Obter usuário por ID", description = "Retorna um usuário específico")
    @PostMapping
    public ResponseEntity<Usuario> cadastrar(@RequestBody @Valid CriarUsuario novoUsuario)  {
            Usuario usuario = UsuarioAdapter.novoUsuarioParaUsuario(novoUsuario);
            usuarios.add(usuario);
            return ResponseEntity.status(201).build();
    }

    @Operation(summary = "Remover usuário por ID", description = "Remove um usuário específico")
    @DeleteMapping("/{id}")
    public ResponseEntity<Usuario> excluir(@PathVariable String id){
        validaId(id);

        usuarios = (ArrayList<Usuario>) usuarios.stream().filter(
                usuario -> !usuario.getID().toString().equals(id)
        ).toList();

        return ResponseEntity.status(204).build();
    }

    @Operation(summary = "Atualizar usuário por ID", description = "Atualiza um usuário específico")
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(@PathVariable String id, @Valid @RequestBody AtualizarUsuarioPut dadosAtualizacao){
        validaId(id);

        Usuario usuario = usuarios.stream().filter(
                u -> u.getID().toString().equals(id)
        ).toList().get(0);

        usuario.atualizar(dadosAtualizacao);
        return ResponseEntity.status(204).build();
    }

    private void validaId(String id){
        if (usuarios.stream().filter(
                usuario -> usuario.getID().toString().equals(id)
        ).toList().isEmpty()){
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "Usuário não encontrado");
        }

    }
}
