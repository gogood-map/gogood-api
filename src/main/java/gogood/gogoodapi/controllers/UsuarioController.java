package gogood.gogoodapi.controllers;

import gogood.gogoodapi.DTOS.AtualizarUsuarioPut;
import gogood.gogoodapi.DTOS.CriarUsuario;
import gogood.gogoodapi.mappers.UsuarioAdapter;
import gogood.gogoodapi.models.Usuario;
import jakarta.validation.Valid;
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

    @GetMapping
    public ResponseEntity<List<Usuario>> getAll(){
        if(usuarios.isEmpty()){
           return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(200).body(usuarios);
    }

    @PostMapping
    public ResponseEntity<Usuario> cadastrar(@RequestBody @Valid CriarUsuario novoUsuario)  {
            Usuario usuario = UsuarioAdapter.novoUsuarioParaUsuario(novoUsuario);
            usuarios.add(usuario);
            return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Usuario> excluir(@PathVariable String id){
        validaId(id);

        usuarios = (ArrayList<Usuario>) usuarios.stream().filter(
                usuario -> !usuario.getID().toString().equals(id)
        ).toList();

        return ResponseEntity.status(204).build();
    }

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
