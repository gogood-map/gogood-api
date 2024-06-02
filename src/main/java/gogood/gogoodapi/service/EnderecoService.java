package gogood.gogoodapi.service;

import gogood.gogoodapi.domain.DTOS.EnderecoUsuarioDTO;
import gogood.gogoodapi.domain.models.endereco.Enderecos;
import gogood.gogoodapi.domain.models.endereco.EnderecosUsuarios;
import gogood.gogoodapi.repository.EnderecoRepository;
import gogood.gogoodapi.repository.EnderecosUsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EnderecoService {
    @Autowired
    EnderecoRepository repository;
    @Autowired
    EnderecosUsuariosRepository enderecoUsuarioRepository;
    public Enderecos save(EnderecoUsuarioDTO enderecoUsuarioDTO) {
        Enderecos endereco = new Enderecos();
        endereco.setCep(enderecoUsuarioDTO.getCep());
        endereco.setRua(enderecoUsuarioDTO.getRua());
        endereco.setNumero(enderecoUsuarioDTO.getNumero());
        endereco.setBairro(enderecoUsuarioDTO.getBairro());
        endereco.setCreatedAt(Date.from(new Date().toInstant()));

        Enderecos savedEndereco = repository.save(endereco);

        // Criar e salvar a relação endereco_usuario
        EnderecosUsuarios enderecoUsuario = new EnderecosUsuarios();
        enderecoUsuario.setIdUsuario(enderecoUsuarioDTO.getIdUsuario());
        enderecoUsuario.setEnderecos(savedEndereco);
        enderecoUsuario.setTipoEndereco(enderecoUsuarioDTO.getTipoEndereco());

        enderecoUsuarioRepository.save(enderecoUsuario);
        return savedEndereco;
    }

    public List<Enderecos> findAllAdress(Integer id) {
        Optional<List<Enderecos>> enderecos = repository.findEnderecosByIdUsuario(id);
        return enderecos.orElse(null);
    }
}
