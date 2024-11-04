package uap.elecciones.model.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="mesa")
@Getter
@Setter
public class Mesa implements Serializable{
    
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_mesa;
    @Column
    private String nombre_mesa;
    @Column
    private String estado;
    @Column
    private String ubicacion;
    @Column
    private String descripcion;
    @Column
    private Integer cant_votantes;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha_registro;

    @Column
    private String estado_full;

    @Column
    private String estado_centro;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "if_facultad")
    private Facultad facultad;


    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_carrera")
    private Carrera carrera;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mesa", fetch = FetchType.LAZY)
	private List<DetalleAsignacionMesa> detalle_asignacion_mesa;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mesa", fetch = FetchType.LAZY)
	private List<AsignacionHabilitado> asignacionHabilitados;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "mesa", fetch = FetchType.LAZY)
	private List<Anfora> anforas;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "mesa", fetch = FetchType.LAZY)
	private List<Delegado> delegados;

}
