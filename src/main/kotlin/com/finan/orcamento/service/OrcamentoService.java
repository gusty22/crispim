package com.finan.orcamento.service;

import com.finan.orcamento.model.ClienteModel;
import com.finan.orcamento.model.OrcamentoModel;
import com.finan.orcamento.model.UsuarioModel;
import com.finan.orcamento.repositories.ClienteRepository;
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
            throw new RuntimeException("Orçamento com ID " + id + " não encontrado");
        }
    }

    // NOVO: Método privado para validar e preencher o orçamento
    // Isso implementa a LÓGICA DE USUÁRIO/CLIENTE INDEPENDENTE
    private OrcamentoModel validarEPreencherCampos(OrcamentoModel orcamento) {
        boolean hasUsuario = orcamento.getUsuario() != null && orcamento.getUsuario().getId() != null;
        boolean hasCliente = orcamento.getCliente() != null && orcamento.getCliente().getId() != null;

        // 1. Validação: Deve ter pelo menos um
        if (!hasUsuario && !hasCliente) {
            throw new RuntimeException("Orçamento deve estar associado a, pelo menos, um Usuário ou um Cliente!");
        }

        // 2. Processa Usuário (se existir)
        if (hasUsuario) {
            Long usuarioId = orcamento.getUsuario().getId();
            UsuarioModel usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuário com ID " + usuarioId + " não encontrado!"));
            orcamento.setUsuario(usuario);
        } else {
            orcamento.setUsuario(null); // Garante que está nulo se não for enviado
        }

        // 3. Processa Cliente (se existir)
        if (hasCliente) {
            Long clienteId = orcamento.getCliente().getId();
            ClienteModel cliente = clienteRepository.findById(clienteId)
                    .orElseThrow(() -> new RuntimeException("Cliente com ID " + clienteId + " não encontrado!"));
            orcamento.setCliente(cliente);
        } else {
            orcamento.setCliente(null); // Garante que está nulo se não for enviado
        }

        // 4. Calcula ICMS
        orcamento.calcularIcms();

        return orcamento;
    }

    // NOVO: Método unificado para Salvar ou Atualizar
    public OrcamentoModel salvarOuAtualizar(OrcamentoModel orcamentoModel) {

        // 1. Valida e preenche os campos (Usuário, Cliente, ICMS)
        // Esta função agora contém a lógica principal da sua regra de negócio
        OrcamentoModel orcamentoValidado = validarEPreencherCampos(orcamentoModel);

        // 2. Verifica se é Crate ou Update
        if (orcamentoModel.getId() != null) {
            // É uma ATUALIZAÇÃO
            // Busca o existente para não perder dados não enviados (ex: data de criação, etc)
            OrcamentoModel orcamentoExistente = buscaId(orcamentoModel.getId());

            // Atualiza os campos do existente com os dados validados
            orcamentoExistente.setIcmsEstados(orcamentoValidado.getIcmsEstados());
            orcamentoExistente.setValorOrcamento(orcamentoValidado.getValorOrcamento());
            orcamentoExistente.setValorICMS(orcamentoValidado.getValorICMS()); // Valor calculado
            orcamentoExistente.setUsuario(orcamentoValidado.getUsuario()); // Pode ser nulo
            orcamentoExistente.setCliente(orcamentoValidado.getCliente()); // Pode ser nulo

            return orcamentoRepository.save(orcamentoExistente);
        } else {
            // É um CADASTRO (novo)
            // O orcamentoValidado já está pronto para ser salvo
            return orcamentoRepository.save(orcamentoValidado);
        }
    }

    // Os métodos 'cadastrarOrcamento' e 'atualizaCadastro' foram substituídos
    // pela lógica unificada em 'salvarOuAtualizar'

    public void deletaOrcamento(Long id){
        // Verifica se existe antes de deletar
        if (!orcamentoRepository.existsById(id)) {
            throw new RuntimeException("Orçamento com ID " + id + " não encontrado, não é possível deletar.");
        }
        orcamentoRepository.deleteById(id);
    }
}