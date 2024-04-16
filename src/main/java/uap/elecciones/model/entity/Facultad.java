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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="facultad")
@Getter
@Setter
public class Facultad implements Serializable {
    
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_facultad;
    @Column(length = 10485760)
    private String nombre_facultad;
    @Column
    private String sigla;
    @Column
    private String estado;
    @Column
    private Integer cantidad_est;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_universidad")
    private Universidad universidad;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facultad", fetch = FetchType.LAZY)
	private List<Carrera> carreras;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facultad", fetch = FetchType.LAZY)
	private List<Mesa> mesas;
}
