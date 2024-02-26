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
 * The Catalogo entity.
 * @author Usuario
 */
@Schema(description = "The Catalogo entity.\n@author Usuario")
@Table("catalogo")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Catalogo implements Serializable {

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

    @Transient
    @JsonIgnoreProperties(value = { "catalogo" }, allowSetters = true)
    private Set<CatalogoItem> items = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Catalogo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Catalogo nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public Catalogo codigo(String codigo) {
        this.setCodigo(codigo);
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Catalogo descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<CatalogoItem> getItems() {
        return this.items;
    }

    public void setItems(Set<CatalogoItem> catalogoItems) {
        if (this.items != null) {
            this.items.forEach(i -> i.setCatalogo(null));
        }
        if (catalogoItems != null) {
            catalogoItems.forEach(i -> i.setCatalogo(this));
        }
        this.items = catalogoItems;
    }

    public Catalogo items(Set<CatalogoItem> catalogoItems) {
        this.setItems(catalogoItems);
        return this;
    }

    public Catalogo addItems(CatalogoItem catalogoItem) {
        this.items.add(catalogoItem);
        catalogoItem.setCatalogo(this);
        return this;
    }

    public Catalogo removeItems(CatalogoItem catalogoItem) {
        this.items.remove(catalogoItem);
        catalogoItem.setCatalogo(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Catalogo)) {
            return false;
        }
        return getId() != null && getId().equals(((Catalogo) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Catalogo{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", codigo='" + getCodigo() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
