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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="votante_habilitado")
@Getter
@Setter
public class VotanteHabilitado implements Serializable{

    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_votante_habilitado;
    
    @Column
    private String estado_mesa;
    @Column
    private String estado_delegado;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha_registro;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estudiante")
    private Estudiante estudiante;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_docente")
    private Docente docente;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "votante_habilitado", fetch = FetchType.LAZY)
	private List<AsignacionHabilitado> asignacion_habilitado;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "votanteHabilitado", fetch = FetchType.LAZY)
	private List<Delegado> delegados;
    
}
