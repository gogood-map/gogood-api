package gogood.gogoodapi.domain.mappers;

import gogood.gogoodapi.domain.DTOS.UsuarioNovo;
import gogood.gogoodapi.domain.models.UsuarioNovoGoogle;
import gogood.gogoodapi.domain.models.Usuarios;
import gogood.gogoodapi.service.usuario.dto.UsuarioTokenDto;

public class UsuarioMapper {
    public static Usuarios of(UsuarioNovo usuarioNovo){
        Usuarios usuarios = new Usuarios();

        usuarios.setEmail(usuarioNovo.email());
        usuarios.setNome( usuarioNovo.nome());
        usuarios.setSenha( usuarioNovo.senha());
        usuarios.setGenero(usuarioNovo.genero());
        usuarios.setDt_nascimento(usuarioNovo.dt_Nascimento());
        return usuarios;
    }
    public static Usuarios of(UsuarioNovoGoogle usuarioNovo){
        Usuarios usuarios = new Usuarios();

        usuarios.setEmail(usuarioNovo.getEmail());
        usuarios.setNome( usuarioNovo.getNome());
        usuarios.setGoogle_id(usuarioNovo.getGoogle_id());
        return usuarios;
    }

    public static UsuarioTokenDto of(Usuarios usuarios, String token){
        UsuarioTokenDto usuarioTokenDto = new UsuarioTokenDto();

        usuarioTokenDto.setUserId(usuarios.getID());
        usuarioTokenDto.setEmail(usuarios.getEmail());
        usuarioTokenDto.setNome(usuarios.getNome());
        usuarioTokenDto.setToken(token);
        usuarioTokenDto.setGenero(usuarios.getGenero());
        usuarioTokenDto.setDt_nascimento(usuarios.getDt_nascimento());
        return usuarioTokenDto;
    }
}
