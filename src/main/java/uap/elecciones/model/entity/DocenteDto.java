package uap.elecciones.model.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocenteDto {
    private Long idDocente;
    private String nombreCompleto;
    private String ci;
    private String rd;
    private Long idPersona;

    public DocenteDto(Long idDocente, String rd, String nombreCompleto, String ci, Long idPersona) {
        this.idDocente = idDocente;
        this.rd = rd;
        this.nombreCompleto = nombreCompleto;
        this.ci = ci;
        this.idPersona = idPersona;
    }
}
