package com.finan.orcamento.model;

import com.finan.orcamento.model.enums.IcmsEstados;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    public void calcularIcms() {
        this.valorICMS = this.icmsEstados.getStrategy().calcular(this.valorOrcamento);
    }

    public OrcamentoModel(){}

    public OrcamentoModel(Long id, IcmsEstados icmsEstados, @NotNull BigDecimal valorOrcamento, BigDecimal valorICMS, UsuarioModel usuario) {
        this.id = id;
        this.icmsEstados = icmsEstados;
        this.valorOrcamento = valorOrcamento;
        this.valorICMS = valorICMS;
        this.usuario = usuario;
    }

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrcamentoModel that = (OrcamentoModel) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
