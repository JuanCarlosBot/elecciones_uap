package uap.elecciones.model.entity;

import java.io.Serializable;
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
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="carrera")
@Getter
@Setter
public class Carrera implements Serializable{
    
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_carrera;
    @Column
    private String nombre_carrera;
    @Column
    private String sigla;
    @Column
    private String estado;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_facultad")
    private Facultad facultad;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "carreras", fetch = FetchType.LAZY)
	private List<Estudiante> estudiantes;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "carreras", fetch = FetchType.LAZY)
	private List<Docente> docentes;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "carrera", fetch = FetchType.LAZY)
	private List<Mesa> mesas;
}
