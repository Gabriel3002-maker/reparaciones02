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
 * The Persona entity.
 * @author Usuario
 */
@Schema(description = "The Persona entity.\n@author Usuario")
@Table("persona")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Persona implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    /**
     * identificacion
     */
    @Schema(description = "identificacion", required = true)
    @NotNull(message = "must not be null")
    @Column("identificacion")
    private String identificacion;

    /**
     * primer apellido
     */
    @Schema(description = "primer apellido", required = true)
    @NotNull(message = "must not be null")
    @Column("primer_apellido")
    private String primerApellido;

    /**
     * segundo apellido
     */
    @Schema(description = "segundo apellido")
    @Column("segundo_apellido")
    private String segundoApellido;

    /**
     * primer Nombre
     */
    @Schema(description = "primer Nombre", required = true)
    @NotNull(message = "must not be null")
    @Column("primer_nombre")
    private String primerNombre;

    /**
     * segundo Nombre
     */
    @Schema(description = "segundo Nombre")
    @Column("segundo_nombre")
    private String segundoNombre;

    /**
     * celular
     */
    @Schema(description = "celular")
    @Column("celular")
    private String celular;

    /**
     * telefono convencional
     */
    @Schema(description = "telefono convencional")
    @Column("telefono_convencional")
    private String telefonoConvencional;

    /**
     * correo
     */
    @Schema(description = "correo")
    @Column("correo")
    private String correo;

    @Transient
    @JsonIgnoreProperties(value = { "catalogo" }, allowSetters = true)
    private CatalogoItem tipoIdentificacion;

    @Transient
    private User usuario;

    @Column("tipo_identificacion_id")
    private Long tipoIdentificacionId;

    @Column("usuario_id")
    private Long usuarioId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Persona id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentificacion() {
        return this.identificacion;
    }

    public Persona identificacion(String identificacion) {
        this.setIdentificacion(identificacion);
        return this;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getPrimerApellido() {
        return this.primerApellido;
    }

    public Persona primerApellido(String primerApellido) {
        this.setPrimerApellido(primerApellido);
        return this;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return this.segundoApellido;
    }

    public Persona segundoApellido(String segundoApellido) {
        this.setSegundoApellido(segundoApellido);
        return this;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getPrimerNombre() {
        return this.primerNombre;
    }

    public Persona primerNombre(String primerNombre) {
        this.setPrimerNombre(primerNombre);
        return this;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoNombre() {
        return this.segundoNombre;
    }

    public Persona segundoNombre(String segundoNombre) {
        this.setSegundoNombre(segundoNombre);
        return this;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getCelular() {
        return this.celular;
    }

    public Persona celular(String celular) {
        this.setCelular(celular);
        return this;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getTelefonoConvencional() {
        return this.telefonoConvencional;
    }

    public Persona telefonoConvencional(String telefonoConvencional) {
        this.setTelefonoConvencional(telefonoConvencional);
        return this;
    }

    public void setTelefonoConvencional(String telefonoConvencional) {
        this.telefonoConvencional = telefonoConvencional;
    }

    public String getCorreo() {
        return this.correo;
    }

    public Persona correo(String correo) {
        this.setCorreo(correo);
        return this;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public CatalogoItem getTipoIdentificacion() {
        return this.tipoIdentificacion;
    }

    public void setTipoIdentificacion(CatalogoItem catalogoItem) {
        this.tipoIdentificacion = catalogoItem;
        this.tipoIdentificacionId = catalogoItem != null ? catalogoItem.getId() : null;
    }

    public Persona tipoIdentificacion(CatalogoItem catalogoItem) {
        this.setTipoIdentificacion(catalogoItem);
        return this;
    }

    public User getUsuario() {
        return this.usuario;
    }

    public void setUsuario(User user) {
        this.usuario = user;
        this.usuarioId = user != null ? user.getId() : null;
    }

    public Persona usuario(User user) {
        this.setUsuario(user);
        return this;
    }

    public Long getTipoIdentificacionId() {
        return this.tipoIdentificacionId;
    }

    public void setTipoIdentificacionId(Long catalogoItem) {
        this.tipoIdentificacionId = catalogoItem;
    }

    public Long getUsuarioId() {
        return this.usuarioId;
    }

    public void setUsuarioId(Long user) {
        this.usuarioId = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Persona)) {
            return false;
        }
        return getId() != null && getId().equals(((Persona) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Persona{" +
            "id=" + getId() +
            ", identificacion='" + getIdentificacion() + "'" +
            ", primerApellido='" + getPrimerApellido() + "'" +
            ", segundoApellido='" + getSegundoApellido() + "'" +
            ", primerNombre='" + getPrimerNombre() + "'" +
            ", segundoNombre='" + getSegundoNombre() + "'" +
            ", celular='" + getCelular() + "'" +
            ", telefonoConvencional='" + getTelefonoConvencional() + "'" +
            ", correo='" + getCorreo() + "'" +
            "}";
    }
}
