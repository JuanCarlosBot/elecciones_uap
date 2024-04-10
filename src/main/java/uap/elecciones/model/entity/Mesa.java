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
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha_registro;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mesa", fetch = FetchType.LAZY)
	private List<DetalleAsignacionMesa> detalle_asignacion_mesa;

}
