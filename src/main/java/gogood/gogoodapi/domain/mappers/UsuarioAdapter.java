package gogood.gogoodapi.domain.mappers;

import gogood.gogoodapi.domain.DTOS.CriarUsuario;
import gogood.gogoodapi.domain.models.Usuario;

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
