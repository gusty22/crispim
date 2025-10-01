package com.finan.orcamento.service;

import com.finan.orcamento.model.UsuarioModel;
import com.finan.orcamento.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Método para buscar todos os usuários
    public List<UsuarioModel> buscarUsuario() {
        return usuarioRepository.findAll();
    }

    // Método para buscar um usuário por ID
    public UsuarioModel buscaId(Long id) {
        Optional<UsuarioModel> obj = usuarioRepository.findById(id);
        if (obj.isPresent()) {
            return obj.get();
        } else {
            throw new RuntimeException("Usuário com ID " + id + " não encontrado");
        }
    }

    // MÉTODO UNIFICADO PARA CRIAR E ATUALIZAR
    public UsuarioModel salvarOuAtualizar(UsuarioModel usuarioModel) {
        // Se o usuário já tem um ID, trata-se de uma atualização
        if (usuarioModel.getId() != null) {
            UsuarioModel usuarioExistente = buscaId(usuarioModel.getId());

            // Atualiza os campos do usuário existente com os novos dados
            usuarioExistente.setNomeUsuario(usuarioModel.getNomeUsuario());
            usuarioExistente.setRg(usuarioModel.getRg());
            usuarioExistente.setCpf(usuarioModel.getCpf());
            usuarioExistente.setNomeMae(usuarioModel.getNomeMae());

            return usuarioRepository.save(usuarioExistente);
        } else {
            // Se o ID é nulo, é um novo usuário sendo cadastrado
            return usuarioRepository.save(usuarioModel);
        }
    }

    // Método para deletar um usuário
    public void deletaUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    // Método para buscar usuários por nome
    public List<UsuarioModel> buscarUsuarioPorNome(String nome) {
        return usuarioRepository.findByNomeUsuarioContainingIgnoreCase(nome);
    }
}