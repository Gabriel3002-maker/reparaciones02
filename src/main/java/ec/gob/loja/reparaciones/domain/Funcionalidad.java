package ec.gob.loja.reparaciones.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * The Funcionalidad entity.
 * @author Usuario
 */
@Schema(description = "The Funcionalidad entity.\n@author Usuario")
@Table("funcionalidad")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Funcionalidad implements Serializable {

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
     * descripcion
     */
    @Schema(description = "descripcion")
    @Column("descripcion")
    private String descripcion;

    /**
     * url
     */
    @Schema(description = "url")
    @Size(max = 80)
    @Column("url")
    private String url;

    /**
     * activo
     */
    @Schema(description = "activo", required = true)
    @NotNull(message = "must not be null")
    @Column("activo")
    private Boolean activo;

    /**
     * icono menu
     */
    @Schema(description = "icono menu")
    @Column("icono")
    private String icono;

    /**
     * visible
     */
    @Schema(description = "visible")
    @Column("visible")
    private Boolean visible;

    @Transient
    @JsonIgnoreProperties(value = { "hijos", "padre" }, allowSetters = true)
    private Set<Funcionalidad> hijos = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "hijos", "padre" }, allowSetters = true)
    private Funcionalidad padre;

    @Column("padre_id")
    private Long padreId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Funcionalidad id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Funcionalidad nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Funcionalidad descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrl() {
        return this.url;
    }

    public Funcionalidad url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public Funcionalidad activo(Boolean activo) {
        this.setActivo(activo);
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getIcono() {
        return this.icono;
    }

    public Funcionalidad icono(String icono) {
        this.setIcono(icono);
        return this;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public Boolean getVisible() {
        return this.visible;
    }

    public Funcionalidad visible(Boolean visible) {
        this.setVisible(visible);
        return this;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Set<Funcionalidad> getHijos() {
        return this.hijos;
    }

    public void setHijos(Set<Funcionalidad> funcionalidads) {
        if (this.hijos != null) {
            this.hijos.forEach(i -> i.setPadre(null));
        }
        if (funcionalidads != null) {
            funcionalidads.forEach(i -> i.setPadre(this));
        }
        this.hijos = funcionalidads;
    }

    public Funcionalidad hijos(Set<Funcionalidad> funcionalidads) {
        this.setHijos(funcionalidads);
        return this;
    }

    public Funcionalidad addHijos(Funcionalidad funcionalidad) {
        this.hijos.add(funcionalidad);
        funcionalidad.setPadre(this);
        return this;
    }

    public Funcionalidad removeHijos(Funcionalidad funcionalidad) {
        this.hijos.remove(funcionalidad);
        funcionalidad.setPadre(null);
        return this;
    }

    public Funcionalidad getPadre() {
        return this.padre;
    }

    public void setPadre(Funcionalidad funcionalidad) {
        this.padre = funcionalidad;
        this.padreId = funcionalidad != null ? funcionalidad.getId() : null;
    }

    public Funcionalidad padre(Funcionalidad funcionalidad) {
        this.setPadre(funcionalidad);
        return this;
    }

    public Long getPadreId() {
        return this.padreId;
    }

    public void setPadreId(Long funcionalidad) {
        this.padreId = funcionalidad;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Funcionalidad)) {
            return false;
        }
        return getId() != null && getId().equals(((Funcionalidad) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Funcionalidad{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", url='" + getUrl() + "'" +
            ", activo='" + getActivo() + "'" +
            ", icono='" + getIcono() + "'" +
            ", visible='" + getVisible() + "'" +
            "}";
    }
}
