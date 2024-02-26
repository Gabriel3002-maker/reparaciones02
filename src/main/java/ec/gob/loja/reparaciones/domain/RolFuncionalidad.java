package ec.gob.loja.reparaciones.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * The RolFuncionalidad entity.
 * @author Usuario
 */
@Schema(description = "The RolFuncionalidad entity.\n@author Usuario")
@Table("rol_funcionalidad")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RolFuncionalidad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    /**
     * rol
     */
    @Schema(description = "rol", required = true)
    @NotNull(message = "must not be null")
    @Column("rol")
    private String rol;

    /**
     * activo
     */
    @Schema(description = "activo")
    @Column("activo")
    private Boolean activo;

    /**
     * prioridad
     */
    @Schema(description = "prioridad", required = true)
    @NotNull(message = "must not be null")
    @Column("prioridad")
    private Integer prioridad;

    @Transient
    @JsonIgnoreProperties(value = { "hijos", "padre" }, allowSetters = true)
    private Funcionalidad funcionalidad;

    @Column("funcionalidad_id")
    private Long funcionalidadId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RolFuncionalidad id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRol() {
        return this.rol;
    }

    public RolFuncionalidad rol(String rol) {
        this.setRol(rol);
        return this;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public RolFuncionalidad activo(Boolean activo) {
        this.setActivo(activo);
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Integer getPrioridad() {
        return this.prioridad;
    }

    public RolFuncionalidad prioridad(Integer prioridad) {
        this.setPrioridad(prioridad);
        return this;
    }

    public void setPrioridad(Integer prioridad) {
        this.prioridad = prioridad;
    }

    public Funcionalidad getFuncionalidad() {
        return this.funcionalidad;
    }

    public void setFuncionalidad(Funcionalidad funcionalidad) {
        this.funcionalidad = funcionalidad;
        this.funcionalidadId = funcionalidad != null ? funcionalidad.getId() : null;
    }

    public RolFuncionalidad funcionalidad(Funcionalidad funcionalidad) {
        this.setFuncionalidad(funcionalidad);
        return this;
    }

    public Long getFuncionalidadId() {
        return this.funcionalidadId;
    }

    public void setFuncionalidadId(Long funcionalidad) {
        this.funcionalidadId = funcionalidad;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RolFuncionalidad)) {
            return false;
        }
        return getId() != null && getId().equals(((RolFuncionalidad) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RolFuncionalidad{" +
            "id=" + getId() +
            ", rol='" + getRol() + "'" +
            ", activo='" + getActivo() + "'" +
            ", prioridad=" + getPrioridad() +
            "}";
    }
}
