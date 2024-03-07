package gogood.gogoodapi.Controllers;

import gogood.gogoodapi.Models.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
