package uap.elecciones.model.entity;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="nivel")
@Getter
@Setter
public class Nivel implements Serializable{
    
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_nivel;
    @Column
    private String nombre_nivel;
    @Column
    private String estado;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nivel", fetch = FetchType.LAZY)
	private List<AsignacionEleccion> asignacion_eleccion;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nivel", fetch = FetchType.LAZY)
	private List<ConteoTotal> conteo_totales;

}
