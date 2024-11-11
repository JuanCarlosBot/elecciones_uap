package uap.elecciones.model.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstudianteDto {
    private Long idEstudiante;
    private String nombreCompleto;
    private String ci;
    private String ru;
    private Long idPersona;

    public EstudianteDto(Long idEstudiante, String ru, String nombreCompleto, String ci, Long idPersona) {
        this.idEstudiante = idEstudiante;
        this.ru = ru;
        this.nombreCompleto = nombreCompleto;
        this.ci = ci;
        this.idPersona = idPersona;
    }
}
