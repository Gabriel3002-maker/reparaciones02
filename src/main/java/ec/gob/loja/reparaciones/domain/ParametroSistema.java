package ec.gob.loja.reparaciones.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * The ParametroSistema entity.
 * @author Usuario
 */
@Schema(description = "The ParametroSistema entity.\n@author Usuario")
@Table("parametro_sistema")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ParametroSistema implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    /**
     * nombre
     */
    @Schema(description = "nombre", required = true)
    @NotNull(message = "must not be null")
    @Column("nombre")
    private String nombre;

    /**
     * codigo
     */
    @Schema(description = "codigo", required = true)
    @NotNull(message = "must not be null")
    @Column("codigo")
    private String codigo;

    /**
     * clase de java
     */
    @Schema(description = "clase de java", required = true)
    @NotNull(message = "must not be null")
    @Column("clase")
    private String clase;

    /**
     * valor
     */
    @Schema(description = "valor", required = true)
    @NotNull(message = "must not be null")
    @Column("valor")
    private String valor;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ParametroSistema id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public ParametroSistema nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public ParametroSistema codigo(String codigo) {
        this.setCodigo(codigo);
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getClase() {
        return this.clase;
    }

    public ParametroSistema clase(String clase) {
        this.setClase(clase);
        return this;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public String getValor() {
        return this.valor;
    }

    public ParametroSistema valor(String valor) {
        this.setValor(valor);
        return this;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParametroSistema)) {
            return false;
        }
        return getId() != null && getId().equals(((ParametroSistema) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParametroSistema{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", codigo='" + getCodigo() + "'" +
            ", clase='" + getClase() + "'" +
            ", valor='" + getValor() + "'" +
            "}";
    }
}
