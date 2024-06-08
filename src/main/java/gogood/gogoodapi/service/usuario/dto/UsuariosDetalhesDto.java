package gogood.gogoodapi.service.usuario.dto;


import gogood.gogoodapi.domain.models.Usuarios;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UsuariosDetalhesDto implements UserDetails {
    @Getter
    private String nome;
    private String email;
    private String senha;

    public UsuariosDetalhesDto(Usuarios usuarios) {
        this.nome = usuarios.getNome();
        this.email = usuarios.getEmail();
        this.senha = usuarios.getSenha();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
