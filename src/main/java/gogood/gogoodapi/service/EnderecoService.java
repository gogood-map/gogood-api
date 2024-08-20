package gogood.gogoodapi.service;

import gogood.gogoodapi.domain.DTOS.EnderecoUsuarioDTO;
import gogood.gogoodapi.domain.models.usuarios.Usuarios;
import gogood.gogoodapi.domain.models.endereco.Enderecos;
import gogood.gogoodapi.domain.models.endereco.EnderecosResponse;
import gogood.gogoodapi.domain.models.endereco.EnderecosUsuarios;
import gogood.gogoodapi.repository.EnderecoRepository;
import gogood.gogoodapi.repository.EnderecosUsuariosRepository;
import gogood.gogoodapi.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EnderecoService {
    @Autowired
    EnderecoRepository repository;

    @Autowired
    EnderecosUsuariosRepository enderecoUsuarioRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    public Enderecos save(EnderecoUsuarioDTO enderecoUsuarioDTO) {
        Optional<Usuarios> usuarioOptional = usuarioRepository.findById(enderecoUsuarioDTO.getUsuarioId());
        if (!usuarioOptional.isPresent()) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        Usuarios usuario = usuarioOptional.get();

        // Criar e salvar o endereço
        Enderecos endereco = new Enderecos();
        endereco.setCep(enderecoUsuarioDTO.getCep());
        endereco.setRua(enderecoUsuarioDTO.getRua());
        endereco.setNumero(enderecoUsuarioDTO.getNumero());
        endereco.setCidade(enderecoUsuarioDTO.getCidade());
        endereco.setBairro(enderecoUsuarioDTO.getBairro());
        endereco.setCreatedAt(new Date());
        Enderecos enderecoSalvo = repository.save(endereco);

        // Criar e salvar a associação entre usuário e endereço
        EnderecosUsuarios enderecosUsuarios = new EnderecosUsuarios();
        enderecosUsuarios.setUsuarios(usuario);
        enderecosUsuarios.setEnderecos(enderecoSalvo);
        enderecosUsuarios.setTipoEndereco(enderecoUsuarioDTO.getTipoEndereco());
        enderecoUsuarioRepository.save(enderecosUsuarios);

        // Adicionar a associação ao endereço salvo para garantir que ela seja serializada
        enderecoSalvo.setUsuarios(Set.of(enderecosUsuarios));

        return enderecoSalvo;
    }

    @Transactional
    public List<EnderecosResponse> getEnderecosByUsuarioId(Integer usuarioId) {
        List<EnderecosUsuarios> enderecosUsuariosList = enderecoUsuarioRepository.findByUsuarioId(usuarioId);
        List<EnderecosResponse> responses = new ArrayList<>();

        for(EnderecosUsuarios enderecosUsuarios: enderecosUsuariosList){
            EnderecosResponse response = new EnderecosResponse();
            response.setTipoEndereco(enderecosUsuarios.getTipoEndereco());
            response.setEnderecos(enderecosUsuarios.getEnderecos());
            responses.add(response);
        }

        return responses;
    }

    public void deleteEnderecoByUsuarioId(Integer usuarioId, Integer enderecoId) {
        // Buscar a associação entre usuário e endereço
        Optional<EnderecosUsuarios> association = enderecoUsuarioRepository.findByUsuarioIdAndEnderecosId(usuarioId, enderecoId);
        if (association.isPresent()) {
            enderecoUsuarioRepository.delete(association.get());

            // Opcional: Deletar o endereço se não houver outras associações
            List<EnderecosUsuarios> remainingAssociations = enderecoUsuarioRepository.findByEnderecosId(enderecoId);
            if (remainingAssociations.isEmpty()) {
                repository.deleteById(enderecoId);
            }
        } else {
            throw new IllegalArgumentException("Associação entre usuário e endereço não encontrada");
        }
    }

    public Enderecos update(Integer id, EnderecoUsuarioDTO enderecoUsuarioDTO) {
        Optional<Enderecos> enderecoOptional = repository.findById(id);
        if (!enderecoOptional.isPresent()) {
            throw new IllegalArgumentException("Endereço não encontrado");
        }

        Enderecos endereco = enderecoOptional.get();
        endereco.setCep(enderecoUsuarioDTO.getCep());
        endereco.setRua(enderecoUsuarioDTO.getRua());
        endereco.setCidade(enderecoUsuarioDTO.getCidade());
        endereco.setNumero(enderecoUsuarioDTO.getNumero());
        endereco.setBairro(enderecoUsuarioDTO.getBairro());
        endereco.setCreatedAt(new Date());
        repository.save(endereco);

        List<EnderecosUsuarios> associacoes = enderecoUsuarioRepository.findByEnderecosId(id);
        for (EnderecosUsuarios associacao : associacoes) {
            associacao.setTipoEndereco(enderecoUsuarioDTO.getTipoEndereco());
            enderecoUsuarioRepository.save(associacao);
        }
        return endereco;
    }
}
