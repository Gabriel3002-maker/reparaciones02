package ec.gob.loja.reparaciones.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A DetalleReporteDanio.
 */
@Table("detalle_reporte_danio")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DetalleReporteDanio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("codigo")
    private Integer codigo;

    @Column("fecha")
    private LocalDate fecha;

    @Column("contribuyente")
    private String contribuyente;

    @Column("direccion")
    private String direccion;

    @Column("referencia")
    private String referencia;

    @Column("horas_trabajadas")
    private Integer horasTrabajadas;

    @Column("personal_responsable")
    private String personalResponsable;

    @Transient
    @JsonIgnoreProperties(value = { "materiales" }, allowSetters = true)
    private MaterialReporteControl materialReporte;

    @Column("material_reporte_id")
    private Long materialReporteId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DetalleReporteDanio id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCodigo() {
        return this.codigo;
    }

    public DetalleReporteDanio codigo(Integer codigo) {
        this.setCodigo(codigo);
        return this;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public DetalleReporteDanio fecha(LocalDate fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getContribuyente() {
        return this.contribuyente;
    }

    public DetalleReporteDanio contribuyente(String contribuyente) {
        this.setContribuyente(contribuyente);
        return this;
    }

    public void setContribuyente(String contribuyente) {
        this.contribuyente = contribuyente;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public DetalleReporteDanio direccion(String direccion) {
        this.setDireccion(direccion);
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getReferencia() {
        return this.referencia;
    }

    public DetalleReporteDanio referencia(String referencia) {
        this.setReferencia(referencia);
        return this;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Integer getHorasTrabajadas() {
        return this.horasTrabajadas;
    }

    public DetalleReporteDanio horasTrabajadas(Integer horasTrabajadas) {
        this.setHorasTrabajadas(horasTrabajadas);
        return this;
    }

    public void setHorasTrabajadas(Integer horasTrabajadas) {
        this.horasTrabajadas = horasTrabajadas;
    }

    public String getPersonalResponsable() {
        return this.personalResponsable;
    }

    public DetalleReporteDanio personalResponsable(String personalResponsable) {
        this.setPersonalResponsable(personalResponsable);
        return this;
    }

    public void setPersonalResponsable(String personalResponsable) {
        this.personalResponsable = personalResponsable;
    }

    public MaterialReporteControl getMaterialReporte() {
        return this.materialReporte;
    }

    public void setMaterialReporte(MaterialReporteControl materialReporteControl) {
        this.materialReporte = materialReporteControl;
        this.materialReporteId = materialReporteControl != null ? materialReporteControl.getId() : null;
    }

    public DetalleReporteDanio materialReporte(MaterialReporteControl materialReporteControl) {
        this.setMaterialReporte(materialReporteControl);
        return this;
    }

    public Long getMaterialReporteId() {
        return this.materialReporteId;
    }

    public void setMaterialReporteId(Long materialReporteControl) {
        this.materialReporteId = materialReporteControl;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DetalleReporteDanio)) {
            return false;
        }
        return getId() != null && getId().equals(((DetalleReporteDanio) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DetalleReporteDanio{" +
            "id=" + getId() +
            ", codigo=" + getCodigo() +
            ", fecha='" + getFecha() + "'" +
            ", contribuyente='" + getContribuyente() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", referencia='" + getReferencia() + "'" +
            ", horasTrabajadas=" + getHorasTrabajadas() +
            ", personalResponsable='" + getPersonalResponsable() + "'" +
            "}";
    }
}
