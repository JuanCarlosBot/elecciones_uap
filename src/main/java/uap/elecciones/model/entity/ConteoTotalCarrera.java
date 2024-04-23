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
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="conteo_total_carrera")
@Getter
@Setter

public class ConteoTotalCarrera implements Serializable {
    
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_conteo_total;
    @Column
    private int nulo_total;
    @Column
    private int blanco_total;
    @Column
    private int voto_valido_total;
    @Column
    private double porcentaje_total;

    @Column
    private String carrera;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "conteo_total_carrera", fetch = FetchType.LAZY)
	private List<Anfora> anforas;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "conteo_total_carrera", fetch = FetchType.LAZY)
	private List<VotoTotalCarrera> voto_total_carrera;


}
