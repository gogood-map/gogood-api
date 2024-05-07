package gogood.gogoodapi.service;

import gogood.gogoodapi.domain.DTOS.AtualizarUsuarioPut;
import gogood.gogoodapi.domain.DTOS.CriarUsuario;
import gogood.gogoodapi.domain.models.Usuarios;
import gogood.gogoodapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    UsuarioRepository repository;

    public Iterable<Usuarios> get() {
        Iterable<Usuarios> usuarios = repository.findAll();
        return usuarios;
    }

    public Usuarios save(CriarUsuario novoUsuario) {
        Usuarios usuario = new Usuarios(novoUsuario);
        repository.save(usuario);
        return usuario;
    }

    public Usuarios delete(Integer id) {
        Optional<Usuarios> usuario = repository.findById(id);
        if (usuario.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado");
        }

        repository.delete(usuario.get());

        return usuario.get();

    }

    public Usuarios update(String id, AtualizarUsuarioPut dadosAtualizacao) {
        Optional<Usuarios> usuario = repository.findById(Integer.parseInt(id));
        if (usuario.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado");
        }

        usuario.get().atualizar(dadosAtualizacao);
        repository.save(usuario.get());

        return usuario.get();
    }
}
