package gogood.gogoodapi.Controllers;

import gogood.gogoodapi.Models.DTOS.CriarUsuarioDTO;
import gogood.gogoodapi.Models.Usuario;
import jakarta.validation.Valid;
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

}
