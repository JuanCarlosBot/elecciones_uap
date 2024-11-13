package uap.elecciones.model.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="docente")
@Getter
@Setter
public class Docente implements Serializable{
    
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_docente;
    @Column
    private String rd;
    @Column
    private String estado;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha_registro;
    @Column
    private String celular;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_persona")
    private Persona persona;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="carrera_docente",
    joinColumns=@JoinColumn(name = "id_docente"),
    inverseJoinColumns = @JoinColumn(name = "id_carrera"))
    private Set<Carrera> carreras;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "docente", fetch = FetchType.LAZY)
	private VotanteHabilitado votante_habilitado;

}
