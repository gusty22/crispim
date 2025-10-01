package com.finan.orcamento.service;

import com.finan.orcamento.model.OrcamentoModel;
import com.finan.orcamento.model.UsuarioModel;
import com.finan.orcamento.repositories.OrcamentoRepository;
import com.finan.orcamento.repositories.UsuarioRepository; // IMPORTAR
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrcamentoService {
    @Autowired
    private OrcamentoRepository orcamentoRepository;

    // ADICIONE ESTA INJEÇÃO
    @Autowired
    private UsuarioRepository usuarioRepository;

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

    public OrcamentoModel cadastrarOrcamento(OrcamentoModel orcamentoModel){
        // 1. Validação: Checar se o objeto de usuário e seu ID foram enviados
        if (orcamentoModel.getUsuario() == null || orcamentoModel.getUsuario().getId() == null) {
            throw new RuntimeException("Usuário não informado para o orçamento!");
        }

        // 2. Buscar o usuário completo no banco de dados
        Long usuarioId = orcamentoModel.getUsuario().getId();
        UsuarioModel usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário com ID " + usuarioId + " não encontrado!"));

        // 3. Associar o usuário completo ao orçamento
        orcamentoModel.setUsuario(usuario);

        // 4. Calcular o ICMS (sua lógica de negócio)
        orcamentoModel.calcularIcms();

        // 5. Salvar o orçamento completo no banco
        return orcamentoRepository.save(orcamentoModel);
    }

    public OrcamentoModel atualizaCadastro(OrcamentoModel orcamentoModel, Long id){
        OrcamentoModel newOrcamentoModel = buscaId(id);

        // Atualiza os campos do orçamento
        newOrcamentoModel.setValorOrcamento(orcamentoModel.getValorOrcamento());
        newOrcamentoModel.setIcmsEstados(orcamentoModel.getIcmsEstados()); // Permitir mudar o estado

        // Recalcula o ICMS
        newOrcamentoModel.calcularIcms();

        // Não alteramos o usuário aqui, mas se fosse um requisito, a lógica seria similar
        return orcamentoRepository.save(newOrcamentoModel);
    }

    public void deletaOrcamento(Long id){
        orcamentoRepository.deleteById(id);
    }
}