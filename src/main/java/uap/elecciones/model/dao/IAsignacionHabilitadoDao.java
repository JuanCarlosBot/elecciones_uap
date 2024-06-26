package uap.elecciones.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import uap.elecciones.model.entity.AsignacionHabilitado;

public interface IAsignacionHabilitadoDao extends CrudRepository<AsignacionHabilitado,Long>{
    
    @Query(value = "SELECT f.nombre_facultad , c.nombre_carrera , p.nombres ,m.nombre_mesa ,ah.delegado, ah.id_asignacion_habilitado ,e.ru "+
    "FROM asignacion_habilitado ah "+
    "LEFT JOIN votante_habilitado vh ON	ah.id_votante_habilitado = vh.id_votante_habilitado "+
    "LEFT JOIN estudiante e ON vh.id_estudiante = e.id_estudiante "+
    "LEFT JOIN carrera_estudiante ce ON	e.id_estudiante = ce.id_estudiante "+
    "LEFT JOIN carrera c ON	ce.id_carrera = c.id_carrera "+
    "LEFT JOIN facultad f ON	c.id_facultad = f.id_facultad "+
    "LEFT JOIN mesa m ON	ah.id_mesa = m.id_mesa "+
    "LEFT JOIN persona p ON e.id_persona = p.id_persona "+
    "WHERE c.id_carrera = ?1",nativeQuery = true)
    List<Object[]> lista_asignados_habilitados(Long id_carrera);

    @Query(value = "SELECT f.nombre_facultad , c.nombre_carrera, e.ru , p.nombres ,m.nombre_mesa ,ah.delegado "+
    "FROM asignacion_habilitado ah "+
    "LEFT JOIN votante_habilitado vh ON	ah.id_votante_habilitado = vh.id_votante_habilitado "+
    "LEFT JOIN estudiante e ON vh.id_estudiante = e.id_estudiante "+
    "LEFT JOIN carrera_estudiante ce ON	e.id_estudiante = ce.id_estudiante "+
    "LEFT JOIN carrera c ON	ce.id_carrera = c.id_carrera "+
    "LEFT JOIN facultad f ON	c.id_facultad = f.id_facultad "+
    "LEFT JOIN mesa m ON	ah.id_mesa = m.id_mesa "+
    "LEFT JOIN persona p ON e.id_persona = p.id_persona "+
    "WHERE ah.delegado = 'Delegado' ORDER BY m.nombre_mesa  ASC",nativeQuery = true)
    List<Object[]> lista_asignados_delegados();

    @Query(value = "SELECT f.nombre_facultad , c.nombre_carrera ,e.ru, p.nombres ,m.nombre_mesa ,ah.delegado "+
    "FROM asignacion_habilitado ah "+
    "LEFT JOIN votante_habilitado vh ON	ah.id_votante_habilitado = vh.id_votante_habilitado "+
    "LEFT JOIN estudiante e ON vh.id_estudiante = e.id_estudiante "+
    "LEFT JOIN carrera_estudiante ce ON	e.id_estudiante = ce.id_estudiante "+
    "LEFT JOIN carrera c ON	ce.id_carrera = c.id_carrera "+
    "LEFT JOIN facultad f ON	c.id_facultad = f.id_facultad "+
    "LEFT JOIN mesa m ON	ah.id_mesa = m.id_mesa "+
    "LEFT JOIN persona p ON e.id_persona = p.id_persona "+
    "WHERE e.ru = ?1",nativeQuery = true)
    Object asignado_habilitado(String ru);

    @Query(value = "select * from asignacion_habilitado ah "+
    "where ah.id_mesa = ?1",nativeQuery = true)
    List<AsignacionHabilitado> lista_asignados_habilitados_por_mesa(Long id_mesa);

}
