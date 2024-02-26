package ec.gob.loja.reparaciones.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A RegistroDanio.
 */
@Table("registro_danio")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RegistroDanio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    /**
     * codigo
     */
    @Schema(description = "codigo", required = true)
    @NotNull(message = "must not be null")
    @Column("codigo")
    private String codigo;

    /**
     * fecha registro
     */
    @Schema(description = "fecha registro")
    @Column("fecha")
    private LocalDate fecha;

    /**
     * fecha inicio
     */
    @Schema(description = "fecha inicio")
    @Column("fecha_inicio")
    private LocalDate fechaInicio;

    /**
     * fecha fin
     */
    @Schema(description = "fecha fin")
    @Column("fecha_fin")
    private LocalDate fechaFin;

    /**
     * direccion
     */
    @Schema(description = "direccion")
    @Column("direccion")
    private String direccion;

    /**
     * parroquia
     */
    @Schema(description = "parroquia")
    @Column("parroquia")
    private String parroquia;

    /**
     * barrio
     */
    @Schema(description = "barrio")
    @Column("barrio")
    private String barrio;

    @Transient
    @JsonIgnoreProperties(value = { "tipoDanio", "materialDanio", "maquinaria", "ordenBodega", "detalleReporteDanio" }, allowSetters = true)
    private DetalleDanio detalleDanio;

    @Column("detalle_danio_id")
    private Long detalleDanioId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RegistroDanio id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public RegistroDanio codigo(String codigo) {
        this.setCodigo(codigo);
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public RegistroDanio fecha(LocalDate fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalDate getFechaInicio() {
        return this.fechaInicio;
    }

    public RegistroDanio fechaInicio(LocalDate fechaInicio) {
        this.setFechaInicio(fechaInicio);
        return this;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return this.fechaFin;
    }

    public RegistroDanio fechaFin(LocalDate fechaFin) {
        this.setFechaFin(fechaFin);
        return this;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public RegistroDanio direccion(String direccion) {
        this.setDireccion(direccion);
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getParroquia() {
        return this.parroquia;
    }

    public RegistroDanio parroquia(String parroquia) {
        this.setParroquia(parroquia);
        return this;
    }

    public void setParroquia(String parroquia) {
        this.parroquia = parroquia;
    }

    public String getBarrio() {
        return this.barrio;
    }

    public RegistroDanio barrio(String barrio) {
        this.setBarrio(barrio);
        return this;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public DetalleDanio getDetalleDanio() {
        return this.detalleDanio;
    }

    public void setDetalleDanio(DetalleDanio detalleDanio) {
        this.detalleDanio = detalleDanio;
        this.detalleDanioId = detalleDanio != null ? detalleDanio.getId() : null;
    }

    public RegistroDanio detalleDanio(DetalleDanio detalleDanio) {
        this.setDetalleDanio(detalleDanio);
        return this;
    }

    public Long getDetalleDanioId() {
        return this.detalleDanioId;
    }

    public void setDetalleDanioId(Long detalleDanio) {
        this.detalleDanioId = detalleDanio;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RegistroDanio)) {
            return false;
        }
        return getId() != null && getId().equals(((RegistroDanio) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RegistroDanio{" +
            "id=" + getId() +
            ", codigo='" + getCodigo() + "'" +
            ", fecha='" + getFecha() + "'" +
            ", fechaInicio='" + getFechaInicio() + "'" +
            ", fechaFin='" + getFechaFin() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", parroquia='" + getParroquia() + "'" +
            ", barrio='" + getBarrio() + "'" +
            "}";
    }
}
