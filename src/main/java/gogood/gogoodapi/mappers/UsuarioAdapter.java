package gogood.gogoodapi.mappers;

import gogood.gogoodapi.DTOS.CriarUsuario;
import gogood.gogoodapi.models.Usuario;

public class UsuarioAdapter {
    public static Usuario novoUsuarioParaUsuario(CriarUsuario novoUsuario) {
        return new Usuario(
                novoUsuario.nome(),
                novoUsuario.email(),
                novoUsuario.senha(),
                novoUsuario.genero(),
                novoUsuario.dt_Nascimento(),
                novoUsuario.google_id()
        );
    }

}
