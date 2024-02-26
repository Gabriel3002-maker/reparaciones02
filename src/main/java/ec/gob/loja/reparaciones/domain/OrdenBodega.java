package ec.gob.loja.reparaciones.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A OrdenBodega.
 */
@Table("orden_bodega")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrdenBodega implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("codigo")
    private String codigo;

    @Column("detalle_necesidad")
    private String detalleNecesidad;

    @Column("fecha")
    private LocalDate fecha;

    @Transient
    @JsonIgnoreProperties(value = { "tipoIdentificacion", "usuario" }, allowSetters = true)
    private Persona receptor;

    @Column("receptor_id")
    private Long receptorId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrdenBodega id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public OrdenBodega codigo(String codigo) {
        this.setCodigo(codigo);
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDetalleNecesidad() {
        return this.detalleNecesidad;
    }

    public OrdenBodega detalleNecesidad(String detalleNecesidad) {
        this.setDetalleNecesidad(detalleNecesidad);
        return this;
    }

    public void setDetalleNecesidad(String detalleNecesidad) {
        this.detalleNecesidad = detalleNecesidad;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public OrdenBodega fecha(LocalDate fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Persona getReceptor() {
        return this.receptor;
    }

    public void setReceptor(Persona persona) {
        this.receptor = persona;
        this.receptorId = persona != null ? persona.getId() : null;
    }

    public OrdenBodega receptor(Persona persona) {
        this.setReceptor(persona);
        return this;
    }

    public Long getReceptorId() {
        return this.receptorId;
    }

    public void setReceptorId(Long persona) {
        this.receptorId = persona;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrdenBodega)) {
            return false;
        }
        return getId() != null && getId().equals(((OrdenBodega) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrdenBodega{" +
            "id=" + getId() +
            ", codigo='" + getCodigo() + "'" +
            ", detalleNecesidad='" + getDetalleNecesidad() + "'" +
            ", fecha='" + getFecha() + "'" +
            "}";
    }
}
