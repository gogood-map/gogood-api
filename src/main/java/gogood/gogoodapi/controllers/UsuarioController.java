package gogood.gogoodapi.controllers;

import gogood.gogoodapi.DTOS.AtualizarUsuarioPut;
import gogood.gogoodapi.DTOS.CriarUsuario;
import gogood.gogoodapi.adapters.UsuarioAdapter;
import gogood.gogoodapi.models.Usuario;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yaml.snakeyaml.parser.ParserException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private ArrayList<Usuario> usuarios = new ArrayList<>();

    @GetMapping
    public ResponseEntity<List<Usuario>> listar(){

        if(usuarios.isEmpty()){
           return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(usuarios);
    }

    @PostMapping
    public ResponseEntity cadastrar(@RequestBody @Valid CriarUsuario novoUsuario)  {

            Usuario usuario = UsuarioAdapter.novoUsuarioParaUsuario(novoUsuario);

            usuarios.add(usuario);
            return ResponseEntity.status(201).build();


    }
    @DeleteMapping("/{indice}")
    public ResponseEntity excluir(@PathVariable int indice){
        if(indice < 0 || indice >= usuarios.size()){
            return ResponseEntity.status(404).build();
        }
        usuarios.remove(indice);
        return ResponseEntity.status(204).build();
    }

    @PutMapping("/{indice}")
    public ResponseEntity atualizar(@PathVariable int indice, @RequestBody AtualizarUsuarioPut dadosAtualizacao){
        if(indice < 0 || indice >= usuarios.size()){
            return ResponseEntity.status(404).build();
        }
        Usuario usuario = usuarios.get(indice);
        usuario.atualizar(dadosAtualizacao);
        return ResponseEntity.status(204).build();
    }


}
