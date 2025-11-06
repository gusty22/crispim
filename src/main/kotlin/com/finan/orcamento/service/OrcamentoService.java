package com.finan.orcamento.service;

import com.finan.orcamento.model.ClienteModel; // IMPORTAR
import com.finan.orcamento.model.OrcamentoModel;
import com.finan.orcamento.model.UsuarioModel;
import com.finan.orcamento.repositories.ClienteRepository; // IMPORTAR
import com.finan.orcamento.repositories.OrcamentoRepository;
import com.finan.orcamento.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrcamentoService {
    @Autowired
    private OrcamentoRepository orcamentoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // INJEÇÃO ADICIONADA
    @Autowired
    private ClienteRepository clienteRepository;

    public List<OrcamentoModel> buscarCadastro(){
        return orcamentoRepository.findAll();
    }

    public OrcamentoModel buscaId(Long id){
        Optional<OrcamentoModel> obj = orcamentoRepository.findById(id);
        if (obj.isPresent()) {
            return obj.get();
        } else {
            throw new RuntimeException("Orçamento não encontrado");
        }
    }

    // MÉTODO ATUALIZADO
    public OrcamentoModel cadastrarOrcamento(OrcamentoModel orcamentoModel){
        // 1. Valida e busca o Usuário
        if (orcamentoModel.getUsuario() == null || orcamentoModel.getUsuario().getId() == null) {
            throw new RuntimeException("Usuário não informado para o orçamento!");
        }
        Long usuarioId = orcamentoModel.getUsuario().getId();
        UsuarioModel usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário com ID " + usuarioId + " não encontrado!"));
        orcamentoModel.setUsuario(usuario);

        // 2. Valida e busca o Cliente
        if (orcamentoModel.getCliente() == null || orcamentoModel.getCliente().getId() == null) {
            throw new RuntimeException("Cliente não informado para o orçamento!");
        }
        Long clienteId = orcamentoModel.getCliente().getId();
        ClienteModel cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente com ID " + clienteId + " não encontrado!"));
        orcamentoModel.setCliente(cliente);

        // 3. Calcula ICMS
        orcamentoModel.calcularIcms();

        // 4. Salva o orçamento completo
        return orcamentoRepository.save(orcamentoModel);
    }

    // ... (demais métodos) ...
    public OrcamentoModel atualizaCadastro(OrcamentoModel orcamentoModel, Long id){
        OrcamentoModel newOrcamentoModel = buscaId(id);

        newOrcamentoModel.setValorOrcamento(orcamentoModel.getValorOrcamento());
        newOrcamentoModel.setIcmsEstados(orcamentoModel.getIcmsEstados());

        // Recalcula o ICMS
        newOrcamentoModel.calcularIcms();

        // Aqui você poderia adicionar a lógica para atualizar o cliente/usuário se necessário

        return orcamentoRepository.save(newOrcamentoModel);
    }

    public void deletaOrcamento(Long id){
        orcamentoRepository.deleteById(id);
    }
}