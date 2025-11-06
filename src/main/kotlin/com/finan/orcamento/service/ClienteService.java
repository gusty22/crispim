package com.finan.orcamento.service;

import com.finan.orcamento.model.ClienteModel;
import com.finan.orcamento.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<ClienteModel> buscarTodos() {
        return clienteRepository.findAll();
    }

    public ClienteModel buscarPorId(Long id) {
        Optional<ClienteModel> obj = clienteRepository.findById(id);
        if (obj.isPresent()) {
            return obj.get();
        } else {
            throw new RuntimeException("Cliente com ID " + id + " não encontrado");
        }
    }

    public ClienteModel salvarOuAtualizar(ClienteModel clienteModel) {
        if (clienteModel.getId() != null) {
            ClienteModel clienteExistente = buscarPorId(clienteModel.getId());
            clienteExistente.setNome(clienteModel.getNome());
            clienteExistente.setCpf(clienteModel.getCpf());
            return clienteRepository.save(clienteExistente);
        } else {
            return clienteRepository.save(clienteModel);
        }
    }

    public void deletar(Long id) {
        clienteRepository.deleteById(id);
    }

    // Método de pesquisa flexível
    public List<ClienteModel> pesquisarPorNomeOuCpf(String termo) {
        return clienteRepository.findByNomeContainingIgnoreCaseOrCpfContaining(termo, termo);
    }
}