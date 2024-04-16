package uap.elecciones.model.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="frente")
@Getter
@Setter
public class Frente implements Serializable {
    
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_frente;
    @Column
    private String nombre_frente;
    @Column
    private String sigla;
    @Column
    private String estado;
    @Column
    private String color;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha_registro;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "frente", fetch = FetchType.LAZY)
	private List<AsignacionEleccion> asignacion_eleccion;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "frente", fetch = FetchType.LAZY)
	private List<DetalleAnfora> detalleAnforas;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "frente", fetch = FetchType.LAZY)
	private List<VotoTotalFrente> votoTotalFrentes;

}
