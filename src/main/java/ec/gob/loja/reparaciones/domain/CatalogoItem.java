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
 * The CatalogoItem entity.
 * @author Usuario
 */
@Schema(description = "The CatalogoItem entity.\n@author Usuario")
@Table("catalogo_item")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CatalogoItem implements Serializable {

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
     * descripcion
     */
    @Schema(description = "descripcion")
    @Column("descripcion")
    private String descripcion;

    /**
     * catalogo Codigo
     */
    @Schema(description = "catalogo Codigo", required = true)
    @NotNull(message = "must not be null")
    @Column("catalogo_codigo")
    private String catalogoCodigo;

    /**
     * activo
     */
    @Schema(description = "activo")
    @Column("activo")
    private Boolean activo;

    @Transient
    @JsonIgnoreProperties(value = { "items" }, allowSetters = true)
    private Catalogo catalogo;

    @Column("catalogo_id")
    private Long catalogoId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CatalogoItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public CatalogoItem nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public CatalogoItem codigo(String codigo) {
        this.setCodigo(codigo);
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public CatalogoItem descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCatalogoCodigo() {
        return this.catalogoCodigo;
    }

    public CatalogoItem catalogoCodigo(String catalogoCodigo) {
        this.setCatalogoCodigo(catalogoCodigo);
        return this;
    }

    public void setCatalogoCodigo(String catalogoCodigo) {
        this.catalogoCodigo = catalogoCodigo;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public CatalogoItem activo(Boolean activo) {
        this.setActivo(activo);
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Catalogo getCatalogo() {
        return this.catalogo;
    }

    public void setCatalogo(Catalogo catalogo) {
        this.catalogo = catalogo;
        this.catalogoId = catalogo != null ? catalogo.getId() : null;
    }

    public CatalogoItem catalogo(Catalogo catalogo) {
        this.setCatalogo(catalogo);
        return this;
    }

    public Long getCatalogoId() {
        return this.catalogoId;
    }

    public void setCatalogoId(Long catalogo) {
        this.catalogoId = catalogo;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CatalogoItem)) {
            return false;
        }
        return getId() != null && getId().equals(((CatalogoItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CatalogoItem{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", codigo='" + getCodigo() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", catalogoCodigo='" + getCatalogoCodigo() + "'" +
            ", activo='" + getActivo() + "'" +
            "}";
    }
}
