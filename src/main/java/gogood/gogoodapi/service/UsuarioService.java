package gogood.gogoodapi.service;

import gogood.gogoodapi.configuration.JdbcConfig;
import gogood.gogoodapi.domain.DTOS.UsuarioRegistro;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UsuarioService {

    JdbcConfig jdbcConfig = new JdbcConfig();



    public List<UsuarioRegistro> obterUsuarios(){
        var lista = jdbcConfig.getConexaoDoBanco().query(
                "SELECT * FROM usuarios",
                new BeanPropertyRowMapper<>(UsuarioRegistro.class)
        );

        return lista;
    }

    public void inserirUsuario(UsuarioRegistro usuario){
        jdbcConfig.getConexaoDoBanco().update(
                "INSERT INTO usuarios (ID, nome, email, senha, dt_nascimento, genero, google_id, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                usuario.getID(), usuario.getNome(), usuario.getEmail(), usuario.getSenha(), usuario.getDt_nascimento(),
                usuario.getGenero(), usuario.getGoogle_id(), usuario.getCreated_at()
        );
    }
}
