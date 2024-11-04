package uap.elecciones.model.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DelegadoDto {
    private String nombre_facultad;
    private String nombre_carrera;
    private String ru_rd;
    private String tipo_persona;
    private String apellidos;
    private Long id_persona;
    private String nombre_mesa;
    private String nombre_tipo_delegado;
    private Long id_votante_habilitado;

    public DelegadoDto(String nombre_facultad, String nombre_carrera, String ru_rd, String tipo_persona, String apellidos,
                       Long id_persona, String nombre_mesa, String nombre_tipo_delegado,
                       Long id_votante_habilitado) {
        this.nombre_facultad = nombre_facultad;
        this.nombre_carrera = nombre_carrera;
        this.ru_rd = ru_rd;
        this.tipo_persona = tipo_persona;
        this.apellidos = apellidos;
        this.id_persona = id_persona;
        this.nombre_mesa = nombre_mesa;
        this.nombre_tipo_delegado = nombre_tipo_delegado;
        this.id_votante_habilitado = id_votante_habilitado;
    }
}

