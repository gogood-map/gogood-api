package gogood.gogoodapi.domain.mappers;

import gogood.gogoodapi.domain.DTOS.CriarUsuario;
import gogood.gogoodapi.domain.models.Usuarios;

public class UsuarioAdapter {
    public static Usuarios novoUsuarioParaUsuario(CriarUsuario novoUsuario) {
        return new Usuarios(
                novoUsuario.nome(),
                novoUsuario.email(),
                novoUsuario.senha(),
                novoUsuario.genero(),
                novoUsuario.dt_Nascimento(),
                novoUsuario.google_id()
        );
    }

}
