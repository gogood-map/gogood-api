package gogood.gogoodapi.controllers;

import gogood.gogoodapi.models.Usuario;
import gogood.gogoodapi.models.config.JdbcConfig;
import gogood.gogoodapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transfer")
public class TransferDataController {

    JdbcConfig jdbcConfig = new JdbcConfig();
    @GetMapping
    public String getData() {
        List<Usuario> allData = jdbcConfig.getConexaoDoBanco().query("SELECT * FROM usuarios", new BeanPropertyRowMapper<>(Usuario.class));
        StringBuilder data = new StringBuilder();
        for (Usuario usuario : allData) {
            data.append(usuario.toString()).append("\n");
        }
        return data.toString();
    }
}
