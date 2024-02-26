package ec.gob.loja.reparaciones.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Machinery.
 */
@Table("machinery")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Machinery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("codigo")
    private String codigo;

    @Column("descripcion")
    private String descripcion;

    @Column("horas_trabajadas")
    private Double horasTrabajadas;

    @Transient
    @JsonIgnoreProperties(value = { "catalogo" }, allowSetters = true)
    private CatalogoItem nombre;

    @Column("nombre_id")
    private Long nombreId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Machinery id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public Machinery codigo(String codigo) {
        this.setCodigo(codigo);
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Machinery descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getHorasTrabajadas() {
        return this.horasTrabajadas;
    }

    public Machinery horasTrabajadas(Double horasTrabajadas) {
        this.setHorasTrabajadas(horasTrabajadas);
        return this;
    }

    public void setHorasTrabajadas(Double horasTrabajadas) {
        this.horasTrabajadas = horasTrabajadas;
    }

    public CatalogoItem getNombre() {
        return this.nombre;
    }

    public void setNombre(CatalogoItem catalogoItem) {
        this.nombre = catalogoItem;
        this.nombreId = catalogoItem != null ? catalogoItem.getId() : null;
    }

    public Machinery nombre(CatalogoItem catalogoItem) {
        this.setNombre(catalogoItem);
        return this;
    }

    public Long getNombreId() {
        return this.nombreId;
    }

    public void setNombreId(Long catalogoItem) {
        this.nombreId = catalogoItem;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Machinery)) {
            return false;
        }
        return getId() != null && getId().equals(((Machinery) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Machinery{" +
            "id=" + getId() +
            ", codigo='" + getCodigo() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", horasTrabajadas=" + getHorasTrabajadas() +
            "}";
    }
}
