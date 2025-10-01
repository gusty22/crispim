package com.finan.orcamento.controller;

import com.finan.orcamento.model.OrcamentoModel;
import com.finan.orcamento.service.OrcamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller; // MUDE PARA @Controller
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller // MUDE AQUI
@RequestMapping("/orcamentos")
public class OrcamentoController {
    @Autowired
    private OrcamentoService orcamentoService;

    // Endpoint para servir a PÁGINA HTML
    @GetMapping
    public String paginaOrcamentos() {
        return "orcamentoPage"; // Nome do arquivo HTML que vamos criar
    }

    // --- A PARTIR DAQUI, SÃO AS APIs (retornam JSON) ---

    // API para LISTAR todos os orçamentos
    @GetMapping("/api")
    @ResponseBody // Adicione @ResponseBody para retornar JSON
    public ResponseEntity<List<OrcamentoModel>> buscaTodosOrcamentos(){
        return ResponseEntity.ok(orcamentoService.buscarCadastro());
    }

    // API para buscar por ID
    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<OrcamentoModel> buscaPorId(@PathVariable Long id){
        return ResponseEntity.ok().body(orcamentoService.buscaId(id));
    }

    // API para CADASTRAR um novo orçamento
    @PostMapping("/api")
    @ResponseBody
    public ResponseEntity<OrcamentoModel> cadastraOrcamento(@RequestBody OrcamentoModel orcamentoModel){
        OrcamentoModel novoOrcamento = orcamentoService.cadastrarOrcamento(orcamentoModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoOrcamento);
    }

    // API para ATUALIZAR
    @PutMapping("/api/{id}") // Use PUT para atualizações completas
    @ResponseBody
    public ResponseEntity<OrcamentoModel> atualizaOrcamento(@RequestBody OrcamentoModel orcamentoModel, @PathVariable Long id){
        OrcamentoModel orcamentoAtualizado = orcamentoService.atualizaCadastro(orcamentoModel, id);
        return ResponseEntity.ok().body(orcamentoAtualizado);
    }

    // API para DELETAR
    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteOrcamento(@PathVariable Long id){
        orcamentoService.deletaOrcamento(id);
        return ResponseEntity.noContent().build();
    }
}