package ec.gob.loja.reparaciones.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A DetalleDanio.
 */
@Table("detalle_danio")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DetalleDanio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("codigo")
    private String codigo;

    @Column("descripcion_danio")
    private String descripcionDanio;

    @Column("tecnico")
    private String tecnico;

    @Column("name_person")
    private String namePerson;

    @Column("direccion")
    private String direccion;

    @Column("estado_reparacion")
    private String estadoReparacion;

    @Column("observacion")
    private String observacion;

    @Transient
    @JsonIgnoreProperties(value = { "catalogo" }, allowSetters = true)
    private CatalogoItem tipoDanio;

    @Transient
    @JsonIgnoreProperties(value = { "materiales" }, allowSetters = true)
    private MaterialDanio materialDanio;

    @Transient
    @JsonIgnoreProperties(value = { "nombre" }, allowSetters = true)
    private Machinery maquinaria;

    @Transient
    @JsonIgnoreProperties(value = { "receptor" }, allowSetters = true)
    private OrdenBodega ordenBodega;

    @Transient
    @JsonIgnoreProperties(value = { "materialReporte" }, allowSetters = true)
    private DetalleReporteDanio detalleReporteDanio;

    @Column("tipo_danio_id")
    private Long tipoDanioId;

    @Column("material_danio_id")
    private Long materialDanioId;

    @Column("maquinaria_id")
    private Long maquinariaId;

    @Column("orden_bodega_id")
    private Long ordenBodegaId;

    @Column("detalle_reporte_danio_id")
    private Long detalleReporteDanioId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DetalleDanio id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public DetalleDanio codigo(String codigo) {
        this.setCodigo(codigo);
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcionDanio() {
        return this.descripcionDanio;
    }

    public DetalleDanio descripcionDanio(String descripcionDanio) {
        this.setDescripcionDanio(descripcionDanio);
        return this;
    }

    public void setDescripcionDanio(String descripcionDanio) {
        this.descripcionDanio = descripcionDanio;
    }

    public String getTecnico() {
        return this.tecnico;
    }

    public DetalleDanio tecnico(String tecnico) {
        this.setTecnico(tecnico);
        return this;
    }

    public void setTecnico(String tecnico) {
        this.tecnico = tecnico;
    }

    public String getNamePerson() {
        return this.namePerson;
    }

    public DetalleDanio namePerson(String namePerson) {
        this.setNamePerson(namePerson);
        return this;
    }

    public void setNamePerson(String namePerson) {
        this.namePerson = namePerson;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public DetalleDanio direccion(String direccion) {
        this.setDireccion(direccion);
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEstadoReparacion() {
        return this.estadoReparacion;
    }

    public DetalleDanio estadoReparacion(String estadoReparacion) {
        this.setEstadoReparacion(estadoReparacion);
        return this;
    }

    public void setEstadoReparacion(String estadoReparacion) {
        this.estadoReparacion = estadoReparacion;
    }

    public String getObservacion() {
        return this.observacion;
    }

    public DetalleDanio observacion(String observacion) {
        this.setObservacion(observacion);
        return this;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public CatalogoItem getTipoDanio() {
        return this.tipoDanio;
    }

    public void setTipoDanio(CatalogoItem catalogoItem) {
        this.tipoDanio = catalogoItem;
        this.tipoDanioId = catalogoItem != null ? catalogoItem.getId() : null;
    }

    public DetalleDanio tipoDanio(CatalogoItem catalogoItem) {
        this.setTipoDanio(catalogoItem);
        return this;
    }

    public MaterialDanio getMaterialDanio() {
        return this.materialDanio;
    }

    public void setMaterialDanio(MaterialDanio materialDanio) {
        this.materialDanio = materialDanio;
        this.materialDanioId = materialDanio != null ? materialDanio.getId() : null;
    }

    public DetalleDanio materialDanio(MaterialDanio materialDanio) {
        this.setMaterialDanio(materialDanio);
        return this;
    }

    public Machinery getMaquinaria() {
        return this.maquinaria;
    }

    public void setMaquinaria(Machinery machinery) {
        this.maquinaria = machinery;
        this.maquinariaId = machinery != null ? machinery.getId() : null;
    }

    public DetalleDanio maquinaria(Machinery machinery) {
        this.setMaquinaria(machinery);
        return this;
    }

    public OrdenBodega getOrdenBodega() {
        return this.ordenBodega;
    }

    public void setOrdenBodega(OrdenBodega ordenBodega) {
        this.ordenBodega = ordenBodega;
        this.ordenBodegaId = ordenBodega != null ? ordenBodega.getId() : null;
    }

    public DetalleDanio ordenBodega(OrdenBodega ordenBodega) {
        this.setOrdenBodega(ordenBodega);
        return this;
    }

    public DetalleReporteDanio getDetalleReporteDanio() {
        return this.detalleReporteDanio;
    }

    public void setDetalleReporteDanio(DetalleReporteDanio detalleReporteDanio) {
        this.detalleReporteDanio = detalleReporteDanio;
        this.detalleReporteDanioId = detalleReporteDanio != null ? detalleReporteDanio.getId() : null;
    }

    public DetalleDanio detalleReporteDanio(DetalleReporteDanio detalleReporteDanio) {
        this.setDetalleReporteDanio(detalleReporteDanio);
        return this;
    }

    public Long getTipoDanioId() {
        return this.tipoDanioId;
    }

    public void setTipoDanioId(Long catalogoItem) {
        this.tipoDanioId = catalogoItem;
    }

    public Long getMaterialDanioId() {
        return this.materialDanioId;
    }

    public void setMaterialDanioId(Long materialDanio) {
        this.materialDanioId = materialDanio;
    }

    public Long getMaquinariaId() {
        return this.maquinariaId;
    }

    public void setMaquinariaId(Long machinery) {
        this.maquinariaId = machinery;
    }

    public Long getOrdenBodegaId() {
        return this.ordenBodegaId;
    }

    public void setOrdenBodegaId(Long ordenBodega) {
        this.ordenBodegaId = ordenBodega;
    }

    public Long getDetalleReporteDanioId() {
        return this.detalleReporteDanioId;
    }

    public void setDetalleReporteDanioId(Long detalleReporteDanio) {
        this.detalleReporteDanioId = detalleReporteDanio;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DetalleDanio)) {
            return false;
        }
        return getId() != null && getId().equals(((DetalleDanio) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DetalleDanio{" +
            "id=" + getId() +
            ", codigo='" + getCodigo() + "'" +
            ", descripcionDanio='" + getDescripcionDanio() + "'" +
            ", tecnico='" + getTecnico() + "'" +
            ", namePerson='" + getNamePerson() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", estadoReparacion='" + getEstadoReparacion() + "'" +
            ", observacion='" + getObservacion() + "'" +
            "}";
    }
}
