package com.finan.orcamento.repositories;

import com.finan.orcamento.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {

    // Método para buscar usuários por nome (ignorando case)
    List<UsuarioModel> findByNomeUsuarioContainingIgnoreCase(String nome);
}