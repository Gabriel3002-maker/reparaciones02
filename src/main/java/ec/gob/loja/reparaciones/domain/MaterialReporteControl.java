package ec.gob.loja.reparaciones.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A MaterialReporteControl.
 */
@Table("material_reporte_control")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MaterialReporteControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("codigo")
    private String codigo;

    @Column("cantidad_usada")
    private Integer cantidadUsada;

    @Column("observacion")
    private String observacion;

    @Transient
    @JsonIgnoreProperties(value = { "categoria", "materialDanio", "materialReporteControl" }, allowSetters = true)
    private Set<Material> materiales = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MaterialReporteControl id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public MaterialReporteControl codigo(String codigo) {
        this.setCodigo(codigo);
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Integer getCantidadUsada() {
        return this.cantidadUsada;
    }

    public MaterialReporteControl cantidadUsada(Integer cantidadUsada) {
        this.setCantidadUsada(cantidadUsada);
        return this;
    }

    public void setCantidadUsada(Integer cantidadUsada) {
        this.cantidadUsada = cantidadUsada;
    }

    public String getObservacion() {
        return this.observacion;
    }

    public MaterialReporteControl observacion(String observacion) {
        this.setObservacion(observacion);
        return this;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Set<Material> getMateriales() {
        return this.materiales;
    }

    public void setMateriales(Set<Material> materials) {
        if (this.materiales != null) {
            this.materiales.forEach(i -> i.setMaterialReporteControl(null));
        }
        if (materials != null) {
            materials.forEach(i -> i.setMaterialReporteControl(this));
        }
        this.materiales = materials;
    }

    public MaterialReporteControl materiales(Set<Material> materials) {
        this.setMateriales(materials);
        return this;
    }

    public MaterialReporteControl addMateriales(Material material) {
        this.materiales.add(material);
        material.setMaterialReporteControl(this);
        return this;
    }

    public MaterialReporteControl removeMateriales(Material material) {
        this.materiales.remove(material);
        material.setMaterialReporteControl(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MaterialReporteControl)) {
            return false;
        }
        return getId() != null && getId().equals(((MaterialReporteControl) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaterialReporteControl{" +
            "id=" + getId() +
            ", codigo='" + getCodigo() + "'" +
            ", cantidadUsada=" + getCantidadUsada() +
            ", observacion='" + getObservacion() + "'" +
            "}";
    }
}
