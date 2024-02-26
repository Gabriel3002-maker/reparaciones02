package ec.gob.loja.reparaciones.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Material.
 */
@Table("material")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Material implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("codigo")
    private String codigo;

    @NotNull(message = "must not be null")
    @Column("nombre")
    private String nombre;

    @NotNull(message = "must not be null")
    @Column("valor_unitario")
    private Double valorUnitario;

    @Column("stock")
    private Integer stock;

    @Column("activo")
    private Boolean activo;

    @Column("descripcion")
    private String descripcion;

    @Column("creado_por")
    private String creadoPor;

    @Column("fecha_creacion")
    private LocalDate fechaCreacion;

    @Column("actualizado_por")
    private String actualizadoPor;

    @Column("fecha_modificacion")
    private LocalDate fechaModificacion;

    @Transient
    @JsonIgnoreProperties(value = { "catalogo" }, allowSetters = true)
    private CatalogoItem categoria;

    @Transient
    @JsonIgnoreProperties(value = { "materiales" }, allowSetters = true)
    private MaterialDanio materialDanio;

    @Transient
    @JsonIgnoreProperties(value = { "materiales" }, allowSetters = true)
    private MaterialReporteControl materialReporteControl;

    @Column("categoria_id")
    private Long categoriaId;

    @Column("material_danio_id")
    private Long materialDanioId;

    @Column("material_reporte_control_id")
    private Long materialReporteControlId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Material id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public Material codigo(String codigo) {
        this.setCodigo(codigo);
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Material nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getValorUnitario() {
        return this.valorUnitario;
    }

    public Material valorUnitario(Double valorUnitario) {
        this.setValorUnitario(valorUnitario);
        return this;
    }

    public void setValorUnitario(Double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public Integer getStock() {
        return this.stock;
    }

    public Material stock(Integer stock) {
        this.setStock(stock);
        return this;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public Material activo(Boolean activo) {
        this.setActivo(activo);
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Material descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCreadoPor() {
        return this.creadoPor;
    }

    public Material creadoPor(String creadoPor) {
        this.setCreadoPor(creadoPor);
        return this;
    }

    public void setCreadoPor(String creadoPor) {
        this.creadoPor = creadoPor;
    }

    public LocalDate getFechaCreacion() {
        return this.fechaCreacion;
    }

    public Material fechaCreacion(LocalDate fechaCreacion) {
        this.setFechaCreacion(fechaCreacion);
        return this;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getActualizadoPor() {
        return this.actualizadoPor;
    }

    public Material actualizadoPor(String actualizadoPor) {
        this.setActualizadoPor(actualizadoPor);
        return this;
    }

    public void setActualizadoPor(String actualizadoPor) {
        this.actualizadoPor = actualizadoPor;
    }

    public LocalDate getFechaModificacion() {
        return this.fechaModificacion;
    }

    public Material fechaModificacion(LocalDate fechaModificacion) {
        this.setFechaModificacion(fechaModificacion);
        return this;
    }

    public void setFechaModificacion(LocalDate fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public CatalogoItem getCategoria() {
        return this.categoria;
    }

    public void setCategoria(CatalogoItem catalogoItem) {
        this.categoria = catalogoItem;
        this.categoriaId = catalogoItem != null ? catalogoItem.getId() : null;
    }

    public Material categoria(CatalogoItem catalogoItem) {
        this.setCategoria(catalogoItem);
        return this;
    }

    public MaterialDanio getMaterialDanio() {
        return this.materialDanio;
    }

    public void setMaterialDanio(MaterialDanio materialDanio) {
        this.materialDanio = materialDanio;
        this.materialDanioId = materialDanio != null ? materialDanio.getId() : null;
    }

    public Material materialDanio(MaterialDanio materialDanio) {
        this.setMaterialDanio(materialDanio);
        return this;
    }

    public MaterialReporteControl getMaterialReporteControl() {
        return this.materialReporteControl;
    }

    public void setMaterialReporteControl(MaterialReporteControl materialReporteControl) {
        this.materialReporteControl = materialReporteControl;
        this.materialReporteControlId = materialReporteControl != null ? materialReporteControl.getId() : null;
    }

    public Material materialReporteControl(MaterialReporteControl materialReporteControl) {
        this.setMaterialReporteControl(materialReporteControl);
        return this;
    }

    public Long getCategoriaId() {
        return this.categoriaId;
    }

    public void setCategoriaId(Long catalogoItem) {
        this.categoriaId = catalogoItem;
    }

    public Long getMaterialDanioId() {
        return this.materialDanioId;
    }

    public void setMaterialDanioId(Long materialDanio) {
        this.materialDanioId = materialDanio;
    }

    public Long getMaterialReporteControlId() {
        return this.materialReporteControlId;
    }

    public void setMaterialReporteControlId(Long materialReporteControl) {
        this.materialReporteControlId = materialReporteControl;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Material)) {
            return false;
        }
        return getId() != null && getId().equals(((Material) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Material{" +
            "id=" + getId() +
            ", codigo='" + getCodigo() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", valorUnitario=" + getValorUnitario() +
            ", stock=" + getStock() +
            ", activo='" + getActivo() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", creadoPor='" + getCreadoPor() + "'" +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", actualizadoPor='" + getActualizadoPor() + "'" +
            ", fechaModificacion='" + getFechaModificacion() + "'" +
            "}";
    }
}
