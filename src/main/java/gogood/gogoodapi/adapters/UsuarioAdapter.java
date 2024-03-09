package gogood.gogoodapi.adapters;

import gogood.gogoodapi.DTOS.CriarUsuario;
import gogood.gogoodapi.models.Usuario;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class UsuarioAdapter {
    public static Usuario novoUsuarioParaUsuario(CriarUsuario novoUsuario) {
        return new Usuario(
                novoUsuario.nome(),
                novoUsuario.email(),
                novoUsuario.senha(),
                novoUsuario.genero(),
                novoUsuario.dt_Nascimento()
        );
    }

}
