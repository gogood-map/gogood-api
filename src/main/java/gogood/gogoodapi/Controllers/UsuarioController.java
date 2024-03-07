package gogood.gogoodapi.Controllers;

import gogood.gogoodapi.Models.DTOS.AtualizarUsuarioPut;
import gogood.gogoodapi.Models.DTOS.CriarUsuarioDTO;
import gogood.gogoodapi.Models.Usuario;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private ArrayList<Usuario> usuarios = new ArrayList<Usuario>();

    @GetMapping
    public ResponseEntity<List<Usuario>> listar(){

        if(usuarios.isEmpty()){
           return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(usuarios);
    }

    @PostMapping
    public ResponseEntity cadastrar(@RequestBody @Valid CriarUsuarioDTO novoUsuario){
        Usuario usuario = new Usuario();


        BeanUtils.copyProperties(novoUsuario,usuario);

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
