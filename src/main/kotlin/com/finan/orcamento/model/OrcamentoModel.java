package com.finan.orcamento.model;

import com.finan.orcamento.model.enums.IcmsEstados;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name="orcamento")
public class OrcamentoModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private IcmsEstados icmsEstados;

    @NotNull
    @Column(name="valor_orcamento")
    private BigDecimal valorOrcamento;

    @Column(name="valor_icms")
    private BigDecimal valorICMS;

    @ManyToOne
    @JoinColumn(name="usuario_id", referencedColumnName = "id")
    private UsuarioModel usuario;

    // Relacionamento com Cliente
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private ClienteModel cliente;

    public void calcularIcms() {
        if (this.icmsEstados != null && this.valorOrcamento != null) {
            this.valorICMS = this.icmsEstados.getStrategy().calcular(this.valorOrcamento);
        }
    }

    public OrcamentoModel(){}

    // Construtor completo
    public OrcamentoModel(Long id, IcmsEstados icmsEstados, @NotNull BigDecimal valorOrcamento, BigDecimal valorICMS, UsuarioModel usuario, ClienteModel cliente) {
        this.id = id;
        this.icmsEstados = icmsEstados;
        this.valorOrcamento = valorOrcamento;
        this.valorICMS = valorICMS;
        this.usuario = usuario;
        this.cliente = cliente;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IcmsEstados getIcmsEstados() {
        return icmsEstados;
    }

    public void setIcmsEstados(IcmsEstados icmsEstados) {
        this.icmsEstados = icmsEstados;
    }

    @NotNull
    public BigDecimal getValorOrcamento() {
        return valorOrcamento;
    }

    public void setValorOrcamento(@NotNull BigDecimal valorOrcamento) {
        this.valorOrcamento = valorOrcamento;
    }

    public BigDecimal getValorICMS() {
        return valorICMS;
    }

    public void setValorICMS(BigDecimal valorICMS) {
        this.valorICMS = valorICMS;
    }

    public UsuarioModel getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioModel usuario) {
        this.usuario = usuario;
    }

    public ClienteModel getCliente() {
        return cliente;
    }

    public void setCliente(ClienteModel cliente) {
        this.cliente = cliente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrcamentoModel that = (OrcamentoModel) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}