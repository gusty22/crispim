package com.finan.orcamento.controller;

import com.finan.orcamento.model.OrcamentoModel;
import com.finan.orcamento.service.OrcamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity; // Removido HttpStatus
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/orcamentos")
public class OrcamentoController {
    @Autowired
    private OrcamentoService orcamentoService;

    // 1. Endpoint para servir a PÁGINA HTML
    @GetMapping
    public String paginaOrcamentos() {
        return "orcamentoPage";
    }

    // --- API REST ---

    // 2. API para LISTAR
    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<List<OrcamentoModel>> buscaTodosOrcamentos(){
        return ResponseEntity.ok(orcamentoService.buscarCadastro());
    }

    // 3. API para OBTER POR ID
    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<OrcamentoModel> buscaPorId(@PathVariable Long id){
        try {
            OrcamentoModel orcamento = orcamentoService.buscaId(id);
            return ResponseEntity.ok(orcamento);
        } catch (RuntimeException e) {
            // Retorna 404 se o serviço lançar a exceção
            return ResponseEntity.notFound().build();
        }
    }

    // 4. API para SALVAR (Criar/Atualizar) - UNIFICADO
    @PostMapping("/api")
    @ResponseBody
    public ResponseEntity<OrcamentoModel> salvarOuAtualizarOrcamento(@RequestBody OrcamentoModel orcamentoModel){
        // Esta única função agora trata tanto a CRIAÇÃO (sem ID no body)
        // quanto a ATUALIZAÇÃO (com ID no body),
        // assim como o seu frontend espera.
        try {
            OrcamentoModel orcamentoSalvo = orcamentoService.salvarOuAtualizar(orcamentoModel);
            return ResponseEntity.ok(orcamentoSalvo);
        } catch (RuntimeException e) {
            // Retorna 400 Bad Request se a validação falhar (ex: sem usuário E sem cliente)
            return ResponseEntity.badRequest().body(null); // ideal seria um DTO de erro
        }
    }

    // 5. API para DELETAR
    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteOrcamento(@PathVariable Long id){
        try {
            orcamentoService.deletaOrcamento(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            // Retorna 404 se tentar deletar algo que não existe
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint PUT removido, pois foi unificado no POST
}