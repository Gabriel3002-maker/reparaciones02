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
 * A MaterialDanio.
 */
@Table("material_danio")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MaterialDanio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("codigo")
    private String codigo;

    @Column("cantidad_pedida")
    private Integer cantidadPedida;

    @Column("observacion")
    private String observacion;

    @Transient
    @JsonIgnoreProperties(value = { "categoria", "materialDanio", "materialReporteControl" }, allowSetters = true)
    private Set<Material> materiales = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MaterialDanio id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public MaterialDanio codigo(String codigo) {
        this.setCodigo(codigo);
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Integer getCantidadPedida() {
        return this.cantidadPedida;
    }

    public MaterialDanio cantidadPedida(Integer cantidadPedida) {
        this.setCantidadPedida(cantidadPedida);
        return this;
    }

    public void setCantidadPedida(Integer cantidadPedida) {
        this.cantidadPedida = cantidadPedida;
    }

    public String getObservacion() {
        return this.observacion;
    }

    public MaterialDanio observacion(String observacion) {
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
            this.materiales.forEach(i -> i.setMaterialDanio(null));
        }
        if (materials != null) {
            materials.forEach(i -> i.setMaterialDanio(this));
        }
        this.materiales = materials;
    }

    public MaterialDanio materiales(Set<Material> materials) {
        this.setMateriales(materials);
        return this;
    }

    public MaterialDanio addMateriales(Material material) {
        this.materiales.add(material);
        material.setMaterialDanio(this);
        return this;
    }

    public MaterialDanio removeMateriales(Material material) {
        this.materiales.remove(material);
        material.setMaterialDanio(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MaterialDanio)) {
            return false;
        }
        return getId() != null && getId().equals(((MaterialDanio) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaterialDanio{" +
            "id=" + getId() +
            ", codigo='" + getCodigo() + "'" +
            ", cantidadPedida=" + getCantidadPedida() +
            ", observacion='" + getObservacion() + "'" +
            "}";
    }
}
